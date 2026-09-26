package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.domain.model.RoomChatMessage
import com.example.domain.model.StudyRoom
import com.example.ui.theme.Amber40
import com.example.ui.theme.Cyan40
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface1
import com.example.ui.theme.DarkSurface2
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.Emerald40
import com.example.ui.theme.Indigo60

@Composable
fun CollaborationScreen(
    studyRooms: List<StudyRoom>,
    selectedRoom: StudyRoom?,
    roomMessages: Map<String, List<RoomChatMessage>>,
    onJoinRoom: (StudyRoom) -> Unit,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var currentChatInput by remember { mutableStateOf("") }
    var activeRoomState by remember { mutableStateOf(selectedRoom ?: studyRooms.firstOrNull()) }
    var showQuizBattleAlert by remember { mutableStateOf(false) }

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
                    modifier = Modifier.testTag("collab_back_button")
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
                        text = "Real-Time Study Rooms",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Peer problem solving with live AI Co-Pilot",
                        fontSize = 12.sp,
                        color = Indigo60
                    )
                }
            }
        }

        // Room Selector Pills
        item {
            Text(
                text = "Active Study Rooms",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkTextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                studyRooms.forEach { room ->
                    val isCurrent = activeRoomState?.id == room.id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isCurrent) Indigo60 else DarkSurface1)
                            .border(1.dp, if (isCurrent) Indigo60 else DarkBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                activeRoomState = room
                                onJoinRoom(room)
                            }
                            .padding(10.dp)
                            .testTag("room_pill_${room.id}")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = room.subject,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else Cyan40
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Emerald40)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "${room.activeMemberCount}",
                                        fontSize = 10.sp,
                                        color = if (isCurrent) Color.White else DarkTextSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = room.name,
                                fontSize = 11.sp,
                                color = if (isCurrent) Color.White.copy(alpha = 0.9f) else DarkTextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        // Active Room Details & Live Whiteboard/Problem
        if (activeRoomState != null) {
            val room = activeRoomState!!
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("active_room_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Indigo60.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = room.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "Host: ${room.hostName} • ${room.gradeLevel}",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Emerald40.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CastConnected, contentDescription = null, tint = Emerald40, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Live Sync", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Emerald40)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Live Peer Avatars with Status
                        Text(
                            text = "Connected Peers & Status:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkTextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            room.members.forEach { peer ->
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(Color(peer.colorHex))
                                            .border(1.5.dp, if (peer.status == "Solving") Cyan40 else DarkBorder, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = peer.avatarInitial,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = peer.name.take(7),
                                        fontSize = 11.sp,
                                        color = DarkTextPrimary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = peer.status,
                                        fontSize = 9.sp,
                                        color = if (peer.status == "Solving") Cyan40 else Amber40
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Shared Problem Board
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface2)
                                .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📌 Shared Question Board",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Cyan40
                                    )
                                    Button(
                                        onClick = { showQuizBattleAlert = true },
                                        modifier = Modifier.height(30.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Amber40),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Group Quiz Battle", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = room.currentQuestion,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkTextPrimary
                                )
                            }
                        }
                    }
                }
            }

            if (showQuizBattleAlert) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Amber40.copy(alpha = 0.15f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Amber40)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Amber40, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Live Quiz Round Activated!", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Amber40)
                                Text("4 peers are competing live on ${room.subject} questions. Leaderboard updating!", fontSize = 11.sp, color = DarkTextPrimary)
                            }
                            IconButton(onClick = { showQuizBattleAlert = false }) {
                                Icon(Icons.Default.Add, contentDescription = "Close", tint = Amber40)
                            }
                        }
                    }
                }
            }

            // Real-Time Chat & AI Co-Pilot Stream
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("room_chat_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface1),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Forum, contentDescription = null, tint = Cyan40, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Live Peer Chat & AI Co-Pilot",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                            }
                            Text(
                                text = "Tag @ai for help",
                                fontSize = 11.sp,
                                color = Indigo60
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Message bubbles
                        val messages = roomMessages[room.id] ?: emptyList()
                        messages.forEach { msg ->
                            val isAi = msg.isAiAssistant
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isAi) Indigo60.copy(alpha = 0.2f) else DarkSurface2)
                                    .border(
                                        1.dp,
                                        if (isAi) Indigo60.copy(alpha = 0.5f) else DarkBorder,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (isAi) {
                                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Cyan40, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                            }
                                            Text(
                                                text = msg.senderName,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isAi) Cyan40 else Amber40
                                            )
                                        }
                                        Text(text = msg.timestamp, fontSize = 9.sp, color = DarkTextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = msg.text,
                                        fontSize = 12.sp,
                                        color = DarkTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Chat Input field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = currentChatInput,
                                onValueChange = { currentChatInput = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("room_chat_input"),
                                shape = RoundedCornerShape(12.dp),
                                placeholder = { Text("Ask peers or tag @ai for formula hint...", color = DarkTextMuted, fontSize = 12.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Cyan40,
                                    unfocusedBorderColor = DarkBorder,
                                    focusedTextColor = DarkTextPrimary,
                                    unfocusedTextColor = DarkTextPrimary
                                ),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (currentChatInput.isNotBlank()) {
                                        onSendMessage(currentChatInput)
                                        currentChatInput = ""
                                    }
                                },
                                modifier = Modifier
                                    .height(50.dp)
                                    .testTag("send_chat_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Cyan40)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
