package com.itamarstern.shnaim_mikra.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BorderText(text: String, isEnabled: Boolean, modifier: Modifier = Modifier) {
    val alphaValue = if (isEnabled) 1f else 0.4f

    Surface(
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Black.copy(alpha = alphaValue))
    ) {
        Box(modifier = modifier.padding(8.dp, 4.dp)) {
            Text(
                text = text,
                color = Color.Black.copy(alpha = alphaValue)
            )
        }
    }
}