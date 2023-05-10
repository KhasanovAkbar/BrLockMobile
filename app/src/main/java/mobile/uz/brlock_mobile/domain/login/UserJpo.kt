package mobile.uz.brlock_mobile.domain.login

data class UserJpo(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Authority>,
    var carNumber: String,
    val credentialsNonExpired: Boolean,
    val enabled: Boolean,
    val id: Int,
    var name: String,
    var password: String,
    var phoneNumber: String,
    var roles: List<Role>,
    var surname: String,
    val username: Any
)