package com.aondepet.ui.models

class Pet(
    val id: Int,
    val animal: Animal,
    val raca: String,
    val genero: Genero,
    val porte: Porte,
    val idade: Int,
    val localizacao: Localizacao,
    val status: Status,
    val nome: String,
    val descricao: String,
    val contato: Contato,
    val fotos: List<String>
    // TO DO - Importar fotos da galeria e incluir no pet
) {}