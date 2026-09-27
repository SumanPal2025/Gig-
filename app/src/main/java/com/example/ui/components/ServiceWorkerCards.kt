package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ServiceItem
import com.example.data.WorkerProfile
import com.example.ui.theme.*

@Composable
fun ServiceCard(
  service: ServiceItem,
  onViewService: (ServiceItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val serviceIcon: ImageVector = when (service.id) {
    "srv_ac" -> Icons.Default.AcUnit
    "srv_electrician" -> Icons.Default.ElectricBolt
    "srv_plumber" -> Icons.Default.Plumbing
    else -> Icons.Default.Handyman
  }

  HomezyCard(
    modifier = modifier.fillMaxWidth(),
    onClick = { onViewService(service) }
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = serviceIcon,
            contentDescription = service.name,
            tint = HomezyPrimary,
            modifier = Modifier.size(26.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = service.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = service.category,
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }
      }

      // Rating badge
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezyAccentContainer
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${service.rating}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Text(
      text = service.shortDescription,
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(12.dp))
    // Cooperative guarantee line
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(HomezySurfaceVariant)
        .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Shield,
        contentDescription = null,
        tint = HomezyPrimary,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = service.cooperativeGuarantee,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = HomezyPrimary
      )
    }

    Spacer(modifier = Modifier.height(12.dp))
    Divider(color = HomezyBorder, thickness = 0.8.dp)
    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Estimated Price",
          fontSize = 11.sp,
          color = HomezyTextSecondary
        )
        Text(
          text = service.priceRange,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary
        )
      }

      HomezyButton(
        text = "View Service",
        onClick = { onViewService(service) },
        variant = ButtonVariant.PRIMARY,
        testTag = "view_service_${service.id}"
      )
    }
  }
}

