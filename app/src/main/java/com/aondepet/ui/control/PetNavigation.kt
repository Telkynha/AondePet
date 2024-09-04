package com.aondepet.ui.control

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aondepet.ui.screens.*


@Composable
fun PetNavigation(petViewModel: PetViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "principal", builder = {
        composable("principal"){ Principal(navController, petViewModel) }
        composable("login"){ Login(navController, petViewModel) }
        composable("cadastro"){ Cadastro(navController, petViewModel) }
        composable("post"){ Post(navController, petViewModel) }
        composable("postFormulario"){ PostFormulario(navController, petViewModel) }
        composable("conta"){ Conta(navController, petViewModel) }
    }
    )

}