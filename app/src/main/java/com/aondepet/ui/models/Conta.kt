package com.aondepet.ui.models

data class Conta(
    var id: String? = null,
    val nome: String = "teste",
    val email: String = "email",
    val senha: String = "123456",
    val favoritos: List<Pet> = emptyList()
    //Usando o ID dos Pets como parametro para a lista
) {
}