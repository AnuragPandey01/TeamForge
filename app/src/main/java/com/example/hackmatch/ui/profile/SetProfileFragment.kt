package com.example.hackmatch.ui.profile

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.databinding.FragmentSetProfileBinding
import com.example.hackmatch.ui.profile.adapter.ExperienceAdapter
import com.example.hackmatch.util.toFormattedDateString
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar
import java.util.Locale
import javax.net.ssl.SSLParameters

@AndroidEntryPoint
class SetProfileFragment : Fragment(R.layout.fragment_set_profile) {

    private val viewModel: SetProfileViewModel by viewModels()
    private var _binding : FragmentSetProfileBinding? = null
    private val binding get() = _binding!!
    private val experienceAdapter by lazy {
        ExperienceAdapter(emptyList())
    }
    private val autoCompleteAdapter by lazy{
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            emptyList<String>()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSetProfileBinding.bind(view)

        setFragmentResultListener(AddExperienceFragment.Result.KEY){ _, bundle ->
            if(!bundle.getBoolean(AddExperienceFragment.Result.TO_UPDATE,false)){
                return@setFragmentResultListener
            }
            viewModel.addExperience(
                User.ExperienceData(
                    title = bundle.getString(AddExperienceFragment.Result.TITLE,""),
                    description = bundle.getString(AddExperienceFragment.Result.DESCRIPTION,""),
                    startDate = bundle.getLong(AddExperienceFragment.Result.START_DATE,System.currentTimeMillis()).toFormattedDateString(),
                    endDate = bundle.getLong(AddExperienceFragment.Result.END_DATE,System.currentTimeMillis()).toFormattedDateString(),
                    link = bundle.getString(AddExperienceFragment.Result.LINK,"")
                )
            )
        }

        //set up
        binding.apply{

            //recyclerview adapter
            rvExperience.adapter = experienceAdapter

            //button clicks
            btnFinish.setOnClickListener {
                viewModel.updateProfile(
                    etTitle.text.toString(),
                    viewModel.selectedTags.value ?: emptySet(),
                    viewModel.experienceData.value ?: emptyList()
                )
            }

            //autocomplete setup
            binding.etSkill.setOnFocusChangeListener { _, _ ->
                binding.etSkill.showDropDown()
            }
            binding.skillLayout.setEndIconOnClickListener {
                viewModel.addSelectedTag(binding.etSkill.text.toString())
                binding.etSkill.editableText.clear()
            }

            binding.btnAddExperience.setOnClickListener {
                findNavController().navigate(
                    R.id.action_setProfileFragment_to_addExperienceFragment
                )
            }
        }

        //autocomplete add when tag data loaded
        viewModel.tagData.observe(viewLifecycleOwner){ list->
            if(list.isEmpty()){
                return@observe
            }

            binding.etSkill.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    list.map { it.name }
                )
            )
        }

        //handle selected tags
        viewModel.selectedTags.observe(viewLifecycleOwner){list ->
            binding.tagGroup.removeAllViews()
            list.forEach {
                binding.tagGroup.addView(
                    createTagChip(requireContext(),it.name){
                        viewModel.removeSelectedTag(it)
                    }
                )
            }
        }

        //handle experiences
        viewModel.experienceData.observe(viewLifecycleOwner){
            if(it.isEmpty()){
                return@observe
            }
            experienceAdapter.addData(it[it.lastIndex])
        }

        //observe state
        viewModel.state.observe(viewLifecycleOwner){
            when(it){
                is SetProfileState.Idle -> {
                    binding.apply {
                        btnFinish.visibility = View.VISIBLE
                    }
                }
                is SetProfileState.Loading ->{
                    binding.apply {
                        btnFinish.visibility = View.GONE
                    }
                }
                is SetProfileState.Success ->{
                    findNavController().navigate(
                        R.id.action_setProfileFragment_to_homeFragment
                    )
                }
                is SetProfileState.Error ->{
                    binding.apply {
                        btnFinish.visibility = View.VISIBLE
                    }
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.msg)
                        .setIcon(R.drawable.ic_error)
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

    private fun createTagChip(
        context: Context,
        chipName:String,
        onCloseIconClick: ()->Unit
    ): Chip{
        return Chip(context).apply {
            text = chipName
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                onCloseIconClick()
            }
        }
    }

}