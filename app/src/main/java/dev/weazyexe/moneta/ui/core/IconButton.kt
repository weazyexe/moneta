package dev.weazyexe.moneta.ui.core

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.material3.IconButton as Material3IconButton

@Composable
fun IconButton(
    painter: Painter,
    contentDescription: String?,
    onClick: () -> Unit,
) {
    Material3IconButton(onClick = onClick) {
        Icon(painter = painter, contentDescription = contentDescription)
    }
}