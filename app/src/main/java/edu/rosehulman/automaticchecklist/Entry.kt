package edu.rosehulman.automaticchecklist

data class Entry(
    private var content: String = "",
    private var isChecked: Boolean = false,
    private var tags: ArrayList<Label> = ArrayList()
) {

    fun getContent() = content
    fun getIsChecked() = isChecked

    fun toggleCheckStatus() {
        isChecked = !isChecked
    }

    fun updateContent(content: String) {
        this.content = content
    }

    companion object {
        const val checkboxCheckedIconSourse = R.drawable.ic_baseline_check_box_24
        const val checkboxNotCheckedIconSourse = R.drawable.ic_baseline_check_box_outline_blank_24
    }
}