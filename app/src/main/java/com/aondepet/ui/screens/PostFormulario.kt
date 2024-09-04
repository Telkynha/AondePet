package com.aondepet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Status

@Composable
fun PostFormulario(navController: NavController, viewModel: PetViewModel){
    var tipoFormulario = "Novo"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = "Icone seta voltar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "$tipoFormulario Post",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
            )
            IconButton(
                onClick = { navController.navigate("conta") },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Icone menu",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } // Row icones topo

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Nome do Pet",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        } // Row textos: Nome
        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "Imagem Pet",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DropdownSelector(
                        label = "Status",
                        options = Status.entries,
                        selectedOption = "",
                        onOptionSelected = { it }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownSelector(
                        label = "Animal",
                        options = Animal.entries,
                        selectedOption = animal,
                        onOptionSelected = { petViewModel.onAnimalChange(it) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownSelector(
                        label = "Genero",
                        options = Genero.entries,
                        selectedOption = genero,
                        onOptionSelected = { petViewModel.onGeneroChange(it) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = peso,
                        onValueChange = { petViewModel.onPesoChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Peso",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = raca,
                        onValueChange = { petViewModel.onRacaChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Raça",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DropdownSelector(
                        label = "Porte",
                        options = Porte.entries,
                        selectedOption = porte,
                        onOptionSelected = { petViewModel.onPorteChange(it) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = idade,
                        onValueChange = { petViewModel.onIdadeChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Idade",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    )
                } // Column que segura a primeira coluna de dados do Pet
            } //Row que possui as duas columns
        } // Surface roxa para dados Pet

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
            shape = RoundedCornerShape(4.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Descrição",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = descricao,
                        modifier = Modifier
                            .background(Color.Transparent)
                            .fillMaxWidth(),
                        onValueChange = { petViewModel.onDescricaoChange(it) }
                    )
                }
            } //Coluna para ajeitar descrição
        } //Fim Surface de descrição

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
            shape = RoundedCornerShape(4.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Contato",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { petViewModel.onEmailChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Email",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    OutlinedTextField(
                        value = telefone,
                        onValueChange = { petViewModel.onTelefoneChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Telefone",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            } //Coluna para ajeitar contato
        } //Fim Surface de Contato
        Button(
            onClick = {
                // Cadastrar novo post pet
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "$tipoFormulario", color = MaterialTheme.colorScheme.onPrimary)
        }
    } // Fim Column Principal
}

@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    text = { Text(option.toString()) }
                )
            }
        }
    }
}
