package com.aondepet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.aondepet.ui.control.PetNavigation
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.theme.AondePetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: PetViewModel by viewModels()
        setContent {
            AondePetTheme {
                PetNavigation(petViewModel = viewModel)
            }
        }
    }
}