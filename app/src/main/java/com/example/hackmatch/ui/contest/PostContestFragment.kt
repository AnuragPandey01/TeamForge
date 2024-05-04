package com.example.hackmatch.ui.contest

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hackmatch.R
import com.example.hackmatch.data.model.request.CreateEventRequest
import com.example.hackmatch.databinding.FragmentPostContestBinding
import com.example.hackmatch.util.toFormattedDateString
import dagger.hilt.android.AndroidEntryPoint
import org.aviran.cookiebar2.CookieBar
import java.util.Calendar

@AndroidEntryPoint
class PostContestFragment : Fragment(R.layout.fragment_post_contest) {

    private var _binding : FragmentPostContestBinding? = null
    private val binding : FragmentPostContestBinding
        get() = _binding!!
    private val viewModel : PostContestViewModel by viewModels()
    private val progressDialog : ProgressDialog by lazy {
        ProgressDialog(requireContext()).apply { setTitle("posting") }
    }
    private val startDateCalender by lazy{ Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }}
    private val endDateCalender by lazy{ Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPostContestBinding.bind(view)

        binding.etStartDate.setOnClickListener {
            createDatePickerDialog(startDateCalender){  year, month, dayOfMonth ->
                startDateCalender.set(year, month, dayOfMonth)
                if(startDateCalender.timeInMillis > endDateCalender.timeInMillis){
                    binding.endDateLayout.error = "End date should be after start date"
                    return@createDatePickerDialog
                }
                binding.etStartDate.setText(startDateCalender.timeInMillis.toFormattedDateString())
                endDateCalender.set(year, month, dayOfMonth)
            }
        }

        binding.etEndDate.setOnClickListener {
            createDatePickerDialog(endDateCalender) { year, month, dayOfMonth ->
                endDateCalender.set(year, month, dayOfMonth)
                if (startDateCalender.timeInMillis > endDateCalender.timeInMillis) {
                    binding.endDateLayout.error = "End date should be after start date"
                    return@createDatePickerDialog
                }
                binding.etEndDate.setText(endDateCalender.timeInMillis.toFormattedDateString())
            }
        }

        binding.participationTypeGroup.setOnCheckedStateChangeListener { _, ids ->
            if(binding.individual.isChecked){
                binding.teamSizeInputLayout.visibility = View.GONE
            }else{
                binding.teamSizeInputLayout.visibility = View.VISIBLE
            }
        }

        binding.participationModeGroup.setOnCheckedStateChangeListener { _, _ ->
            if(binding.online.isChecked){
                binding.locationInputLayout.visibility = View.GONE
            }else{
                binding.locationInputLayout.visibility = View.VISIBLE
            }
        }

        binding.btnDone.setOnClickListener {
            if(
                binding.etEventTitle.text.isNullOrBlank() ||
                binding.etEventDesc.text.isNullOrBlank() ||
                binding.etEventLink.text.isNullOrBlank() ||
                (binding.teamSizeInputLayout.isVisible && binding.etTeamSize.text.isNullOrBlank()) ||
                (binding.locationInputLayout.isVisible && binding.etLocation.text.isNullOrBlank())
            ){
                CookieBar.build(requireActivity())
                    .setTitle("Error")
                    .setMessage("Please fill all required fields")
                    .setBackgroundColor(R.color.error_container)
                    .setDuration(3000)
                    .show()
                return@setOnClickListener
            }else{
                viewModel.postEvent(
                    CreateEventRequest(
                        title = binding.etEventTitle.text.toString(),
                        description = binding.etEventDesc.text.toString(),
                        startDate = startDateCalender.timeInMillis.toFormattedDateString(),
                        endDate = endDateCalender.timeInMillis.toFormattedDateString(),
                        link = binding.etEventLink.text.toString(),
                        participationType = participationTypeGroupToEnum(),
                        teamSize = binding.etTeamSize.text.toString().toIntOrNull(),
                        eventType = eventTypeGroupToEnum(),
                        location = binding.etLocation.text.toString(),
                        isOnline = binding.online.isChecked
                    )
                )
            }

        }

        viewModel.state.observe(viewLifecycleOwner){
            when(it){
                PostContestState.Loading -> {
                    progressDialog.show()
                }
                PostContestState.Idle -> {
                    progressDialog.dismiss()
                }
                is PostContestState.Error -> {
                    progressDialog.dismiss()
                    CookieBar.build(requireActivity())
                        .setTitle("Error")
                        .setMessage(it.msg)
                        .setBackgroundColor(R.color.error_container)
                        .setDuration(3000)
                        .show()
                }
                PostContestState.Success -> {
                    CookieBar.build(requireActivity())
                        .setTitle("Success")
                        .setMessage("posted successfully")
                        .setBackgroundColor(R.color.green)
                        .setDuration(3000)
                        .show()
                    progressDialog.dismiss()
                    findNavController().popBackStack()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.participationTypeGroup
    }

    private fun participationTypeGroupToEnum(): CreateEventRequest.ParticipationType{
        return when(binding.participationTypeGroup.checkedChipId) {
            R.id.individual -> CreateEventRequest.ParticipationType.INDIVIDUAL
            R.id.team -> CreateEventRequest.ParticipationType.TEAM
            else -> throw IllegalArgumentException("Invalid participation type")
        }
    }

    private fun eventTypeGroupToEnum(): CreateEventRequest.EventType{
        return when(binding.eventTypeGroup.checkedChipId) {
            R.id.hackathon -> CreateEventRequest.EventType.HACKATHON
            R.id.seminar -> CreateEventRequest.EventType.SEMINAR
            R.id.workshop -> CreateEventRequest.EventType.WORKSHOP
            else -> throw IllegalArgumentException("Invalid event type")
        }
    }
}