@Composable
fun WorkerCard(
  worker: WorkerProfile,
  onBookWorker: (WorkerProfile) -> Unit,
  modifier: Modifier = Modifier
) {
  var showProfileDetails by remember { mutableStateOf(false) }

  HomezyCard(
    modifier = modifier.fillMaxWidth(),
    elevation = 1.dp
  ) {
    // Top Row: Worker name, verified badge, skill, rating
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = worker.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = HomezyPrimary
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = worker.name,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = HomezyText
            )
            if (worker.isVerified) {
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified",
                tint = HomezySecondary,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(2.dp))
              Text(
                text = "Verified",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = HomezySecondary
              )
            }
          }
          Text(
            text = worker.trade,
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }

      // Rating badge
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = HomezyAccentContainer
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFD97706),
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${worker.rating}",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Second Row: Distance and Availability
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = HomezyTextSecondary,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "${worker.distanceKm} km away",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }

      Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (worker.isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
      ) {
        Text(
          text = if (worker.isAvailable) "Available Now" else "Busy",
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          color = if (worker.isAvailable) Color(0xFF166534) else Color(0xFF92400E),
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }

    // Progressive Disclosure: Details ONLY when user asks
    AnimatedVisibility(visible = showProfileDetails) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(HomezySurfaceVariant)
          .padding(10.dp)
      ) {
        Text(
          text = "Worker Profile & Verification",
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = HomezyPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("• Experience: ${worker.experienceYears} years", fontSize = 11.sp, color = HomezyText)
        Text("• Skills: ${worker.skills.joinToString(", ")}", fontSize = 11.sp, color = HomezyText)
        Text("• Jobs completed: ${worker.completedJobs}", fontSize = 11.sp, color = HomezyText)
        Text("• Detailed rating: ★ ${worker.rating} (${(worker.completedJobs * 0.9).toInt()} reviews)", fontSize = 11.sp, color = HomezyText)
        Text("• Service Area: ${worker.locationArea}", fontSize = 11.sp, color = HomezyText)
        Text("• Availability: ${if (worker.isAvailable) "Immediate dispatch" else "Next slot tomorrow"}", fontSize = 11.sp, color = HomezyText)
        Text("• Cooperative ID: ${worker.cooperativeId}", fontSize = 11.sp, color = HomezyText)
        Text("• Welfare: Enrolled in Co-op Health & Accident Pool", fontSize = 11.sp, color = HomezySecondary)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Action Row: [ View Profile ] and [ Book Now ]
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedButton(
        onClick = { showProfileDetails = !showProfileDetails },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .weight(1f)
          .testTag("view_profile_${worker.id}"),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = HomezyPrimary)
      ) {
        Text(
          text = if (showProfileDetails) "Hide Profile" else "View Profile",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Button(
        onClick = { onBookWorker(worker) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
        modifier = Modifier
          .weight(1f)
          .testTag("book_worker_${worker.id}")
      ) {
        Text(
          text = "Book Now",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}

@Composable
fun InstaHelpBanner(
  onGetHelpNow: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
    modifier = modifier
      .fillMaxWidth()
      .testTag("insta_help_banner_card")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFFF59E0B)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "Need help right now?",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF92400E)
          )
          Text(
            text = "Find a nearby verified worker.",
            fontSize = 12.sp,
            color = Color(0xFFB45309)
          )
        }
      }

      Button(
        onClick = onGetHelpNow,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
        modifier = Modifier.testTag("insta_help_cta")
      ) {
        Text(
          text = "Get Help",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}

@Composable
fun WhyHomezyCards(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Mission Statement Header Card
    Card(
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = HomezyPrimary.copy(alpha = 0.06f)),
      border = androidx.compose.foundation.BorderStroke(1.dp, HomezyPrimary.copy(alpha = 0.2f)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "The HOMEZY Vision",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "\"HOMEZY connects customers with verified cooperative workers while using AI to create smarter, fairer and more sustainable local service delivery.\"",
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = HomezyText,
          lineHeight = 19.sp
        )
      }
    }

    Text(
      text = "Core Differentiators",
      fontSize = 17.sp,
      fontWeight = FontWeight.Bold,
      color = HomezyText,
      modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )

    WhyHomezyItem(
      icon = Icons.Default.Groups,
      iconColor = HomezyPrimary,
      title = "1. Cooperative Ownership & Democratic Governance",
      description = "Owned by service professionals. Democratic voting, zero middleman exploitation, and member patronage dividends."
    )

    WhyHomezyItem(
      icon = Icons.Default.Balance,
      iconColor = Color(0xFFD97706),
      title = "2. Fair AI Work Allocation",
      description = "Entropy & Gini-optimized dispatch ensures work is shared equitably rather than monopolized by algorithms."
    )

    WhyHomezyItem(
      icon = Icons.Default.CurrencyRupee,
      iconColor = Color(0xFF16A34A),
      title = "3. Transparent Worker Earnings",
      description = "Workers take home 95% of job fees. No hidden cuts, arbitrary deductions, or predatory surge markups."
    )

    WhyHomezyItem(
      icon = Icons.Default.Handshake,
      iconColor = Color(0xFF2563EB),
      title = "4. AI Fair Price Assistance",
      description = "Explainable cost breakdown engine based on equipment, skill, and time with democratic counter-offer negotiations."
    )

    WhyHomezyItem(
      icon = Icons.Default.HealthAndSafety,
      iconColor = Color(0xFFDC2626),
      title = "5. Worker Welfare & Social Shield",
      description = "₹5L cashless medical insurance, on-duty accident protection, and NSDC skill certification."
    )

    WhyHomezyItem(
      icon = Icons.Default.LocationOn,
      iconColor = Color(0xFF9333EA),
      title = "6. Hyperlocal Matching",
      description = "Precision neighborhood clustering connects residents with local verified guild members within 1–3 km."
    )

    WhyHomezyItem(
      icon = Icons.Default.TrendingUp,
      iconColor = Color(0xFF0284C7),
      title = "7. AI Demand Forecasting",
      description = "Predictive weather, holiday, and heatwave modeling to proactively alert cooperatives to surge preparation."
    )

    WhyHomezyItem(
      icon = Icons.Default.Bolt,
      iconColor = Color(0xFFEA580C),
      title = "8. Insta Help Emergency Services",
      description = "One-tap priority emergency dispatch for immediate power trips, pipe bursts, and critical breakdowns."
    )
  }
}

@Composable
private fun WhyHomezyItem(
  icon: ImageVector,
  iconColor: Color,
  title: String,
  description: String
) {
  HomezyCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = 0.5.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(10.dp))
          .background(iconColor.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(22.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = description,
          fontSize = 13.sp,
          color = HomezyTextSecondary,
          lineHeight = 18.sp
        )
      }
    }
  }
}
