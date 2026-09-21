package com.example.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdminNavDestination
import com.example.ui.theme.*

@Composable
fun AdminNavigationBar(
  currentRoute: String,
  onNavigate: (AdminNavDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Surface(
    color = HomezyCard,
    border = androidx.compose.foundation.BorderStroke(1.dp, HomezyBorder),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(scrollState)
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AdminNavDestination.values().forEach { destination ->
        val isSelected = currentRoute == destination.route
        FilterChip(
          selected = isSelected,
          onClick = { onNavigate(destination) },
          leadingIcon = {
            Icon(
              imageVector = destination.icon,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = if (isSelected) Color.White else HomezyPrimary
            )
          },
          label = {
            Text(
              text = destination.title,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = HomezyPrimary,
            selectedLabelColor = Color.White,
            containerColor = HomezySurfaceVariant,
            labelColor = HomezyText
          ),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.testTag("admin_tab_${destination.title.lowercase().replace(" ", "_")}")
        )
      }
    }
  }
}
