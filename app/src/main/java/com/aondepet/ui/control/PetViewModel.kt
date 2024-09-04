package com.aondepet.ui.control

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class PetViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init{ checkAuthState() }

    fun checkAuthState(){
        if(auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, senha: String){
        if(email.isEmpty() || senha.isEmpty()){
            _authState.value = AuthState.Error("Preencha todos os campos")
        }else{
            _authState.value = AuthState.Loading
        }
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Erro desconhecido")
            }

    }

    fun logout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun registrar(email: String, senha: String, confirmarSenha: String){
        if(email.isEmpty() || senha.isEmpty()){
            _authState.value = AuthState.Error("Preencha todos os campos")
        }else if(!senha.equals(confirmarSenha)){
            _authState.value = AuthState.Error("As senhas não coincidem")
        }else{
            _authState.value = AuthState.Loading
        }
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                _authState.value = AuthState.Authenticated
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Erro desconhecido")
            }
    }
}

sealed class AuthState{
    object Authenticated: AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String): AuthState()
}