package com.aondepet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet

@Composable
fun CardPet(navController: NavController, pet: Pet, authState: AuthState, viewModel: PetViewModel, userId: String) {
    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(pet.id) {
        viewModel.isFavorito(userId, pet.id!!).addOnSuccessListener { result ->
            isFavorite = result
        }
    }
    Surface(
        onClick = { navController.navigate("post/${pet.id}") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
        shape = RoundedCornerShape(4.dp),
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = "Imagem do Pet ${pet.nome}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(128.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )

            } // Row com a imagem do pet
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val generoIcon = when (pet.genero) {
                    Genero.Macho -> R.drawable.male
                    Genero.Femea -> R.drawable.female
                    else -> R.drawable.question_mark
                }
                Icon(
                    painter = painterResource(generoIcon),
                    contentDescription = "Ícone gênero",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = pet.nome,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                if (authState == AuthState.Authenticated) {
                    if (pet.conta == userId) {
                        IconButton(
                            onClick = { navController.navigate("postFormularioAlterar/${pet.id}") }
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
                                pet.id?.let { petId ->
                                    viewModel.favoritar(userId, petId)
                                    viewModel.isFavorito(userId, petId)
                                        .addOnSuccessListener { result ->
                                            isFavorite = result
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
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}