package io.github.kabirnayeem99.islamqaorg.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    /**
     * Sets up the toolbar and navigation drawer.
     */
    private fun initViews() {
        setUpNavigation()
    }

    private val backIconDrawable by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
    }

    private val settingsIconDrawable by lazy {
        ContextCompat.getDrawable(this, R.drawable.ic_settings)
    }

    /**
     * Sets up the navigation for the app.
     */
    private fun setUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            configureMenuIconBasedOnCurrentFragment(destination, navController)
        }
    }

    /**
     * If the user is navigating to the HomeFragment, then show the hamburger menu icon. Otherwise,
     * show the back arrow icon.
     *
     * @param destination The destination that the user is navigating to.
     * @param navController The NavController that is navigating to the destination.
     */
    private fun configureMenuIconBasedOnCurrentFragment(
        destination: NavDestination,
        navController: NavController
    ) {
        when (destination.id) {
            R.id.HomeFragment -> onNavigatingToHomeFragment()
            else -> onNavigatingToNonHomeFragment(navController)
        }
    }

    private fun onNavigatingToNonHomeFragment(navController: NavController) {
        binding.apply {
            ivSettingsCumBackButton.apply {
                setImageDrawable(backIconDrawable)
                setOnClickListener { navController.navigateUp() }
            }
            ivFilterButton.visibility = View.GONE
        }
    }


    private fun onNavigatingToHomeFragment() {
        binding.apply {
            ivSettingsCumBackButton.apply {
                setImageDrawable(settingsIconDrawable)
                ivFilterButton.visibility = View.VISIBLE
                setOnClickListener {
                    Toast.makeText(
                        this@MainActivity,
                        "Should open settings",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}