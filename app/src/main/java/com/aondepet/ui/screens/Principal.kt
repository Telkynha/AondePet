package com.aondepet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.theme.Spacing

@Composable
fun Principal(navController: NavController, viewModel: PetViewModel) {
    val petsList by viewModel.petsList.observeAsState(emptyList())
    val authState by viewModel.authState.observeAsState() // Observando o estado de autenticação
    val userId by viewModel.userId.observeAsState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }
    var currentMessage by remember { mutableStateOf("Salve um pet hoje mesmo") } // Mensagem inicial

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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão Conta
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { navController.navigate("conta") }) {
                        Icon(
                            painter = painterResource(R.drawable.account_icon),
                            contentDescription = "Ícone conta",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                }

                // Botão Adicionar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (authState !is AuthState.Authenticated) {
                                currentMessage = "Faça login para usar isso"
                                isVisible = true
                            } else {
                                navController.navigate("postFormularioNovo")
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Adicionar Post Pet",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                }

                // Botão Favoritos
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (authState !is AuthState.Authenticated) {
                                currentMessage = "Faça login para usar isso"
                                isVisible = true
                            } else {
                                viewModel.toggleFavoritosFilter()
                                currentMessage = "Seus pets favoritos"
                                isVisible = true
                            }
                        }
                    ) {
                        Icon(
                            painter = if (viewModel.mostrarFavoritos.observeAsState().value == true)
                                painterResource(R.drawable.favorite_fill)
                            else
                                painterResource(R.drawable.favorite),
                            contentDescription = "Filtrar favoritos",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                }

                // Botão Meus Pets
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (authState !is AuthState.Authenticated) {
                                currentMessage = "Faça login para usar isso"
                                isVisible = true
                            } else {
                                viewModel.toggleMeusPetsFilter()
                                currentMessage = "Meus pets"
                                isVisible = true
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.pet),
                            contentDescription = "Filtrar meus pets",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Filtros", color = MaterialTheme.colorScheme.primary) },
                icon = { Icon(painterResource(R.drawable.search), contentDescription = "Filtrar", tint = MaterialTheme.colorScheme.primary,) },
                onClick = { showBottomSheet = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        },
    ) { paddingValues ->
        FilterBottomSheet(showBottomSheet, { showBottomSheet = false }, viewModel::applyFilters)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(start = Spacing.small, end = Spacing.small, top = Spacing.small),
        ) {
            if (isVisible) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable { isVisible = false },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentMessage,
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.medium))
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
