package com.jsloane.littleone.util

import android.util.Log
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // MM/dd/YYYY
        if (text.text.length < 7) return TransformedText(text, OffsetMapping.Identity)

        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        val match = Regex("(\\d{1,2})(\\d{2})(\\d{4})")
            .find(trimmed)
            ?.destructured
            ?.toList()
            ?: return TransformedText(AnnotatedString(trimmed), OffsetMapping.Identity)

        val out = match.joinToString("/")

        val numberOffsetTranslator = object : OffsetMapping {
            // 1011970 -> 1/01/1970
            // 01011970 -> 01/01/1970
            override fun originalToTransformed(offset: Int): Int {
                Log.d("Transform", "trans: $offset\t${match[0].length}\t${match[1].length}")
                if (offset <= match[0].length) return offset
                if (offset <= match[0].length + match[1].length) return offset + 1
                return (offset + 2).coerceAtMost(out.length)
            }

            // 1/01/1970 -> 1011970
            // 01/01/1970 -> 01011970
            override fun transformedToOriginal(offset: Int): Int {
                Log.d("Transform", "back: $offset\t${match[0].length}\t${match[1].length}")
                if (offset <= match[0].length + 1) return offset
                if (offset <= match[0].length + match[1].length + 2) return offset - 1
                return (offset - 2).coerceAtMost(trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}
