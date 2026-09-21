package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.ui.components.HomezyBadge
import com.example.ui.theme.*

@Composable
fun CustomerMessagesScreen(
  modifier: Modifier = Modifier
) {
  val messages = HomezyRepository.messages
  var inputText by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_messages_screen")
  ) {
    // Header
    Surface(
      color = HomezyCard,
      border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Text("RS", fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Rahul Sharma",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyText
          )
          Text(
            text = "Electrician & AC Tech • Online",
            fontSize = 12.sp,
            color = HomezySecondary
          )
        }
        HomezyBadge(
          text = "Co-op Verified",
          icon = Icons.Default.Shield
        )
      }
    }

    // Message List
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = HomezySurfaceVariant
          ) {
            Text(
              text = "Direct end-to-end communication with your cooperative worker.",
              fontSize = 11.sp,
              color = HomezyTextSecondary,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }
        }
      }

      items(messages) { msg ->
        val isMe = msg.isFromMe
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
        ) {
          Surface(
            shape = RoundedCornerShape(
              topStart = 16.dp,
              topEnd = 16.dp,
              bottomStart = if (isMe) 16.dp else 2.dp,
              bottomEnd = if (isMe) 2.dp else 16.dp
            ),
            color = if (isMe) HomezyPrimary else HomezyCard,
            contentColor = if (isMe) Color.White else HomezyText,
            shadowElevation = 0.5.dp,
            modifier = Modifier.widthIn(max = 280.dp)
          ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
              Text(
                text = msg.text,
                fontSize = 14.sp,
                lineHeight = 19.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = msg.timestamp,
                fontSize = 10.sp,
                color = if (isMe) Color.White.copy(alpha = 0.7f) else HomezyTextSecondary,
                modifier = Modifier.align(Alignment.End)
              )
            }
          }
        }
      }
    }

    // Quick suggestions
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      listOf("I am at home", "Call when you reach", "Doorbell works").forEach { suggestion ->
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = HomezyCard,
          border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
          modifier = Modifier.clip(RoundedCornerShape(14.dp))
        ) {
          Text(
            text = suggestion,
            fontSize = 11.sp,
            color = HomezyPrimary,
            modifier = Modifier
              .clickable {
                HomezyRepository.addMessage(
                  ChatMessage(
                    id = "msg_${System.currentTimeMillis()}",
                    senderName = "You",
                    isFromMe = true,
                    text = suggestion,
                    timestamp = "Just now"
                  )
                )
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }
      }
    }

    // Chat Input Bar
    Surface(
      color = HomezyCard,
      border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = { Text("Type a message to Rahul...", fontSize = 14.sp, color = HomezyTextSecondary) },
          modifier = Modifier
            .weight(1f)
            .testTag("chat_input"),
          shape = RoundedCornerShape(24.dp),
          singleLine = true,
          textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 14.sp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = HomezyPrimary,
            unfocusedBorderColor = HomezyBorder,
            focusedContainerColor = HomezySurfaceVariant,
            unfocusedContainerColor = HomezySurfaceVariant,
            cursorColor = Color.Black
          )
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
                  timestamp = "Just now"
                )
              )
              inputText = ""
            }
          },
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(HomezyPrimary)
            .testTag("chat_send_button")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
