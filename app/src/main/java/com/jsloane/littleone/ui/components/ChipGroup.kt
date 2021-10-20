package com.jsloane.littleone.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.jsloane.littleone.ui.theme.LittleOneTheme

@Composable
fun ChipGroup(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    FlowRow(
        modifier = modifier,
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = FlowMainAxisAlignment.Start,
        mainAxisSpacing = 8.dp,
        crossAxisAlignment = FlowCrossAxisAlignment.Start,
        crossAxisSpacing = 8.dp
    ) {
        content()
    }
}

@Preview
@Composable
fun ChipGroupPreview() {
    LittleOneTheme {
        Surface(color = MaterialTheme.colors.primary) {
            val list = remember {
                mutableStateListOf("Chip 1", "Chip 2", "Chip 3", "Chip 4", "Chip 5", "Chip 6")
            }
            ChipGroup(modifier = Modifier.padding(16.dp)) {
                list.forEachIndexed { index, s ->
                    OutlinedFilterChip(
                        text = s,
                        selected = index % 2 == 0,
                        enabled = index % 3 != 0
                    )
                }
            }
        }
    }
}
