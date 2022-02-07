package edu.rosehulman.automaticchecklist

import edu.rosehulman.automaticchecklist.models.Label
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helpers {
    const val TAG = "ACL"
    //var defaultLabelArray = arrayListOf<Label>(Label("School"), Label("Work"), Label("Leisure"))

    var defaultLabelStrings = arrayListOf<String>("school", "work", "leisure")

    fun parseFrequencyToArray() = enumValues<Frequency>().map {
        it.name.replace("_", " ")
    }

    fun parseSingleFrequency(f: Frequency) = f.name.replace("_", " ")


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
