package com.jsloane.littleone.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LightColorPalette = lightColors(
    primary = PinkMedium,
    primaryVariant = PinkDark,
    secondary = GreenMedium,
    secondaryVariant = GreenDark,
    onPrimary = Color.Black
)

private val DarkColorPalette = darkColors(
    primary = PinkDark,
    primaryVariant = PinkLight,
    onPrimary = Color.White,
    secondary = GreenDark,
    secondaryVariant = GreenMedium,
    onSecondary = Color.White
)

@Composable
fun LittleOneTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Preview("Light Color Palette", backgroundColor = 0xE5E5E5, showBackground = true)
@Composable
private fun lightColorPreview() {
    Column(Modifier.padding(32.dp)) {
        drawSwatch(
            name = "Primary",
            color = LightColorPalette.primary,
            onColor = LightColorPalette.onPrimary
        )
        drawSwatch(name = "Primary Variant", color = LightColorPalette.primaryVariant)
        drawSwatch(
            name = "Secondary",
            color = LightColorPalette.secondary,
            onColor = LightColorPalette.onSecondary
        )
        drawSwatch(name = "Secondary Variant", color = LightColorPalette.secondaryVariant)
        drawSwatch(
            name = "Background",
            color = LightColorPalette.background,
            onColor = LightColorPalette.onBackground
        )
        drawSwatch(
            name = "Surface",
            color = LightColorPalette.surface,
            onColor = LightColorPalette.onSurface
        )
        drawSwatch(
            name = "Error",
            color = LightColorPalette.error,
            onColor = LightColorPalette.onError
        )
    }
}

@Preview("Dark Color Palette", backgroundColor = 0x292929, showBackground = true)
@Composable
private fun darkColorPreview() {
    Column(Modifier.padding(32.dp)) {
        drawSwatch(
            name = "Primary",
            color = DarkColorPalette.primary,
            onColor = DarkColorPalette.onPrimary
        )
        drawSwatch(name = "Primary Variant", color = DarkColorPalette.primaryVariant)
        drawSwatch(
            name = "Secondary",
            color = DarkColorPalette.secondary,
            onColor = DarkColorPalette.onSecondary
        )
        drawSwatch(name = "Secondary Variant", color = DarkColorPalette.secondaryVariant)
        drawSwatch(
            name = "Background",
            color = DarkColorPalette.background,
            onColor = DarkColorPalette.onBackground
        )
        drawSwatch(
            name = "Surface",
            color = DarkColorPalette.surface,
            onColor = DarkColorPalette.onSurface
        )
        drawSwatch(
            name = "Error",
            color = DarkColorPalette.error,
            onColor = DarkColorPalette.onError
        )
    }
}

@Composable
private fun drawSwatch(name: String, color: Color, onColor: Color? = null) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    name,
                    style = MaterialTheme.typography.h6,
                    color = onColor ?: readableTextColor(color)
                )
                Text(
                    String.format("#%06X", 0xFFFFFF and color.toArgb()),
                    style = MaterialTheme.typography.body1,
                    color = onColor ?: readableTextColor(color)
                )
            }
            if (onColor != null) {
                Surface(color = onColor) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = String.format("#%06X", 0xFFFFFF and onColor.toArgb()),
                        style = MaterialTheme.typography.body1,
                        color = readableTextColor(onColor)
                    )
                }
            }
        }
    }
}

private fun readableTextColor(color: Color): Color =
    if (color.luminance() > 0.179) Color.Black else Color.White
