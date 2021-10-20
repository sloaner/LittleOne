package com.jsloane.littleone.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ActivityLog(modifier: Modifier = Modifier) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(15) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "09:30 am", style = MaterialTheme.typography.caption)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp)
                        .background(Color.Cyan, CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Air, contentDescription = null)
                }
                Text(text = "16m, Right Breast", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview
@Composable
fun previewList() {
    ActivityLog()
}
