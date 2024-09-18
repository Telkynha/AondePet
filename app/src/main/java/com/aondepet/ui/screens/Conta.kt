package com.aondepet.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Conta
import com.aondepet.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Conta(navController: NavController, viewModel: PetViewModel) {

    val authState = viewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    var conta by remember { mutableStateOf<Conta?>(null) }
    LaunchedEffect(Unit) {
        viewModel.getContaById().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                conta = document.toObject(Conta::class.java)
            }
        }
    }

    // Menu
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Conta",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        // Conteúdo da tela
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.medium)
                .verticalScroll(rememberScrollState()),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Icone do AondePet",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(250.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Nome: ${conta?.nome ?: "Carregando..."}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }
            SenhaComOlho("${conta?.senha ?: "Carregando..."}")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Email: ${conta?.email ?: "Carregando..."}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Spacing.medium),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = "Sair",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        } // Coluna principal
    }
}


@Composable
fun SenhaComOlho(senha: String?) {
    var senhaVisivel by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        (if (senhaVisivel) senha else "*****")?.let {
            Text(
                text = "Senha: $it",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }
        IconButton(
            onClick = { senhaVisivel = !senhaVisivel }
        ) {
            Icon(
                painter = painterResource(
                    id = if (senhaVisivel) R.drawable.visibility else R.drawable.visibility_off
                ),
                contentDescription = if (senhaVisivel) "Esconder senha" else "Mostrar senha",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
