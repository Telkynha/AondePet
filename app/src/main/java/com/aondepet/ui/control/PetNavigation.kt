package com.aondepet.ui.control

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aondepet.ui.screens.*


@Composable
fun PetNavigation(petViewModel: PetViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "principal", builder = {
        composable("principal") { Principal(navController, petViewModel) }
        composable("login") { Login(navController, petViewModel) }
        composable("cadastro") { Cadastro(navController, petViewModel) }
        composable("post/{petId}") { backStackEntry ->
            var petId = backStackEntry.arguments?.getString("petId")
            Post(navController, petViewModel, petId)
        }
        composable("postFormularioNovo") {
            PostFormularioNovo(navController, petViewModel)
        }
        composable("postFormularioAlterar/{petId}") { backStackEntry ->
            var petId = backStackEntry.arguments?.getString("petId")
            PostFormularioAlterar(navController, petViewModel, petId)
        }
        composable("conta") { Conta(navController, petViewModel) }
    }
    )
}