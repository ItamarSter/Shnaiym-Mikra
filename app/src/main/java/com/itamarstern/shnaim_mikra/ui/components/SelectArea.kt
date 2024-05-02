package com.itamarstern.shnaim_mikra.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun SelectArea(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String? = null,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit
) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            text2?.let { text2safe ->
                Row (modifier.weight(1f).padding(4.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween){
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = "back",
                        )
                    }
                    Text(
                        text = text2safe,
                        style = TextStyle(textDirection = TextDirection.Rtl)
                    )
                }
                Row (modifier.weight(1f).padding(4.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = text1)
                    IconButton(
                        onClick = onForwardClick,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "forward"
                        )
                    }
                }
            } ?: let {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "back",
                    )
                }
                Text(text = text1)
                IconButton(
                    onClick = onForwardClick,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "forward"
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun SelectAreaPreview() {
    SelectArea(text1 = "ספר", text2 = "במדבר", onBackClick = { }) {

    }
}