package io.adev.demo.server

import kotlinx.serialization.Serializable

@Serializable
data class AddNew(
    val id: String,
    val isHot: Boolean = false,
    val sourceId: String,
    val subscribers: String,
    val photoUrl: String,
    val title: String,
    val textPreview: String,
    val publishedTime: Long,
)