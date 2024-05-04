package com.example.hackmatch.ui.team

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentTeamDetailBinding
import com.example.hackmatch.ui.team.adapter.TeamMemberAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class TeamDetailFragment : Fragment(R.layout.fragment_team_detail) {

    val viewModel: TeamDetailViewModel by viewModels()
    private var _binding: FragmentTeamDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<TeamDetailFragmentArgs>()
    private val adapter by lazy{
        TeamMemberAdapter(emptyList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamDetailBinding.inflate(inflater, container, false)
        viewModel.getTeamInfo(args.teamId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.teamMembers.adapter = adapter
        binding.btnAddMember.setOnClickListener {
            findNavController().navigate(
                TeamDetailFragmentDirections.actionTeamDetailFragmentToUserSearchFragment(args.teamId)
            )
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when(state) {
                is TeamDetailState.Idle -> {
                    binding.progressBar.visibility = View.GONE
                }

                is TeamDetailState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is TeamDetailState.Success -> {
                    Toast.makeText(requireContext(), state.teamInfo.toString() , Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    adapter.setData(state.teamInfo)
                    binding.teamName.text = state.teamInfo[0].teamName
                    Glide.with(requireContext())
                        .load("https://api.dicebear.com/8.x/shapes/jpeg?seed=${state.teamInfo[0].teamName}")
                        .into(binding.teamLogo)

                    if(state.teamInfo.first { it.isLeader }.userId == state.userId) {
                        binding.btnAddMember.visibility = View.VISIBLE
                    }else{
                        binding.btnAddMember.visibility = View.GONE
                    }

                }

                is TeamDetailState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(state.message)
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