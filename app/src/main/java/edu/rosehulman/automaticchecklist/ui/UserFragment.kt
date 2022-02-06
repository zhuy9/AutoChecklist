package edu.rosehulman.automaticchecklist.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.R
import edu.rosehulman.automaticchecklist.databinding.FragmentUserBinding
import edu.rosehulman.automaticchecklist.models.UserViewModel


class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var userModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        binding.logoutButton.setOnClickListener {
            userModel.user = null
            Firebase.auth.signOut()
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_user_edit)
        }
        updateView()
        return binding.root
    }

    private fun updateView(){
        userModel.getOrMakeUser {
            with(userModel.user!!) {
                Log.d(Helpers.TAG, "$this")
                binding.userName.setText(name)
                binding.userAge.setText(age.toString())
            }
        }
    }
}