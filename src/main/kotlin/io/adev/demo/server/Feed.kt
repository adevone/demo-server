package io.adev.demo.server

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement

@Serializable
data class Feed(
    val fields: Fields
)

@Serializable
data class Fields(
    val data: List<JsonElement>
)
