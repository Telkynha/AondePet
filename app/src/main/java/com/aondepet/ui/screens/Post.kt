package com.aondepet.ui.screens

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

@Composable
fun Post(navController: NavController, viewModel: PetViewModel){
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
                    painter = painterResource(R.drawable.chevron_left),
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
                text = "Garfield",
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Status: Perdido",
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
            Icon(
                painter = painterResource(R.drawable.male),
                contentDescription = "Icone genero macho",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Codigo: G000001",
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icone favoritar animal",
                    tint = MaterialTheme.colorScheme.secondary
                )
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
                        text = "Animal: Gato",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Sexo: Macho",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Peso: 5kg",
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
                        text = "Raça: Persa",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Porte: Médio",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Idade: 3 anos",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                } // Column que segura a segunda coluna de dados do Pet
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
                        text = "Texto descrevendo o que aconteceu - Lorem ipsum dolor sit amet, " +
                                "consectetur adipiscing elit. Maecenas mattis eget massa sed " +
                                "ultricies. Nulla facilisi. Cras fermentum, nunc ac laoreet " +
                                "ultricies, ipsum nunc ultricies eros, sed tincidunt massa nunc ac" +
                                " massa. Maecenas eget lacus eget nunc tincidunt laoreet. Maecenas " +
                                "eget lacus eget nunc tincidunt laoreet.",
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
                        text = "Email: Email@email.com",
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
                        text = "Telefone: (11) 91234-5678",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            } //Coluna para ajeitar contato
        } //Fim Surface de Contato
    } // Fim Column Principal
}