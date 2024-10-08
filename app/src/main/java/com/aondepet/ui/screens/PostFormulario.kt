package com.aondepet.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.aondepet.R
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Animal
import com.aondepet.ui.models.Estado
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet
import com.aondepet.ui.models.Porte
import com.aondepet.ui.models.Status
import com.aondepet.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormularioNovo(navController: NavController, viewModel: PetViewModel) {

    var nome by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf(Genero.Macho) }
    var idade by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(Status.Adotado) }
    var animal by remember { mutableStateOf(Animal.Cachorro) }
    var porte by remember { mutableStateOf(Porte.Medio) }
    var estado by remember { mutableStateOf(Estado.AC) }
    var cidade by remember { mutableStateOf("") }
    val userId by viewModel.userId.observeAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Novo Pet",
                        color = MaterialTheme.colorScheme.primary,
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
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.large)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Detalhes do pet:", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = Spacing.medium)
                    .offset(x = (-Spacing.large))
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(Spacing.medium))
            DropdownSelector(
                label = "Status",
                options = Status.entries,
                selectedOption = status,
                onOptionSelected = { status = it }
            )
            val painter: Painter = if (imageUri != null) {
                rememberAsyncImagePainter(
                    model = imageUri,
                    contentScale = ContentScale.Crop
                )
            } else {
                painterResource(R.drawable.img)
            }
            Image(painter = painter, contentDescription = "Imagem Pet",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = Spacing.medium)
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            OutlinedTextField(
                value = nome,
                singleLine = true,
                onValueChange = { nome = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Nome do Pet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Animal",
                options = Animal.entries,
                selectedOption = animal,
                onOptionSelected = { animal = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            OutlinedTextField(
                value = raca,
                singleLine = true,
                onValueChange = { raca = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Raça",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Porte",
                options = Porte.entries,
                selectedOption = porte,
                onOptionSelected = { porte = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            OutlinedTextField(
                value = idade,
                singleLine = true,
                onValueChange = { idade = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Idade",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Gênero",
                options = Genero.entries,
                selectedOption = genero,
                onOptionSelected = { genero = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.medium)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Descrição",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.titleSmall,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.medium)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Contato", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = email,
                        singleLine = true,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Email",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = telefone,
                        singleLine = true,
                        onValueChange = { telefone = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Telefone",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    DropdownSelector(
                        label = "Estado",
                        options = Estado.entries,
                        selectedOption = estado.toString(),
                        onOptionSelected = { estado = it as Estado }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = cidade,
                        singleLine = true,
                        onValueChange = { cidade = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Cidade",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.small))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    if (userId != null) {
                        val pet = Pet(
                            nome = nome,
                            raca = raca,
                            genero = genero,
                            idade = idade.toIntOrNull() ?: 0,
                            descricao = descricao,
                            status = status,
                            animal = animal,
                            porte = porte,
                            email = email,
                            telefone = telefone,
                            estado = estado,
                            cidade = cidade,
                            conta = userId!!,
                            foto = imageUri.toString()
                        )

                        viewModel.addPet(pet)
                        navController.clearBackStack("postFormularioNovo")
                        navController.clearBackStack("principal")
                        navController.navigate("principal")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Adicionar", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostFormularioAlterar(
    navController: NavController,
    viewModel: PetViewModel,
    petId: String? = ""
) {

    var pet by remember { mutableStateOf<Pet?>(null) }

    var nome by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf(Genero.Macho) }
    var idade by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(Status.Adotado) }
    var animal by remember { mutableStateOf(Animal.Cachorro) }
    var porte by remember { mutableStateOf(Porte.Medio) }
    var estado by remember { mutableStateOf(Estado.AC) }
    var cidade by remember { mutableStateOf("") }
    val userId by viewModel.userId.observeAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val petImage by viewModel.getPetImage(petId!!).observeAsState()
    imageUri = petImage

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    LaunchedEffect(petId) {
        viewModel.getPetById(petId!!).addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                pet = document.toObject(Pet::class.java)
                nome = pet?.nome ?: ""
                raca = pet?.raca ?: ""
                genero = pet?.genero ?: Genero.Macho
                idade = pet?.idade?.toString() ?: ""
                descricao = pet?.descricao ?: ""
                email = pet?.email ?: ""
                telefone = pet?.telefone ?: ""
                status = pet?.status ?: Status.Adotado
                animal = pet?.animal ?: Animal.Cachorro
                porte = pet?.porte ?: Porte.Medio
                estado = pet?.estado ?: Estado.AC
                cidade = pet?.cidade ?: ""
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Alterar Pet",
                        color = MaterialTheme.colorScheme.primary,
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
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.large)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "Detalhes do pet:", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = Spacing.medium)
                    .offset(x = (-Spacing.large))
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(Spacing.medium))
            DropdownSelector(
                label = "Status",
                options = Status.entries,
                selectedOption = status,
                onOptionSelected = { status = it }
            )
            val painter: Painter = if (imageUri != null) {
                rememberAsyncImagePainter(
                    model = imageUri,
                    contentScale = ContentScale.Crop
                )
            } else {
                painterResource(R.drawable.img)
            }
            Image(painter = painter, contentDescription = "Imagem Pet",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(vertical = Spacing.medium)
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
                    .clickable {
                        launcher.launch("image/*")
                    }
            )
            OutlinedTextField(
                value = nome,
                singleLine = true,
                onValueChange = { nome = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Nome do Pet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Animal",
                options = Animal.entries,
                selectedOption = animal,
                onOptionSelected = { animal = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            OutlinedTextField(
                value = raca,
                singleLine = true,
                onValueChange = { raca = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Raça",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Porte",
                options = Porte.entries,
                selectedOption = porte,
                onOptionSelected = { porte = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            OutlinedTextField(
                value = idade,
                singleLine = true,
                onValueChange = { idade = it },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        "Idade",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            DropdownSelector(
                label = "Gênero",
                options = Genero.entries,
                selectedOption = genero,
                onOptionSelected = { genero = it }
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.medium)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Descrição",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = descricao,
                        onValueChange = { descricao = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.titleSmall,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.small)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp)),
                shape = RoundedCornerShape(4.dp),
                color = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.medium)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Contato", style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = email,
                        singleLine = true,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Email",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = telefone,
                        singleLine = true,
                        onValueChange = { telefone = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Telefone",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    DropdownSelector(
                        label = "Estado",
                        options = Estado.entries,
                        selectedOption = estado.toString(),
                        onOptionSelected = { estado = it as Estado }
                    )
                    Spacer(modifier = Modifier.height(Spacing.small))
                    OutlinedTextField(
                        value = cidade,
                        singleLine = true,
                        onValueChange = { cidade = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                "Cidade",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(Spacing.small))
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = {
                    val pet = userId?.let {
                        Pet(
                            nome = nome,
                            raca = raca,
                            genero = genero,
                            idade = idade.toIntOrNull() ?: 0,
                            descricao = descricao,
                            status = status,
                            animal = animal,
                            porte = porte,
                            email = email,
                            telefone = telefone,
                            estado = estado,
                            cidade = cidade,
                            conta = it,
                            foto = imageUri.toString(),
                        )
                    }
                    if (pet != null) {
                        viewModel.updatePet(petId!!, pet)
                    }
                    navController.clearBackStack("postFormularioAlterar/${petId}")
                    navController.clearBackStack("principal")
                    navController.navigate("principal")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Alterar", color = MaterialTheme.colorScheme.onPrimary)
            }
            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = {
                    viewModel.deletePet(petId!!)
                    navController.navigate("principal") {
                        popUpTo("postFormularioAlterar") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Excluir")
            }
        }
    }
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