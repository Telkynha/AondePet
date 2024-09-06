package com.aondepet.ui.control

import FirestoreRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aondepet.ui.models.Conta
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

class PetViewModel : ViewModel() {
    private val authRepository = FirebaseAuthRepository()
    private val firestoreRepository = FirestoreRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _userId = MutableLiveData<String?>()
    val userId: LiveData<String?> get() = _userId

    private val _petData = MutableLiveData<Pet>()
    val petData: LiveData<Pet> get() = _petData

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> get() = _pets

    private val _petsList = MutableLiveData<List<Pet>>()
    val petsList: LiveData<List<Pet>> get() = _petsList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        checkAuthState()
        fetchPets()
        _petData.value = Pet()
    }

    fun checkAuthState() {
        if (authRepository.checkAuthState()) {
            _authState.value = AuthState.Authenticated
            _userId.value = authRepository.currentUserId
        } else {
            _authState.value = AuthState.Unauthenticated
            _userId.value = null
        }
    }

    // ========== METODOS - AUTENTICAÇÃO LOGIN/REGISTRO ==========

    fun setUserId(id: String?) {
        _userId.value = id
    }

    fun clearUserId() {
        _userId.value = null
    }

    fun login(email: String, senha: String) {
        _authState.value = AuthState.Loading
        authRepository.login(
            email,
            senha,
            onSuccess = {
                _authState.value = AuthState.Authenticated
                _userId.value = authRepository.currentUserId
            },
            onFailure = { _authState.value = AuthState.Error(it) }
        )
    }

    fun logout() {
        authRepository.logout {
            _authState.value = AuthState.Unauthenticated
            _userId.value = null
        }
    }

    fun registrar(email: String, senha: String, confirmarSenha: String, nome: String) {
        _authState.value = AuthState.Loading
        authRepository.registrar(
            email,
            senha,
            confirmarSenha,
            onSuccess = { userId ->
                _authState.value = AuthState.Authenticated
                _userId.value = userId.toString()
                val conta = Conta(nome = nome, email = email, senha = senha, id = userId.toString())
                addConta(userId.toString(), conta) // Use o userId do Firebase Authentication
            },
            onFailure = { _authState.value = AuthState.Error(it) }
        )
    }

    // ========== METODOS - CONTA ==========

    fun addConta(contaId: String, conta: Conta) {
        firestoreRepository.addConta(contaId, conta)
    }

    fun updateConta(contaId: String, updatedConta: Conta) {
        firestoreRepository.updateConta(contaId, updatedConta)
    }

    fun deleteConta() {
        _userId.value?.let {
            firestoreRepository.deleteConta(it)
        }
    }

    fun getNomeContaById(): Task<DocumentSnapshot> {
        return _userId.value?.let {
            firestoreRepository.getNomeContaById(it)
        } ?: throw IllegalStateException("UserId vazio")
    }

    // ========== METODOS - PET ==========

    fun addPet(pet: Pet) {
        firestoreRepository.addPet(pet)
            .addOnSuccessListener {
                fetchPets() // Atualiza a lista de pets após adicionar
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    fun fetchPets() {
        firestoreRepository.getPets()
            .addOnSuccessListener { querySnapshot ->
                val petList = querySnapshot.toObjects(Pet::class.java)
                _pets.value = petList
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    fun getPetById(petId: String): Task<DocumentSnapshot> {
        return firestoreRepository.getPetById(petId)
            .addOnSuccessListener { document ->
                val pet = document.toObject(Pet::class.java)
                if (pet != null) {
                    _petData.value = pet!!
                } else {
                    _errorMessage.value = "Pet não encontrado"
                }
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    fun getPets() = firestoreRepository.getPets()

    fun updatePet(petId: String, updatedPet: Pet) {
        firestoreRepository.updatePet(petId, updatedPet)
        fetchPets()
    }

    fun deletePet(petId: String) {
        firestoreRepository.deletePet(petId)
        fetchPets()
    }

    fun fetchPetsByField(fieldName: String, value: String) {
        firestoreRepository.getPetsByField(fieldName, value)
            .addOnSuccessListener { querySnapshot ->
                val pets = querySnapshot.documents.mapNotNull { it.toObject(Pet::class.java) }
                _petsList.value = pets
            }
            .addOnFailureListener { exception ->
                _errorMessage.value = exception.message
            }
    }
}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}