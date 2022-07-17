package io.adev.demo.server

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val id: String,
    val title: String,
    val iconUrl: String,
)