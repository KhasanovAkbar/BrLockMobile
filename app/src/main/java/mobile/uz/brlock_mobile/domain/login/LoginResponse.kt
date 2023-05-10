package mobile.uz.brlock_mobile.domain.login

import mobile.uz.brlock_mobile.domain.FailureMessage

data class LoginResponse(
    val entities: List<Entity>,
    val failureMessage: FailureMessage?,
    val requestFailed: Boolean
)