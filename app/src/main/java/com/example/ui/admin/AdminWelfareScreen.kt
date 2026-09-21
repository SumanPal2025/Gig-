package com.example.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import com.example.ui.components.*
import com.example.ui.theme.*

data class WelfareRenewalItem(
  val id: String,
  val workerName: String,
  val trade: String,
  val coOpId: String,
  val type: String, // "Insurance", "Certification", "Training", "Emergency Grant"
  val dueDate: String,
  val grantAmount: String,
  val status: String
)

@Composable
fun AdminWelfareScreen(
  modifier: Modifier = Modifier
) {
  var renewals by remember {
    mutableStateOf(
      listOf(
        WelfareRenewalItem("GNT-881", "Rahul Das", "Electrician", "WKR-101", "Insurance", "Expires in 12 days", "₹5,000", "Pending Renewal"),
        WelfareRenewalItem("GNT-882", "Amit Roy", "Plumber", "WKR-102", "Certification", "Level 2 Renewal in 18 days", "₹3,500", "Pending Audit"),
        WelfareRenewalItem("GNT-883", "Suresh Patel", "AC Technician", "WKR-103", "Training", "Inverter AC Cohort Required", "₹7,500", "Pending Grant"),
        WelfareRenewalItem("GNT-884", "Priya Sen", "Deep Cleaning", "WKR-105", "Insurance", "Expires in 24 days", "₹5,000", "Pending Renewal"),
        WelfareRenewalItem("GNT-885", "Manoj Das", "House Painter", "WKR-104", "Certification", "Eco-Paint Safety due in 8 days", "₹4,000", "Pending Audit")
      )
    )
  }

  var selectedItemForApproval by remember { mutableStateOf<WelfareRenewalItem?>(null) }
  var snackbarMsg by remember { mutableStateOf<String?>(null) }

  // Approval Modal Dialog
  selectedItemForApproval?.let { item ->
    Dialog(onDismissRequest = { selectedItemForApproval = null }) {
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = HomezyCard,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
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
                  .background(HomezyPrimaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.HealthAndSafety,
                  contentDescription = null,
                  tint = HomezyPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Approve Welfare Grant",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = HomezyText
                )
                Text(
                  text = "Grant Ref: ${item.id}",
                  fontSize = 11.sp,
                  color = HomezyTextSecondary
                )
              }
            }
            IconButton(
              onClick = { selectedItemForApproval = null },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Default.Close, contentDescription = "Close", tint = HomezyTextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Divider(color = HomezyBorder)
          Spacer(modifier = Modifier.height(14.dp))

          // Recipient Details Card
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = HomezySurfaceVariant,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Beneficiary:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text("${item.workerName} (${item.trade})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Co-op Member ID:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text(item.coOpId, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HomezyText)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Purpose:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text(item.type, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HomezyPrimary)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Disbursal Grant:", fontSize = 12.sp, color = HomezyTextSecondary)
                Text(item.grantAmount, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF15803D))
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "100% funded from 3% Cooperative Social Security Reserve.",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            OutlinedButton(
              onClick = { selectedItemForApproval = null },
              modifier = Modifier.weight(1f).height(42.dp),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Cancel", fontSize = 13.sp)
            }

            Button(
              onClick = {
                renewals = renewals.map {
                  if (it.id == item.id) it.copy(status = "Renewed via Co-op Pool ✓") else it
                }
                snackbarMsg = "Welfare Grant ${item.grantAmount} approved & disbursed to ${item.workerName}."
                selectedItemForApproval = null
              },
              modifier = Modifier.weight(1f).height(42.dp).testTag("confirm_approve_grant_btn"),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Confirm", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("admin_welfare_screen"),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    item {
      Column {
        Text(
          text = "Worker Welfare & Protection Fund",
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Federation social security pool, healthcare coverage, and democratic upskilling",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // 4 Core Required Welfare Metrics
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Insured Workers",
          value = "180",
          subtext = "100% of Co-op Members",
          badgeText = "₹5L Cashless",
          isPositive = true,
          icon = Icons.Default.Security,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Insurance Expiring",
          value = "8",
          subtext = "Next 30 days renewal",
          badgeText = "Action Req.",
          isPositive = false,
          icon = Icons.Default.WarningAmber,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        StatCard(
          title = "Training Required",
          value = "14",
          subtext = "Inverter & Solar standard",
          badgeText = "Co-op Sponsored",
          isPositive = true,
          icon = Icons.Default.School,
          modifier = Modifier.weight(1f)
        )
        StatCard(
          title = "Certification Expiring",
          value = "5",
          subtext = "ITI Skill Passport audits",
          icon = Icons.Default.CardMembership,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Federation Welfare Treasury Pool
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyPrimary,
        elevation = 2.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Federation Welfare Pool Balance", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("₹4,82,500.00", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp)
          }

          HomezyBadge(
            text = "3% Platform Surcharge Pool",
            containerColor = Color.White.copy(alpha = 0.2f),
            contentColor = Color.White
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = Color.White.copy(alpha = 0.2f))
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Medical Claims Disbursed: ₹1,20,000", color = HomezyAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
          Text("0% Interest Tool Loans: 14 Active", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
        }
      }
    }

    // Welfare Compliance & Renewals Queue
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Actionable Welfare & Renewal Queue",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = HomezyText
        )
        Text(
          text = "${renewals.count { !it.status.contains("Renewed") }} Pending",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }
    }

    items(renewals) { item ->
      val isApproved = item.status.contains("Renewed") || item.status.contains("Completed")

      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Left details column
          Row(
            modifier = Modifier.weight(1f).padding(end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isApproved) Color(0xFFDCFCE7) else HomezyPrimaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (isApproved) Icons.Default.CheckCircle else Icons.Default.HealthAndSafety,
                contentDescription = null,
                tint = if (isApproved) Color(0xFF15803D) else HomezyPrimary,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Text(
                text = "${item.workerName} • ${item.trade}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = HomezyText
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${item.type} • ${item.dueDate}",
                fontSize = 11.sp,
                color = if (item.dueDate.contains("12") || item.dueDate.contains("8")) Color(0xFFDC2626) else HomezyTextSecondary,
                fontWeight = FontWeight.Medium
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Grant Value: ${item.grantAmount}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isApproved) Color(0xFF15803D) else HomezyPrimary
              )
            }
          }

          // Right Action Button
          if (isApproved) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFDCFCE7),
              modifier = Modifier.wrapContentSize()
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color(0xFF15803D),
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Approved ✓",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF15803D)
                )
              }
            }
          } else {
            Button(
              onClick = { selectedItemForApproval = item },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
              contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
              modifier = Modifier
                .height(36.dp)
                .wrapContentWidth()
                .testTag("approve_grant_${item.id.lowercase()}")
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Approve Grant",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // Recent Disbursed Grants
    item {
      Text(
        text = "Recent Welfare Disbursals",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        WelfareApprovalRow("Anand Kumar (Plumber)", "Tool Purchase Loan (Drill Kit)", "₹15,000 (0% Interest)", "Disbursed ✓")
        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        WelfareApprovalRow("Rahul Sharma (Electrician)", "Accident Outpatient Claim", "₹4,200", "Settled ✓")
        Divider(color = HomezyBorder, modifier = Modifier.padding(vertical = 10.dp))
        WelfareApprovalRow("Govind Swamy (Carpenter)", "Child School Education Grant", "₹10,000", "Approved ✓")
      }
    }
  }
}

@Composable
private fun WelfareApprovalRow(name: String, purpose: String, amount: String, status: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
      Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
      Text(text = purpose, fontSize = 11.sp, color = HomezyTextSecondary)
    }
    Column(horizontalAlignment = Alignment.End) {
      Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = HomezyPrimary)
      Text(text = status, fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Medium)
    }
  }
}

