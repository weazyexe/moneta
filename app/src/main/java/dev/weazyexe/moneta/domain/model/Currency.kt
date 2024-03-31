package dev.weazyexe.moneta.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val symbol: String,
    val title: String,
    val code: String,
    val emoji: String
)