package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerProfileScreen(
  onSwitchToWorker: () -> Unit,
  onSwitchToAdmin: () -> Unit,
  onLogout: () -> Unit = {},
  currentUser: com.example.data.AuthUser? = null,
  modifier: Modifier = Modifier
) {
  var selectedLanguage by remember { mutableStateOf("English (EN)") }
  var showLanguageDialog by remember { mutableStateOf(false) }

  val languages = listOf("English (EN)", "हिंदी (Hindi)", "ಕನ್ನಡ (Kannada)", "தமிழ் (Tamil)", "తెలుగు (Telugu)")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Profile Header
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(60.dp)
              .clip(CircleShape)
              .background(HomezyPrimary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = currentUser?.avatarInitials ?: "PS",
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
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
            Spacer(modifier = Modifier.height(2.dp))
            HomezyBadge(
              text = "Patron Member • Tier Silver",
              containerColor = HomezyPrimaryContainer,
              contentColor = HomezyPrimary
            )
          }
        }
      }
    }

    // Cooperative Patronage Reward Box
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezySurfaceVariant,
        borderColor = HomezyPrimary.copy(alpha = 0.3f)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Co-op Patronage Dividend",
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              color = HomezyTextSecondary
            )
            Text(
              text = "₹240.00",
              fontSize = 22.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = HomezyAccent
          ) {
            Text(
              text = "Redeemable on next booking",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "As a cooperative patron, platform surplus is distributed back to household customers and service workers rather than venture capitalists.",
          fontSize = 11.sp,
          color = HomezyTextSecondary,
          lineHeight = 16.sp
        )
      }
    }

    // Saved Addresses
    item {
      Text(
        text = "Saved Addresses",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(HomezyPrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Icon(imageVector = Icons.Default.Home, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(18.dp))
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text("Home", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("Flat 402, Green Glen Layout, Bellandur, Bengaluru", fontSize = 12.sp, color = HomezyTextSecondary)
          }
          Icon(imageVector = Icons.Default.Check, contentDescription = "Default", tint = HomezySecondary)
        }
      }
    }

    // Language & Localization (For rural and semi-urban users)
    item {
      Text(
        text = "Preferences & Accessibility",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = { showLanguageDialog = true }
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = HomezyPrimary)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("Regional Language", fontWeight = FontWeight.Bold, fontSize = 14.sp)
              Text(selectedLanguage, fontSize = 12.sp, color = HomezyTextSecondary)
            }
          }
          Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = HomezyTextSecondary)
        }
      }
    }

    // Session & Role Actions
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        HomezyButton(
          text = "Logout of Account",
          onClick = onLogout,
          variant = ButtonVariant.DANGER,
          icon = Icons.Default.Logout,
          modifier = Modifier.fillMaxWidth().testTag("customer_logout_btn")
        )
      }
    }
  }

  // Language Picker Dialog
  if (showLanguageDialog) {
    HomezyModal(
      visible = true,
      onDismissRequest = { showLanguageDialog = false },
      title = "Choose Language"
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        languages.forEach { lang ->
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (selectedLanguage == lang) HomezyPrimaryContainer else HomezyCard,
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = lang,
                fontSize = 14.sp,
                fontWeight = if (selectedLanguage == lang) FontWeight.Bold else FontWeight.Normal,
                color = if (selectedLanguage == lang) HomezyPrimary else HomezyText
              )
              RadioButton(
                selected = selectedLanguage == lang,
                onClick = {
                  selectedLanguage = lang
                  showLanguageDialog = false
                },
                colors = RadioButtonDefaults.colors(selectedColor = HomezyPrimary)
              )
            }
          }
        }
      }
    }
  }
}
