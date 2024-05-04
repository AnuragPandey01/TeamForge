package com.example.hackmatch.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                email = binding.etEmail.text.toString(),
                password = binding.etPass.text.toString(),
                confirmPassword = binding.etCnfPass.text.toString(),
                instituteName = binding.etInstituteName.text.toString(),
                name = binding.etName.text.toString()
            )
        }
        binding.tvLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        viewModel.signupState.observe(viewLifecycleOwner){
            when(it){

                is SignupState.InvalidInput -> {
                    binding.apply {
                        btnSignup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.message)
                        .setBackgroundColor(R.color.error_container)
                        .setIcon(R.drawable.ic_error)
                        .setDuration(3000)
                        .show()
                }

                is SignupState.Loading -> {
                    binding.apply {
                        btnSignup.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                }

                is SignupState.Success -> {
                    findNavController().navigate(R.id.action_signupFragment_to_setProfileFragment)
                }

                is SignupState.Error -> {
                    binding.apply {
                        btnSignup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.message)
                        .setBackgroundColor(R.color.error_container)
                        .setIcon(R.drawable.ic_error)
                        .setDuration(5000)
                        .show()
                }

                is SignupState.Idle -> {
                    binding.apply {
                        btnSignup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}