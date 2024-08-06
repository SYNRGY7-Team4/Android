package com.synrgy7team4.feature_dashboard.presentation.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jer.shared.ViewModelFactoryProvider
import com.synrgy7team4.feature_dashboard.R

import androidx.navigation.findNavController

import com.synrgy7team4.feature_dashboard.R
import com.synrgy7team4.feature_dashboard.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import com.synrgy7team4.feature_mutasi.presentation.ui.MutasiFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var isHidden: Boolean = false
    private lateinit var fullBalance: String
    private lateinit var hiddenBalance: String
//    private val viewModel by viewModels<HomeViewModel> {
//
////        val app = requireActivity().application
////        (app as MyApplication).viewModelFactory
////        val app = requireActivity().application as ViewModelFactoryProvider
////        app.provideViewModelFactory()
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.tvName
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(HomeViewModel::class.java)


        fullBalance = getString(R.string.dummy_account_balance)
        hiddenBalance = fullBalance.replace(Regex("\\d"), "*").replace(Regex("[,.]"), "")


        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("RegisterPrefs", Context.MODE_PRIVATE)
        val getUsername = sharedPreferences.getString("name", null)
        binding.tvName.text = getUsername
        binding.tvAccName.text = getUsername

        val getToken = sharedPreferences.getString("token", null)
        val getAccountNumber = sharedPreferences.getString("accountNumber", null)

        viewModel.fectData(getToken!!, getAccountNumber!!)

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data.success) {
                binding.tvAccBalance.text = data.data.toString()
                Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()

            }
        }




        binding.btnElectric.setOnClickListener {
            showToast()
        }

        binding.btnPulsa.setOnClickListener {
            showToast()
        }

        binding.btnData.setOnClickListener {
            showToast()
        }

        binding.btnEwallet.setOnClickListener {
            showToast()
        }

        binding.btnTransfer.setOnClickListener {
            showToast()
        }

        binding.btnHistory.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.full_framelayout, MutasiFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
            //view.findNavController().navigate(R.id.action_navigation_home_to_mutasiFragment)
        }

        binding.tvAccBalance.text = fullBalance
        binding.toggleBalance.setImageResource(com.synrgy7team4.common.R.drawable.ic_visibility_on)

        binding.toggleBalance.setOnClickListener{
            balanceVisibility()
        }





    }

    private fun showToast( duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), "Fitur ini akan segera hadir", duration).show()
    }


    private fun balanceVisibility() {
        if(isHidden){
            binding.tvAccBalance.text = fullBalance
            binding.toggleBalance.setImageResource(com.synrgy7team4.common.R.drawable.ic_visibility_on)
        } else {
            binding.tvAccBalance.text = hiddenBalance
            binding.toggleBalance.setImageResource(com.synrgy7team4.common.R.drawable.ic_visibility_off)
        }
        isHidden = !isHidden

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}