package mobile.uz.brlock_mobile.domain

data class Entity(
    val carNumber: String,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val roles: List<String>,
    val surname: String
)