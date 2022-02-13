package edu.rosehulman.automaticchecklist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.automaticchecklist.adapters.SimpleViewEntryAdapter
import edu.rosehulman.automaticchecklist.databinding.FragmentCategoryBinding
import edu.rosehulman.automaticchecklist.models.UserViewModel

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
}