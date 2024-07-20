package com.synrgy7team4.feature_dashboard.presentation.ui.dashboard

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.synrgy7team4.feature_dashboard.R
import com.synrgy7team4.feature_dashboard.databinding.FragmentDashboardBinding
import com.synrgy7team4.feature_dashboard.presentation.ui.account.AccountFragment
import com.synrgy7team4.feature_dashboard.presentation.ui.qris.QrisFragment

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

       // val textView: TextView = binding.tvName

        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up BottomNavigationView
//        binding.navView.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> {
//                    replaceFragment(DashboardFragment())
//                    true
//                }
//                R.id.navigation_qris -> {
//                    replaceFragment(AccountFragment())
//                    true
//                }
//                R.id.navigation_account -> {
//                    replaceFragment(QrisFragment())
//                    true
//                }
//                else -> false
//            }
//        }
//
//        // Set the initial fragment
//        if (savedInstanceState == null) {
//            binding.navView.selectedItemId = R.id.navigation_home
//        }
//    }

//    private fun replaceFragment(fragment: Fragment) {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.content, fragment)
//            .commit()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.bottom_nav_menu, menu)
//    }

        fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }}