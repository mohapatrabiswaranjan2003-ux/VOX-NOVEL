package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.TtsAudioController

@Composable
fun SleepTimerDialog(
    audioController: TtsAudioController,
    onDismiss: () -> Unit
) {
    val remainingSec by audioController.sleepTimerSec.collectAsStateWithLifecycle()

    val options = listOf(5, 15, 30, 45, 60)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Sleep Timer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (remainingSec != null) {
                    val mins = remainingSec!! / 60
                    val secs = remainingSec!! % 60
                    Text(
                        text = "Active countdown: ${String.format("%02d:%02d", mins, secs)} remaining",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                } else {
                    Text(
                        text = "Select when to pause speech automatically. Perfect for relaxing and listening before sleep.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    options.take(3).forEach { minutes ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                audioController.startSleepTimer(minutes)
                                onDismiss()
                            },
                            label = { Text("${minutes}m", fontSize = 13.sp) },
                            modifier = Modifier.testTag("sleep_timer_${minutes}m")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    options.drop(3).forEach { minutes ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                audioController.startSleepTimer(minutes)
                                onDismiss()
                            },
                            label = { Text("${minutes}m", fontSize = 13.sp) },
                            modifier = Modifier.testTag("sleep_timer_${minutes}m")
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (remainingSec != null) {
                OutlinedButton(
                    onClick = {
                        audioController.cancelSleepTimer()
                        onDismiss()
                    },
                    modifier = Modifier.testTag("cancel_sleep_timer_button")
                ) {
                    Text("Turn Off Timer")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.testTag("close_sleep_timer_dialog")
            ) {
                Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
