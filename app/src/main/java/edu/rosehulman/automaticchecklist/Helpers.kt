package edu.rosehulman.automaticchecklist

import android.widget.AutoCompleteTextView
import edu.rosehulman.automaticchecklist.models.Label
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object Helpers {
    const val TAG = "ACL"
    const val tag = "tag"
    //var defaultLabelArray = arrayListOf<Label>(Label("School"), Label("Work"), Label("Leisure"))

    var defaultLabelStrings = arrayListOf<String>("school", "work", "leisure")


    fun parseFrequencyToArray(filterOutNone: Boolean = false): Array<String> {
        val output = ArrayList<String>()
        enumValues<Frequency>().forEach {
            if (!filterOutNone || it.name != Frequency.NONE.toString())
                output.add(it.name)
        }
        return output.toTypedArray()
    }


    fun indexOfFrequency(f: String) = enumValues<Frequency>().indexOf(
        Frequency.valueOf(f)
    ) // index

    fun parseSingleFrequency(f: Frequency) = f.name.replace("_", " ")

    /* parse from milliseconds into dd/MM/yyyy format */
    /* off by one day bug fix sources: */
    /* https://github.com/material-components/material-components-android/issues/882 */
    /* https://github.com/material-components/material-components-android/issues/967 */
    fun parseDateFromMs(dateTime: Long): String {
        val date =
            Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = dateTime }
        return String.format(
            "%d/%d/%d",
            date.get(Calendar.MONTH) + 1,
            date.get(Calendar.DAY_OF_MONTH),
            date.get(Calendar.YEAR)
        )
    }
    /* reference: https://stackoverflow.com/questions/44963164/autocompletetextview-item-selection-programmatically */
    fun AutoCompleteTextView.selectItem(text: String, position: Int = 0) {
        this.setText(text)
        //this.showDropDown() // expand dropdown
        this.setSelection(position)
        this.listSelection = position
        this.performCompletion()
    }

}
