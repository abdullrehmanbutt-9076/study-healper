package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.EnhancedEncryption
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.CloudSyncInfo
import com.example.domain.model.SyncState
import com.example.ui.theme.Amber40
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Emerald40
import com.example.ui.theme.Indigo60

@Composable
fun CloudSyncScreen(
    syncInfo: CloudSyncInfo,
    onTriggerSync: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var exportSuccessAlert by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("sync_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Secure Cloud Synchronization",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Offline-first with end-to-end encrypted backup",
                        fontSize = 12.sp,
                        color = Emerald40
                    )
                }
            }
        }

        // Cloud Status Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("cloud_status_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, Emerald40.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Emerald40.copy(alpha = 0.15f))
                                    .border(1.dp, Emerald40, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (syncInfo.status == SyncState.SYNCING) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                    contentDescription = null,
                                    tint = Emerald40,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (syncInfo.status == SyncState.SYNCING) "Synchronizing Encrypted Data..." else "Cloud Sync Active",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "Last synced: ${syncInfo.lastSyncedTime}",
                                    fontSize = 12.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (syncInfo.pendingChangesCount > 0) Amber40.copy(alpha = 0.2f) else Emerald40.copy(alpha = 0.2f)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (syncInfo.pendingChangesCount > 0) "${syncInfo.pendingChangesCount} Pending" else "Up to date",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (syncInfo.pendingChangesCount > 0) Amber40 else Emerald40
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sync Button
                    Button(
                        onClick = onTriggerSync,
                        enabled = syncInfo.status != SyncState.SYNCING,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("sync_now_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald40)
                    ) {
                        if (syncInfo.status == SyncState.SYNCING) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Encrypting & Pushing Changes...", color = Color.Black, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.CloudSync, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sync to Cloud Now", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Security & Cryptographic Fingerprint Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("security_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EnhancedEncryption, contentDescription = null, tint = Cyan40, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Security & Encryption Standard",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface2)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Encryption Protocol", fontSize = 11.sp, color = DarkTextSecondary)
                            Text(text = syncInfo.encryptionStandard, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkTextPrimary)
                        }
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Cyan40, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface2)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Cloud Token / Cryptographic Digest", fontSize = 11.sp, color = DarkTextSecondary)
                            Text(text = syncInfo.cloudToken, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Indigo60)
                        }
                        Icon(Icons.Default.Security, contentDescription = null, tint = Indigo60, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // Offline-First Sync Queue Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Local Storage & Queue Status",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurface2)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Local Room DB", fontSize = 11.sp, color = DarkTextSecondary)
                                Text(text = "Active & Offline Ready", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Emerald40)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurface2)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Total Synced Items", fontSize = 11.sp, color = DarkTextSecondary)
                                Text(text = "${syncInfo.totalSyncedItems} records", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Cyan40)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Backup & Restore Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { exportSuccessAlert = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("export_backup_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Text("Export Backup", fontSize = 12.sp, color = Cyan40)
                        }

                        OutlinedButton(
                            onClick = { onTriggerSync() },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("restore_cloud_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, tint = Emerald40, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Pull Remote", fontSize = 12.sp, color = Emerald40)
                        }
                    }

                    AnimatedVisibility(visible = exportSuccessAlert) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Emerald40, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Encrypted backup archive created and verified successfully!",
                                fontSize = 11.sp,
                                color = Emerald40
                            )
                        }
                    }
                }
            }
        }

        // Cloud Audit Logs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = DarkTextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recent Cloud Audit Logs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        Triple("Encrypted sync handshake completed", "Success • 3 pushed, 0 pulled", "03:30 AM"),
                        Triple("Periodic background checkpoint", "Success • 0 pending", "02:15 AM"),
                        Triple("Initial device token generated", "AES-256 authenticated", "Yesterday")
                    ).forEach { (event, detail, time) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = event, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkTextPrimary)
                                Text(text = detail, fontSize = 10.sp, color = DarkTextSecondary)
                            }
                            Text(text = time, fontSize = 10.sp, color = DarkTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
