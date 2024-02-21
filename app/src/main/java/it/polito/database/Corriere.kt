package it.polito.database

data class Corriere (
    var id: String = "",
    var email: String = "",
    var password: String = "",
    var ordini: List<String> = listOf(),
    var resi: List<String> = listOf(),
    var number: Number
    )
