package com.aondepet.ui.theme.Samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aondepet.ui.theme.AondePetTheme
import com.aondepet.ui.theme.Spacing

@Preview(showBackground = true)
@Composable
fun PetSpacing(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.colunaPrincipal)
    ) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.extraSmall)
        ) {
            Text(
                text = "Extra Small Spacing",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.small)
        ) {
            Text(
                text = "Small Spacing",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.medium)
        ) {
            Text(
                text = "Medium Spacing",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.large)
        ) {
            Text(
                text = "Large Spacing",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(Spacing.extraLarge)
        ) {
            Text(
                text = "Extra Large Spacing",
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = {},
            modifier = Modifier.padding(Spacing.buttonPadding)
        ) {
            Text(text = "Button with Padding", style = MaterialTheme.typography.labelLarge)
        }

        Card(
            modifier = Modifier
                .padding(Spacing.cardPadding)
                .fillMaxWidth()
        ) {
            Text(
                text = "Card with Padding",
                modifier = Modifier.padding(Spacing.medium),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.padding(Spacing.textFieldPadding),
            label = { Text(text = "TextField with Padding", style = MaterialTheme.typography.bodySmall) }
        )
    }
}