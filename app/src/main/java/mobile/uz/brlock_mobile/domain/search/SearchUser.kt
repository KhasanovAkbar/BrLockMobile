package mobile.uz.brlock_mobile.domain.search

import mobile.uz.brlock_mobile.domain.Entity
import mobile.uz.brlock_mobile.domain.FailureMessage

data class SearchUser(
    val entities: List<Entity>,
    val failureMessage: FailureMessage,
    val requestFailed: Boolean
)