package com.example.hackmatch.ui.invitation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentInvitationsBinding
import com.example.hackmatch.databinding.FragmentInviteBinding
import com.example.hackmatch.ui.invitation.adapter.InvitationAdapter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Cookie
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class InvitationsFragment : Fragment(R.layout.fragment_invitations) {

    private var _binding: FragmentInvitationsBinding? = null
    private val binding:FragmentInvitationsBinding
        get() = _binding!!
    private val adapter by lazy {
        InvitationAdapter(emptyList())
    }
    private val viewModel : InvitationsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInvitationsBinding.bind(view)

        adapter.setOnAcceptClick {
            viewModel.acceptTeamInvitation(it.teamId)
        }
        adapter.setOnRejectClick {
            viewModel.rejectTeamInvitation(it.teamId)
        }
        binding.invitationsRecyclerView.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner){
            when(it){
                is InvitationScreenState.Idle ->{}
                is InvitationScreenState.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is InvitationScreenState.Data ->{
                    binding.progressBar.visibility = View.GONE
                    adapter.setData(it.invites)
                }
                is InvitationScreenState.Error ->{
                    binding.progressBar.visibility = View.GONE
                    CookieBar.build(requireActivity())
                        .setTitle("error")
                        .setMessage(it.msg)
                        .setBackgroundColor(R.color.error_container)
                        .setDuration(5000)
                        .setIcon(R.drawable.ic_error)
                        .show()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }



}