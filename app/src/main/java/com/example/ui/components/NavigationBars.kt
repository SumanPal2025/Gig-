package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRole
import com.example.ui.theme.*

// Customer destinations
enum class CustomerNavDestination(val route: String, val title: String, val icon: ImageVector) {
  HOME("/customer/home", "Home", Icons.Default.Home),
  SERVICES("/customer/services", "Services", Icons.Default.Handyman),
  BOOKINGS("/customer/bookings", "Bookings", Icons.Default.CalendarToday),
  INSTA_HELP("/customer/insta-help", "Insta Help", Icons.Default.Bolt),
  MESSAGES("/customer/messages", "Messages", Icons.Default.ChatBubbleOutline),
  PROFILE("/customer/profile", "Profile", Icons.Default.PersonOutline)
}

// Worker destinations
enum class WorkerNavDestination(val route: String, val title: String, val icon: ImageVector) {
  DASHBOARD("/worker/dashboard", "Dashboard", Icons.Default.Dashboard),
  JOBS("/worker/jobs", "Jobs", Icons.Default.WorkOutline),
  EARNINGS("/worker/earnings", "Earnings", Icons.Default.AccountBalanceWallet),
  WELFARE("/worker/welfare", "Welfare", Icons.Default.HealthAndSafety),
  PROFILE("/worker/profile", "Profile", Icons.Default.PersonOutline),
  FAIR_ALLOCATION("/worker/fair-allocation", "Fair Work", Icons.Default.Balance)
}

val primaryWorkerDestinations = listOf(
  WorkerNavDestination.DASHBOARD,
  WorkerNavDestination.JOBS,
  WorkerNavDestination.EARNINGS,
  WorkerNavDestination.WELFARE,
  WorkerNavDestination.PROFILE
)

// Admin destinations
enum class AdminNavDestination(val route: String, val title: String, val icon: ImageVector) {
  DASHBOARD("/admin/dashboard", "Overview", Icons.Default.Insights),
  WORKERS("/admin/workers", "Workers", Icons.Default.People),
  BOOKINGS("/admin/bookings", "Bookings", Icons.Default.Assignment),
  FAIR_ALLOCATION("/admin/fair-allocation", "Fair Allocation", Icons.Default.Balance),
  WELFARE("/admin/welfare", "Welfare", Icons.Default.Security),
  AI_INSIGHTS("/admin/ai-insights", "AI Forecast", Icons.Default.TrendingUp),
  PAYMENTS("/admin/payments", "Payments", Icons.Default.Payments),
  REPORTS("/admin/reports", "Reports", Icons.Default.BarChart),
  SETTINGS("/admin/settings", "Settings", Icons.Default.Settings)
}

@Composable
fun CustomerBottomBar(
  currentRoute: String,
  onNavigate: (CustomerNavDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = HomezyCard,
    contentColor = HomezyText,
    tonalElevation = 6.dp,
    modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    CustomerNavDestination.values().forEach { destination ->
      val selected = currentRoute == destination.route
      val isInstaHelp = destination == CustomerNavDestination.INSTA_HELP

      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(destination) },
        icon = {
          Box(
            modifier = if (isInstaHelp) {
              Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (selected) HomezyAccent else HomezyPrimaryContainer)
            } else {
              Modifier
            },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = destination.icon,
              contentDescription = destination.title,
              tint = if (isInstaHelp) {
                if (selected) HomezyText else HomezyPrimary
              } else if (selected) {
                HomezyPrimary
              } else {
                HomezyTextSecondary
              },
              modifier = Modifier.size(20.dp)
            )
          }
        },
        label = {
          Text(
            text = destination.title,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) HomezyPrimary else HomezyTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = HomezyPrimary,
          selectedTextColor = HomezyPrimary,
          indicatorColor = HomezyPrimaryContainer,
          unselectedIconColor = HomezyTextSecondary,
          unselectedTextColor = HomezyTextSecondary
        ),
        modifier = Modifier.testTag("nav_${destination.route.replace("/", "_")}")
      )
    }
  }
}

