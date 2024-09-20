package com.aondepet.ui.models

import com.google.firebase.Timestamp

data class Pet(
    var id: String = "",
    val nome: String = "",
    val animal: Animal = Animal.Outro,
    val raca: String = "",
    val genero: Genero = Genero.Macho,
    val porte: Porte = Porte.Medio,
    val idade: Int = 0,
    val estado: Estado = Estado.AC,
    val cidade: String = "",
    val status: Status = Status.Adotado,
    val descricao: String = "",
    val conta: String = "",
    val email: String = "",
    val telefone: String = "",
    var foto: String = "",
    val adicionadoEm: Timestamp = Timestamp.now()
) {

}