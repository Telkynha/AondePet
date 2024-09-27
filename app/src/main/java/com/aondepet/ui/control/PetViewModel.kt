package com.aondepet.ui.control

import FirestoreRepository
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aondepet.ui.models.Animal
import com.aondepet.ui.models.Conta
import com.aondepet.ui.models.Estado
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet
import com.aondepet.ui.models.Porte
import com.aondepet.ui.models.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

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

    private val _petsList = MutableLiveData<List<Pet>>() // Lista para os pets filtrados
    val petsList: LiveData<List<Pet>> get() = _petsList

    private val _favoritos = MutableLiveData<List<String>>() // Lista de IDs dos pets favoritos
    val favoritos: LiveData<List<String>> get() = _favoritos

    private val _mostrarFavoritos = MutableLiveData<Boolean>(false)
    val mostrarFavoritos: LiveData<Boolean> get() = _mostrarFavoritos

    private val _mostrarMeusPets = MutableLiveData<Boolean>(false)
    val mostrarMeusPets: LiveData<Boolean> get() = _mostrarMeusPets

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        checkAuthState()
        loadFavoritos()
        fetchPets()
        _petData.value = Pet()
    }

    // ========== METODOS - AUTENTICAÇÃO LOGIN/REGISTRO ==========

    fun checkAuthState() {
        if (authRepository.checkAuthState()) {
            _authState.value = AuthState.Authenticated
            _userId.value = authRepository.currentUserId
            loadFavoritos() // Carregar favoritos ao autenticar
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

    fun deleteConta() {
        _userId.value?.let {
            firestoreRepository.deleteConta(it)
        }
    }

    fun getContaById(): Task<DocumentSnapshot> {
        return _userId.value?.let {
            firestoreRepository.getContaById(it)
        } ?: throw IllegalStateException("UserId vazio")
    }

    fun getFavoritos(contaId: String): Task<List<String>> {
        return firestoreRepository.getFavoritos(contaId)
    }

    fun favoritar(contaId: String, petId: String) {
        getFavoritos(contaId).addOnSuccessListener { favoritos ->
            if (favoritos.contains(petId)) {
                // Remover dos favoritos
                firestoreRepository.removeFavorito(contaId, petId)
                    .addOnSuccessListener {
                        _favoritos.value = favoritos - petId // Atualiza a lista de favoritos localmente
                    }
                    .addOnFailureListener { e ->
                        _errorMessage.value = e.message
                    }
            } else {
                // Adicionar aos favoritos
                firestoreRepository.addFavorito(contaId, petId)
                    .addOnSuccessListener {
                        _favoritos.value = favoritos + petId // Atualiza a lista de favoritos localmente
                    }
                    .addOnFailureListener { e ->
                        _errorMessage.value = e.message
                    }
            }
        }.addOnFailureListener { e ->
            _errorMessage.value = e.message
        }
    }

    fun toggleMeusPetsFilter() {
        _mostrarMeusPets.value = _mostrarMeusPets.value?.not()
        if (_mostrarMeusPets.value == true) {
            mostrarMeusPets()
        } else {
            _petsList.value = _pets.value
            applyFilters()
        }
    }

    private fun mostrarMeusPets() {
        val userId = _userId.value
        val allPets = _pets.value ?: emptyList()

        if (userId != null) {
            val meusPets = allPets.filter { it.conta == userId }
            _petsList.value = meusPets
        } else {
            _errorMessage.value = "Usuário não autenticado"
        }
    }

    fun isFavorito(contaId: String, petId: String): Task<Boolean> {
        return firestoreRepository.isFavorito(contaId, petId)
    }

    fun toggleFavoritosFilter() {
        _mostrarFavoritos.value = _mostrarFavoritos.value?.not()
        if (_mostrarFavoritos.value == true) {
            applyFavoritosFilter() // Aplicar filtro somente se mostrarFavoritos for true
        } else {
            _petsList.value = _pets.value // Mostrar todos os pets
            applyFilters()
        }
    }

    fun loadFavoritos() {
        _userId.value?.let { contaId ->
            firestoreRepository.getFavoritos(contaId)
                .addOnSuccessListener { favoritosIds ->
                    _favoritos.value = favoritosIds
                }
                .addOnFailureListener { e ->
                    _errorMessage.value = e.message
                }
        }
    }

    private fun applyFavoritosFilter() {
        val favoritosIds = _favoritos.value ?: emptyList()
        val allPets = _pets.value ?: emptyList()
        val filteredPets = allPets.filter { favoritosIds.contains(it.id) }
        _petsList.value = filteredPets
    }

    // ========== METODOS - PET ==========

    fun addPet(pet: Pet) {
        firestoreRepository.addPet(pet)
            .addOnSuccessListener { documentReference ->
                fetchPets()
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    fun fetchPets() {
        firestoreRepository.getPets()
            .addOnSuccessListener { querySnapshot ->
                val petList = querySnapshot.toObjects(Pet::class.java)
                    .sortedByDescending { it.adicionadoEm }
                _pets.value = petList
                if (_mostrarFavoritos.value == true) {
                    applyFavoritosFilter()
                } else {
                    _petsList.value = petList
                }
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

    fun updatePet(petId: String, updatedPet: Pet) {
        firestoreRepository.updatePet(petId, updatedPet)
        uploadImage(updatedPet.foto.toUri(), updatedPet.id!!)
        fetchPets()
    }

    fun deletePet(petId: String) {
        firestoreRepository.deletePet(petId)
        fetchPets()
    }

    fun applyFilters(
        animals: List<Animal> = emptyList(),
        generos: List<Genero> = emptyList(),
        portes: List<Porte> = emptyList(),
        estados: List<Estado> = emptyList(),
        status: List<Status> = emptyList()
    ) {
        val animalsList = animals.map { it.name }
        val generosList = generos.map { it.name }
        val portesList = portes.map { it.name }
        val estadosList = estados.map { it.name }
        val statusList = status.map { it.name }

        val onSuccessListener = { querySnapshot: QuerySnapshot ->
            val pets = querySnapshot.documents
                .mapNotNull { it.toObject(Pet::class.java) }
                .sortedByDescending { it.adicionadoEm } // Ordena pela data de adição em ordem decrescente
            _petsList.value = pets
        }

        if (animalsList.isEmpty() && generosList.isEmpty() && portesList.isEmpty() && estadosList.isEmpty() && statusList.isEmpty()) {
            firestoreRepository.getPetsByFilters()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener { exception ->
                    _errorMessage.value = exception.message
                }
        } else {
            firestoreRepository.getPetsByFilters(animalsList, generosList, portesList, estadosList, statusList)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener { exception ->
                    _errorMessage.value = exception.message
                }
        }
    }


    fun uploadImage(imageUri: Uri, petId: String) {
        firestoreRepository.uploadImage(imageUri, petId)
    }

    fun getPetImage(petId: String): LiveData<Uri?> {
        val imageUrl = MutableLiveData<Uri?>()
        firestoreRepository.getImageUrl(petId)
            .addOnSuccessListener { uri ->
                imageUrl.value = uri
            }
            .addOnFailureListener {
                imageUrl.value = null
            }
        return imageUrl
    }

}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}