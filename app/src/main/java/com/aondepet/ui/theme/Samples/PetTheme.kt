package com.aondepet.ui.theme.Samples

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aondepet.ui.theme.AondePetTheme

@Preview(showBackground = true)
@Composable
fun PetTheme(){
    Row(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Color", style = MaterialTheme.typography.bodyLarge)
            ColorBox("Primary", MaterialTheme.colorScheme.primary)
            ColorBox("Primary Container", MaterialTheme.colorScheme.primaryContainer)
            ColorBox("Secondary", MaterialTheme.colorScheme.secondary)
            ColorBox("Secondary Container", MaterialTheme.colorScheme.secondaryContainer)
            ColorBox("Tertiary", MaterialTheme.colorScheme.tertiary)
            ColorBox("Tertiary Container", MaterialTheme.colorScheme.tertiaryContainer)
            ColorBox("Error", MaterialTheme.colorScheme.error)
            ColorBox("Error Container", MaterialTheme.colorScheme.errorContainer)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "On Color", style = MaterialTheme.typography.bodyLarge)
            ColorBox("On Primary", MaterialTheme.colorScheme.onPrimary)
            ColorBox("On Primary Container", MaterialTheme.colorScheme.onPrimaryContainer)
            ColorBox("On Secondary", MaterialTheme.colorScheme.onSecondary)
            ColorBox("On Secondary Container", MaterialTheme.colorScheme.onSecondaryContainer)
            ColorBox("On Tertiary", MaterialTheme.colorScheme.onTertiary)
            ColorBox("On Tertiary Container", MaterialTheme.colorScheme.onTertiaryContainer)
            ColorBox("On Error", MaterialTheme.colorScheme.onError)
            ColorBox("On Error Container", MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}

@Composable
fun ColorBox(name: String, color: androidx.compose.ui.graphics.Color) {
    Column(modifier = Modifier
        .padding(vertical = 8.dp)) {
        Text(text = name, style = MaterialTheme.typography.bodyMedium)
        Box(modifier = Modifier
            .size(48.dp)
            .background(color))
    }
}