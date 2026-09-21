package com.example.ui.worker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun WorkerWelfareScreen(
  modifier: Modifier = Modifier
) {
  var showSosDialog by remember { mutableStateOf(false) }
  var showTrainingDialog by remember { mutableStateOf(false) }
  var showInsuranceDetailsDialog by remember { mutableStateOf(false) }
  var showCertificateDialog by remember { mutableStateOf(false) }

  var isEnrolledInTraining by remember { mutableStateOf(true) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_welfare_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Worker Welfare",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Cooperative safety net, health insurance, certifications & emergency support",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // Co-op Reserve & Solidarity Status Pill Banner
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyPrimary,
        elevation = 3.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Co-op Welfare Reserve Fund",
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "₹12,48,500 Pooled",
              color = Color.White,
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Your 3% contribution guarantees 100% safety & coverage",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
          }

          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.HealthAndSafety,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }
        }
      }
    }

    // 4 MAIN CARDS REQUIRED BY SPEC:
    // 1. Insurance (Insurance: Active ✓)
    // 2. Skill Certification (Skill Certification: Electrician — Level 3)
    // 3. Training (Training: "Advanced AC Repair")
    // 4. Emergency Support

    // CARD 1: INSURANCE
    item {
      WelfareFeatureCard(
        title = "Insurance",
        statusText = "Active ✓",
        statusColor = Color(0xFF15803D),
        statusBgColor = Color(0xFFDCFCE7),
        icon = Icons.Default.MedicalServices,
        iconTint = HomezyPrimary,
        primaryInfo = "Rashtriya Co-op Cashless Cover (₹5,00,000 / yr)",
        secondaryInfo = "Covers hospitalization for Rahul + 4 family members across 850+ network hospitals. Plus ₹10L on-duty accidental protection.",
        actionButtonText = "View Policy & E-Card",
        onActionClick = { showInsuranceDetailsDialog = true }
      )
    }

    // CARD 2: CERTIFICATION
    item {
      WelfareFeatureCard(
        title = "Skill Certification",
        statusText = "Electrician — Level 3",
        statusColor = Color(0xFF1D4ED8),
        statusBgColor = Color(0xFFEFF6FF),
        icon = Icons.Default.Verified,
        iconTint = Color(0xFF2563EB),
        primaryInfo = "NSDC & Govt. ITI Master Technician",
        secondaryInfo = "Verified Level 3 Certification in HVAC Systems, Domestic Electrical Wiring, and High-Voltage Safety Standards.",
        actionButtonText = "Download Skill Certificate",
        onActionClick = { showCertificateDialog = true }
      )
    }

    // CARD 3: TRAINING
    item {
      WelfareFeatureCard(
        title = "Training",
        statusText = if (isEnrolledInTraining) "Enrolled • Batch Starts Mon" else "Available",
        statusColor = Color(0xFFB45309),
        statusBgColor = Color(0xFFFEF3C7),
        icon = Icons.Default.School,
        iconTint = Color(0xFFD97706),
        primaryInfo = "Advanced AC Repair",
        secondaryInfo = "Comprehensive module on inverter PCB troubleshooting, smart diagnostics, and high-efficiency heat pump servicing. 100% Co-op funded.",
        actionButtonText = if (isEnrolledInTraining) "View Schedule & Materials" else "Enroll for Free",
        onActionClick = { showTrainingDialog = true }
      )
    }

    // CARD 4: EMERGENCY SUPPORT
    item {
      HomezyCard(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("welfare_card_emergency"),
        elevation = 3.dp,
        borderColor = Color(0xFFFCA5A5),
        backgroundColor = Color(0xFFFFF1F2)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFFEE2E2)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Emergency,
                  contentDescription = null,
                  tint = Color(0xFFDC2626),
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Emergency Support",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFF991B1B)
                )
                Text(
                  text = "24/7 Co-op Distress & Safety Net",
                  fontSize = 11.sp,
                  color = Color(0xFFB91C1C)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFFEE2E2)
            ) {
              Text(
                text = "Instant 24/7",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFDC2626),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Accident on duty, vehicle breakdown, customer conflict, or technical hazard? The Co-op rapid response team will immediately dispatch assistance and provide legal protection.",
            fontSize = 12.sp,
            color = Color(0xFF7F1D1D),
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            HomezyButton(
              text = "Call Co-op Helpline",
              onClick = { showSosDialog = true },
              variant = ButtonVariant.OUTLINE,
              icon = Icons.Default.Phone,
              modifier = Modifier.weight(1f)
            )

            HomezyButton(
              text = "Trigger SOS Alert",
              onClick = { showSosDialog = true },
              variant = ButtonVariant.DANGER,
              icon = Icons.Default.Warning,
              modifier = Modifier.weight(1f),
              testTag = "btn_trigger_sos"
            )
          }
        }
      }
    }

    // Additional Benefits Accordion / Summary
    item {
      Text(
        text = "More Co-op Welfare Entitlements",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        BenefitRow(
          title = "Interest-Free Tool & Gear Loan",
          desc = "Eligible for up to ₹25,000 for advanced power tools at 0% interest.",
          badge = "Eligible ✓"
        )
        HorizontalDivider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        BenefitRow(
          title = "Children Education Subsidy",
          desc = "₹15,000 yearly scholarship per child for Co-op workers with >1 yr tenure.",
          badge = "Active"
        )
        HorizontalDivider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        BenefitRow(
          title = "Retirement & Gratuity Fund",
          desc = "Matching 2% monthly contribution deposited into National Pension System (NPS).",
          badge = "Accumulating"
        )
      }
    }
  }

  // DIALOGS
  if (showSosDialog) {
    AlertDialog(
      onDismissRequest = { showSosDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFDC2626))
          Spacer(modifier = Modifier.width(8.dp))
          Text("24/7 Co-op Emergency SOS")
        }
      },
      text = {
        Column {
          Text(
            text = "Your GPS location will be broadcasted to the Bengaluru Co-op Emergency Dispatch and your emergency contact (Sunita Sharma).",
            fontSize = 13.sp,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Toll-Free Dispatch: 1800-419-COOP\nField Coordinator: +91 98450 00112",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Confirm SOS Broadcast",
          onClick = { showSosDialog = false },
          variant = ButtonVariant.DANGER
        )
      },
      dismissButton = {
        HomezyButton(
          text = "Cancel",
          onClick = { showSosDialog = false },
          variant = ButtonVariant.TEXT
        )
      }
    )
  }

  if (showTrainingDialog) {
    AlertDialog(
      onDismissRequest = { showTrainingDialog = false },
      title = { Text("Training: Advanced AC Repair") },
      text = {
        Column {
          Text("Module: Inverter Split AC & PCB Diagnostics (40 Hours)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Text("• Schedule: Mon & Wed (6:30 PM - 8:30 PM)\n• Location: Co-op Skill Academy, Indiranagar\n• Certification: Govt. Skill India Gold Badge\n• Cost: ₹0 (100% Co-op Sponsored)", fontSize = 12.sp, color = HomezyText)
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Done",
          onClick = { showTrainingDialog = false }
        )
      }
    )
  }

  if (showInsuranceDetailsDialog) {
    AlertDialog(
      onDismissRequest = { showInsuranceDetailsDialog = false },
      title = { Text("Co-op Health & Accident Policy") },
      text = {
        Column {
          Text("Policy #: HMZ-HLTH-2026-0429", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          Spacer(modifier = Modifier.height(6.dp))
          Text("• Primary Insured: Rahul Sharma\n• Dependents: 4 Enrolled (Wife, 2 Children, Mother)\n• Sum Insured: ₹5,00,000 Cashless\n• Accident Shield: ₹10,00,000\n• TPA Partner: MediAssist Healthcare", fontSize = 12.sp, color = HomezyText)
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Download E-Card PDF",
          onClick = { showInsuranceDetailsDialog = false }
        )
      }
    )
  }

  if (showCertificateDialog) {
    AlertDialog(
      onDismissRequest = { showCertificateDialog = false },
      title = { Text("Electrician — Level 3 Certificate") },
      text = {
        Column {
          Text("Govt. ITI & NSDC Verified", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1D4ED8))
          Spacer(modifier = Modifier.height(6.dp))
          Text("Credential ID: NSDC-ELEC-KA-883912\nIssuing Authority: Directorate General of Training (DGT)\nValidity: Lifetime Verified\nSkills: High-voltage wiring, HVAC diagnosis, load balancing", fontSize = 12.sp, color = HomezyText)
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Close",
          onClick = { showCertificateDialog = false }
        )
      }
    )
  }
}

