package com.jsloane.littleone.ui.tracking

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterChipsSection(
    title: String? = null,
) {
    Column {
        FilterHeader(title = title.orEmpty())
    }
}

@Composable
fun FilterHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.subtitle1
    )
}
