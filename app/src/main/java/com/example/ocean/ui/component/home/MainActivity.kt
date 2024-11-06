package com.example.ocean.ui.component.home


import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.ocean.R
import com.example.ocean.presentation.CountryListViewModel
import com.example.ocean.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import com.example.ocean.presentation.LoginViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private val countryListViewModel: CountryListViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}