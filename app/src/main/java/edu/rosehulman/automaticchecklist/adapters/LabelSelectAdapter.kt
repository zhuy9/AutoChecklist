package edu.rosehulman.automaticchecklist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.UserViewModel
import edu.rosehulman.automaticchecklist.ui.EntryEditFragment

class LabelSelectAdapter(val fragment: EntryEditFragment) :
    RecyclerView.Adapter<LabelSelectAdapter.LabelSelectViewHolder>() {
    val entryModel = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)
    val userModel = ViewModelProvider(fragment.requireActivity()).get(UserViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelSelectViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_checkbox_remove, parent, false)
        return LabelSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelSelectViewHolder, position: Int) {
        holder.bind(userModel.user!!.labels[position], entryModel.getCurrentEntry().tags)
    }

    override fun getItemCount() = userModel.user!!.labels.size


    inner class LabelSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelText: TextView = itemView.findViewById(R.id.row_checkbox_text)
        var cb: CheckBox = itemView.findViewById(R.id.row_checkbox_box)

        init {
            cb.setOnClickListener {
                if (cb.isChecked) {
                    if (!entryModel.getCurrentEntry().tags.contains(labelText.text.toString())) {
                        entryModel.getCurrentEntry().tags.add(labelText.text.toString())
                    }
                } else {
                    if (entryModel.getCurrentEntry().tags.contains(labelText.text.toString())) {
                        entryModel.getCurrentEntry().tags.remove(labelText.text.toString())
                    }
                }
            }
        }

        fun bind(label: String, tags: ArrayList<String>) {
            labelText.text = label
            cb.isChecked = tags.contains(label)
        }
    }
}