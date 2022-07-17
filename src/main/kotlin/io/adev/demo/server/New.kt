package io.adev.demo.server

import kotlinx.serialization.Serializable

@Serializable
data class New(
    val id: String,
    val isHot: Boolean,
    val source: String,
    val sourceIconUrl: String,
    val subscribers: String,
    val isSubscribed: Boolean,
    val photoUrl: String,
    val title: String,
    val textPreview: String,
    val likesCount: Int,
    val publishedTime: String,
)