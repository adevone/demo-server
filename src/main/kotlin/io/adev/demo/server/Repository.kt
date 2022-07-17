package io.adev.demo.server

import java.text.SimpleDateFormat
import java.util.*

class Repository(
    private val database: Database
) {
    fun insertNew(new: AddNew) {
        database.newsQueries.insert(
            id = new.id,
            source_id = new.sourceId,
            is_hot = if (new.isHot) 1 else 0,
            photo_url = new.photoUrl,
            title = new.title,
            text_preview = new.textPreview,
            published_time = new.publishedTime,
        )
    }

    fun insertSource(source: Source) {
        database.sourcesQueries.insert(
            id = source.id,
            title = source.title,
            icon_url = source.iconUrl
        )
    }

    fun getNews(offset: Long?): List<New> {
        val query = if (offset != null) {
            database.newsQueries.paginate(offset)
        } else {
            database.newsQueries.firstPage()
        }
        return query.executeAsList().map { newRow ->
            val source = database.sourcesQueries.get(newRow.source_id).executeAsOne()
            val subscriptions = database.subscriptionsQueries.get(newRow.id).executeAsList()
            val likes = database.likesQueries.get(newRow.id).executeAsList()
            New(
                id = newRow.id,
                isHot = newRow.is_hot == 1,
                source = Source(
                    id = source.id,
                    title = source.title,
                    iconUrl = source.icon_url,
                ),
                subscribers = subscriptions.size.toString(),
                isSubscribed = subscriptions.isNotEmpty(),
                isLiked = likes.isNotEmpty(),
                photoUrl = newRow.photo_url,
                title = newRow.title,
                textPreview = newRow.text_preview,
                publishedTime = formatDate(Date(newRow.published_time)),
                likesCount = likes.size
            )
        }
    }

    fun addLike(newId: String) {
        database.likesQueries.add(UUID.randomUUID().toString(), newId)
    }

    fun removeLike(newId: String) {
        database.likesQueries.remove(newId)
    }

    fun addDislike(newId: String) {
        database.dislikesQueries.add(UUID.randomUUID().toString(), newId)
    }

    fun removeDislike(newId: String) {
        database.dislikesQueries.remove(newId)
    }

    fun subscribe(sourceId: String) {
        database.subscriptionsQueries.add(UUID.randomUUID().toString(), sourceId)
    }

    fun unsubscribe(sourceId: String) {
        database.subscriptionsQueries.remove(sourceId)
    }

    private fun formatDate(date: Date): String {
        val nowCalendar = Calendar.getInstance()
        val dateCalendar = Calendar.getInstance().also { it.time = date }
        if (dateCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) &&
            dateCalendar.get(Calendar.MONTH) == nowCalendar.get(Calendar.MONTH)
        ) {
            if (dateCalendar.get(Calendar.DAY_OF_MONTH) == nowCalendar.get(Calendar.DAY_OF_MONTH)) {
                val hoursAgo = nowCalendar.get(Calendar.HOUR_OF_DAY) - dateCalendar.get(Calendar.HOUR_OF_DAY)
                return "${hoursPlural(hoursAgo)} назад"
            }
            if (nowCalendar.get(Calendar.DAY_OF_MONTH) - dateCalendar.get(Calendar.DAY_OF_MONTH) == 1) {
                return "вчера"
            }
        }
        return dateFormat.format(date)
    }

    private fun hoursPlural(hours: Int): String {
        return when (hours % 10) {
            1 -> "$hours час"
            in 2..4 -> "$hours часа"
            else -> "$hours часов"
        }
    }

    private companion object {
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
    }
}