package com.example.hackmatch.ui.profile

import android.app.DatePickerDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.databinding.FragmentAddExperienceBinding
import com.example.hackmatch.util.toFormattedDateString
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AddExperienceFragment : Fragment(R.layout.fragment_add_experience) {

    private var _binding: FragmentAddExperienceBinding? = null
    private val binding: FragmentAddExperienceBinding
        get() = _binding!!
    private val viewModel: AddExperienceViewModel by viewModels()
    private val startDateCalender by lazy{ Calendar.getInstance()}
    private val endDateCalender by lazy{ Calendar.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddExperienceBinding.bind(view)

        binding.etStartDate.setOnClickListener {
            createDatePickerDialog(startDateCalender){  year, month, dayOfMonth ->
                startDateCalender.set(year, month, dayOfMonth)
                if(startDateCalender.timeInMillis > endDateCalender.timeInMillis){
                    binding.endDateInputLayout.error = "End date should be after start date"
                    return@createDatePickerDialog
                }
                endDateCalender.set(year, month, dayOfMonth)
                binding.etStartDate.setText(startDateCalender.timeInMillis.toFormattedDateString())
            }
        }

        binding.etEndDate.setOnClickListener {
            createDatePickerDialog(endDateCalender) { year, month, dayOfMonth ->
                endDateCalender.set(year, month, dayOfMonth)
                if (startDateCalender.timeInMillis > endDateCalender.timeInMillis) {
                    binding.endDateInputLayout.error = "End date should be after start date"
                    return@createDatePickerDialog
                }
                binding.etEndDate.setText(endDateCalender.timeInMillis.toFormattedDateString())
            }
        }

        binding.btnSave.setOnClickListener {
            if(startDateCalender.timeInMillis > endDateCalender.timeInMillis){
                binding.endDateInputLayout.error = "End date should be after start date"
                return@setOnClickListener
            }
            setFragmentResult(
                Result.KEY,
                bundleOf(
                    Result.TO_UPDATE to true,
                    Result.TITLE to binding.etName.text.toString(),
                    Result.DESCRIPTION to binding.etDescription.text.toString(),
                    Result.LINK to binding.etLink.text.toString(),
                    Result.START_DATE to startDateCalender.timeInMillis,
                    Result.END_DATE to endDateCalender.timeInMillis
                )
            )
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }

    companion object {
        val Result = _Result
        data object _Result {
            const val KEY: String = "newExperience"
            const val TO_UPDATE: String = "toUpdate"
            const val TITLE: String = "title"
            const val DESCRIPTION: String = "description"
            const val START_DATE: String = "startDa.te"
            const val END_DATE: String = "endDate"
            const val LINK: String = "link"
        }
    }

    private fun createDatePickerDialog(calendar: Calendar, dateSetter: (Int, Int, Int) -> Unit) {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                dateSetter(year,month,dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}