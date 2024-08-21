package com.synrgy7team4.feature_transfer.presentation.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferDetailBinding
import com.synrgy7team4.feature_transfer.databinding.FragmentTransferPinBinding


class TransferDetailFragment : Fragment() {
    private var _binding: FragmentTransferDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferDetailBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDetail.setOnClickListener {
            val deepLinkUri = Uri.parse("app://com.example.app/trans/transSuccess")
            requireView().findNavController().navigate(deepLinkUri)
        }

        binding.btnShare.setOnClickListener {

        }

        binding.btnDone.setOnClickListener {
            val deepLinkUri = Uri.parse("app://com.example.app/dashboard/home")
            val navController = requireView().findNavController()
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(navController.graph.startDestinationId, true)
                .build()

            navController.navigate(deepLinkUri, navOptions)
                }

        }


    }
