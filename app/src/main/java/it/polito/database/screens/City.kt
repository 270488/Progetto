package it.polito.database.screens

enum class City  {
    Bari, Bergamo, Bologna, Brescia, Como, Cremona, Catania, Ferrara,
    Milano, Napoli, Piacenza, Padova, Perugia, Parma, Pavia, Roma,
    Torino, Treviso, Udine, Varese, Venezia, Vicenza, Verona;
    companion object {
        private val cityMap = values().associateBy { it.name.toLowerCase() }

        fun fromString(city: String): City? {
            return cityMap[city.toLowerCase()]
        }
    }
}
