package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiFairPriceNegotiationScreen(
  initialService: String = "AC Service",
  initialWorkerName: String = "Rahul Das",
  initialWorkerTrade: String = "AC Specialist",
  initialCustomerBudget: Int = 500,
  onBack: () -> Unit,
  onPriceAgreed: (Int) -> Unit = {}
) {
  var offerInput by remember { mutableStateOf(initialCustomerBudget.toString()) }
  var currentOffer by remember { mutableStateOf(initialCustomerBudget) }
  var isWhyRangeExpanded by remember { mutableStateOf(false) }
  var isOfferSent by remember { mutableStateOf(false) }
  var agreedPrice by remember { mutableStateOf<Int?>(null) }

  val estimatedRangeText = "₹525 – ₹575"

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Text(
              text = "Fair Price Assistant",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "$initialService • $initialWorkerName",
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("fair_price_back_btn")) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = HomezyText
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = HomezyCard)
      )
    },
    modifier = Modifier.testTag("ai_fair_price_screen")
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(HomezyBackground)
        .padding(paddingValues),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // 1. Initial Simple View
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth().testTag("fair_price_card")) {
          Text(
            text = "Estimated Range:",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
          Text(
            text = estimatedRangeText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )

          Spacer(modifier = Modifier.height(14.dp))

          // "Why this range?" expandable toggle
          Row(
            modifier = Modifier
              .clickable { isWhyRangeExpanded = !isWhyRangeExpanded }
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (isWhyRangeExpanded) "Why this range? ▲" else "Why this range? ▼",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }

          // Expandable explanation
          AnimatedVisibility(visible = isWhyRangeExpanded) {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(HomezySurfaceVariant)
                .padding(10.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "• Cooperative standard base rate: ₹450",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Senior technician certified rating: +₹50",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• Fuel & transit cost for 2.1 km: +₹25",
                fontSize = 12.sp,
                color = HomezyText
              )
              Text(
                text = "• No surge pricing applied",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = HomezySecondary
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Your Offer input
          Text(
            text = "Your Offer:",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(6.dp))

          OutlinedTextField(
            value = offerInput,
            onValueChange = {
              offerInput = it
              it.toIntOrNull()?.let { num -> currentOffer = num }
            },
            leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = HomezyText) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("fair_price_offer_input")
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Send Offer CTA
          Button(
            onClick = {
              isOfferSent = true
              val budget = offerInput.toIntOrNull() ?: currentOffer
              agreedPrice = budget
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
            modifier = Modifier.fillMaxWidth().testTag("send_offer_btn")
          ) {
            Text(
              text = if (isOfferSent) "Update Offer" else "Send Offer",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      // 2. Worker Response & Agreement
      if (isOfferSent) {
        item {
          HomezyCard(modifier = Modifier.fillMaxWidth().testTag("offer_status_card")) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFDCFCE7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = null,
                  tint = Color(0xFF15803D),
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Offer Accepted by $initialWorkerName",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF15803D)
                )
                Text(
                  text = "Agreed cooperative rate: ₹$currentOffer",
                  fontSize = 12.sp,
                  color = HomezyTextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
              onClick = {
                onPriceAgreed(agreedPrice ?: currentOffer)
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
              modifier = Modifier.fillMaxWidth().testTag("accept_and_book_btn")
            ) {
              Text(
                text = "Continue with ₹$currentOffer",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }
    }
  }
}
