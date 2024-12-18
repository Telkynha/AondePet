package com.aondepet.ui.control

import android.net.Uri
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

    private val _pets = MutableLiveData<List<Pet>>()

    private val _petsList = MutableLiveData<List<Pet>>() // Lista para os pets filtrados
    val petsList: LiveData<List<Pet>> get() = _petsList

    private val _favoritos = MutableLiveData<List<String>>() // Lista de IDs dos pets favoritos
    val favoritos: LiveData<List<String>> get() = _favoritos

    private val _mostrarFavoritos = MutableLiveData(false)
    val mostrarFavoritos: LiveData<Boolean> get() = _mostrarFavoritos

    private val _mostrarMeusPets = MutableLiveData(false)

    private val _errorMessage = MutableLiveData<String>()

    init {
        checkAuthState()
        loadFavoritos()
        fetchPets()
        _petData.value = Pet()
    }

    // ========== METODOS - AUTENTICAÇÃO LOGIN/REGISTRO ==========

    private fun checkAuthState() {
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

    private fun addConta(contaId: String, conta: Conta) {
        firestoreRepository.addConta(contaId, conta)
    }

    fun deleteConta() {
        _userId.value?.let { userId ->
            firestoreRepository.getPetsByUserId(userId)
                .addOnSuccessListener { querySnapshot ->
                    val meusPets = querySnapshot.toObjects(Pet::class.java)
                    meusPets.forEach { pet ->
                        firestoreRepository.deletePet(pet.id)
                    }
                    firestoreRepository.deleteConta(userId)
                        .addOnSuccessListener {
                            authRepository.deletarContaByid()
                        }
                        .addOnFailureListener { e ->
                            _errorMessage.value = "Erro ao deletar a conta: ${e.message}"
                        }
                }
                .addOnFailureListener { e ->
                    _errorMessage.value = "Erro ao buscar os pets do usuário: ${e.message}"
                }
        } ?: run {
            _errorMessage.value = "Usuário não autenticado"
        }
    }



    fun getContaById(): Task<DocumentSnapshot> {
        return _userId.value?.let {
            firestoreRepository.getContaById(it)
        } ?: throw IllegalStateException("UserId vazio")
    }

    private fun getFavoritos(contaId: String): Task<List<String>> {
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

    private fun loadFavoritos() {
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
            .addOnSuccessListener {
                fetchPets()
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }

    private fun fetchPets() {
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


    private fun uploadImage(imageUri: Uri, petId: String) {
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
    data object Authenticated: AuthState()
    data object Unauthenticated: AuthState()
    data object Loading: AuthState()
    data class Error(val message: String): AuthState()
}