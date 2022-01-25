package edu.rosehulman.automaticchecklist

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helpers {
    const val TAG = "ACL"
    var defaultLabelArray = arrayListOf<String>("School", "Work", "Leisure")
    fun parseFrequencyToArray() = enumValues<Frequency>().map {
        it.name.replace("_", " ")
    }

    fun parseDate(dateTime: Long?): String {
        var currentTime = dateTime
        if (currentTime === null)
            currentTime = System.currentTimeMillis()
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTime), ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
    }
}
