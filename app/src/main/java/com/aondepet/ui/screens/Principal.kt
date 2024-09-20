package com.aondepet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Principal(navController: NavController, viewModel: PetViewModel) {
    val petsList by viewModel.petsList.observeAsState(emptyList())
    val authState by viewModel.authState.observeAsState()
    val userId by viewModel.userId.observeAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.applyFilters(
            animals = emptyList(),
            generos = emptyList(),
            portes = emptyList(),
            estados = emptyList(),
            status = emptyList()
        )
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Aonde Pet",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("conta") },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.account_icon),
                            contentDescription = "Icone menu",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Filtros") },
                icon = { Icon(painterResource(R.drawable.search), contentDescription = "Filtrar") },
                onClick = { showBottomSheet = true }
            )
        }
    ) { paddingValues ->
        FilterBottomSheet(showBottomSheet, { showBottomSheet = false }, viewModel::applyFilters)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.medium),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pets",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                if (authState == AuthState.Authenticated) {
                    Row {
                        SmallFloatingActionButton(
                            onClick = {
                                navController.navigate("postFormularioNovo")
                            },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.secondary
                        ) {
                            Icon(Icons.Filled.Add, "Adicionar Post Pet")
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                viewModel.toggleFavoritosFilter()
                            },
                            containerColor = if (viewModel.mostrarFavoritos.value == true)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = if (viewModel.mostrarFavoritos.value == true)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.secondary
                        ) {
                            Icon(
                                painter = if (viewModel.mostrarFavoritos.observeAsState().value == true)
                                    painterResource(R.drawable.favorite_fill) // Ícone quando é favorito
                                else
                                    painterResource(R.drawable.favorite), // Ícone quando não é favorito
                                contentDescription = "Filtrar favoritos",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                    }
                } // Autenticado
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(petsList) { pet ->
                    authState?.let {
                        CardPet(
                            navController = navController,
                            pet = pet,
                            authState = it,
                            viewModel = viewModel,
                            userId = userId.toString()
                        )
                    }
                }
            }
        } // Fim da coluna
    }
}
