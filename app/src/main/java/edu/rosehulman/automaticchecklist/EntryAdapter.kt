package edu.rosehulman.automaticchecklist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.rosehulman.automaticchecklist.ui.EntriesViewModel
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

        init {
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

            if (entry.getIsChecked()) {
                checkboxIconView.setImageResource(Entry.checkboxCheckedIconSource)
                contentTextView.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = entry.getContent()
                }
            } else {
                checkboxIconView.setImageResource(Entry.checkboxNotCheckedIconSource)
                contentTextView.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    text = entry.getContent()
                }
            }
        }
    }
}