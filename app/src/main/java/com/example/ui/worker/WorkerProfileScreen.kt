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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthUser
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun WorkerProfileScreen(
  currentUser: AuthUser,
  onLogout: () -> Unit = {},
  onSwitchToCustomer: () -> Unit = {},
  onSwitchToAdmin: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var enrolledCourses by remember { mutableStateOf(setOf("Advanced AC Repair")) }
  var showQrDialog by remember { mutableStateOf(false) }
  var showEnrollSuccessDialog by remember { mutableStateOf<String?>(null) }

  val passportSkills = listOf(
    "AC Deep Jet Cleaning",
    "Residential Wiring",
    "Switchboard & MCB",
    "Inverter Setup",
    "Pipe Leakage & Fixtures",
    "PCB Diagnostics"
  )

  val passportCertifications = listOf(
    "Govt. ITI Diploma in Electrical (DGT)",
    "Level 3 Master HVAC Specialist (NSDC)",
    "High-Voltage Safety & Earthing Standard",
    "Eco-Refrigerant R32 Certified (BEE)"
  )

  val recommendedSkills = listOf(
    "EV Charging Station Installation" to "+₹350/job higher fare",
    "Smart Home Automation (Zigbee/Matter)" to "Surging in Koramangala",
    "Solar Rooftop Inverter Grid Sync" to "Govt. Subsidized module"
  )

  val recommendedTrainings = listOf(
    TrainingItem(
      id = "tr_1",
      title = "EV Charging Station Setup",
      duration = "24 Hours • 4 Practical Labs",
      instructor = "National Skill Dev. Council (NSDC)",
      subsidy = "100% Co-op Sponsored (Free)",
      tag = "High Demand"
    ),
    TrainingItem(
      id = "tr_2",
      title = "Smart Home IoT & Automation",
      duration = "16 Hours • Weekend Batch",
      instructor = "Bengaluru Co-op Tech Guild",
      subsidy = "100% Co-op Sponsored (Free)",
      tag = "Premium Rate"
    ),
    TrainingItem(
      id = "tr_3",
      title = "Solar Rooftop Grid Integration",
      duration = "32 Hours • Govt. Certification",
      instructor = "Ministry of Skill Development",
      subsidy = "100% Co-op Sponsored (Free)",
      tag = "Govt. Certified"
    )
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("worker_profile_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Top Title
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Digital Skill Passport",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Tamper-proof verifiable cooperative credential",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }

        IconButton(
          onClick = { showQrDialog = true },
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(HomezyPrimaryContainer)
        ) {
          Icon(Icons.Default.QrCode, contentDescription = "Show QR Token", tint = HomezyPrimary, modifier = Modifier.size(22.dp))
        }
      }
    }

    // ==========================================
    // HERO: ATTRACTIVE DIGITAL SKILL PASSPORT
    // ==========================================
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("digital_skill_passport_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0xFF0F172A),
                  Color(0xFF1E293B),
                  Color(0xFF0F766E).copy(alpha = 0.6f)
                )
              )
            )
            .padding(18.dp)
        ) {
          // Passport Header: Republic / Co-op Crest & Verification
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFF59E0B)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = Color(0xFF0F172A),
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "HOMEZY COOPERATIVE FEDERATION",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.ExtraBold,
                  letterSpacing = 1.sp,
                  color = Color(0xFFF59E0B)
                )
                Text(
                  text = "DIGITAL SKILL PASSPORT",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }

            // Verification Status Badge
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = Color(0xFF065F46),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF34D399))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Verified,
                  contentDescription = null,
                  tint = Color(0xFF34D399),
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Verified ✓",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFD1FAE5)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Worker Name & Avatar Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFF0D9488))
                .border(2.dp, Color(0xFFF59E0B), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "RS",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
              Text(
                text = "Rahul Sharma",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "Member ID: #HMZ-KA-0429",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
              )
              Text(
                text = "Co-op: Bengaluru Trades Federation (Zone East)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFCD34D)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = Color.White.copy(alpha = 0.15f))
          Spacer(modifier = Modifier.height(14.dp))

          // 3 Passport Metrics: Experience, Rating, Jobs Completed
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            PassportMetricItem("Experience", "8 Years", Icons.Default.Timeline)
            PassportMetricItem("Rating", "4.92 ★", Icons.Default.Star)
            PassportMetricItem("Jobs Done", "642 Jobs", Icons.Default.TaskAlt)
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Verified Skills Section
          Text(
            text = "VERIFIED SKILLS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = Color(0xFF94A3B8)
          )
          Spacer(modifier = Modifier.height(6.dp))

          // Skills Flow Row
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            val chunked = passportSkills.chunked(2)
            chunked.forEach { rowSkills ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                rowSkills.forEach { skill ->
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.weight(1f)
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF34D399),
                        modifier = Modifier.size(12.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(
                        text = skill,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 1
                      )
                    }
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Certifications
          Text(
            text = "CERTIFICATIONS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = Color(0xFF94A3B8)
          )
          Spacer(modifier = Modifier.height(6.dp))

          passportCertifications.forEach { cert ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 2.dp)
            ) {
              Icon(
                imageVector = Icons.Default.MilitaryTech,
                contentDescription = null,
                tint = Color(0xFFF59E0B),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = cert,
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }
        }
      }
    }

    // ==========================================
    // SKILL GROWTH SECTION
    // ==========================================
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Skill Growth",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "AI recommendations & Co-op subsidized upskilling",
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        }

        HomezyBadge(
          text = "100% CO-OP FUNDED",
          containerColor = Color(0xFFFEF3C7),
          contentColor = Color(0xFFB45309)
        )
      }
    }

    // Recommended Skills Pills
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "High-Demand Skills in Your Area (Koramangala / Indiranagar):",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(8.dp))

        recommendedSkills.forEach { (skill, benefit) ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = HomezyPrimary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = skill,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = HomezyText
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = HomezySurfaceVariant
            ) {
              Text(
                text = benefit,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
        }
      }
    }

    // Recommended Training Courses
    item {
      Text(
        text = "Recommended Training Modules",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    items(recommendedTrainings) { training ->
      val isEnrolled = enrolledCourses.contains(training.title)

      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = training.title,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = training.duration,
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
            Text(
              text = "By ${training.instructor}",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }

          HomezyBadge(
            text = training.tag,
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = training.subsidy,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF15803D)
          )

          HomezyButton(
            text = if (isEnrolled) "Enrolled ✓" else "Enroll for Free",
            onClick = {
              if (!isEnrolled) {
                enrolledCourses = enrolledCourses + training.title
                showEnrollSuccessDialog = training.title
              }
            },
            variant = if (isEnrolled) ButtonVariant.SECONDARY else ButtonVariant.PRIMARY,
            modifier = Modifier.height(36.dp),
            testTag = "btn_enroll_${training.id}"
          )
        }
      }
    }

    // Co-op Membership & Federation Info
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Cooperative Governance & Rights",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "As an active Co-op Member, you hold 1 equal vote in quarterly tariff setting, welfare pool allocation, and federation leadership elections.",
          fontSize = 12.sp,
          color = HomezyTextSecondary,
          lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          HomezyButton(
            text = "Annual General Meeting",
            onClick = {},
            variant = ButtonVariant.OUTLINE,
            modifier = Modifier.weight(1f)
          )
          HomezyButton(
            text = "Voting Booth",
            onClick = {},
            variant = ButtonVariant.OUTLINE,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Role Switchers & Logout
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        HomezyButton(
          text = "Switch to Customer Portal",
          onClick = onSwitchToCustomer,
          variant = ButtonVariant.OUTLINE,
          icon = Icons.Default.SwapHoriz,
          fullWidth = true,
          testTag = "switch_to_customer_from_worker"
        )

        HomezyButton(
          text = "Switch to Admin Co-op Panel",
          onClick = onSwitchToAdmin,
          variant = ButtonVariant.OUTLINE,
          icon = Icons.Default.AdminPanelSettings,
          fullWidth = true,
          testTag = "switch_to_admin_from_worker"
        )

        HomezyButton(
          text = "Sign Out",
          onClick = onLogout,
          variant = ButtonVariant.TEXT,
          icon = Icons.Default.Logout,
          fullWidth = true,
          testTag = "worker_logout_button"
        )
      }
    }
  }

  // QR Token Dialog
  if (showQrDialog) {
    AlertDialog(
      onDismissRequest = { showQrDialog = false },
      title = { Text("Worker Verifiable QR Token") },
      text = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .size(160.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Color.White)
              .border(2.dp, HomezyPrimary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.QrCode2, contentDescription = "QR Code", tint = HomezyText, modifier = Modifier.size(130.dp))
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Scan with any UPI / Govt. Digilocker app to verify Rahul Sharma's credentials and active police clearance.",
            fontSize = 12.sp,
            color = HomezyTextSecondary,
            lineHeight = 16.sp
          )
        }
      },
      confirmButton = {
        HomezyButton(
          text = "Close",
          onClick = { showQrDialog = false }
        )
      }
    )
  }

  if (showEnrollSuccessDialog != null) {
    AlertDialog(
      onDismissRequest = { showEnrollSuccessDialog = null },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Enrolled Successfully!")
        }
      },
      text = {
        Text("You have been enrolled in '$showEnrollSuccessDialog'. Training materials and class timings have been sent to your WhatsApp.")
      },
      confirmButton = {
        HomezyButton(
          text = "Great!",
          onClick = { showEnrollSuccessDialog = null }
        )
      }
    )
  }
}

@Composable
private fun PassportMetricItem(
  label: String,
  value: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(0xFFF59E0B),
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = value,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    }
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      color = Color(0xFF94A3B8)
    )
  }
}

data class TrainingItem(
  val id: String,
  val title: String,
  val duration: String,
  val instructor: String,
  val subsidy: String,
  val tag: String
)
