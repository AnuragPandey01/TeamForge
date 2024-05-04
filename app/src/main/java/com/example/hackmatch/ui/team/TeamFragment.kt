package com.example.hackmatch.ui.team

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentTeamBinding
import com.example.hackmatch.ui.team.adapter.TeamAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class TeamFragment : Fragment(R.layout.fragment_team) {

    private val viewModel : TeamViewModel by viewModels()
    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!
    private val teamAdapter by lazy {
        TeamAdapter(emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTeamBinding.bind(view)

        teamAdapter.setOnItemClickListener {
            findNavController().navigate(
                TeamFragmentDirections.actionTeamFragmentToTeamDetailFragment(it.teamId)
            )
        }
        binding.recyclerViewTeam.adapter = teamAdapter
        viewModel.teamState.observe(viewLifecycleOwner){
            when(it){
                is TeamState.Idle -> {
                }
                is TeamState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvNoTeam.visibility = View.GONE
                    binding.recyclerViewTeam.visibility = View.GONE
                }
                is TeamState.Data -> {
                    if(it.data.isEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoTeam.visibility = View.VISIBLE
                        binding.recyclerViewTeam.visibility = View.GONE
                    }else{
                        binding.progressBar.visibility = View.GONE
                        binding.tvNoTeam.visibility = View.GONE
                        binding.recyclerViewTeam.visibility = View.VISIBLE
                        teamAdapter.setData(it.data)
                    }
                }
                is TeamState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvNoTeam.visibility = View.GONE
                    binding.recyclerViewTeam.visibility = View.GONE

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