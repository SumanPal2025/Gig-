package com.example.ui.components

import androidx.compose.foundation.background
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
  HomezyCard(
    modifier = modifier.fillMaxWidth(),
    elevation = 1.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.Top
    ) {
      // Worker Avatar with verification badge
      Box(modifier = Modifier.size(52.dp)) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = worker.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = HomezyPrimary
          )
        }
        if (worker.isVerified) {
          Box(
            modifier = Modifier
              .size(18.dp)
              .clip(CircleShape)
              .background(HomezySecondary)
              .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Verified Co-op Worker",
              tint = Color.White,
              modifier = Modifier.size(11.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = worker.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = HomezyText
          )
          HomezyBadge(
            text = "★ ${worker.rating}",
            containerColor = HomezyAccentContainer,
            contentColor = Color(0xFF92400E)
          )
        }

        Text(
          text = worker.trade,
          fontSize = 13.sp,
          color = HomezyTextSecondary,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Co-op ID: ${worker.cooperativeId} • ${worker.completedJobs} jobs completed",
          fontSize = 11.sp,
          color = HomezyPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = HomezyTextSecondary,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "${worker.locationArea} (${worker.distanceKm} km)",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }

          // Availability indicator
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (worker.isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(if (worker.isAvailable) Color(0xFF16A34A) else Color(0xFFD97706))
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (worker.isAvailable) "Available Today" else "Busy on Job",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (worker.isAvailable) Color(0xFF166534) else Color(0xFF92400E)
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Skill pills
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      worker.skills.take(3).forEach { skill ->
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = HomezySurfaceVariant
        ) {
          Text(
            text = skill,
            fontSize = 11.sp,
            color = HomezyTextSecondary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
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
          text = "Estimated price",
          fontSize = 11.sp,
          color = HomezyTextSecondary
        )
        Text(
          text = "₹${worker.hourlyRate}",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
      }

      HomezyButton(
        text = "Book Worker",
        onClick = { onBookWorker(worker) },
        variant = ButtonVariant.PRIMARY,
        testTag = "book_worker_${worker.id}"
      )
    }
  }
}

@Composable
fun InstaHelpBanner(
  onGetHelpNow: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(
        Brush.horizontalGradient(
          colors = listOf(Color(0xFFDC2626), Color(0xFF991B1B))
        )
      )
      .padding(18.dp)
      .testTag("insta_help_banner_card")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        HomezyBadge(
          text = "INSTA HELP • PRIORITY DISPATCH",
          containerColor = Color(0xFFFEF08A),
          contentColor = Color(0xFF854D0E),
          icon = Icons.Default.Bolt
        )

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color.White.copy(alpha = 0.2f),
          modifier = Modifier.padding(2.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(Color(0xFF4ADE80))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Workers Ready",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Need help right now?",
        fontSize = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(3.dp))

      Text(
        text = "Find the nearest available verified worker for electrical, plumbing, or AC emergencies.",
        fontSize = 13.sp,
        color = Color.White.copy(alpha = 0.95f),
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.White.copy(alpha = 0.15f)
          ) {
            Text(
              "⚡ Electrical",
              fontSize = 11.sp,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.White.copy(alpha = 0.15f)
          ) {
            Text(
              "💧 Plumbing",
              fontSize = 11.sp,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.White.copy(alpha = 0.15f)
          ) {
            Text(
              "❄️ AC",
              fontSize = 11.sp,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Button(
          onClick = onGetHelpNow,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFFDC2626)
          ),
          contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
          modifier = Modifier.testTag("insta_help_cta")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("INSTA HELP", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
          }
        }
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
