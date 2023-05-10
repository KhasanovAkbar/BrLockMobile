package mobile.uz.brlock_mobile.domain.register

import mobile.uz.brlock_mobile.domain.Entity
import mobile.uz.brlock_mobile.domain.FailureMessage

data class RegisterResponse(
    val entities: List<Entity>,
    val failureMessage: FailureMessage,
    val requestFailed: Boolean
)