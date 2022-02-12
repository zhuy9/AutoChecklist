package edu.rosehulman.automaticchecklist.adapters

import android.content.Intent
import android.graphics.Paint
import android.provider.CalendarContract
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.rosehulman.automaticchecklist.models.Frequency
import edu.rosehulman.automaticchecklist.Constants
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import edu.rosehulman.automaticchecklist.models.Label
import edu.rosehulman.automaticchecklist.ui.InboxFragment

class EntryAdapter(private val fragment: InboxFragment) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(EntriesViewModel::class.java)

    fun removeListener(fragmentName: String) {
        model.removeListener(fragmentName)
    }

    fun addListener(fragmentName: String) {
        model.addListener(fragmentName) {
            notifyDataSetChanged()
        }
    }

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
        private val recurringIconView: ImageView = itemView.findViewById(R.id.entry_recurring_icon)
        private val recurringTextView: TextView = itemView.findViewById(R.id.entry_recurring_text)
        private val locationIconView: ImageView = itemView.findViewById(R.id.entry_location_icon)
        private val locationTextView: TextView = itemView.findViewById(R.id.entry_location_text)
        private val labelIconView: ImageView = itemView.findViewById(R.id.entry_label_icon)
        private val labelTextView: TextView = itemView.findViewById(R.id.entry_label_text)

        init {
            editImageButton.setOnClickListener {
                model.updatePos(adapterPosition)
                fragment.findNavController().navigate(R.id.navigation_update)
                // TODO add animations
            }
            shareImageButton.setOnClickListener {
                model.updatePos(adapterPosition)
                val curEntry = model.getCurrentEntry()
                if (curEntry.dueDate == null) {
                    Snackbar.make(
                        itemView,
                        "Missing due date for this event",
                        Snackbar.LENGTH_SHORT
                    ).setAction(android.R.string.ok, null).show() //TODO check if this is working
                    return@setOnClickListener
                }
                setupCalendarEvent(model.getCurrentEntry())
            }

            deleteImageButton.setOnClickListener {
                model.updatePos(adapterPosition)
                model.deleteCurrentEntry()
                notifyDataSetChanged()
                Snackbar.make(itemView, "This entry has been removed", Snackbar.LENGTH_LONG)
                    .setAction(R.string.undo) {
                        model.undoLastDelete()
                        notifyDataSetChanged()
                    }
                    //.setAnchorView(itemView.findViewById(R.id.nav_view))
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
            Log.d(Constants.TAG, "in bind of EntryAdpt $entry")
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
                //timeIconView.setImageResource(Label.LABEL_RECURRING)
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
        /* reference: https://itnext.io/android-calendar-intent-8536232ecb38 */
        /* reference: RRULE: https://datatracker.ietf.org/doc/html/rfc5545#section-3.8.5.3 */
        /* https://stackoverflow.com/questions/58586819/how-to-add-events-to-calendar-on-android-device-without-using-intent */
        //https://stackoverflow.com/questions/38110754/android-permissions-read-calendar-write-calendar
        //https://stackoverflow.com/questions/66551781/android-onrequestpermissionsresult-is-deprecated-are-there-any-alternatives/66552678#66552678

        fun setupCalendarEvent(entry: Entry) {
            val intent = Intent(Intent.ACTION_EDIT)
            //intent.data = CalendarContract.CONTENT_URI
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra(CalendarContract.Events.TITLE, entry.content)
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, entry.tags.toString())
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, entry.location)
            intent.putExtra(CalendarContract.Events.DTSTART, entry.dueDate!!.toLong())
            if(entry.recurring != "NONE" && entry.recurring.isNotBlank())
                intent.putExtra(CalendarContract.Events.RRULE, "FREQ=${entry.recurring};COUNT=${entry.recurCount}")
            intent.putExtra(CalendarContract.Events.DTEND, entry.dueDate!!.toLong() + 1)

            if (intent.resolveActivity(fragment.requireContext().packageManager) != null) {
                // fragment.requireContext().startActivity(intent)
                Log.d(Constants.TAG, "-- APP support intent")
            } else {
                // Snackbar.make( itemView, "There is no App supporting this intent", Snackbar.LENGTH_SHORT ).show()
                Log.d(Constants.TAG, "There is no App supporting this intent")
            }
            fragment.requireContext().startActivity(intent)
        }

    }
}