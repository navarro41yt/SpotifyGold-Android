package com.navarro.spotifygold.utils

class FileUtils {
    companion object {

        private val needToSanitize = arrayOf("/", "\\")

        fun sanitizeString(s : String) : String {
            var sanitized = s.trim()
            for (c in needToSanitize) {
                sanitized = sanitized.replace(c, "")
            }
            return sanitized
        }
    }
}