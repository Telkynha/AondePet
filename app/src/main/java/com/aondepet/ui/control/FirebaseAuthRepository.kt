package com.aondepet.ui.control

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun login(email: String, senha: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (email.isEmpty() || senha.isEmpty()) {
            onFailure("Preencha todos os campos")
            return
        }
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Erro desconhecido") }
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }

    fun registrar(email: String, senha: String, confirmarSenha: String, onSuccess: (Any?) -> Unit, onFailure: (String) -> Unit) {
        if (email.isEmpty() || senha.isEmpty()) {
            onFailure("Preencha todos os campos")
            return
        }
        if (senha != confirmarSenha) {
            onFailure("As senhas não coincidem")
            return
        }
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid
                onSuccess(userId)
            }
            .addOnFailureListener { onFailure(it.message ?: "Erro desconhecido") }
    }

    fun deletarContaByid() {
        auth.currentUser?.delete()
    }

    fun checkAuthState(): Boolean {
        return auth.currentUser != null
    }
}
