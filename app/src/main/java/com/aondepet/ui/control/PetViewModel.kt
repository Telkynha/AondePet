package com.aondepet.ui.control

import FirestoreRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aondepet.ui.models.Conta
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class PetViewModel : ViewModel() {
    private val authRepository = FirebaseAuthRepository()
    private val firestoreRepository = FirestoreRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

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
        _authState.value = if (authRepository.checkAuthState()) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    // ========== METODOS - AUTENTICAÇÃO LOGIN/REGISTRO ==========

    fun login(email: String, senha: String) {
        _authState.value = AuthState.Loading
        authRepository.login(
            email,
            senha,
            onSuccess = { _authState.value = AuthState.Authenticated },
            onFailure = { _authState.value = AuthState.Error(it) }
        )
    }

    fun logout() {
        authRepository.logout {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun registrar(email: String, senha: String, confirmarSenha: String, nome: String) {
        _authState.value = AuthState.Loading
        authRepository.registrar(
            email,
            senha,
            confirmarSenha,
            onSuccess = { _authState.value = AuthState.Authenticated },
            onFailure = { _authState.value = AuthState.Error(it) }
        )
    }

    // ========== METODOS - PET ==========

    fun addPet(pet: Pet) {
        firestoreRepository.addPet(pet)
        fetchPets()
    }

    fun fetchPets() {
        firestoreRepository.getPets().addOnSuccessListener { querySnapshot ->
            val petList = querySnapshot.toObjects(Pet::class.java)
            _pets.value = petList
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

    fun getPetById(petId: String): Task<DocumentSnapshot> {
       return firestoreRepository.getPetById(petId)
    }

}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}