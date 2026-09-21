package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun AdminSettingsScreen(
  onSwitchToCustomer: () -> Unit,
  onSwitchToWorker: () -> Unit,
  onLogout: () -> Unit = {},
  currentUser: com.example.data.AuthUser? = null,
  modifier: Modifier = Modifier
) {
  var platformFeeCap by remember { mutableStateOf("5.0%") }
  var welfarePoolShare by remember { mutableStateOf("50%") }
  var instaHelpRadius by remember { mutableStateOf("5.0 km") }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_settings_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Column {
        Text(
          text = "Cooperative Federation Settings",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Configure democratic bylaws, economic caps, and dispatch limits",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Economic Policy & Fee Caps", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = HomezyText)
        Spacer(modifier = Modifier.height(12.dp))

        SettingRow(
          title = "Platform Fee Cap",
          description = "Fixed by Cooperative Bylaw Article 4. Cannot exceed 5% without 2/3 member vote.",
          value = platformFeeCap
        )

        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 12.dp))

        SettingRow(
          title = "Welfare Reserve Allocation",
          description = "Share of platform surplus directed immediately into worker health and emergency loans.",
          value = welfarePoolShare
        )

        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 12.dp))

        SettingRow(
          title = "Insta Help Emergency Radius",
          description = "Maximum dispatch radius for high-priority emergency household repairs.",
          value = instaHelpRadius
        )
      }
    }

    // Session & Testing Actions
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        HomezyButton(
          text = "Logout of Federation Admin",
          onClick = onLogout,
          variant = ButtonVariant.DANGER,
          icon = Icons.Default.Logout,
          modifier = Modifier.fillMaxWidth().testTag("admin_logout_btn")
        )
      }
    }
  }
}

@Composable
private fun SettingRow(title: String, description: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = HomezyText)
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = description, fontSize = 12.sp, color = HomezyTextSecondary, lineHeight = 16.sp)
    }

    Surface(
      shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
      color = HomezySurfaceVariant
    ) {
      Text(
        text = value,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = HomezyPrimary,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
      )
    }
  }
}
