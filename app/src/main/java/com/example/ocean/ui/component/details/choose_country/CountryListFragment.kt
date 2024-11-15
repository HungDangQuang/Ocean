package com.example.ocean.ui.component.details.choose_country

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Country
import com.example.ocean.databinding.FragmentCountryListBinding
import com.example.ocean.presentation.CountryListViewModel
import com.example.ocean.ui.adapter.CountryAdapter
import com.example.ocean.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.ceil

@AndroidEntryPoint
class CountryListFragment : BaseFragment() {

    private lateinit var binding: FragmentCountryListBinding
    private val viewModel: CountryListViewModel by activityViewModels()
    private lateinit var countryAdapter: CountryAdapter
    private val items = mutableListOf<Country>()
    private val allItems = mutableListOf<Country>()
    private var currentPage = 1
    private val itemsPerPage = 50
    private var isLoading: Boolean = false
    private val TAG = CountryListFragment::class.java.simpleName

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCountryListViewModel()
    }

    private fun setupScrolling() {
        Log.d(TAG, "setupScrolling")
        binding.rvCountryList.addOnScrollListener(object :
            PaginationScrollListener(binding.rvCountryList.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return currentPage - 1 >= ceil(allItems.size.toDouble() / itemsPerPage)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if (currentPage != 1) {
                    isLoading = true
                    loadItems(currentPage)
                    currentPage++
                }
            }

        })
    }

    private fun fetchItems(page: Int): List<Country> {
        Log.d(TAG, "fetchItems")
        val start = (page - 1) * itemsPerPage
        val end = minOf(start + itemsPerPage, allItems.size)
        // increase the page number by 1 to prevent reloading the same page
        if (page == 1) {
            currentPage++
        }
        return allItems.subList(start, end)
    }

    private fun loadItems(page: Int) {
        Log.d(TAG, "loadItems")

        countryAdapter.showLoading()

        Handler(Looper.getMainLooper()).postDelayed({

            countryAdapter.hideLoading()

            val newItems = fetchItems(page)
            countryAdapter.addItems(newItems)
            isLoading = false
        }, 500)
    }


    override fun setUpClickableView() {
        binding.rlApply.setOnClickListener {
            Log.d(TAG, "button apply is clicked")

            // update selected item to view model
            viewModel.setCurrentCountry(countryAdapter.getSelectedCountryName())
            // back to previous screen
            findNavController().popBackStack()
        }

    }

    private fun setUpCountryListViewModel() {
//        viewModel = ViewModelProvider(
//            requireActivity(),
//            viewModelFactory
//        )[CountryListViewModel::class.java]

        viewModel.items.observe(viewLifecycleOwner) { it ->
            if (it != null) {
                allItems.addAll(it)
            }
            allItems.sortWith(compareBy { it.countryName })

            countryAdapter = CountryAdapter(items, requireContext())
            countryAdapter.setSelectedCountryPosition(
                findCountryPosition(
                    allItems,
                    getStoredCountryName()
                )
            )
            binding.rvCountryList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCountryList.adapter = countryAdapter

            loadItems(currentPage)
            setupScrolling()
        }
    }

    private fun findCountryPosition(countryList: List<Country>, countryName: String): Int {
        return countryList.indexOfFirst {
            it.countryName == countryName
        }
    }

    private fun getStoredCountryName(): String {
        return if (viewModel.isSelectingInputLanguage.value!!) {
            viewModel.currentInputCountry.value!!
        } else {
            viewModel.currentOutputCountry.value!!
        }
    }

    override fun goToNextScreen() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // reset the flag
        viewModel.setIsSelectingInputLanguage(false)
        // store the current countries
        viewModel.storeCurrentCountries()
    }
}