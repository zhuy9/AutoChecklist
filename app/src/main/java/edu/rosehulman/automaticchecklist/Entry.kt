package edu.rosehulman.automaticchecklist

import java.time.DayOfWeek
import java.time.LocalDate

data class Entry(
    private var content: String = "",
    private var isChecked: Boolean = false,
    private var dueDate: LocalDate? = null,
    private var location: String = "",
    private var recurring: Frequency = Frequency.NONE,
    private var recurringOn: DayOfWeek? = null,
    private var tags: ArrayList<Label> = ArrayList()
) {

    fun getContent() = content
    fun getIsChecked() = isChecked
    fun getDueDate() = dueDate
    fun getLocation() = location
    fun getRecurring() = recurring
    fun getRecurringOn() = recurringOn
    fun getTags() = tags

    fun toggleCheckStatus() {
        isChecked = !isChecked
    }

    fun updateContent(content: String) {
        this.content = content
    }

    companion object {
        const val checkboxCheckedIconSource = R.drawable.ic_baseline_check_box_24
        const val checkboxNotCheckedIconSource = R.drawable.ic_baseline_check_box_outline_blank_24
    }
}