package com.example.hackmatch.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : Fragment() {


    private val viewmodel: OnBoardingViewModel by viewModels()
    private var _binding : FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)

        if(viewmodel.isUserLoggedIn()){
            findNavController().navigate(
                R.id.action_onBoardingFragment_to_homeFragment
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnLogin.setOnClickListener {
                findNavController().navigate(
                    R.id.action_onBoardingFragment_to_loginFragment
                )
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(
                    R.id.action_onBoardingFragment_to_signupFragment
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}