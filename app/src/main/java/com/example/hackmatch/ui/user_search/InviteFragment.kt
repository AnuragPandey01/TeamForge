package com.example.hackmatch.ui.user_search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentInviteBinding
import com.example.hackmatch.ui.profile.adapter.ExperienceAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class InviteFragment : Fragment(R.layout.fragment_invite) {

    val viewModel: InviteViewModel by viewModels()
    private var _binding: FragmentInviteBinding? = null
    private val binding get() = _binding!!
    private val experienceAdapter by lazy { ExperienceAdapter(emptyList()) }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInviteBinding.bind(view)

        binding.experienceRecyclerView.adapter = experienceAdapter

        binding.inviteButton.setOnClickListener {
            viewModel.inviteUser()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when(it) {
                is InviteScreenState.Idle -> {
                    // Do nothing
                }

                is InviteScreenState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                    binding.userInfoLayout.visibility = View.GONE
                }

                is InviteScreenState.Data -> {
                    binding.progressIndicator.visibility = View.GONE
                    binding.userInfoLayout.visibility = View.VISIBLE

                    binding.userName.text = it.data.name
                    binding.userTitle.text = it.data.role
                    Glide.with(requireContext())
                        .load(it.data.iconUrl)
                        .into(binding.userAvatar)

                    it.data.tags.forEach { tag->
                        val chip = Chip(requireContext())
                        chip.text = tag.name
                        chip.isCheckable = true
                        chip.isChecked = true
                        chip.isCloseIconVisible = false
                        binding.skillsChipGroup.addView(chip)
                    }
                    experienceAdapter.setData(it.data.experiences)
                }

                is InviteScreenState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.error)
                        .setBackgroundColor(R.color.error_container)
                        .setDuration(5000)
                        .show()
                }

                is InviteScreenState.InviteSending -> {
                    binding.inviteProgressIndicator.visibility = View.VISIBLE
                    binding.inviteButton.visibility = View.GONE
                }

                is InviteScreenState.InviteSent -> {
                    binding.inviteProgressIndicator.visibility = View.GONE
                    binding.inviteButton.visibility = View.GONE
                    CookieBar.build(requireActivity())
                        .setTitle("Success")
                        .setMessage(it.message)
                        .setBackgroundColor(R.color.green)
                        .setDuration(5000)
                        .show()
                }

                is InviteScreenState.InviteError -> {
                    binding.inviteProgressIndicator.visibility = View.GONE
                    binding.inviteButton.visibility = View.VISIBLE
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.error)
                        .setBackgroundColor(R.color.error_container)
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