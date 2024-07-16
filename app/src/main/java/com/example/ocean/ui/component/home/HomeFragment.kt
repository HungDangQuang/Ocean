package com.example.ocean.ui.component.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ocean.ui.adapter.MenuItem
import com.example.ocean.ui.adapter.MenuItemAdapter
import com.example.ocean.ui.adapter.OnMenuItemClickListener
import com.example.ocean.R
import com.example.ocean.databinding.FragmentHomeBinding
import com.example.ocean.ui.base.BaseFragment

class HomeFragment : BaseFragment(), OnMenuItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val TAG = HomeFragment::class.simpleName

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenuItems()
    }

    override fun setUpClickableView() {

    }

    override fun goToNextScreen() {

    }

    private fun initMenuItems(): List<MenuItem> {
        return listOf(
            MenuItem(ContextCompat.getDrawable(requireContext(), R.drawable.add_icon)!!, getString(R.string.menu_title_add_item)),
            MenuItem(ContextCompat.getDrawable(requireContext(), R.drawable.dictionary_icon)!!, getString(
                R.string.menu_item_dictionary
            )),
            MenuItem(ContextCompat.getDrawable(requireContext(), R.drawable.stopwatch_icon)!!, getString(
                R.string.menu_item_start_test
            )),
            MenuItem(ContextCompat.getDrawable(requireContext(), R.drawable.speaker_icon)!!, getString(
                R.string.menu_item_speaker
            ))
        )
    }

    private fun setUpMenuItems() {
        binding.rvMenu.apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = MenuItemAdapter(initMenuItems(), this@HomeFragment)
        }
    }

    override fun onItemClick(item: MenuItem) {
        Log.d(TAG, "onItemClick: ${item.menuTitle}")
        when(item.menuTitle) {
            
            getString(R.string.menu_title_add_item) -> {
                Log.d(TAG, "Start opening word addition screen")
                findNavController().navigate(R.id.vocabularyAdditionFragment)
            }
            
            getString(R.string.menu_item_dictionary) -> {
                Log.d(TAG, "onItemClick: Start opening dictionary screen")
            }

            getString(R.string.menu_item_start_test) -> {
                Log.d(TAG, "onItemClick: start opening exam screen")
            }

            getString(R.string.menu_item_speaker) -> {
                Log.d(TAG, "onItemClick: start opening speaking test screen")
            }
        }
    }

}