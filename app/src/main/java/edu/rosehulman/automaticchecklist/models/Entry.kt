package edu.rosehulman.automaticchecklist.models

import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.R

data class Entry(
    var content: String = "",
    var isChecked: Boolean = false,
    var dueDate: String? = null,
    var location: String = "",
    var recurring: String = Frequency.NONE.toString(),
    // var recurringOn: DayOfWeek? = null,
    var tags: ArrayList<String> = ArrayList()
) {
    companion object {
        const val checkboxCheckedIconSource = R.drawable.ic_baseline_check_box_24
        const val checkboxNotCheckedIconSource = R.drawable.ic_baseline_check_box_outline_blank_24
    }
}