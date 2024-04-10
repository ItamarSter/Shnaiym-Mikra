package com.itamarstern.shnaim_mikra.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FailureText() {
    Box(
        modifier =
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "אין חיבור אינטרנט",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}