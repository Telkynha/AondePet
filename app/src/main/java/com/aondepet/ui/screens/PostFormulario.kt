package com.aondepet.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import java.net.URI

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Ícone seta voltar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Novo Pet",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { navController.navigate("conta") },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Ícone menu",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        val painter: Painter = if (imageUri != null) {
            rememberAsyncImagePainter(
                model = imageUri, // Use a URI de conteúdo diretamente
                contentScale = ContentScale.Crop
            )
        } else {
            painterResource(R.drawable.img)
        }

        Image(painter = painter, contentDescription = "Imagem Pet",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable {
                    launcher.launch("image/*")
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Nome do Pet",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                DropdownSelector(
                    label = "Status",
                    options = Status.entries,
                    selectedOption = status,
                    onOptionSelected = { status = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Animal",
                    options = Animal.entries,
                    selectedOption = animal,
                    onOptionSelected = { animal = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Gênero",
                    options = Genero.entries,
                    selectedOption = genero,
                    onOptionSelected = { genero = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = raca,
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
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Porte",
                    options = Porte.entries,
                    selectedOption = porte,
                    onOptionSelected = { porte = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = idade,
                    onValueChange = { idade = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            "Idade",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Estado",
                    options = Estado.entries,
                    selectedOption = estado.toString(),
                    onOptionSelected = { estado = it as Estado }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = cidade,
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
                Text(
                    text = "Descrição",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }

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
                Text(
                    text = "Contato",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            "Email",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
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
        }

        Button(
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
                    navController.navigate("principal")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Adicionar", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun PostFormularioAlterar(
    navController: NavController,
    viewModel: PetViewModel,
    petId: String? = ""
) {

    // Pet mockado
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
    var imageUri by remember { mutableStateOf<Uri?>(viewModel.getPetImage(petId!!).value ?: null) }
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
                imageUri = pet?.foto?.toUri()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Ícone seta voltar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Alterar Pet",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { navController.navigate("conta") },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Ícone menu",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        val painter: Painter = if (imageUri != null) {
            rememberAsyncImagePainter(
                model = imageUri, // Use a URI de conteúdo diretamente
                contentScale = ContentScale.Crop
            )
        } else {
            painterResource(R.drawable.img)
        }

        Image(painter = painter, contentDescription = "Imagem Pet",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable {
                    launcher.launch("image/*")
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Nome do Pet",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                DropdownSelector(
                    label = "Status",
                    options = Status.entries,
                    selectedOption = status,
                    onOptionSelected = { status = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Animal",
                    options = Animal.entries,
                    selectedOption = animal,
                    onOptionSelected = { animal = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Gênero",
                    options = Genero.entries,
                    selectedOption = genero,
                    onOptionSelected = { genero = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = raca,
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
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Porte",
                    options = Porte.entries,
                    selectedOption = porte,
                    onOptionSelected = { porte = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = idade,
                    onValueChange = { idade = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            "Idade",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                DropdownSelector(
                    label = "Estado",
                    options = Estado.entries,
                    selectedOption = estado.toString(),
                    onOptionSelected = { estado = it as Estado }
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = cidade,
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
                Text(
                    text = "Descrição",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
            }
        }

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
                Text(
                    text = "Contato",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            "Email",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = telefone,
                    onValueChange = { telefone = it },
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
        }

        Button(
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
