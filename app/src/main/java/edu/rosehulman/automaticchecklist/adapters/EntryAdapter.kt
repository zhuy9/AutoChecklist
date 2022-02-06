package edu.rosehulman.automaticchecklist.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import edu.rosehulman.automaticchecklist.models.Label
import edu.rosehulman.automaticchecklist.ui.InboxFragment

class EntryAdapter(private val fragment: InboxFragment) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(model.getEntryAt(position))
    }

    override fun getItemCount() = model.size()

    fun addEntry(entry: Entry?) {
        model.addEntry(entry)
        notifyDataSetChanged()
    }


    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contentTextView: TextView = itemView.findViewById(R.id.entry_content)
        private val checkboxIconView: ImageView = itemView.findViewById(R.id.entry_checkbox)
        private val deleteImageButton: ImageButton = itemView.findViewById(R.id.entry_delete)
        private val editImageButton: ImageButton = itemView.findViewById(R.id.entry_edit)
        private val shareImageButton: ImageButton = itemView.findViewById(R.id.entry_share)
        private val timeIconView: ImageView = itemView.findViewById(R.id.entry_time_icon)
        private val timeTextView: TextView = itemView.findViewById(R.id.entry_time_text)
        private val locationIconView: ImageView = itemView.findViewById(R.id.entry_location_icon)
        private val locationTextView: TextView = itemView.findViewById(R.id.entry_location_text)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.entry_labels)
        val labelAdapter: LabelAdapter = LabelAdapter(fragment)

        init {

            recyclerView.adapter = labelAdapter
            recyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)

            editImageButton.setOnClickListener {
                model.updatePos(adapterPosition)
                fragment.findNavController().navigate(R.id.navigation_update)
                // TODO add animations
            }

            deleteImageButton.setOnClickListener {
                model.updatePos(adapterPosition)
                model.deleteCurrentEntry()
                notifyDataSetChanged()
                Snackbar.make(itemView, "This entry has been removed", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Undo") {
                        model.undoLastDelete()
                        notifyDataSetChanged()
                    }.setAnchorView(itemView.findViewById(R.id.nav_view))
                    .show()

            }
            checkboxIconView.setOnClickListener {
                model.updatePos(adapterPosition)
                model.toggleCurrentEntryState()
                notifyDataSetChanged()
            }

            itemView.setOnLongClickListener {
                model.updatePos(adapterPosition)
                model.toggleCurrentEntryState()
                notifyDataSetChanged()
                true
            }
        }


        fun bind(entry: Entry) {
            Log.d(Helpers.TAG, "in bind of EntryAdpt $entry")
            if (entry.recurring !== Frequency.NONE.toString()) {
                timeIconView.visibility = VISIBLE
                timeTextView.visibility = VISIBLE
                timeIconView.setImageResource(Label.LABEL_RECURRING)
                timeTextView.text = entry.recurring.toString()
            } else if (entry.dueDate !== null) {
                timeIconView.visibility = VISIBLE
                timeTextView.visibility = VISIBLE
                timeIconView.setImageResource(Label.LABEL_DUE)
                timeTextView.text = "DATE"
            } else {
                timeIconView.visibility = GONE
                timeTextView.visibility = GONE
            }

            if (entry.location !== "") {
                locationIconView.visibility = VISIBLE
                locationTextView.visibility = VISIBLE
                locationIconView.setImageResource(Label.LABEL_LOCATION)
                locationTextView.text = entry.location
            } else {
                locationIconView.visibility = GONE
                locationTextView.visibility = GONE

            }

            // entry.tags.forEach { labelAdapter.addLabel(it) }
            if (entry.isChecked) {
                checkboxIconView.setImageResource(Entry.checkboxCheckedIconSource)
                contentTextView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = entry.content
                }
            } else {
                checkboxIconView.setImageResource(Entry.checkboxNotCheckedIconSource)
                contentTextView.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    text = entry.content
                }
            }
        }
    }
}