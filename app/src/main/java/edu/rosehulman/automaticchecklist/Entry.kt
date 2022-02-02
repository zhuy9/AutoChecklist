package edu.rosehulman.automaticchecklist

import java.time.DayOfWeek
import java.time.LocalDate

data class Entry(
    var content: String = "",
    var isChecked: Boolean = false,
    var dueDate: LocalDate? = null,
    var location: String = "",
    var recurring: Frequency = Frequency.NONE,
    // var recurringOn: DayOfWeek? = null,
    var tags: ArrayList<Label> = ArrayList()
) {
    companion object {
        const val checkboxCheckedIconSource = R.drawable.ic_baseline_check_box_24
        const val checkboxNotCheckedIconSource = R.drawable.ic_baseline_check_box_outline_blank_24
    }
}