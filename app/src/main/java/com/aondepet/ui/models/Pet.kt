package com.aondepet.ui.models

data class Pet(
    var id: String? = null,
    val animal: Animal = Animal.CACHORRO,
    val raca: String = "",
    val genero: Genero = Genero.MACHO,
    val porte: Porte = Porte.MEDIO,
    val idade: Int = 0,
    val localizacao: Localizacao = Localizacao("", "", ""),
    val status: Status = Status.ADOTADO,
    val nome: String = "",
    val descricao: String = "",
    val conta: Conta = Conta(),
    val email: String = "",
    val telefone: String = "",
    val fotos: List<Foto> = emptyList()
    // TO DO - Importar fotos da galeria e incluir no pet
) {

}