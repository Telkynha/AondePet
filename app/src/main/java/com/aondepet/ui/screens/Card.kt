package com.aondepet.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.aondepet.R
import com.aondepet.ui.control.AuthState
import com.aondepet.ui.control.PetViewModel
import com.aondepet.ui.models.Genero
import com.aondepet.ui.models.Pet
import com.aondepet.ui.theme.Spacing

@Composable
fun CardPet(navController: NavController, pet: Pet, authState: AuthState, viewModel: PetViewModel, userId: String) {
    val favoritos by viewModel.favoritos.observeAsState(emptyList()) // Observe a lista de favoritos
    val isFavorite = favoritos.contains(pet.id)
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val petImage by viewModel.getPetImage(pet.id!!).observeAsState()

    LaunchedEffect(petImage) {
        imageUri = petImage
    }

    Surface(
        onClick = { navController.navigate("post/${pet.id}") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.small)
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val painter: Painter = if (imageUri != null) {
                    rememberAsyncImagePainter(
                        model = imageUri,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    painterResource(R.drawable.img)
                }

                Image(
                    painter = painter,
                    contentDescription = "Imagem do Pet ${pet.nome}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp))
                )

            } // Row com a imagem do pet
            Row(
                modifier = Modifier
                    .padding(Spacing.small)
                    .height(40.dp)
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
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(start = Spacing.small)
                        .size(24.dp)
                )
                Text(
                    text = pet.nome,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
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
                            onClick = {
                                viewModel.favoritar(userId, pet.id!!) // Aciona o método para favoritar
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
                }else{
                    Icon(
                        painter = painterResource(generoIcon),
                        contentDescription = "Espaçamento",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                            .alpha(0f)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(Spacing.medium))
}