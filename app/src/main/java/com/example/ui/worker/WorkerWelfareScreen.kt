package com.example.ui.worker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun WorkerWelfareScreen(
  modifier: Modifier = Modifier
) {
  var expandedCard by remember { mutableStateOf<String?>(null) }
  var showSkillPassport by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_welfare_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Header
    item {
      Text(
        text = "Welfare & Safety",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Cooperative coverage & verified credentials",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // 1. Insurance Status Card (Tap for details)
    item {
      val isExpanded = expandedCard == "insurance"
      HomezyCard(
        onClick = { expandedCard = if (isExpanded) null else "insurance" },
        modifier = Modifier.fillMaxWidth().testTag("welfare_insurance_card")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(HomezyPrimaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("Insurance", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Text("Tap for policy details", fontSize = 11.sp, color = HomezyTextSecondary)
            }
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFDCFCE7)
          ) {
            Text(
              text = "Active ✓",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        AnimatedVisibility(visible = isExpanded) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("• Policy: National Co-op Health & Accident Shield", fontSize = 12.sp, color = HomezyText)
            Text("• Sum Insured: ₹5,00,000 Medical + ₹10,00,000 Accidental", fontSize = 12.sp, color = HomezyText)
            Text("• Premium: 100% funded by Cooperative Solidarity Pool", fontSize = 12.sp, color = HomezySecondary)
            Text("• Renewal: Active till 31 Dec 2027", fontSize = 12.sp, color = HomezyText)
          }
        }
      }
    }

    // 2. Certification Status Card (Tap for details)
    item {
      val isExpanded = expandedCard == "certification"
      HomezyCard(
        onClick = { expandedCard = if (isExpanded) null else "certification" },
        modifier = Modifier.fillMaxWidth().testTag("welfare_certification_card")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(HomezyPrimaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("Certification", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Text("ITI & Skill Council accredited", fontSize = 11.sp, color = HomezyTextSecondary)
            }
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFFDCFCE7)
          ) {
            Text(
              text = "Verified ✓",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF15803D),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        AnimatedVisibility(visible = isExpanded) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("• Certificate: ITI Electrician & Wireman Grade A", fontSize = 12.sp, color = HomezyText)
            Text("• Issuing Body: National Council for Vocational Training", fontSize = 12.sp, color = HomezyText)
            Text("• Verification: Verified by Bengaluru Federation Admin", fontSize = 12.sp, color = HomezySecondary)
          }
        }
      }
    }

    // 3. Training Status Card (Tap for details)
    item {
      val isExpanded = expandedCard == "training"
      HomezyCard(
        onClick = { expandedCard = if (isExpanded) null else "training" },
        modifier = Modifier.fillMaxWidth().testTag("welfare_training_card")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(HomezyAccentContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text("Training", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Text("Co-op sponsored skill upgrade", fontSize = 11.sp, color = HomezyTextSecondary)
            }
          }

          Surface(
            shape = RoundedCornerShape(6.dp),
            color = HomezyAccentContainer
          ) {
            Text(
              text = "1 recommendation",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF92400E),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        AnimatedVisibility(visible = isExpanded) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 10.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text("• Recommended Module: Inverter & Solar Hybrid Systems", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HomezyText)
            Text("• Schedule: Weekend batch at Indiranagar Co-op Training Hub", fontSize = 12.sp, color = HomezyText)
            Text("• Cost: 100% covered by Federation Upskilling Fund", fontSize = 12.sp, color = HomezySecondary)
          }
        }
      }
    }

    // 4. DIGITAL SKILL PASSPORT: Behind "View Skill Passport"
    item {
      Spacer(modifier = Modifier.height(6.dp))
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("skill_passport_container")) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showSkillPassport = !showSkillPassport }
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Badge, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Digital Skill Passport",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }
          Text(
            text = if (showSkillPassport) "▲ Hide" else "View Skill Passport ▼",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }

        AnimatedVisibility(visible = showSkillPassport) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 12.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text("• Worker: Rahul Sharma (Member #0429)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
            Text("• Verification: State Federation Certified ✓", fontSize = 12.sp, color = HomezySecondary)
            Text("• Skills: AC Deep Clean, High-Voltage Wiring, Inverter Diagnostics", fontSize = 12.sp, color = HomezyText)
            Text("• Certifications: ITI Wireman, Skill India NSQF Level 4", fontSize = 12.sp, color = HomezyText)
            Text("• Experience: 8 years verified trade experience", fontSize = 12.sp, color = HomezyText)
            Text("• Rating: ★ 4.92 (642 completed cooperative jobs)", fontSize = 12.sp, color = HomezyText)
          }
        }
      }
    }
  }
}
