package mobile.uz.brlock_mobile.domain.getAll

import mobile.uz.brlock_mobile.domain.Entity
import mobile.uz.brlock_mobile.domain.FailureMessage

data class AllUsers(
    val entities: List<List<Entity>>,
    val failureMessage: FailureMessage,
    val requestFailed: Boolean
)