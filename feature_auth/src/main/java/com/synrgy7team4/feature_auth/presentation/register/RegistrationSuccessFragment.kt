package com.synrgy7team4.feature_auth.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.jer.shared.ViewModelFactoryProvider
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentRegistrationSuccessBinding
import com.synrgy7team4.feature_auth.databinding.FragmentVerifikasiKtpBinding
import com.synrgy7team4.feature_auth.presentation.viewmodel.RegisterViewModel
import com.synrgy7team4.feature_dashboard.presentation.DashboardActivity

class RegistrationSuccessFragment : Fragment() {
    private var _binding: FragmentRegistrationSuccessBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RegisterViewModel> {
//        val app = requireActivity().application
//        (app as MyApplication).viewModelFactory

        val app = requireActivity().application as ViewModelFactoryProvider
        app.provideViewModelFactory()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegistrationSuccessBinding.inflate(inflater,container,false);
        val view = binding.root;
        return view;
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setupAccessibility()
        binding.regisSuccessLayout.setOnClickListener {


            requireView().findNavController().navigate(R.id.action_registrationSuccessFragment_to_homeFragment)

        }


    }

    private fun setToast(msg: String) {
        Toast.makeText(requireActivity(),msg, Toast.LENGTH_SHORT).show()

    }
    private fun setupAccessibility() {
        binding.apply {
            tvRegisSuccess.contentDescription = getString(R.string.anda_berhasil_membuka_rekening)
        }
    }

}
