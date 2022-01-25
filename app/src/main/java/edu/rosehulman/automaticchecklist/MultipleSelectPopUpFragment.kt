package edu.rosehulman.automaticchecklist

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.fragment.app.DialogFragment

class MultipleSelectPopUpFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val selectedLabels = ArrayList<Int>()
            val builder = AlertDialog.Builder(it).setTitle("Choose the labels")
            builder.setMultiChoiceItems(
                Helpers.defaultLabelArray.toTypedArray(),
                null
            ) { dialog, index, checked ->
                if (checked) {
                    selectedLabels.add(index)
                } else if (selectedLabels.contains(index)) {
                    selectedLabels.remove(index)
                }
            }

            var input = EditText(context)
            input.hint = "Make a new label"
            builder.setView(input)
            /*
             val button = Button(context)
            button.text = "CREATE NEW"
            button.setOnClickListener {
                val newLabel = input.text.toString()
                if (newLabel.isNotEmpty() and newLabel.isNotBlank()) {
                    Log.d(Helpers.TAG, "CREATE NEW : $newLabel")
                    Helpers.defaultLabelArray.add(newLabel)
                    selectedLabels.add(Helpers.defaultLabelArray.indexOf(newLabel))

                    // update the selected items
                    builder.setMultiChoiceItems(
                        Helpers.defaultLabelArray.toTypedArray(),
                        null
                    ) { dialog, index, checked ->
                        if (checked) {
                            selectedLabels.add(index)
                        } else if (selectedLabels.contains(index)) {
                            selectedLabels.remove(index)
                        }
                    }
                }
            }

            builder.setView(button)
             */
            builder.setNeutralButton("CREATE NEW") { dialog, id ->
                val newLabel = input.text.toString()
                if(newLabel.isNotEmpty() and newLabel.isNotBlank()) {
                    Log.d(Helpers.TAG, "CREATE NEW : $newLabel")
                    Helpers.defaultLabelArray.add(newLabel)
                    selectedLabels.add(Helpers.defaultLabelArray.indexOf(newLabel))

                    // update the selected items
                    builder.setMultiChoiceItems(
                        Helpers.defaultLabelArray.toTypedArray(),
                        null
                    ) { dialog, index, checked ->
                        if (checked) {
                            selectedLabels.add(index)
                        } else if (selectedLabels.contains(index)) {
                            selectedLabels.remove(index)
                        }
                    }
                }
            }

            builder.setNegativeButton("CANCEL", null)

            builder.setPositiveButton(
                "OK"
            ) { dialog, id ->
                Log.d(Helpers.TAG, "OK : $selectedLabels")
            }
            builder.create()
        } ?: throw IllegalStateException("MultipleSelectPopUpFrag: err")
    }
}