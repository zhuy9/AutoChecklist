package edu.rosehulman.automaticchecklist.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.Helpers.selectItem
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.adapters.EntryAdapter
import edu.rosehulman.automaticchecklist.adapters.SimpleViewEntryAdapter
import edu.rosehulman.automaticchecklist.databinding.FragmentCategoryBinding
import edu.rosehulman.automaticchecklist.databinding.FragmentInboxBinding
import edu.rosehulman.automaticchecklist.models.UserViewModel
import edu.rosehulman.automaticchecklist.ui.dashboard.DashboardViewModel

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter: SimpleViewEntryAdapter
    private lateinit var userModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        adapter = SimpleViewEntryAdapter(this, "NONE")
        binding.categoryRecyclerView.adapter = adapter
        // adapter.addListener(fragmentName)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecyclerView.setHasFixedSize(true)
        binding.categoryRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        var tempLabels: ArrayList<String> = userModel.user!!.labels.clone() as ArrayList<String>
        tempLabels.add("NONE")
        binding.categoryDropdownSelect.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                tempLabels.toTypedArray()
            )
        )


        binding.categoryDropdownSelect.addTextChangedListener {
            adapter = SimpleViewEntryAdapter(this, it.toString())
            binding.categoryRecyclerView.adapter = adapter
            // adapter.addListener(fragmentName)
            binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.categoryRecyclerView.setHasFixedSize(true)
            binding.categoryRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // adapter.removeListener(fragmentName)
    }

    companion object {
        const val fragmentName = "CategoryFragment" // keep this way
    }
}