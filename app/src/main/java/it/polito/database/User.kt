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
    var preferiti: List<String> = listOf(),
    var ordini: List<String> = listOf(),
    var resi: List<String> = listOf(),
    var preferenzeNotifiche: List<Boolean> = listOf(),
    var newsletter: List<Boolean> = listOf(),

)
{
    companion object {
        fun fromMap(map: Map<String, Any>): User {
            return User(
                id = map["id"] as? String ?: "",
                nome = map["nome"] as? String ?: "",
                cognome = map["cognome"] as? String ?: "",
                city = map["city"] as? String ?: "",
                gender = map["gender"] as? String ?: "",
                email = map["email"] as? String ?: "",
                username = map["username"] as? String ?: "",
                password = map["password"] as? String ?: "",
                preferiti = map["preferiti"] as? List<String> ?: emptyList(),
                ordini = map["ordini"] as? List<String> ?: emptyList(),
                resi = map["resi"] as? List<String> ?: emptyList(),
                preferenzeNotifiche = map["preferenzeNotifiche"] as? List<Boolean> ?: emptyList(),
                newsletter = map["newsletter"] as? List<Boolean> ?: emptyList()
            )
        }
    }
}

