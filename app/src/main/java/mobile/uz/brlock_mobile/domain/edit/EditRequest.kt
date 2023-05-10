package mobile.uz.brlock_mobile.domain.edit

data class EditRequest(
    val sequence: Int,
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val carNumber: String
)