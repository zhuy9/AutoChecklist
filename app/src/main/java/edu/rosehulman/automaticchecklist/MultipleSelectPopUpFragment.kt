package edu.rosehulman.automaticchecklist

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import edu.rosehulman.automaticchecklist.databinding.FragmentTextButtonBinding
import edu.rosehulman.automaticchecklist.ui.EntriesViewModel

class MultipleSelectPopUpFragment(var labels: ArrayList<Label>) : DialogFragment() {

    lateinit var model: EntriesViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        model = ViewModelProvider(requireActivity()).get(EntriesViewModel::class.java)

        return activity?.let {
            val selectedLabels = labels
            val builder = AlertDialog.Builder(it)
            val textArea = layoutInflater.inflate(R.layout.fragment_text_button, null)
            val addNewButton = textArea.findViewById<Button>(R.id.entry_edit_label_confirm_button)
            val addNewText = textArea.findViewById<EditText>(R.id.entry_edit_label_new_input)
            val currentUserLabels: ArrayList<Label> = Helpers.defaultLabelArray
            addNewButton.setOnClickListener {
                val newLabel = addNewText.text.toString()
                // TODO get current User

                if (newLabel.isNotEmpty()
                    && newLabel.isNotBlank()
                    && !Helpers.labelExistsIn(newLabel, currentUserLabels)
                ) {
                    Log.d(Helpers.TAG, "CREATE NEW LABEL : $newLabel")
                    val label = Label(newLabel)
                    // Helpers.defaultLabelArray.add(label)
                    selectedLabels.add(label)
                    currentUserLabels.add(label)

                    // TODO FIXME update the selected items
                    builder.setMultiChoiceItems(
                        Helpers.defaultLabelArray.map { label -> label.name }.toTypedArray(),
                        null
                    ) { dialog, index, checked ->
                        if (checked) {
                            selectedLabels.add(currentUserLabels.get(index))
                        } else if (selectedLabels.contains(currentUserLabels.get(index))) {
                            selectedLabels.remove(currentUserLabels.get(index))
                        }
                    }
                }
            }

            builder.setCancelable(false)
                .setTitle("Choose the labels")
                .setView(textArea)
                .setMultiChoiceItems(
                    Helpers.defaultLabelArray.map { label -> label.name }.toTypedArray(),
                    null
                ) { dialog, index, checked ->
                    if (checked) {
                        selectedLabels.add(currentUserLabels.get(index))
                    } else if (selectedLabels.contains(currentUserLabels.get(index))) {
                        selectedLabels.remove(currentUserLabels.get(index))
                    }
                }
//                .setNeutralButton("CREATE NEW") { dialog, id ->
//                    val newLabel = input.text.toString()
//                    if (newLabel.isNotEmpty() and newLabel.isNotBlank()) {
//                        Log.d(Helpers.TAG, "CREATE NEW : $newLabel")
//                        Helpers.defaultLabelArray.add(newLabel)
//                        selectedLabels.add(Helpers.defaultLabelArray.indexOf(newLabel))
//
//                        // update the selected items
//                        builder.setMultiChoiceItems(
//                            Helpers.defaultLabelArray.toTypedArray(),
//                            null
//                        ) { dialog, index, checked ->
//                            if (checked) {
//                                selectedLabels.add(index)
//                            } else if (selectedLabels.contains(index)) {
//                                selectedLabels.remove(index)
//                            }
//                        }
//                    }
//                }
                .setNegativeButton("CANCEL") { dialog, id ->
                    Log.d(Helpers.TAG, "MultiSelect CANCEL : $selectedLabels")
                    //dialog.dismiss()
                    //.observer()
                }
                .setPositiveButton(
                    "OK"
                ) { dialog, id ->
                    Log.d(Helpers.TAG, "MultiSelect OK : $selectedLabels")
                    //dialog.dismiss()
                    val currentTags = model.getCurrentEntry().tags
                    currentTags.clear()
                    currentTags.addAll(selectedLabels)

                }

            builder.create()
        } ?: throw IllegalStateException("MultipleSelectPopUpFrag: err")
    }

    fun updateView() {

    }

}