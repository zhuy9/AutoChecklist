package edu.rosehulman.automaticchecklist.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.R

data class Entry(
    var content: String = "",
    var isChecked: Boolean = false,
    var dueDate: String? = null,
    var location: String = "",
    var recurring: String = Frequency.NONE.toString(),
    // var recurringOn: DayOfWeek? = null,
    var tags: ArrayList<String> = ArrayList() // TODO change arraylist to SET
) {
    @get:Exclude
    var id = "" // keep id local

    @ServerTimestamp
    var created: Timestamp? = null

    companion object {
        const val COLLECTION_PATH = "entries"
        const val CREATED_KEY = "created"
        const val checkboxCheckedIconSource = R.drawable.ic_baseline_check_box_24
        const val checkboxNotCheckedIconSource = R.drawable.ic_baseline_check_box_outline_blank_24

        fun from(snapshot: DocumentSnapshot): Entry {
            val et = snapshot.toObject(Entry::class.java)!! // data only
            et.id = snapshot.id
            return et
        }

    }
}