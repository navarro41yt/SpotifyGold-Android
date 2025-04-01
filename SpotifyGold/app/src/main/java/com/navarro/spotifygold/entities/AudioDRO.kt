package com.navarro.spotifygold.entities

import com.navarro.spotifygold.entities.metadata.MetadataEntity
import com.navarro.spotifygold.utils.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioDRO(
    var metadata: MetadataEntity?,
    var route: String,
    var pos: Int
) {
    var uploadDate: Date = Date()

    init {
        parseDate()
    }

    private fun parseDate() {
        if (this.metadata != null) {
            val strDate = this.metadata!!.uploadAt

            val sdf = SimpleDateFormat("M/d/yyyy h:mm:ss a Z", Locale.US)
            try {
                // Parse the input date string
                this.uploadDate = sdf.parse(strDate) ?: Date()
            } catch (e: ParseException) {
                // Handle parse exception
                e.printStackTrace()
            }
        }
    }

    fun getSafeTitle(): String {
        return this.metadata?.title ?: this.route.split("/").last().split(".").first()
    }

    fun getSafeArtist(): String {
        return this.metadata?.author?.name ?: this.route.split(".").last()
    }

    fun getSafeThumbnail(): String {
        return this.metadata?.thumbnail ?: Constants.defaultImage
    }

    fun getSafeViews(): Long {
        return this.metadata?.engagement?.views ?: 0
    }

    fun getSafeLikes(): Long {
        return this.metadata?.engagement?.likes ?: 0
    }

    fun getSafeUploadAt(): String {
        val date = this.metadata?.uploadAt ?: ""
        return date.ifEmpty {
            SimpleDateFormat("M/d/yyyy h:mm:ss a Z", Locale.US).format(this.uploadDate)
        }
    }

    fun getSafeId(): String {
        return this.metadata?.id ?: this.route
    }

    fun getSafeLink(): String {
        return this.metadata?.getLink() ?: "https://es.wikipedia.org/wiki/HTTP_404"
    }
}
