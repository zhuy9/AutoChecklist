package edu.rosehulman.automaticchecklist

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Helpers {
    const val TAG = "ACL"
    var defaultLabelArray = arrayListOf<Label>(Label("School"), Label("Work"), Label("Leisure"))

    fun parseFrequencyToArray() = enumValues<Frequency>().map {
        it.name.replace("_", " ")
    }

    fun parseSingleFrequency(f: Frequency) = f.name.replace("_", " ")

    fun labelExistsIn(s: String, labels: ArrayList<Label>): Boolean {
        for(label: Label in labels){
            if (label.name === s)
                return false
        }
        return true
    }

    fun arrayToString(arr: ArrayList<Label>): String {
        var string = ""
        arr.forEach { string += it.name + "," }
        return string
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
