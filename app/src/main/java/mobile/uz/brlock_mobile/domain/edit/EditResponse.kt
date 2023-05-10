package mobile.uz.brlock_mobile.domain.edit

import mobile.uz.brlock_mobile.domain.Entity
import mobile.uz.brlock_mobile.domain.FailureMessage

data class EditResponse(
    val entities: List<Entity>,
    val failureMessage: FailureMessage,
    var requestFailed: Boolean
)