@Composable
fun WorkerBottomBar(
  currentRoute: String,
  onNavigate: (WorkerNavDestination) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    containerColor = HomezyCard,
    contentColor = HomezyText,
    tonalElevation = 6.dp,
    modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars)
  ) {
    primaryWorkerDestinations.forEach { destination ->
      val selected = currentRoute == destination.route
      NavigationBarItem(
        selected = selected,
        onClick = { onNavigate(destination) },
        icon = {
          Icon(
            imageVector = destination.icon,
            contentDescription = destination.title,
            modifier = Modifier.size(22.dp)
          )
        },
        label = {
          Text(
            text = destination.title,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = HomezyPrimary,
          selectedTextColor = HomezyPrimary,
          indicatorColor = HomezyPrimaryContainer,
          unselectedIconColor = HomezyTextSecondary,
          unselectedTextColor = HomezyTextSecondary
        ),
        modifier = Modifier.testTag("worker_nav_${destination.route.replace("/", "_")}")
      )
    }
  }
}

@Composable
fun HomezyAuthenticatedTopBar(
  currentUser: com.example.data.AuthUser,
  onLogout: () -> Unit,
  onAttemptRoleSwitch: (AppRole) -> Unit = {},
  modifier: Modifier = Modifier
) {
  Surface(
    color = HomezyPrimary,
    contentColor = Color.White,
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f, fill = false)
      ) {
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(HomezyAccent),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Handshake,
            contentDescription = null,
            tint = HomezyText,
            modifier = Modifier.size(16.dp)
          )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column {
          Text(
            text = "HOMEZY",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            letterSpacing = 0.5.sp,
            color = Color.White
          )
          Text(
            text = "${currentUser.name} • ${currentUser.role.label}",
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.85f),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Role switch tabs for quick testing and RBAC validation
        AppRole.values().forEach { role ->
          val isCurrentRole = role == currentUser.role
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = if (isCurrentRole) Color.White.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.12f),
            onClick = { onAttemptRoleSwitch(role) },
            modifier = Modifier.testTag("role_tab_${role.name.lowercase()}")
          ) {
            Text(
              text = role.label,
              fontSize = 9.sp,
              fontWeight = if (isCurrentRole) FontWeight.Bold else FontWeight.Normal,
              color = Color.White,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)
            )
          }
        }

        // Logout action button
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.White.copy(alpha = 0.18f),
          onClick = onLogout,
          modifier = Modifier.testTag("app_logout_btn")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Logout,
              contentDescription = "Logout",
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = "Logout",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun RoleSwitcherBar(
  currentRole: AppRole,
  onRoleSelected: (AppRole) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = HomezyPrimary,
    contentColor = Color.White,
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(HomezyAccent),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Handshake,
              contentDescription = null,
              tint = HomezyText,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "HOMEZY",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            letterSpacing = 1.sp,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // 3-Role Pills
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(Color.Black.copy(alpha = 0.2f))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        AppRole.values().forEach { role ->
          val isSelected = role == currentRole
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(6.dp))
              .background(if (isSelected) HomezyCard else Color.Transparent)
              .clickable { onRoleSelected(role) }
              .padding(vertical = 5.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = role.label,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) HomezyPrimary else Color.White.copy(alpha = 0.85f),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

@Composable
fun HomezyTopBar(
  locationText: String = "Indiranagar, Bengaluru",
  onLocationClick: () -> Unit = {},
  onNotificationClick: () -> Unit = {},
  notificationCount: Int = 2,
  modifier: Modifier = Modifier
) {
  Surface(
    color = HomezyCard,
    border = androidx.compose.foundation.BorderStroke(0.5.dp, HomezyBorder),
    modifier = modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Location selector
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable(onClick = onLocationClick)
          .padding(vertical = 4.dp, horizontal = 2.dp)
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = HomezyPrimary,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Current Location",
            fontSize = 10.sp,
            color = HomezyTextSecondary,
            fontWeight = FontWeight.Medium
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = locationText,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Icon(
              imageVector = Icons.Default.KeyboardArrowDown,
              contentDescription = null,
              tint = HomezyTextSecondary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Notifications
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(HomezySurfaceVariant)
          .testTag("notifications_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = HomezyText,
            modifier = Modifier.size(20.dp)
          )
          if (notificationCount > 0) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(HomezyAccent)
                .align(Alignment.TopEnd)
            )
          }
        }
      }
    }
  }
}
