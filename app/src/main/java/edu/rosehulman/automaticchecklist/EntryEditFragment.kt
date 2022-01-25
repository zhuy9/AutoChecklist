package edu.rosehulman.automaticchecklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import edu.rosehulman.automaticchecklist.databinding.FragmentEntryEditBinding
import edu.rosehulman.automaticchecklist.ui.EntriesViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

        // hide frequency dropdown
        binding.entryEditFrequency.visibility = GONE

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
        binding.entryEditFrequencyText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Helpers.parseFrequencyToArray()
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
            } else {
                binding.entryEditWarning1.visibility = INVISIBLE
            }
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
        binding.entryEditLabels.setOnClickListener {
            MultipleSelectPopUpFragment().show(parentFragmentManager, "mydialog")
        }
    }

    fun updateView() {
        // TODO for edit details
    }
}