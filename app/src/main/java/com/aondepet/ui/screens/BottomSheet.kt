package com.aondepet.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aondepet.ui.models.Animal
import com.aondepet.ui.models.Estado
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Porte
import com.aondepet.ui.models.Status
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onApplyFilters: (List<Animal>, List<Genero>, List<Porte>, List<Estado>, List<Status>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // Listas para armazenar as seleções
    var selectedAnimals by remember { mutableStateOf(listOf<Animal>()) }
    var selectedGeneros by remember { mutableStateOf(listOf<Genero>()) }
    var selectedPortes by remember { mutableStateOf(listOf<Porte>()) }
    var selectedEstados by remember { mutableStateOf(listOf<Estado>()) }
    var selectedStatus by remember { mutableStateOf(listOf<Status>()) }

    val statusOptions = listOf(Status.Adotar, Status.Perdido, Status.Outro)

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filtros:", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

                Text(text = "Status:", style = MaterialTheme.typography.titleMedium)
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(statusOptions) { status ->
                        FilterButton(status, selectedStatus.contains(status)) {
                            selectedStatus = toggleSelection(selectedStatus, status)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FilterSection(title = "Animal:", enumValues = Animal.values(), selectedItems = selectedAnimals) { animal ->
                    selectedAnimals = toggleSelection(selectedAnimals, animal)
                }

                FilterSection(title = "Gênero:", enumValues = Genero.values(), selectedItems = selectedGeneros) { genero ->
                    selectedGeneros = toggleSelection(selectedGeneros, genero)
                }

                FilterSection(title = "Porte:", enumValues = Porte.values(), selectedItems = selectedPortes) { porte ->
                    selectedPortes = toggleSelection(selectedPortes, porte)
                }

                FilterSection(title = "Estado:", enumValues = Estado.values(), selectedItems = selectedEstados) { estado ->
                    selectedEstados = toggleSelection(selectedEstados, estado)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row {
                    Button(
                        onClick = {
                            selectedAnimals = emptyList()
                            selectedGeneros = emptyList()
                            selectedPortes = emptyList()
                            selectedEstados = emptyList()
                            selectedStatus = emptyList()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Limpar Filtros")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            onApplyFilters(selectedAnimals, selectedGeneros, selectedPortes, selectedEstados, selectedStatus)
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismissRequest()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aplicar Filtros")
                    }
                }
            }
        }
    }
}

// Função para alternar a seleção dos itens
fun <T> toggleSelection(list: List<T>, item: T): List<T> {
    return if (list.contains(item)) list - item else list + item
}

@Composable
fun <T : Enum<T>> FilterSection(
    title: String,
    enumValues: Array<T>,
    selectedItems: List<T>,
    onItemSelected: (T) -> Unit
) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(enumValues) { value ->
                FilterButton(value, selectedItems.contains(value)) { onItemSelected(value) }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun <T> FilterButton(value: T, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .padding(4.dp)
    ) {
        Text(text = value.toString(), color = MaterialTheme.colorScheme.onSurface)
    }
}