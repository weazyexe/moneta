package dev.weazyexe.moneta.ui.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun SearchTextField(
    text: String,
    placeholder: String,
    onTextChanged: (String) -> Unit,
    textStyle: TextStyle,
    modifier: Modifier
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        modifier = modifier,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box {
                if (text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = textStyle.copy(color = textStyle.color.copy(alpha = 0.5f))
                    )
                }

                innerTextField()
            }
        }
    )
}