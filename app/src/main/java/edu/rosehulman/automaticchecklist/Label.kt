package edu.rosehulman.automaticchecklist;

data class Label(var name: String = "label", var source: Int = LABEL_CUSTOMIZED, var color: String = "#FF28334A") {
    companion object{
        const val LABEL_LOCATION = R.drawable.ic_baseline_location_on_24
        const val LABEL_DUE = R.drawable.ic_baseline_calendar_month_24
        const val LABEL_RECURRING = R.drawable.ic_baseline_repeat_24
        const val LABEL_CUSTOMIZED = R.drawable.ic_baseline_label_important_24

    }
}
