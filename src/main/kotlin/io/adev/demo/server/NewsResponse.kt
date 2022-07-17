package io.adev.demo.server

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val news: List<New>
)