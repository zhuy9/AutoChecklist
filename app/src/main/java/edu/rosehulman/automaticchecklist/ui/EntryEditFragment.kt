package edu.rosehulman.automaticchecklist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import edu.rosehulman.automaticchecklist.R
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.adapters.LabelSelectAdapter
import edu.rosehulman.automaticchecklist.databinding.FragmentEntryEditBinding
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import java.time.LocalDate

class EntryEditFragment : Fragment() {

    lateinit var model: EntriesViewModel
    lateinit var binding: FragmentEntryEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(requireActivity()).get(EntriesViewModel::class.java)
        binding = FragmentEntryEditBinding.inflate(inflater, container, false)

        setups()
        updateView()

        return binding.root
    }

    fun setups() {
        // prefill the form
        val currentEntry = model.getCurrentEntry()
        if (currentEntry.content.isNotBlank()) {
            binding.entryEditText.setText(currentEntry.content)
        }
        if (currentEntry.location.isNotBlank()) {
            binding.entryEditLocation.setText(currentEntry.location)
        }
        // set frequency dropdown
        if (currentEntry.recurring != Frequency.NONE.toString()) {
            binding.entryEditFrequency.visibility = VISIBLE
            binding.entryEditFrequencyText.selectItem(
                currentEntry.recurring, // string
                enumValues<Frequency>().indexOf(
                    Frequency.valueOf(
                        currentEntry.recurring.replace(" ", "_")
                    )
                ) // index
            )
            binding.entryEditCheckbox.setImageResource(Entry.checkboxCheckedIconSource)
            binding.entryEditChooseDate.isEnabled = false
            binding.entryEditCalendarIcon.isEnabled = false
        } else {
            binding.entryEditChooseDate.isEnabled = true
            binding.entryEditCalendarIcon.isEnabled = true
            binding.entryEditCheckbox.setImageResource(Entry.checkboxNotCheckedIconSource)
            binding.entryEditFrequency.visibility = GONE
        }

        // hide warning for content
        binding.entryEditWarning1.visibility = INVISIBLE

        // toggle freq dropdown & date selection
        binding.entryEditCheckbox.setOnClickListener {
            if (binding.entryEditFrequency.isVisible) {
                // TODO set source to constant
                binding.entryEditCheckbox.setImageResource(Entry.checkboxNotCheckedIconSource)
                binding.entryEditFrequency.visibility = GONE
                binding.entryEditChooseDate.isEnabled = true
                binding.entryEditCalendarIcon.isEnabled = true
            } else {
                binding.entryEditCheckbox.setImageResource(Entry.checkboxCheckedIconSource)
                binding.entryEditFrequency.visibility = VISIBLE
                binding.entryEditChooseDate.isEnabled = false
                binding.entryEditCalendarIcon.isEnabled = false
            }
        }
        // set freq dropdown
        // TODO SET to user options
        binding.entryEditFrequencyText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Helpers.parseFrequencyToArray() // TODO
            )
        )
        // set current Selection to N/A
        binding.entryEditChooseDate.setText("N/A")

        // save button, return to inbox if successful
        binding.entryEditSaveButton.setOnClickListener {
            val content = binding.entryEditText.text.toString()
            // TODO more to be saved
            if (content.isNullOrBlank()) {
                binding.entryEditWarning1.visibility = VISIBLE
                return@setOnClickListener
            }

            binding.entryEditWarning1.visibility = INVISIBLE
            currentEntry.content = content
            // IF frequency is NONE
            // val checkedStat: Boolean = !binding.entryEditChooseDate.isEnabled
            val dueLocalDate: LocalDate? = null
            val locString = binding.entryEditLocation.text.toString()
            if (locString.isNotBlank())
                currentEntry.location = locString
            val recurString = binding.entryEditFrequencyText.text.toString()
            if (recurString.isNotBlank())
                currentEntry.recurring = recurString
            Log.d(
                Helpers.TAG,
                "\n\nContent: $content\ndueDate: $dueLocalDate\nloc: ${currentEntry.location}\nfreq: ${currentEntry.recurring}\ntags: ${currentEntry.tags}"
            )
            model.updateCurrentEntry(currentEntry)
            findNavController().navigate(R.id.navigation_inbox)
        }

        // jump to calendar pop up window
        binding.entryEditCalendarIcon.setOnClickListener {
            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(System.currentTimeMillis())
                    .setTitleText("Select Due Date").build()

            picker.addOnPositiveButtonClickListener {
                binding.entryEditChooseDate.setText(Helpers.parseDate(it))
            }

            // TODO make TAG constant
            picker.show(parentFragmentManager, "TAG")
        }

        // select labels
        var adapter = LabelSelectAdapter(this, currentEntry)
        binding.entryEditLabelRecyclerView.adapter = adapter
        binding.entryEditLabelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun AutoCompleteTextView.selectItem(text: String, position: Int = 0) {
        this.setText(text)
        //this.showDropDown() // expand dropdown
        this.setSelection(position)
        this.listSelection = position
        this.performCompletion()
    }

    fun updateView() {
        // TODO for edit details
        // binding.entryEditLabels.text = Helpers.arrayToString(model.getCurrentEntry().tags)


    }
}