@Composable
private fun WelfareFeatureCard(
  title: String,
  statusText: String,
  statusColor: Color,
  statusBgColor: Color,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  iconTint: Color,
  primaryInfo: String,
  secondaryInfo: String,
  actionButtonText: String,
  onActionClick: () -> Unit
) {
  HomezyCard(
    modifier = Modifier.fillMaxWidth(),
    elevation = 2.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(iconTint.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = HomezyText
          )
          Text(
            text = primaryInfo,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary,
            maxLines = 1
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = statusBgColor
      ) {
        Text(
          text = statusText,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = statusColor,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = secondaryInfo,
      fontSize = 12.sp,
      color = HomezyTextSecondary,
      lineHeight = 16.sp
    )

    Spacer(modifier = Modifier.height(10.dp))

    HomezyButton(
      text = actionButtonText,
      onClick = onActionClick,
      variant = ButtonVariant.OUTLINE,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
private fun BenefitRow(
  title: String,
  desc: String,
  badge: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
      Text(text = desc, fontSize = 11.sp, color = HomezyTextSecondary, lineHeight = 15.sp)
    }
    Spacer(modifier = Modifier.width(10.dp))
    HomezyBadge(
      text = badge,
      containerColor = HomezyPrimaryContainer,
      contentColor = HomezyPrimary
    )
  }
}
