package edu.rosehulman.automaticchecklist.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.automaticchecklist.adapters.EntryAdapter
import edu.rosehulman.automaticchecklist.Constants
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.databinding.FragmentInboxBinding


class InboxFragment : Fragment() {

    // private lateinit var entriesViewModel: EntriesViewModel
    private lateinit var binding: FragmentInboxBinding
    private lateinit var adapter: EntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestPermissions(null)
        binding = FragmentInboxBinding.inflate(inflater, container, false)
        adapter = EntryAdapter(this)
        binding.recyclerView.adapter = adapter
        adapter.addListener(fragmentName)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        binding.fab.setOnClickListener {
            Log.d(Constants.TAG, "FAB clicked!")
            adapter.model.addNew()
            //adapter.addEntry(null)
            findNavController().navigate(R.id.navigation_create)

        }

        return binding.root
    }

    fun requestPermissions(observer: (() -> Unit)?) {
        var permissions: Array<String> =
            arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
        val register =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if(it[Manifest.permission.READ_CALENDAR]!! && it[Manifest.permission.WRITE_CALENDAR]!! ){
                    Log.d(Constants.TAG, "GRANTED-------------------")

                } else {

                }
            }
        register.launch(permissions)
        if (observer != null) {
            observer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.removeListener(fragmentName)
    }

    companion object {
        const val fragmentName = "InboxFragment" // keep this way
    }
}