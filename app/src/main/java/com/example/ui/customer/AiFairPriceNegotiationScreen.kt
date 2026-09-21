package com.example.ui.customer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiFairPriceNegotiationScreen(
  initialService: String = "AC Service",
  initialWorkerName: String = "Rahul Das (Certified Senior)",
  initialWorkerTrade: String = "AC Service Specialist",
  initialCustomerBudget: Int = 500,
  onBack: () -> Unit,
  onPriceAgreed: (Int) -> Unit = {}
) {
  // Configurable Parameters for Demo Pricing
  var selectedService by remember { mutableStateOf(initialService) }
  var jobComplexity by remember { mutableStateOf("Standard") }
  var workerSkill by remember { mutableStateOf("Certified Senior") }
  var location by remember { mutableStateOf("Indiranagar, Bengaluru") }
  var customerBudgetInput by remember { mutableStateOf(initialCustomerBudget.toString()) }

  // Perspective switcher for interactive demo testing: Customer vs Worker
  var activePerspective by remember { mutableStateOf(NegotiationActor.CUSTOMER) }

  // Current Negotiation State
  var customerOffer by remember { mutableStateOf(initialCustomerBudget) }
  var workerCounterOfferInput by remember { mutableStateOf("550") }
  var workerCounterOffer by remember { mutableStateOf<Int?>(null) }
  var agreedPrice by remember { mutableStateOf<Int?>(null) }
  var isNegotiationRejected by remember { mutableStateOf(false) }
  var isCounterOfferMode by remember { mutableStateOf(false) }

  // Recalculate AI Fair Price Suggestion on parameter changes
  val fairPriceSuggestion = remember(selectedService, jobComplexity, workerSkill, location, customerOffer) {
    SampleData.calculateFairPriceSuggestion(
      serviceType = selectedService,
      jobComplexity = jobComplexity,
      workerSkill = workerSkill,
      location = location,
      customerBudget = customerOffer
    )
  }

  // Negotiation History Timeline
  var history by remember {
    mutableStateOf(
      listOf(
        NegotiationHistoryEntry(
          id = "h-0",
          actor = NegotiationActor.AI_ASSISTANT,
          actorName = "AI Fair Price Assistant",
          actionType = NegotiationActionType.AI_SUGGESTION,
          amount = null,
          message = "Recommended Fair Range: ${fairPriceSuggestion.suggestedRangeText} based on standard cooperative baseline rate cards.",
          timestamp = "10:14 AM"
        ),
        NegotiationHistoryEntry(
          id = "h-1",
          actor = NegotiationActor.CUSTOMER,
          actorName = "Customer (You)",
          actionType = NegotiationActionType.OFFER_SENT,
          amount = initialCustomerBudget,
          message = "Submitted initial offer of ₹$initialCustomerBudget.",
          timestamp = "10:15 AM"
        )
      )
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Fair Price Assistant",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFEFF6FF)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(11.dp)
                  )
                  Spacer(modifier = Modifier.width(3.dp))
                  Text(
                    text = "AI-assisted",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8)
                  )
                }
              }
            }
            Text(
              text = "$selectedService • $initialWorkerName",
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
      // 1. Perspective Switcher Banner (To test both Customer and Worker sides)
      item {
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Interactive Role Simulator:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = HomezyTextSecondary
              )
              Text(
                text = if (activePerspective == NegotiationActor.CUSTOMER) "Viewing as Customer" else "Viewing as Worker",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (activePerspective == NegotiationActor.CUSTOMER) Color(0xFF2563EB) else Color(0xFF15803D)
              )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { activePerspective = NegotiationActor.CUSTOMER },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (activePerspective == NegotiationActor.CUSTOMER) Color(0xFF2563EB) else Color.White,
                  contentColor = if (activePerspective == NegotiationActor.CUSTOMER) Color.White else HomezyText
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("switch_to_customer_view")
              ) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Customer View", fontSize = 12.sp)
              }

              Button(
                onClick = { activePerspective = NegotiationActor.WORKER },
                colors = ButtonDefaults.buttonColors(
                  containerColor = if (activePerspective == NegotiationActor.WORKER) Color(0xFF15803D) else Color.White,
                  contentColor = if (activePerspective == NegotiationActor.WORKER) Color.White else HomezyText
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("switch_to_worker_view")
              ) {
                Icon(Icons.Default.Engineering, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Worker View", fontSize = 12.sp)
              }
            }
          }
        }
      }

      // 2. AI Fair Price Suggestion Card
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth().testTag("ai_fair_price_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header with AI indicator
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEFF6FF)),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(16.dp)
                  )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "AI Fair Price Range",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
              }

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF1F5F9)
              ) {
                Text(
                  text = "AI-assisted price suggestion",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Medium,
                  color = HomezyTextSecondary,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Price Numbers
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Bottom
            ) {
              Column {
                Text(
                  text = "Suggested Fair Range",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = HomezyTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = fairPriceSuggestion.suggestedRangeText,
                  fontSize = 28.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFF1E3A8A)
                )
              }

              Column(horizontalAlignment = Alignment.End) {
                Text(
                  text = "Customer Budget",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = HomezyTextSecondary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "₹$customerOffer",
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFFD97706) else Color(0xFF16A34A)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AI Explanation Callout
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFFFFFBEB) else Color(0xFFF0FDF4),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFFFDE68A) else Color(0xFFBBF7D0)
              ),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
              ) {
                Icon(
                  imageVector = if (customerOffer < fairPriceSuggestion.suggestedMin) Icons.Default.Info else Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFFB45309) else Color(0xFF15803D),
                  modifier = Modifier
                    .size(18.dp)
                    .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = "AI explanation:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFFB45309) else Color(0xFF15803D)
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = fairPriceSuggestion.explanation,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (customerOffer < fairPriceSuggestion.suggestedMin) Color(0xFF92400E) else Color(0xFF166534),
                    lineHeight = 17.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mandatory Disclaimer note
            Text(
              text = "AI is an assistant, not an autonomous decision maker. Configurable demo pricing based on cooperative baseline rate cards.",
              fontSize = 10.sp,
              color = HomezyTextSecondary,
              lineHeight = 14.sp
            )
          }
        }
      }

      // 3. Final Agreed Price Card (Displayed prominently if agreed)
      if (agreedPrice != null) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF10B981)),
            modifier = Modifier.fillMaxWidth().testTag("final_agreed_price_card")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Final Agreed Price",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46)
                  )
                }
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFFD1FAE5)
                ) {
                  Text(
                    text = "MUTUALLY AGREED",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "₹$agreedPrice",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF047857)
              )

              Text(
                text = "Both Customer and Worker agreed to ₹$agreedPrice. Zero intermediary markups.",
                fontSize = 12.sp,
                color = Color(0xFF065F46)
              )

              Spacer(modifier = Modifier.height(12.dp))

              Button(
                onClick = { onPriceAgreed(agreedPrice!!) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().testTag("confirm_and_proceed_btn")
              ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Proceed with Agreed Rate (₹$agreedPrice)", fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // 4. Rejection Banner if rejected
      if (isNegotiationRejected && agreedPrice == null) {
        item {
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFDC2626))
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Negotiation Ended",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF991B1B)
                )
                Text(
                  text = "The offer was declined. You can submit a revised offer or re-engage with the suggested range.",
                  fontSize = 11.sp,
                  color = Color(0xFFB91C1C)
                )
              }
              TextButton(
                onClick = {
                  isNegotiationRejected = false
                  customerOffer = fairPriceSuggestion.suggestedMin
                  customerBudgetInput = fairPriceSuggestion.suggestedMin.toString()
                }
              ) {
                Text("Restart", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // 5. Active Action Panel: Customer vs Worker
      if (agreedPrice == null && !isNegotiationRejected) {
        item {
          Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth().testTag("negotiation_action_panel")
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              if (activePerspective == NegotiationActor.CUSTOMER) {
                // ==================== CUSTOMER PERSPECTIVE ====================
                Text(
                  text = "Customer Action Panel",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
                Spacer(modifier = Modifier.height(4.dp))

                if (workerCounterOffer != null) {
                  // Customer sees worker's counter offer
                  Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                      Text(
                        text = "Worker Counter Offer Received:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF166534)
                      )
                      Text(
                        text = "₹$workerCounterOffer",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF15803D)
                      )
                      Text(
                        text = "${initialWorkerName.split(" ")[0]} proposed ₹$workerCounterOffer within the fair range (${fairPriceSuggestion.suggestedRangeText}).",
                        fontSize = 11.sp,
                        color = Color(0xFF166534)
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(12.dp))

                  // Customer actions: Accept, Reject, Continue negotiation
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Button(
                      onClick = {
                        agreedPrice = workerCounterOffer
                        history = history + NegotiationHistoryEntry(
                          id = "h-${history.size}",
                          actor = NegotiationActor.CUSTOMER,
                          actorName = "Customer (You)",
                          actionType = NegotiationActionType.ACCEPTED,
                          amount = workerCounterOffer,
                          message = "Accepted worker's counter offer of ₹$workerCounterOffer.",
                          timestamp = "10:18 AM"
                        )
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(1f).testTag("customer_accept_counter_btn")
                    ) {
                      Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Accept (₹$workerCounterOffer)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                      onClick = {
                        isNegotiationRejected = true
                        history = history + NegotiationHistoryEntry(
                          id = "h-${history.size}",
                          actor = NegotiationActor.CUSTOMER,
                          actorName = "Customer (You)",
                          actionType = NegotiationActionType.REJECTED,
                          amount = null,
                          message = "Declined the counter offer.",
                          timestamp = "10:19 AM"
                        )
                      },
                      colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(0.8f).testTag("customer_reject_btn")
                    ) {
                      Text("Reject", fontSize = 12.sp)
                    }
                  }

                  Spacer(modifier = Modifier.height(8.dp))

                  OutlinedButton(
                    onClick = {
                      workerCounterOffer = null
                      isCounterOfferMode = false
                    },
                    modifier = Modifier.fillMaxWidth().testTag("customer_continue_negotiation_btn"),
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text("Continue Negotiation (Revise Offer)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                  }
                } else {
                  // Standard Customer: Enter offer & Send Offer
                  Text(
                    text = "Enter your proposal for this job:",
                    fontSize = 12.sp,
                    color = HomezyTextSecondary
                  )
                  Spacer(modifier = Modifier.height(8.dp))

                  OutlinedTextField(
                    value = customerBudgetInput,
                    onValueChange = { input ->
                      customerBudgetInput = input.filter { it.isDigit() }
                    },
                    label = { Text("Enter offer (₹)") },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = HomezyPrimary) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("customer_offer_input")
                  )

                  Spacer(modifier = Modifier.height(8.dp))

                  // Quick Suggestion Chips
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                  ) {
                    val quickPicks = listOf(
                      fairPriceSuggestion.suggestedMin,
                      (fairPriceSuggestion.suggestedMin + fairPriceSuggestion.suggestedMax) / 2,
                      fairPriceSuggestion.suggestedMax
                    )
                    quickPicks.forEach { pick ->
                      SuggestionChip(
                        onClick = {
                          customerBudgetInput = pick.toString()
                          customerOffer = pick
                        },
                        label = { Text("₹$pick", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                      )
                    }
                  }

                  Spacer(modifier = Modifier.height(12.dp))

                  Button(
                    onClick = {
                      val newOffer = customerBudgetInput.toIntOrNull() ?: 500
                      customerOffer = newOffer
                      history = history + NegotiationHistoryEntry(
                        id = "h-${history.size}",
                        actor = NegotiationActor.CUSTOMER,
                        actorName = "Customer (You)",
                        actionType = NegotiationActionType.OFFER_SENT,
                        amount = newOffer,
                        message = "Sent offer of ₹$newOffer.",
                        timestamp = "10:16 AM"
                      )
                      // Automatically switch to worker view to let reviewer experience worker actions
                      activePerspective = NegotiationActor.WORKER
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("send_offer_btn")
                  ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Send Offer (₹${customerBudgetInput.ifEmpty { "0" }})", fontWeight = FontWeight.Bold)
                  }
                }
              } else {
                // ==================== WORKER PERSPECTIVE ====================
                Text(
                  text = "Worker Action Panel (${initialWorkerName.split(" ")[0]})",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF15803D)
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Worker receives: Customer Offer & Suggested Range
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = Color(0xFFF8FAFC),
                  border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text("Customer Offer", fontSize = 11.sp, color = HomezyTextSecondary)
                      Text("₹$customerOffer", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = HomezyText)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                      Text("Suggested Range", fontSize = 11.sp, color = HomezyTextSecondary)
                      Text(fairPriceSuggestion.suggestedRangeText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (isCounterOfferMode) {
                  // Worker counter offer input (e.g. ₹550)
                  Text(
                    text = "Enter Counter Offer:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = HomezyText
                  )
                  Spacer(modifier = Modifier.height(6.dp))

                  OutlinedTextField(
                    value = workerCounterOfferInput,
                    onValueChange = { workerCounterOfferInput = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Counter Offer (₹)") },
                    leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = Color(0xFF15803D)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("worker_counter_offer_input")
                  )

                  Spacer(modifier = Modifier.height(8.dp))

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Button(
                      onClick = {
                        val counterAmt = workerCounterOfferInput.toIntOrNull() ?: 550
                        workerCounterOffer = counterAmt
                        isCounterOfferMode = false
                        history = history + NegotiationHistoryEntry(
                          id = "h-${history.size}",
                          actor = NegotiationActor.WORKER,
                          actorName = initialWorkerName.split(" ")[0],
                          actionType = NegotiationActionType.COUNTER_OFFER,
                          amount = counterAmt,
                          message = "Proposed counter offer of ₹$counterAmt.",
                          timestamp = "10:17 AM"
                        )
                        // Switch back to customer view so user sees the counter offer response
                        activePerspective = NegotiationActor.CUSTOMER
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15803D)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(1f).testTag("send_counter_offer_btn")
                    ) {
                      Text("Send Counter (₹${workerCounterOfferInput.ifEmpty { "0" }})", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                      onClick = { isCounterOfferMode = false },
                      shape = RoundedCornerShape(8.dp)
                    ) {
                      Text("Cancel")
                    }
                  }
                } else {
                  // Worker actions: Accept, Reject, Counter Offer
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                    Button(
                      onClick = {
                        agreedPrice = customerOffer
                        history = history + NegotiationHistoryEntry(
                          id = "h-${history.size}",
                          actor = NegotiationActor.WORKER,
                          actorName = initialWorkerName.split(" ")[0],
                          actionType = NegotiationActionType.ACCEPTED,
                          amount = customerOffer,
                          message = "Worker accepted offer at ₹$customerOffer.",
                          timestamp = "10:17 AM"
                        )
                      },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(1f).testTag("worker_accept_offer_btn")
                    ) {
                      Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Accept", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                      onClick = { isCounterOfferMode = true },
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(1.2f).testTag("worker_counter_offer_btn")
                    ) {
                      Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Counter Offer", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                      onClick = {
                        isNegotiationRejected = true
                        history = history + NegotiationHistoryEntry(
                          id = "h-${history.size}",
                          actor = NegotiationActor.WORKER,
                          actorName = initialWorkerName.split(" ")[0],
                          actionType = NegotiationActionType.REJECTED,
                          amount = null,
                          message = "Worker declined offer.",
                          timestamp = "10:18 AM"
                        )
                      },
                      colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                      shape = RoundedCornerShape(8.dp),
                      modifier = Modifier.weight(0.8f).testTag("worker_reject_offer_btn")
                    ) {
                      Text("Reject", fontSize = 12.sp)
                    }
                  }
                }
              }
            }
          }
        }
      }

      // 6. Negotiation History Section
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth().testTag("negotiation_history_card")
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.History,
                  contentDescription = null,
                  tint = HomezyText,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Negotiation History",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
              }
              Text(
                text = "${history.size} steps",
                fontSize = 11.sp,
                color = HomezyTextSecondary
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              history.forEach { item ->
                NegotiationHistoryRow(item)
              }
            }
          }
        }
      }

      // 7. Configurable Demo Parameters (Expandable)
      item {
        var isConfigExpanded by remember { mutableStateOf(false) }

        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { isConfigExpanded = !isConfigExpanded }
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Tune,
                  contentDescription = null,
                  tint = HomezyTextSecondary,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Configurable Demo Pricing Parameters",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = HomezyText
                )
              }
              Icon(
                imageVector = if (isConfigExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = HomezyTextSecondary
              )
            }

            if (isConfigExpanded) {
              Spacer(modifier = Modifier.height(12.dp))
              Divider(color = Color(0xFFE2E8F0))
              Spacer(modifier = Modifier.height(12.dp))

              // Job Complexity
              Text("Job Complexity:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("Minor", "Standard", "Complex").forEach { level ->
                  FilterChip(
                    selected = jobComplexity == level,
                    onClick = { jobComplexity = level },
                    label = { Text(level, fontSize = 11.sp) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Worker Skill
              Text("Worker Skill Level:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("Specialist", "Certified Senior", "Master").forEach { skill ->
                  FilterChip(
                    selected = workerSkill == skill,
                    onClick = { workerSkill = skill },
                    label = { Text(skill, fontSize = 11.sp) }
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              // Location
              Text("Location:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = HomezyText)
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                listOf("Indiranagar", "Howrah", "New Town").forEach { loc ->
                  FilterChip(
                    selected = location.contains(loc),
                    onClick = { location = "$loc, Cooperative Zone" },
                    label = { Text(loc, fontSize = 11.sp) }
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun NegotiationHistoryRow(entry: NegotiationHistoryEntry) {
  val (badgeColor, textColor, icon) = when (entry.actor) {
    NegotiationActor.CUSTOMER -> Triple(Color(0xFFEFF6FF), Color(0xFF1D4ED8), Icons.Default.Person)
    NegotiationActor.WORKER -> Triple(Color(0xFFF0FDF4), Color(0xFF15803D), Icons.Default.Engineering)
    NegotiationActor.AI_ASSISTANT -> Triple(Color(0xFFFAF5FF), Color(0xFF7E22CE), Icons.Default.AutoAwesome)
  }

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .clip(CircleShape)
        .background(badgeColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(15.dp)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    Column(modifier = Modifier.weight(1f)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = entry.actorName,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = textColor
        )
        Text(
          text = entry.timestamp,
          fontSize = 10.sp,
          color = HomezyTextSecondary
        )
      }

      Spacer(modifier = Modifier.height(2.dp))

      Text(
        text = entry.message,
        fontSize = 12.sp,
        color = HomezyText,
        lineHeight = 16.sp
      )
    }
  }
}
