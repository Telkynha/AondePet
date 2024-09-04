package com.aondepet.ui.models

class Conta(
    val nome: String,
    val email: String,
    val senha: String,
    val telefone: String,
    val listaFavoritos: List<Int> = emptyList()
    //Usando o ID dos Pets como parametro para a lista
) {
}