package com.example.hackmatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.hackmatch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNav.setupWithNavController(
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .navController
        )

        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController
            .addOnDestinationChangedListener { _, destination, _ ->
                if (
                    destination.id == R.id.profileFragment ||
                    destination.id == R.id.contestFragment ||
                    destination.id == R.id.teamFragment
                ) {
                    binding.bottomNav.visibility = View.VISIBLE
                } else {
                    binding.bottomNav.visibility = View.GONE

                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}