package com.aondepet.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Animal
import com.aondepet.ui.models.Estado
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet
import com.aondepet.ui.models.Porte
import com.aondepet.ui.models.Status

@Composable
fun Post(navController: NavController, viewModel: PetViewModel, petId: String? = ""){

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
                    idade = pet?.idade.toString()
                    estado = pet?.estado ?: Estado.AC
                    cidade = pet?.cidade ?: ""
                    status = pet?.status ?: Status.Adotado
                    descricao = pet?.descricao ?: ""
                    email = pet?.email ?: ""
                    telefone = pet?.telefone ?: ""
                    foto = pet?.foto ?: Uri.EMPTY
                    conta = pet?.conta ?: ""
                }
            }
        }
    }

    val userId by viewModel.userId.observeAsState()
    val authState = viewModel.authState.observeAsState()

    var isFavorite by remember { mutableStateOf(false) }
    LaunchedEffect(petId) {
        userId?.let {
            viewModel.isFavorito(it, petId!!).addOnSuccessListener { result ->
                isFavorite = result
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 8.dp)
        ,
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
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Icone seta voltar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Post",
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
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$nome",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Status: $status",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        } // Row textos: Nome e Status

        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "Imagem Pet",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
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
                tint = MaterialTheme.colorScheme.primary
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
                            modifier = Modifier.size(24.dp)
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
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        } // Row icone gênero e Coração

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Animal: $animal",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Raça: $raca",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                } // Column que segura a primeira coluna de dados do Pet
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Porte: $porte",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Idade: $idade anos",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Estado: $estado",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Cidade: $cidade",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }// Column que segura a segunda coluna de dados do Pet
            } //Row que possui as duas columns
        } // Surface roxa para dados Pet

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
                    Text(
                        text = "$descricao",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } //Coluna para ajeitar descrição
        } //Fim Surface de descrição

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
                    Text(
                        text = "Email: $email",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Telefone: $telefone",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } //Coluna para ajeitar contato
        } //Fim Surface de Contato
    } // Fim Column Principal
}