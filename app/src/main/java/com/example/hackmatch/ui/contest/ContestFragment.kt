package com.example.hackmatch.ui.contest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentContestBinding
import com.example.hackmatch.ui.contest.adapter.EventAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class ContestFragment : Fragment(R.layout.fragment_contest) {

    private val viewModel : ContestViewModel by viewModels()
    private var _binding :FragmentContestBinding? = null
    private val binding : FragmentContestBinding
        get() = _binding!!
    private val eventAdapter : EventAdapter by lazy{
        EventAdapter(emptyList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContestBinding.bind(view)

        setUpRecyclerView()
        setUpListeners()
        observeState()
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ContestFragmentState.Idle -> {}
                is ContestFragmentState.Loading -> {
                    binding.apply {
                        progressBar.visibility = View.VISIBLE
                        rvEvents.visibility = View.GONE
                        noEventsText.visibility = View.GONE
                    }
                }

                is ContestFragmentState.Data -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        rvEvents.visibility = View.VISIBLE
                        noEventsText.visibility = View.GONE
                        eventAdapter.addList(it.events)
                    }
                }

                is ContestFragmentState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.msg)
                        .setDuration(5000)
                        .show()
                    binding.noEventsText.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.fabPostEvent.setOnClickListener {
            findNavController().navigate(R.id.action_contestFragment_to_postContestFragment)
        }
    }

    private fun setUpRecyclerView() {
        eventAdapter.addOnShare {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey! Check out ${it.title} \n link:${it.link} \n let's participate together!"
                )
                type = "text/plain"
            }.let {
                startActivity(Intent.createChooser(it, "share via"))
            }
        }
        eventAdapter.addOnMore {
            findNavController().navigate(
                ContestFragmentDirections.actionContestFragmentToDetailContestFragment(it)
            )
        }
        binding.rvEvents.adapter = eventAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}