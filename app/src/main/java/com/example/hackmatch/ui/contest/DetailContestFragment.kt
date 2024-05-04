package com.example.hackmatch.ui.contest

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentDetailContestBinding
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class DetailContestFragment : Fragment(R.layout.fragment_detail_contest) {


    private val viewModel: DetailContestViewModel by viewModels()
    private var _binding: FragmentDetailContestBinding? = null
    private val binding get() = _binding!!
    private val argsLazy by lazy {
        DetailContestFragmentArgs.fromBundle(requireArguments())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailContestBinding.bind(view)

        binding.createTeam.setOnClickListener {
            viewModel.createTeam()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.eventTitle.text = argsLazy.eventData.title
        binding.eventDescription.text = argsLazy.eventData.description
        binding.eventDate.text = "${argsLazy.eventData.startDate}-${argsLazy.eventData.endDate}"
        binding.eventLocation.text = argsLazy.eventData.location
        binding.eventType.text = argsLazy.eventData.eventType

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailContestState.Idle -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        createTeam.visibility = View.VISIBLE
                    }
                }

                is DetailContestState.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        createTeam.visibility = View.GONE
                    }
                }
                is DetailContestState.Success -> {
                    findNavController().navigate(
                        DetailContestFragmentDirections.actionDetailContestFragmentToUserSearchFragment(
                            state.teamID
                        )
                    )
                }
                is DetailContestState.Error -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        createTeam.visibility = View.VISIBLE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(state.msg)
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