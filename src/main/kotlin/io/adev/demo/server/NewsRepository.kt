package io.adev.demo.server

class NewsRepository(
    private val database: Database
) {
    fun insert(new: New) {
        database.newsQueries.insert(
            id = new.id,
            is_hot = if (new.isHot) 1 else 0,
            source_title = new.source,
            source_icon_url = new.sourceIconUrl,
            subscribers = new.subscribers,
            is_subscribed = if (new.isSubscribed) 1 else 0,
            photo_url = new.photoUrl,
            title = new.title,
            text_preview = new.textPreview,
            published_time = new.publishedTime,
        )
    }

    fun all(): List<New> {
        return database.newsQueries.all().executeAsList().map { newRow ->
            New(
                id = newRow.id,
                isHot = newRow.is_hot == 1,
                source = newRow.source_title,
                sourceIconUrl = newRow.source_icon_url,
                subscribers = newRow.subscribers,
                isSubscribed = newRow.is_subscribed == 1,
                photoUrl = newRow.photo_url,
                title = newRow.title,
                textPreview = newRow.text_preview,
                publishedTime = newRow.published_time,
                likesCount = 40
            )
        }
    }
}