package com.synrgy7team4.feature_transfer.ui.savedAccountScreen

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.synrgy7team4.common.NavigationHandler
import com.synrgy7team4.common.makeSnackbar
import com.synrgy7team4.domain.feature_transfer.model.response.SavedAccountGetDataDomain
import com.synrgy7team4.feature_transfer.R
import com.synrgy7team4.feature_transfer.databinding.FragmentSavedAccountBinding
import com.synrgy7team4.feature_transfer.viewmodel.TransferViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedAccountFragment : Fragment() {
    private var _binding: FragmentSavedAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TransferViewModel>()
    private val navHandler: NavigationHandler by inject()
    private var savedAccountList: MutableList<Any> = mutableListOf()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: SavedAccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSavedAccountBinding.inflate(layoutInflater).also {
                _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("TransferPrefs", Context.MODE_PRIVATE)
        lifecycleScope.launch {
            awaitAll(viewModel.initializeData())
            viewModel.getSavedAccounts()
        }

        adapter = SavedAccountAdapter(savedAccountList, sharedPreferences, requireContext(), savedAccountList)
        binding.savedAccountList.adapter = adapter
        binding.savedAccountList.layoutManager = LinearLayoutManager(context)

        binding.btnBack.setOnClickListener {
//            findNavController().popBackStack()
            navHandler.navigateToDashboard()

        }
        binding.sameBankButton.setOnClickListener { handleSameBankButtonClick() }
        binding.differentBankButton.setOnClickListener { handleDifferentBankButtonClick() }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean = false
            override fun onQueryTextChange(msg: String): Boolean {
                adapter.getFilter().filter(msg)
                return false
            }
        })

        binding.addNewAccountInfo.setOnClickListener {
            findNavController().navigate(R.id.action_savedAccountFragment_to_fellowAccountBankInputFragment)
        }

        viewModel.savedAccountsData.observe(viewLifecycleOwner) { response ->
            response.data?.let { dataList ->
                val sortedList = dataList.sortedBy { it?.name }
                savedAccountList = transformSortedListToMutableList(sortedList)
                adapter.updateData(savedAccountList)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            makeSnackbar(requireView(), error.toString())
        }
    }

    private fun transformSortedListToMutableList(list: List<SavedAccountGetDataDomain?>): MutableList<Any> {
        val mutableList = mutableListOf<Any>()
        var currentSection = ""

        for (account in list) {
            val firstLetter = account?.name?.get(0).toString().uppercase()
            if (currentSection != firstLetter) {
                currentSection = firstLetter
                mutableList.add(firstLetter)
            }
            account?.let {
                mutableList.add(account)
            }
        }
        return mutableList
    }

    private fun handleSameBankButtonClick() {
        sharedPreferences.edit().apply {
            putString("differentbank", "false")
            apply()
        }
        binding.differentBankButton.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.white
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.primary_blue
                )
            )
        }

        binding.sameBankButton.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.primary_blue
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.white
                )
            )
        }
    }

    private fun handleDifferentBankButtonClick() {
        sharedPreferences.edit().apply {
            putString("differentbank", "true")
            apply()
        }
        binding.differentBankButton.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.primary_blue
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.white
                )
            )
        }

        binding.sameBankButton.apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.white
                )
            )
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    com.synrgy7team4.common.R.color.primary_blue
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}