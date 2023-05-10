package mobile.uz.brlock_mobile.domain.register

data class RegisterRequest(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val carNumber: String,
    val password: String,
    val roles: List<String>
)