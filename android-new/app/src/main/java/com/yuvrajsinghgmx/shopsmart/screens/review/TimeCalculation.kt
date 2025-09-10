package com.yuvrajsinghgmx.shopsmart.screens.review

import java.time.Instant
import java.time.temporal.ChronoUnit

fun getTimeAgo(createdAt: String): String {
    return try {
        val reviewInstant = Instant.parse(createdAt)
        val now = Instant.now()
        val days = ChronoUnit.DAYS.between(reviewInstant, now)
        val hours = ChronoUnit.HOURS.between(reviewInstant, now)
        val minutes = ChronoUnit.MINUTES.between(reviewInstant, now)

        when {
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    } catch (e: Exception) {
        createdAt
    }
}
