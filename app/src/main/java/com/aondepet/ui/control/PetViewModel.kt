package com.aondepet.ui.control

import FirestoreRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aondepet.ui.models.Pet
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class PetViewModel : ViewModel() {
    private val authRepository = FirebaseAuthRepository()
    private val firestoreRepository = FirestoreRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
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

    fun registrar(email: String, senha: String, confirmarSenha: String) {
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
    }

    fun getPets() = firestoreRepository.getPets()

    fun updatePet(petId: String, updatedPet: Pet) {
        firestoreRepository.updatePet(petId, updatedPet)
    }

    fun deletePet(petId: String) {
        firestoreRepository.deletePet(petId)
    }

    fun getPetById(petId: String) = firestoreRepository.getPetById(petId)

    fun getPetsByStatus(status: String): Task<QuerySnapshot> = firestoreRepository.getPetsByStatus(status)

    fun getPetsByGenero(genero: String): Task<QuerySnapshot> = firestoreRepository.getPetsByGenero(genero)

    fun getPetsByPorte(porte: String): Task<QuerySnapshot> = firestoreRepository.getPetsByPorte(porte)

    fun getPetsByAnimal(animal: String): Task<QuerySnapshot> = firestoreRepository.getPetsByAnimal(animal)

    fun getPetsByNome(nome: String): Task<QuerySnapshot> = firestoreRepository.getPetsByNome(nome)

    fun getPetsByRaca(raca: String): Task<QuerySnapshot> = firestoreRepository.getPetsByRaca(raca)

    fun getPetsByIdade(idade: String): Task<QuerySnapshot> = firestoreRepository.getPetsByIdade(idade)

    fun getPetsByPeso(peso: String): Task<QuerySnapshot> = firestoreRepository.getPetsByPeso(peso)

}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}