package com.aondepet.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Animal
import com.aondepet.ui.models.Estado
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet
import com.aondepet.ui.models.Porte
import com.aondepet.ui.models.Status
import com.aondepet.ui.theme.Spacing

@Composable
fun Post(navController: NavController, viewModel: PetViewModel, petId: String? = "") {

    var pet by remember { mutableStateOf<Pet?>(null) }
    var nome by remember { mutableStateOf("") }
    var animal by remember { mutableStateOf(Animal.Cachorro) }
    var raca by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf(Genero.Macho) }
    var porte by remember { mutableStateOf(Porte.Medio) }
    var idade by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf(Estado.AC) }
    var cidade by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(Status.Adotado) }
    var descricao by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf<Uri?>(null) }
    var conta by remember { mutableStateOf("") }

    LaunchedEffect(petId) {
        if (petId != null) {
            viewModel.getPetById(petId).addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    pet = document.toObject(Pet::class.java)
                    nome = pet?.nome ?: ""
                    animal = pet?.animal ?: Animal.Cachorro
                    raca = pet?.raca ?: ""
                    genero = pet?.genero ?: Genero.Macho
                    porte = pet?.porte ?: Porte.Medio
                    idade = pet?.idade ?: ""
                    estado = pet?.estado ?: Estado.AC
                    cidade = pet?.cidade ?: ""
                    status = pet?.status ?: Status.Adotado
                    descricao = pet?.descricao ?: ""
                    email = pet?.email ?: ""
                    telefone = pet?.telefone ?: ""
                    conta = pet?.conta ?: ""
                    foto = pet?.foto?.toUri()
                }
            }
        }
    }

    val userId by viewModel.userId.observeAsState()
    val authState = viewModel.authState.observeAsState()

    val petImage by viewModel.getPetImage(petId!!).observeAsState()

    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(petId) {
        userId?.let {
            viewModel.isFavorito(it, petId!!).addOnSuccessListener { result ->
                isFavorite = result
            }
        }
    }

    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Ícone voltar pagina anterior",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Spacing.large)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f) // Peso 1
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    // Box vazia, ocupa espaço
                }
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
            }
        }
    ) { paddingValues ->
        // Conteúdo da tela
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.large)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = nome,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            if (petImage != null) {
                AsyncImage(
                    model = petImage,
                    contentDescription = "Imagem do Pet",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = Spacing.medium)
                        .fillMaxWidth()
                        .aspectRatio(4 / 3f)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Imagem Pet",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = Spacing.medium)
                        .fillMaxWidth()
                        .aspectRatio(4 / 3f)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(4.dp))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val generoIcon = when (genero) {
                    Genero.Macho -> R.drawable.male
                    Genero.Femea -> R.drawable.female
                    else -> R.drawable.question_mark
                }
                Icon(
                    painter = painterResource(generoIcon),
                    contentDescription = "Ícone gênero",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(Spacing.large)
                )
                if (authState.value == AuthState.Authenticated) {
                    if (conta == userId) {
                        IconButton(
                            onClick = { navController.navigate("postFormularioAlterar/${petId}") }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "Ícone alterar post animal",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(Spacing.large)
                            )
                        }
                    } else {
                        IconButton(
                            modifier = Modifier.padding(0.dp),
                            onClick = {
                                petId?.let { petId ->
                                    userId?.let { viewModel.favoritar(it, petId) }
                                    userId?.let {
                                        viewModel.isFavorito(it, petId)
                                            .addOnSuccessListener { result ->
                                                isFavorite = result
                                            }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(if (isFavorite) R.drawable.favorite_fill else R.drawable.favorite),
                                contentDescription = "Ícone favoritar animal",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(Spacing.large)
                            )
                        }
                    }
                }
            } // Row icone gênero e Coração

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Spacing.medium)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp), // Arredondando mais a borda
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .padding(Spacing.large)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Primeira Coluna
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Animal: $animal",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineHeight = 22.sp // Aumentando o espaçamento entre linhas
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Idade: $idade",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Porte: $porte",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Raça: $raca",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineHeight = 22.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
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
                    Row(
                        modifier = Modifier
                            .padding(bottom = Spacing.small)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Descrição",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.small))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = descricao,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } //Coluna para ajeitar descrição
            } //Fim Surface de descrição

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
                    Row(
                        modifier = Modifier
                            .padding(bottom = Spacing.small)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Contato",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.small))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically // Centralizar verticalmente
                    ) {
                        Text(
                            text = "Email: $email",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f) // Ocupar espaço disponível
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(email))
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.content_copy),
                                contentDescription = "Copiar Email",
                                modifier = Modifier.size(Spacing.medium)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically // Centralizar verticalmente
                    ) {
                        Text(
                            text = "Telefone: $telefone",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(telefone))
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.content_copy),
                                contentDescription = "Copiar Telefone",
                                modifier = Modifier.size(Spacing.medium)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(Spacing.medium))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Estado: $estado",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(Spacing.large))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        Text(
                            text = "Cidade: $cidade",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } //Coluna para ajeitar contato
            } //Fim Surface de Contato
        } // Fim Column Principal
    }
}