package edu.rosehulman.automaticchecklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.automaticchecklist.ui.EntriesViewModel
import edu.rosehulman.automaticchecklist.ui.InboxFragment

class LabelAdapter(val fragment: InboxFragment) :
    RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LabelAdapter.LabelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.label, parent, false)
        return LabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelAdapter.LabelViewHolder, position: Int) {
        holder.bind(model.getCurrentEntry().tags[position])
    }

    override fun getItemCount() = model.getCurrentEntry().tags.size

    fun addLabel(label: String?) {
        Log.d(Helpers.TAG, "addLabel: ${label}")
        if (label != null) {
            model.getCurrentEntry().tags.add(label)
        }
        notifyDataSetChanged()
    }

    inner class LabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var labelView: ImageView = itemView.findViewById(R.id.label_icon)
        var labelText: TextView = itemView.findViewById(R.id.label_text)

        fun bind(label: String) {
            labelView.setImageResource(Label.LABEL_CUSTOMIZED)
            labelText.text = label
        }
    }
}