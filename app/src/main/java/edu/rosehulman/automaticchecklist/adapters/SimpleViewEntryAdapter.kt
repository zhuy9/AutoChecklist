package edu.rosehulman.automaticchecklist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.automaticchecklist.models.Frequency
import edu.rosehulman.automaticchecklist.Constants
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import edu.rosehulman.automaticchecklist.ui.CategoryFragment

class SimpleViewEntryAdapter(fragment: CategoryFragment, label: String) :
    RecyclerView.Adapter<SimpleViewEntryAdapter.EntryViewHolder>() {
    private val model = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)
    private val entries = model.getEntriesByTag(label)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount() = entries.size

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTextView: TextView = itemView.findViewById(R.id.entry_content)
        private val checkboxIconView: ImageView = itemView.findViewById(R.id.entry_checkbox)

        private val deleteImageButton: ImageButton = itemView.findViewById(R.id.entry_delete)
        private val editImageButton: ImageButton = itemView.findViewById(R.id.entry_edit)
        private val shareImageButton: ImageButton = itemView.findViewById(R.id.entry_share)

        private val timeIconView: ImageView = itemView.findViewById(R.id.entry_time_icon)
        private val timeTextView: TextView = itemView.findViewById(R.id.entry_time_text)
        private val recurringIconView: ImageView = itemView.findViewById(R.id.entry_recurring_icon)
        private val recurringTextView: TextView = itemView.findViewById(R.id.entry_recurring_text)
        private val locationIconView: ImageView = itemView.findViewById(R.id.entry_location_icon)
        private val locationTextView: TextView = itemView.findViewById(R.id.entry_location_text)
        private val labelIconView: ImageView = itemView.findViewById(R.id.entry_label_icon)
        private val labelTextView: TextView = itemView.findViewById(R.id.entry_label_text)

        init {
            deleteImageButton.visibility = GONE
            editImageButton.visibility = GONE
            shareImageButton.visibility = GONE
            checkboxIconView.visibility = GONE
        }

        fun bind(entry: Entry) {
            contentTextView.text = entry.content
            if (entry.tags.size == 0) {
                labelIconView.visibility = GONE
                labelTextView.visibility = GONE
            } else {
                labelIconView.visibility = VISIBLE
                labelTextView.visibility = VISIBLE
                labelTextView.text = entry.tags.joinToString(",")
            }
            if (entry.recurring != Frequency.NONE.toString()) {
                recurringIconView.visibility = VISIBLE
                recurringTextView.visibility = VISIBLE
                recurringTextView.text = entry.recurring
            } else {
                recurringIconView.visibility = GONE
                recurringTextView.visibility = GONE
            }

            if (entry.dueDate != null) {
                timeIconView.visibility = VISIBLE
                timeTextView.visibility = VISIBLE
                timeTextView.text = Constants.parseDateFromMs(entry.dueDate!!.toLong())
            } else {
                timeIconView.visibility = GONE
                timeTextView.visibility = GONE
            }

            if (entry.location.isNotBlank()) {
                locationIconView.visibility = VISIBLE
                locationTextView.visibility = VISIBLE
                locationTextView.text = entry.location
            } else {
                locationIconView.visibility = GONE
                locationTextView.visibility = GONE
            }
        }
    }
}