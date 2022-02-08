package edu.rosehulman.automaticchecklist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.rosehulman.automaticchecklist.R
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.Helpers.selectItem
import edu.rosehulman.automaticchecklist.adapters.LabelSelectAdapter
import edu.rosehulman.automaticchecklist.databinding.FragmentEntryEditBinding
import edu.rosehulman.automaticchecklist.models.EntriesViewModel
import edu.rosehulman.automaticchecklist.models.Entry
import edu.rosehulman.automaticchecklist.models.UserViewModel

class EntryEditFragment : Fragment() {

    lateinit var entriesModel: EntriesViewModel
    lateinit var userModel: UserViewModel
    lateinit var binding: FragmentEntryEditBinding
    lateinit var currentEntry: Entry
    lateinit var labelSelectAdapter: LabelSelectAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        entriesModel = ViewModelProvider(requireActivity()).get(EntriesViewModel::class.java)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding = FragmentEntryEditBinding.inflate(inflater, container, false)
        currentEntry = entriesModel.getCurrentEntry()
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        /* set listener: add new labels */
        binding.entryEditLabelConfirmButton.setOnClickListener {
            userModel.addLabel(binding.entryEditLabelNewInput.text.toString())
            labelSelectAdapter.update()
        }

        /* set listener: toggle freq dropdown & date selection */
        binding.entryEditCheckbox.setOnClickListener {
            if (binding.entryEditFrequency.isVisible) {
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

        /* set listener: jump to calendar pop up window */
        /* reference: https://material.io/components/date-pickers/android#using-date-pickers */
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText(R.string.select_due_date).build()
        picker.addOnPositiveButtonClickListener {
            binding.entryEditChooseDate.setText(Helpers.parseDateFromMs(it))
            currentEntry.dueDate = it.toString() // update current entry on save
        }
        binding.entryEditCalendarIcon.setOnClickListener {
            picker.show(parentFragmentManager, Helpers.tag)
        }

        /* set listener: save button, return to inbox if successful */
        binding.entryEditSaveButton.setOnClickListener {
            val content = binding.entryEditText.text.toString()
            if (content.isNullOrBlank()) {
                binding.entryEditWarning1.visibility = VISIBLE
                return@setOnClickListener
            }
            binding.entryEditWarning1.visibility = INVISIBLE
            currentEntry.content = content

            val locString = binding.entryEditLocation.text.toString()
            if (locString.isNotBlank())
                currentEntry.location = locString
            if (binding.entryEditFrequency.isVisible) {
                currentEntry.dueDate = null
                val recurString = binding.entryEditFrequencyText.text.toString()
                if (recurString.isNotBlank())
                    currentEntry.recurring = recurString
            } else {
                currentEntry.recurring = Frequency.NONE.toString()
            }
            Log.d(
                Helpers.TAG,
                "-\t\nContent: ${currentEntry.content}\n" +
                        "dueDate: ${currentEntry.dueDate}\n" +
                        "loc: ${currentEntry.location}\n" +
                        "freq: ${currentEntry.recurring}\n" +
                        "tags: ${currentEntry.tags}"
            )
            entriesModel.updateCurrentEntry(currentEntry)
            userModel.resetUndoMemory()
            findNavController().navigate(R.id.navigation_inbox)
        }
    }

    private fun setupView() {
        /* initialize the form by prefilling data */
        if (currentEntry.content.isNotBlank()) {
            binding.entryEditWarning1.visibility = INVISIBLE
            binding.entryEditText.setText(currentEntry.content)
        } else {
            binding.entryEditWarning1.visibility = INVISIBLE /* hide warning for content */
        }
        if (currentEntry.location.isNotBlank()) {
            binding.entryEditLocation.setText(currentEntry.location)
        }

        /* initialize frequency dropdown */
        if (currentEntry.recurring != Frequency.NONE.toString()) {
            binding.entryEditFrequency.visibility = VISIBLE
            binding.entryEditFrequencyText.selectItem(
                currentEntry.recurring, // string
                Helpers.indexOfFrequency(currentEntry.recurring) // index
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

        /* initialize freq dropdown */
        binding.entryEditFrequencyText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Helpers.parseFrequencyToArray(filterOutNone = true)
            )
        )

        /* initialize date text view */
        if (currentEntry.dueDate != null) {
            binding.entryEditChooseDate.setText(Helpers.parseDateFromMs(currentEntry.dueDate!!.toLong()))
        } else {
            //TODO populate with existing date if in edit mode
            binding.entryEditChooseDate.hint =
                Helpers.parseDateFromMs(MaterialDatePicker.todayInUtcMilliseconds())
        }

        /* initialize select labels adapter */
        labelSelectAdapter = LabelSelectAdapter(this, currentEntry)
        binding.entryEditLabelRecyclerView.adapter = labelSelectAdapter
        binding.entryEditLabelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}