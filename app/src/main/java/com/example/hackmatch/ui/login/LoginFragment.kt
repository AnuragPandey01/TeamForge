package com.example.hackmatch.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.alpha
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.aviran.cookiebar2.CookieBar

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signupFragment
            )
        }

        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){

                    is LoginState.Idle -> {
                        binding.progressCircular.visibility = View.GONE
                    }

                    is LoginState.Loading -> {
                        binding.emailInputLayout.error = null
                        binding.passwordInputLayout.error = null
                        binding.progressCircular.visibility = View.VISIBLE
                    }

                    is LoginState.Success -> {
                        findNavController().navigate(
                            R.id.action_loginFragment_to_homeFragment
                        )
                    }

                    is LoginState.ValidationError -> {
                        binding.emailInputLayout.error = it.nameError
                        binding.passwordInputLayout.error = it.passwordError
                    }

                    is LoginState.Error -> {
                        binding.progressCircular.visibility = View.GONE
                        // Show error message
                        CookieBar.build(requireActivity())
                            .setTitle("Error")
                            .setMessage(it.message)
                            .setBackgroundColor(R.color.error_container)
                            .setCookiePosition(CookieBar.TOP)
                            .setDuration(5000)
                            .setIcon(R.drawable.ic_error)
                            .show()
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