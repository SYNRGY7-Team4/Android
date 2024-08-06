package com.synrgy7team4.feature_mutasi.presentation.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.synrgy7team4.feature_mutasi.R
import com.synrgy7team4.feature_mutasi.databinding.FragmentMutasiBinding
import com.synrgy7team4.feature_mutasi.presentation.MutationAdapter
import com.synrgy7team4.feature_mutasi.presentation.viewmodel.MutasiViewmodel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class MutasiFragment : Fragment() {

    private var _binding: FragmentMutasiBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MutasiViewmodel by viewModel()
    private lateinit var adapter: MutationAdapter

    private lateinit var tvDateStart: TextView
    private lateinit var tvDateEnd: TextView
    private lateinit var startDate: String
    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMutasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        tvDateStart = view.findViewById(R.id.tv_date_start)
        tvDateEnd = view.findViewById(R.id.tv_date_end)

        val calendarStartIcon: CardView = view.findViewById(R.id.cv_date_start) // Your ImageView id
        val calendarEndIcon: CardView = view.findViewById(R.id.cv_date_end)

        calendarStartIcon.setOnClickListener {
            showDateEndPickerDialog(tvDateStart){ selectedEndDate ->
                startDate = selectedEndDate
            }
        }// Your ImageView id

        calendarEndIcon.setOnClickListener {
            showDateEndPickerDialog(tvDateEnd) { selectedEndDate ->
                lifecycleScope.launch {
                    // Call fetchFilteredUserData after the date is selected
                    viewModel.fetchFilteredUserData(
                        startDate,
                        selectedEndDate
                    )
                    // Observe the ViewModel data
                    observeViewModel()
                }
                Log.d("test", "${binding.tvDateStart.text} sampai $selectedEndDate")
            }
        }

        // Set up the spinner
        val transaksi = resources.getStringArray(R.array.spinner_jenis_transaksi)
        val spinner = binding.spinner
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transaksi)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Handle spinner selection
                //viewModel.loadMutations() // Replace with actual account number if needed
                lifecycleScope.launch {
                    viewModel.fetchFilteredUserData(
                        "2024-01-01T12:00:00",
                        "2029-08-01T12:00:00"
                    )
                    // Observe the ViewModel data
                    observeViewModel()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                observeViewModel()
            }
        }
        // Set up the RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = MutationAdapter(emptyList())
        binding.rvMutasi.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMutasi.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.mutationsbydate.observe(viewLifecycleOwner, Observer { mutations ->
            adapter = MutationAdapter(mutations)
            binding.rvMutasi.adapter = adapter
        })

        // Optionally, observe loading and error states
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            // Show/hide loading indicator
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            // Show error message
        })
    }

    /*private fun showDatePickerDialog(textView: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(textView)
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }*/

    private fun showDateEndPickerDialog(textView: TextView, onDateSelected: (String) -> Unit) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val selectedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(calendar.time)

            textView.text = LocalDateTime.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).toLocalDate().toString()
            onDateSelected(selectedDate)
        }

        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun updateDateLabel(textView: TextView) {
        textView.text = dateFormat.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
