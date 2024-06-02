package com.itamarstern.shnaim_mikra.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.itamarstern.shnaim_mikra.module.StyledText

@Composable
fun MultiStylesText(
    modifier: Modifier = Modifier,
    textData: ArrayList<StyledText>,
    textSize: Int
) {
    Text(
        modifier = modifier,
        style = TextStyle(textDirection = TextDirection.Rtl, fontSize = textSize.sp),
        text = buildAnnotatedString {
            textData.forEach { styledText ->
                if (styledText.bold && !styledText.bigger) {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        ),
                    ) {
                        append(styledText.text)
                    }
                } else if (styledText.bold && styledText.bigger) {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = (textSize + 4).sp
                        ),
                    ) {
                        append(styledText.text)
                    }
                } else if (!styledText.bold && styledText.bigger) {
                    withStyle(
                        style = SpanStyle(
                            fontSize = (textSize + 4).sp
                        ),
                    ) {
                        append(styledText.text)
                    }
                } else {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(styledText.text)
                    }
                }
            }
        }
    )
}