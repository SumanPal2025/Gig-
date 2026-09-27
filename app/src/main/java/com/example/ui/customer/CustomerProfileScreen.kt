package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthUser
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun CustomerProfileScreen(
  onSwitchToWorker: () -> Unit = {},
  onSwitchToAdmin: () -> Unit = {},
  onLogout: () -> Unit = {},
  currentUser: AuthUser? = null,
  modifier: Modifier = Modifier
) {
  var selectedLanguage by remember { mutableStateOf("English") }
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showInfoDialog by remember { mutableStateOf<String?>(null) }

  val languages = listOf("English", "हिंदी (Hindi)", "ಕನ್ನಡ (Kannada)", "தமிழ் (Tamil)")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Profile Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(HomezyPrimary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = currentUser?.avatarInitials ?: "PS",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
          Text(
            text = currentUser?.name ?: "Priya Sundaram",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = HomezyText
          )
          Text(
            text = currentUser?.email ?: "customer@homezy.demo",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }
    }

    // Grouped Simple List Rows:
    // Personal Information, Saved Address, Language, Payments, Bookings, Help
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Column {
          ProfileListRow(
            title = "Personal Information",
            icon = Icons.Default.PersonOutline,
            onClick = { showInfoDialog = "Name: ${currentUser?.name ?: "Priya Sundaram"}\nPhone: ${currentUser?.phone ?: "+91 98451 90812"}\nEmail: ${currentUser?.email ?: "customer@homezy.demo"}" }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          ProfileListRow(
            title = "Saved Address",
            icon = Icons.Default.LocationOn,
            onClick = { showInfoDialog = "Saved Address:\nFlat 402, Green Glen Layout, Bellandur, Outer Ring Road, Bengaluru - 560103" }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          ProfileListRow(
            title = "Language",
            subtitle = selectedLanguage,
            icon = Icons.Default.Language,
            onClick = { showLanguageDialog = true }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          ProfileListRow(
            title = "Payments",
            subtitle = "UPI & Card details",
            icon = Icons.Default.CreditCard,
            onClick = { showInfoDialog = "Payment Methods:\n• UPI: customer@okaxis (Primary)\n• Card: Visa ending in 8921" }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          ProfileListRow(
            title = "Bookings",
            subtitle = "History & Receipts",
            icon = Icons.Default.CalendarToday,
            onClick = { showInfoDialog = "All previous cooperative booking invoices are preserved and accessible from the Bookings tab." }
          )
          HorizontalDivider(color = HomezyBorder.copy(alpha = 0.6f))

          ProfileListRow(
            title = "Help & Support",
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            onClick = { showInfoDialog = "Cooperative Helpdesk:\nHelpline: 1800-425-HOMEZY\nEmail: support@homezy.coop" }
          )
        }
      }
    }

    // Logout
    item {
      Spacer(modifier = Modifier.height(6.dp))
      Button(
        onClick = onLogout,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFFEE2E2),
          contentColor = Color(0xFFDC2626)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("customer_logout_btn")
      ) {
        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("Logout", fontSize = 14.sp, fontWeight = FontWeight.Bold)
      }
    }
  }

  // Info Dialog
  if (showInfoDialog != null) {
    AlertDialog(
      onDismissRequest = { showInfoDialog = null },
      title = { Text("Details", fontWeight = FontWeight.Bold, color = HomezyText) },
      text = { Text(showInfoDialog ?: "", fontSize = 14.sp, color = HomezyText) },
      confirmButton = {
        TextButton(onClick = { showInfoDialog = null }) {
          Text("Close", fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }
      }
    )
  }

  // Language Dialog
  if (showLanguageDialog) {
    AlertDialog(
      onDismissRequest = { showLanguageDialog = false },
      title = { Text("Select Language", fontWeight = FontWeight.Bold, color = HomezyText) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          languages.forEach { lang ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  selectedLanguage = lang
                  showLanguageDialog = false
                }
                .padding(vertical = 8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = selectedLanguage == lang,
                onClick = {
                  selectedLanguage = lang
                  showLanguageDialog = false
                },
                colors = RadioButtonDefaults.colors(selectedColor = HomezyPrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(lang, fontSize = 14.sp, color = HomezyText)
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showLanguageDialog = false }) {
          Text("Cancel", color = HomezyTextSecondary)
        }
      }
    )
  }
}

@Composable
fun ProfileListRow(
  title: String,
  subtitle: String? = null,
  icon: ImageVector,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 12.dp, horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
      Icon(icon, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.width(12.dp))
      Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = HomezyText)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
      if (subtitle != null) {
        Text(subtitle, fontSize = 12.sp, color = HomezyTextSecondary)
        Spacer(modifier = Modifier.width(6.dp))
      }
      Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = HomezyTextSecondary, modifier = Modifier.size(14.dp))
    }
  }
}
