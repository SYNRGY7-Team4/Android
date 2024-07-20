package com.synrgy7team4.feature_auth.presentation.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.synrgy7team4.feature_auth.R
import com.synrgy7team4.feature_auth.databinding.FragmentRegistrationSuccessBinding

class RegistrationSuccessFragment : Fragment() {
    private var _binding: FragmentRegistrationSuccessBinding? = null
    private val binding get() = _binding!!

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

        binding.ivRegisSuccess.setOnClickListener {

            view.findNavController().navigate(R.id.action_registrationSuccessFragment_to_dashboardFragment2)

        }
    }

}
