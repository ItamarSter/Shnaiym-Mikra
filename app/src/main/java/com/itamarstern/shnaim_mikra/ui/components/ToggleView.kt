package com.itamarstern.shnaim_mikra.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.itamarstern.shnaim_mikra.R

@Composable
fun ToggleView(
    isConnected: Boolean,
    onConnectParashasToggled: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(color = colorResource(id = R.color.brown3)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BorderText(
            text = "הצג פרשות נפרדות",
            isEnabled = isConnected,
            modifier = Modifier
                .clickable(enabled = isConnected) {
                    if (isConnected) onConnectParashasToggled()
                }
                .background(color = colorResource(id = R.color.brown3))
        )
        BorderText(
            text = "הצג פרשות מחוברות",
            isEnabled = !isConnected,
            modifier = Modifier
                .clickable(enabled = !isConnected) {
                    if (!isConnected) onConnectParashasToggled()
                }
                .background(color = colorResource(id = R.color.brown3))
        )
    }
}