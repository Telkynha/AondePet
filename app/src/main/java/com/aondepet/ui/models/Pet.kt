package com.aondepet.ui.models

data class Pet(
    var id: String? = null,
    val animal: Animal = Animal.CACHORRO,
    val raca: String = "Viralata",
    val genero: Genero = Genero.MACHO,
    val porte: Porte = Porte.MEDIO,
    val idade: Int = 2,
    val localizacao: Localizacao = Localizacao("1", "Sao paulo", "Osasco"),
    val status: Status = Status.ADOTADO,
    val nome: String = "Auau",
    val descricao: String = "Teste",
    val conta: Conta = Conta(),
    val email: String = "email",
    val telefone: String = "telefone",
    val fotos: List<Foto> = emptyList()
    // TO DO - Importar fotos da galeria e incluir no pet
) {

}