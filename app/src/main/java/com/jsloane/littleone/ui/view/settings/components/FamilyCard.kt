package com.jsloane.littleone.ui.view.settings.components

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jsloane.littleone.ui.theme.LittleOneTheme
import com.jsloane.littleone.util.RelativeTimeFormatter
import java.time.Instant

@Composable
fun FamilyCard(
    modifier: Modifier = Modifier,
    inviteCode: String?,
    expiration: Instant?,
    refresh: () -> Unit,
    delete: () -> Unit,
) {
    val context = LocalContext.current

    Card(modifier = modifier.width(IntrinsicSize.Max)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.Start),
                text = "Family Members",
                style = MaterialTheme.typography.body1
            )
            if (inviteCode.isNullOrBlank() || expiration?.isBefore(Instant.now()) == true) {
                Text(
                    text = "Invite another caretaker to your family?",
                    style = MaterialTheme.typography.body2
                )
                OutlinedButton(onClick = refresh) {
                    Text(
                        text = "Create new invite code",
                        style = MaterialTheme.typography.button
                    )
                }
            } else {
                Text(
                    text = "Current invite code",
                    style = MaterialTheme.typography.body2
                )
                SelectionContainer {
                    Text(
                        text = inviteCode.orEmpty(),
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.primary
                    )
                }
                Text(
                    text = "Expires ${RelativeTimeFormatter.format(expiration ?: Instant.EPOCH)}",
                    style = MaterialTheme.typography.body2
                )
                Row {
                    IconButton(onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                            .putExtra(Intent.EXTRA_TEXT, inviteCode)
                            .setType("text/plain")
                        context.startActivity(Intent.createChooser(shareIntent, null))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "share code"
                        )
                    }
                    IconButton(onClick = refresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "refresh code"
                        )
                    }
                    IconButton(onClick = delete) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "delete code"
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFamilyCard() {
    LittleOneTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FamilyCard(
                modifier = Modifier.fillMaxWidth(),
                inviteCode = "aFd4J1",
                expiration = Instant.now().plusSeconds(4 * 60 * 60),
                refresh = {},
                delete = {}
            )
            FamilyCard(
                modifier = Modifier.fillMaxWidth(),
                inviteCode = null,
                expiration = Instant.now().plusSeconds(4 * 60 * 60),
                refresh = {},
                delete = {}
            )
        }
    }
}