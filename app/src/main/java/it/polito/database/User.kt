package it.polito.database

data class User(
    var id: String = "",
    var nome: String = "",
    var cognome: String = "",
    var city: String = "",
    var gender: String = "",
    var email: String = "",
    var username: String = "",
    var password: String = "",
    var preferiti: Map<String, String>,
    var ordini: Map<String, String>,
    var resi: Map<String, String>,
)


