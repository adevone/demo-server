package io.adev.demo.server

import kotlinx.serialization.json.Json

object MockFeedRepository {

    private val feed = object {}.javaClass.getResource("/feed.json")?.readText().let {
        Json.decodeFromString(Feed.serializer(), it ?: "")
    }

    fun getFeed(limit: Long? = 10, offset: Long? = 0): Feed {
        val l = (limit ?: 10).toInt()
        val o = (offset ?: 10).toInt()
        val size = feed.fields.data.size
        val lo = l  + o
        if (lo > size) {
            return if (lo % 2 == 0) {
                feed.getPart(10, 0)
            } else {
                feed.getPart(10, 10)
            }
        }
        return feed.getPart(l, o)
    }

    private fun Feed.getPart(limit: Int, offset: Int): Feed {
        val cutData = fields.data.subList(offset, offset + limit)
        return Feed(Fields(cutData))
    }
}