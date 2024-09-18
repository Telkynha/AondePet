package com.aondepet.ui.models

import android.net.Uri

data class Pet(
    var id: String? = null,
    val animal: Animal = Animal.CACHORRO,
    val raca: String = "",
    val genero: Genero = Genero.MACHO,
    val porte: Porte = Porte.MEDIO,
    val idade: Int = 0,
    val estado: String = "",
    val cidade: String = "",
    val status: Status = Status.ADOTADO,
    val nome: String = "",
    val descricao: String = "",
    val conta: String = "",
    val email: String = "",
    val telefone: String = "",
    val foto: String = ""
) {

}