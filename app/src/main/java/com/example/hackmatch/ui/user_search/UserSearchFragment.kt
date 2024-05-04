package com.example.hackmatch.ui.user_search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentUserSearchBinding
import com.example.hackmatch.ui.user_search.adapter.UserSearchAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class UserSearchFragment : Fragment(R.layout.fragment_user_search) {

    companion object {
        fun newInstance() = UserSearchFragment()
    }
    private val args by navArgs<UserSearchFragmentArgs>()
    private val viewModel: UserSearchViewModel by viewModels()
    private var _binding : FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private val adapter : UserSearchAdapter by lazy { UserSearchAdapter(emptyList()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserSearchBinding.bind(view)

        adapter.setOnItemClickListener {
            findNavController().navigate(
                UserSearchFragmentDirections.actionUserSearchFragmentToInviteFragment(teamID = args.teamID, userID = it.id)
            )
        }
        binding.userSearchRecyclerView.adapter = adapter

        viewModel.tags.observe(viewLifecycleOwner){
             it.forEach { tag ->
                val chip = Chip(requireContext())
                chip.text = tag.name
                chip.isCheckable = true
                 chip.isCloseIconVisible = false
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    viewModel.selectTag(tag.id)
                }
                binding.chipGroup.addView(chip)
             }
        }

        viewModel.userSearchState.observe(viewLifecycleOwner) {
            when(it) {
                is UserSearchState.Idle -> {
                    // Do nothing
                }

                is UserSearchState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.userSearchRecyclerView.visibility = View.GONE
                }

                is UserSearchState.Data -> {
                    binding.progressBar.visibility = View.GONE
                    binding.userSearchRecyclerView.visibility = View.VISIBLE
                    adapter.addSearchResult(it.data)
                }
                is UserSearchState.Error -> {
                    // Handle error
                    binding.apply {
                        progressBar.visibility = View.GONE
                        userSearchRecyclerView.visibility = View.GONE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.message)
                        .setBackgroundColor(R.color.error_container)
                        .setDuration(5000)
                        .show()
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}