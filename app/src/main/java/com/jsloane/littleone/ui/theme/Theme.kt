package com.jsloane.littleone.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.R

private val LightColorPalette = lightColors(
    primary = PurpleDark,
    primaryVariant = PurpleDark,
    onPrimary = OffWhite,
    secondary = PurpleGray,
    secondaryVariant = PurpleGray,
    onSecondary = DarkText,
    background = Blush,
    onBackground = PurpleDark,
    surface = OffWhite,
    onSurface = PurpleDark
)

private val DarkColorPalette = darkColors(
    primary = BlushDark,
    primaryVariant = Blush,
    onPrimary = DarkText,
    secondary = PurpleGray,
    secondaryVariant = PurpleGray,
    onSecondary = DarkText
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

@Preview("Gallery - Day")
@Preview("Gallery - Night", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun Gallery() {
    LittleOneTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar(cutoutShape = CircleShape) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Menu, "")
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Share, "")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Search, "")
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {}
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) {
                    Card(elevation = 2.dp) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .padding(16.dp)
                                        .background(Color(0xFFE6E6E6), CircleShape)
                                        .size(40.dp)
                                        .padding(8.dp)
                                ) {
                                    Image(painterResource(id = R.drawable.ic_preview), "")
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(bottom = 2.dp)
                                ) {
                                    Text(
                                        text = "Headline 6",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        modifier = Modifier.alpha(ContentAlpha.medium),
                                        text = "Body 2",
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                Icon(
                                    Icons.Default.Share, "",
                                    modifier = Modifier.padding(24.dp)
                                )
                            }
                            Image(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(Color(0xFFE6E6E6))
                                    .padding(32.dp)
                                    .fillMaxWidth()
                                    .aspectRatio(2f),
                                painter = painterResource(id = R.drawable.ic_preview),
                                contentDescription = ""
                            )
                            Row(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(3) {
                                    Image(
                                        modifier = Modifier
                                            .background(Color(0xFFE6E6E6))
                                            .padding(32.dp)
                                            .aspectRatio(1f)
                                            .weight(1f),
                                        painter = painterResource(id = R.drawable.ic_preview),
                                        contentDescription = ""
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 12.dp)
                                    .alpha(ContentAlpha.medium),
                                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                                    "sed do eiusmod tempor",
                                style = MaterialTheme.typography.body2,
                            )
                            Row {
                                repeat(2) {
                                    TextButton(
                                        modifier = Modifier.padding(8.dp),
                                        onClick = {}
                                    ) {
                                        Text("BUTTON")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun readableTextColor(color: Color): Color =
    if (color.luminance() > 0.179) Color.Black else Color.White
