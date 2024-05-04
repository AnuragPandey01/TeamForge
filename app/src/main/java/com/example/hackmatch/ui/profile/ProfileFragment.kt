package com.example.hackmatch.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentProfileBinding
import com.example.hackmatch.ui.profile.adapter.ExperienceAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ExperienceAdapter(emptyList()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        binding.experienceRecyclerView.adapter = adapter
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(
                R.id.action_profileFragment_to_onBoardingFragment
            )
        }
        binding.invitationsCardView.setOnClickListener {
            findNavController().navigate(
                R.id.action_profileFragment_to_invitationsFragment
            )
        }

        viewModel.state.observe(viewLifecycleOwner){
            when(it){
                is ProfileState.Idle ->{}
                is ProfileState.Loading ->{
                    binding.apply {
                        progressIndicator.visibility = View.VISIBLE
                        userInfoLayout.visibility = View.GONE
                    }
                }
                is ProfileState.Data->{
                    binding.progressIndicator.visibility = View.GONE
                    binding.userInfoLayout.visibility = View.VISIBLE

                    binding.userName.text = it.user.name
                    binding.userTitle.text = it.user.role
                    Glide.with(requireContext())
                        .load(it.user.iconUrl)
                        .into(binding.userAvatar)

                    it.user.tags.forEach { tag->
                        val chip = Chip(requireContext())
                        chip.text = tag.name
                        chip.isChecked = true
                        chip.isCheckable = false
                        chip.isCloseIconVisible = false
                        chip.isClickable = false
                        binding.skillsChipGroup.addView(chip)
                    }
                    adapter.setData(it.user.experiences)
                    binding.invitationsCount.text = it.user.teams.filter { !it.invitationAccepted }.size.toString()
                }
                is ProfileState.Error ->{
                    binding.apply {
                        progressIndicator.visibility = View.GONE
                        userInfoLayout.visibility = View.GONE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.message)
                        .setBackgroundColor(R.color.error_container)
                        .setIcon(R.drawable.ic_error)
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