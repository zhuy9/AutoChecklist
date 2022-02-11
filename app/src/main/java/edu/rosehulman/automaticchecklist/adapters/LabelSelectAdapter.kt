package edu.rosehulman.automaticchecklist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import edu.rosehulman.automaticchecklist.models.UserViewModel
import edu.rosehulman.automaticchecklist.ui.EntryEditFragment

class LabelSelectAdapter(
    val fragment: EntryEditFragment,
    var current: Entry?
) :
    RecyclerView.Adapter<LabelSelectAdapter.LabelSelectViewHolder>() {
    private val entryModel = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)
    private val userModel = ViewModelProvider(fragment.requireActivity()).get(UserViewModel::class.java)
    val currentEntry = if (!entryModel.onCreate) entryModel.getCurrentEntry() else current
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelSelectViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_checkbox_remove, parent, false)
        return LabelSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelSelectViewHolder, position: Int) {
        holder.bind(userModel.user!!.labels[position], currentEntry!!.tags)
    }

    override fun getItemCount() = userModel.user!!.labels.size

    fun update() = notifyDataSetChanged()

    inner class LabelSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelText: TextView = itemView.findViewById(R.id.row_checkbox_text)
        var cb: CheckBox = itemView.findViewById(R.id.row_checkbox_box)
        var deleteButton: ImageView = itemView.findViewById(R.id.row_checkbox_delete)

        init {
            cb.setOnClickListener {
                if (cb.isChecked) {
                    if (!currentEntry!!.tags.contains(labelText.text.toString())) {
                        currentEntry.tags.add(labelText.text.toString())
                    }
                } else {
                    if (currentEntry!!.tags.contains(labelText.text.toString())) {
                        currentEntry.tags.remove(labelText.text.toString())
                    }
                }
            }

            deleteButton.setOnClickListener {
                userModel.deleteLabel(labelText.text.toString())
                notifyDataSetChanged()
                Snackbar.make(
                    itemView,
                    "Label {${userModel.removedLabelsToString()}} has been removed",
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.undo) {
                        userModel.undoDelete()
                        notifyDataSetChanged()
                    }
                    //.setAnchorView(itemView.findViewById(R.id.nav_view))
                    .show()
            }

        }

        fun bind(label: String, tags: ArrayList<String>) {
            labelText.text = label
            cb.isChecked = tags.contains(label)
        }
    }
}