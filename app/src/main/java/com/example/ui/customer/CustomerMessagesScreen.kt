package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.HomezyRepository
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

data class ChatConversation(
  val id: String,
  val partnerName: String,
  val partnerRole: String,
  val lastMessage: String,
  val time: String,
  val avatarInitials: String
)

@Composable
fun CustomerMessagesScreen(
  modifier: Modifier = Modifier
) {
  var selectedConversationId by remember { mutableStateOf<String?>(null) }
  var inputText by remember { mutableStateOf("") }

  val conversations = listOf(
    ChatConversation(
      id = "c1",
      partnerName = "Rahul Sharma",
      partnerRole = "Electrician",
      lastMessage = "I am on my way with tools and spare parts.",
      time = "10:30 AM",
      avatarInitials = "RS"
    ),
    ChatConversation(
      id = "c2",
      partnerName = "Sunita Devi",
      partnerRole = "Master Plumber",
      lastMessage = "Leakage fixed and pipe seal replaced.",
      time = "Yesterday",
      avatarInitials = "SD"
    )
  )

  val activeChat = conversations.firstOrNull { it.id == selectedConversationId }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_messages_screen")
  ) {
    if (activeChat == null) {
      // 1. CONVERSATION LIST VIEW:
      // Show Worker/Customer name, Last message, Time.
      Surface(
        color = HomezyCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Text(
            text = "Messages",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Direct communication with assigned co-op workers",
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }
      }

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(conversations) { conv ->
          HomezyCard(
            onClick = { selectedConversationId = conv.id },
            modifier = Modifier.fillMaxWidth().testTag("chat_item_${conv.id}")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(HomezyPrimaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Text(conv.avatarInitials, fontWeight = FontWeight.Bold, color = HomezyPrimary, fontSize = 15.sp)
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = conv.partnerName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = HomezyText
                  )
                  Text(
                    text = conv.time,
                    fontSize = 11.sp,
                    color = HomezyTextSecondary
                  )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = conv.lastMessage,
                  fontSize = 13.sp,
                  color = HomezyTextSecondary,
                  maxLines = 1
                )
              }
            }
          }
        }
      }
    } else {
      // 2. CHAT DETAIL VIEW (Open chat only when selected)
      Surface(
        color = HomezyCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { selectedConversationId = null },
            modifier = Modifier.testTag("chat_back_btn")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = HomezyText)
          }
          Spacer(modifier = Modifier.width(6.dp))
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(HomezyPrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Text(activeChat.avatarInitials, fontWeight = FontWeight.Bold, color = HomezyPrimary, fontSize = 14.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = activeChat.partnerName,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = HomezyText
            )
            Text(
              text = "${activeChat.partnerRole} • Verified",
              fontSize = 11.sp,
              color = HomezySecondary
            )
          }
        }
      }

      // Messages in thread
      val threadMessages = HomezyRepository.messages
      LazyColumn(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(threadMessages) { msg ->
          val isFromCustomer = msg.isFromMe
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = if (isFromCustomer) Alignment.CenterEnd else Alignment.CenterStart
          ) {
            Surface(
              shape = RoundedCornerShape(
                topStart = 12.dp,
                topEnd = 12.dp,
                bottomStart = if (isFromCustomer) 12.dp else 2.dp,
                bottomEnd = if (isFromCustomer) 2.dp else 12.dp
              ),
              color = if (isFromCustomer) HomezyPrimary else HomezyCard,
              border = if (isFromCustomer) null else androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder)
            ) {
              Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                  text = msg.text,
                  fontSize = 13.sp,
                  color = if (isFromCustomer) Color.White else HomezyText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = msg.timestamp,
                  fontSize = 10.sp,
                  color = if (isFromCustomer) Color.White.copy(alpha = 0.7f) else HomezyTextSecondary,
                  modifier = Modifier.align(Alignment.End)
                )
              }
            }
          }
        }
      }

      // Simple Chat Input Bar
      Surface(
        color = HomezyCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Type a message...", fontSize = 13.sp) },
            singleLine = true,
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            shape = RoundedCornerShape(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          IconButton(
            onClick = {
              if (inputText.isNotBlank()) {
                HomezyRepository.addMessage(
                  ChatMessage(
                    id = "msg_${System.currentTimeMillis()}",
                    senderName = "You",
                    isFromMe = true,
                    text = inputText.trim(),
                    timestamp = "Now"
                  )
                )
                inputText = ""
              }
            },
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(HomezyPrimary)
              .testTag("chat_send_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}
