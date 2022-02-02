package edu.rosehulman.automaticchecklist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.automaticchecklist.Entry
import edu.rosehulman.automaticchecklist.EntryAdapter
import edu.rosehulman.automaticchecklist.Frequency
import edu.rosehulman.automaticchecklist.databinding.FragmentInboxBinding


class InboxFragment : Fragment() {

    // private lateinit var entriesViewModel: EntriesViewModel
    private lateinit var binding: FragmentInboxBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        val adapter = EntryAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.fab.setOnClickListener {
            var entry: Entry = Entry()
            entry.content = "InboxFrag"
            entry.location = "Classroom"
            entry.recurring = Frequency.EVERY_DAY.toString()
            adapter.addEntry(entry)
        }

        return binding.root
    }

}