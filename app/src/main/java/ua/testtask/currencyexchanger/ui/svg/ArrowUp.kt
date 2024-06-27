package ua.testtask.currencyexchanger.ui.svg

import androidx.compose.material.icons.Icons
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.ArrowUp: ImageVector by lazy {
    Builder(
        name = "Vector1", defaultWidth = 8.0.dp, defaultHeight = 14.0.dp,
        viewportWidth = 8.0f, viewportHeight = 14.0f,
    ).apply {
        path(
            fill = linearGradient(
                0.0f to Color(0xFF00CA50), 1.0f to Color(0x0000CA50),
                start = Offset(4.0f, 8.0f),
                end = Offset(4.0f, 14.0f),
            ),
            stroke = null,
            strokeLineWidth = 0.0f,
            strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
            pathFillType = NonZero,
        ) {
            moveTo(3.0f, 3.99f)
            lineTo(3.0f, 14.0f)
            horizontalLineTo(5.0f)
            lineTo(5.0f, 3.99f)
            horizontalLineTo(8.0f)
            lineTo(4.0f, 0.0f)
            lineTo(0.0f, 3.99f)
            horizontalLineTo(3.0f)
            close()
        }
    }.build()
}
