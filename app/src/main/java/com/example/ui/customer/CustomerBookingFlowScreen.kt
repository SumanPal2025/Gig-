package com.example.ui.customer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BookingFlowStep(val title: String) {
  SELECT_SERVICE("Select Service"),
  SELECT_WORKER("Choose Worker"),
  SERVICE_DETAILS("Problem Details"),
  DATE_TIME("Date & Time"),
  ADDRESS("Service Address"),
  PRICE_NEGOTIATION("Fair Pricing"),
  CONFIRMATION("Review & Pricing"),
  PAYMENT("Secure Payment"),
  PAYMENT_CONFIRMATION("Payment Receipt"),
  SERVICE_TRACKER("Service Progress"),
  RATING("Verified Rating"),
  FINAL_SUCCESS("Completed")
}

@Composable
fun CustomerBookingFlowScreen(
  initialService: ServiceItem? = null,
  initialWorker: WorkerProfile? = null,
  currentUser: AuthUser,
  onBookingFinished: (BookingItem) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()

  // State
  var currentStep by remember {
    mutableStateOf(
      when {
        initialWorker != null -> BookingFlowStep.SERVICE_DETAILS
        initialService != null -> BookingFlowStep.SELECT_WORKER
        else -> BookingFlowStep.SELECT_SERVICE
      }
    )
  }

  var selectedService by remember {
    mutableStateOf(initialService ?: SampleData.services.first())
  }
  var selectedWorker by remember {
    mutableStateOf(
      initialWorker ?: SampleData.workers.firstOrNull { it.trade.contains("AC", ignoreCase = true) }
      ?: SampleData.workers.first()
    )
  }

  // Smart match toggle
  var isSmartMatchActive by remember { mutableStateOf(false) }

  // Form details
  var problemDescription by remember {
    mutableStateOf("Indoor unit is dripping water and cooling efficiency has dropped.")
  }
  var urgencyLevel by remember { mutableStateOf("Standard Service") }

  // Schedule
  var selectedDate by remember { mutableStateOf("Today, 20 Sep") }
  var selectedTimeSlot by remember { mutableStateOf("02:00 PM - 04:00 PM") }

  // Address
  var flatNumber by remember { mutableStateOf("Flat 402, Green Glen Layout") }
  var areaLocality by remember { mutableStateOf("Bellandur, Outer Ring Road") }
  var cityPincode by remember { mutableStateOf("Bengaluru - 560103") }
  var contactPhone by remember { mutableStateOf(currentUser.phone) }

  // Price negotiation
  val basePrice = selectedWorker.hourlyRate + selectedService.startingPrice / 2
  var negotiatedPrice by remember(selectedWorker, selectedService) {
    mutableStateOf(basePrice)
  }
  var negotiationMode by remember { mutableStateOf("Standard") } // "Standard" or "Custom"
  var priceAdjustmentNote by remember { mutableStateOf("Standard cooperative member rate accepted.") }

  // Transparent Price Breakdown calculations (No fixed percentage claims)
  val workerEarnings = (negotiatedPrice * 0.88).toInt().coerceAtLeast(100)
  val cooperativeContribution = (negotiatedPrice * 0.08).toInt().coerceAtLeast(20)
  val platformFee = negotiatedPrice - workerEarnings - cooperativeContribution
  val totalPrice = negotiatedPrice

  // Payment State
  var selectedPaymentMethod by remember { mutableStateOf("UPI") } // "UPI", "Card", "Cash"
  var upiProvider by remember { mutableStateOf("Google Pay") }
  var upiIdInput by remember { mutableStateOf("customer@okaxis") }
  var cardNumber by remember { mutableStateOf("4532 •••• •••• 8921") }
  var cardExpiry by remember { mutableStateOf("09/28") }
  var cardCvv by remember { mutableStateOf("382") }
  var isPaymentProcessing by remember { mutableStateOf(false) }
  var transactionRefId by remember { mutableStateOf("TXN-HMZ-89410") }

  // Active Booking reference
  var createdBooking by remember {
    mutableStateOf<BookingItem?>(null)
  }

  // Work Completion & Verification State
  var isWorkMarkedCompleteByWorker by remember { mutableStateOf(false) }
  var isCompletionVerifiedByCustomer by remember { mutableStateOf(false) }
  var isIssueReported by remember { mutableStateOf(false) }
  var reportedIssueReason by remember { mutableStateOf("") }
  var showReportIssueDialog by remember { mutableStateOf(false) }

  // Rating State (Only unlocked after completion verification)
  var ratingScore by remember { mutableStateOf(5) }
  var ratingComment by remember { mutableStateOf("Punctual, thorough, and highly professional work. Left the workspace spotless!") }
  var selectedCompliments by remember {
    mutableStateOf(setOf("Punctual", "Expert Craftsperson", "Fair Pricing"))
  }

  // Filter workers based on selected service
  val serviceWorkers = remember(selectedService) {
    val term = when (selectedService.id) {
      "srv_ac" -> "AC"
      "srv_electrician" -> "Electrician"
      "srv_plumber" -> "Plumber"
      else -> "Tech"
    }
    val matched = SampleData.workers.filter {
      it.trade.contains(term, ignoreCase = true) ||
      it.skills.any { skill -> skill.contains(term, ignoreCase = true) }
    }
    if (matched.isNotEmpty()) matched else SampleData.workers
  }

  // Smart Match Best Worker: highest fair allocation score & available
  val smartMatchWorker = remember(serviceWorkers) {
    serviceWorkers.maxByOrNull { it.fairAllocationScore + (if (it.isAvailable) 20 else 0) }
      ?: serviceWorkers.first()
  }

  val displayedWorkers = if (isSmartMatchActive) {
    listOf(smartMatchWorker)
  } else {
    serviceWorkers
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_booking_flow_screen")
  ) {
    // Header Navigation Bar
    Surface(
      color = HomezyCard,
      shadowElevation = 2.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            when (currentStep) {
              BookingFlowStep.SELECT_SERVICE -> onNavigateBack()
              BookingFlowStep.SELECT_WORKER -> currentStep = BookingFlowStep.SELECT_SERVICE
              BookingFlowStep.SERVICE_DETAILS -> currentStep = BookingFlowStep.SELECT_WORKER
              BookingFlowStep.DATE_TIME -> currentStep = BookingFlowStep.SERVICE_DETAILS
              BookingFlowStep.ADDRESS -> currentStep = BookingFlowStep.DATE_TIME
              BookingFlowStep.PRICE_NEGOTIATION -> currentStep = BookingFlowStep.ADDRESS
              BookingFlowStep.CONFIRMATION -> currentStep = BookingFlowStep.PRICE_NEGOTIATION
              BookingFlowStep.PAYMENT -> currentStep = BookingFlowStep.CONFIRMATION
              BookingFlowStep.PAYMENT_CONFIRMATION -> currentStep = BookingFlowStep.PAYMENT
              BookingFlowStep.SERVICE_TRACKER -> onNavigateBack()
              BookingFlowStep.RATING -> currentStep = BookingFlowStep.SERVICE_TRACKER
              BookingFlowStep.FINAL_SUCCESS -> onNavigateBack()
            }
          },
          modifier = Modifier.testTag("booking_flow_back_btn")
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = HomezyText
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = currentStep.title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Service: ${selectedService.name}",
            fontSize = 12.sp,
            color = HomezyPrimary
          )
        }

        // Cooperative Badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = HomezyPrimaryContainer
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = HomezyPrimary,
              modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Verified Flow",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }
        }
      }
    }

    // Step Progress Indicator
    LinearProgressIndicator(
      progress = {
        val steps = BookingFlowStep.values()
        (currentStep.ordinal + 1).toFloat() / steps.size.toFloat()
      },
      modifier = Modifier.fillMaxWidth().height(3.dp),
      color = HomezyPrimary,
      trackColor = HomezyBorder
    )

    // Body Content based on step
    Box(modifier = Modifier.fillMaxSize().weight(1f)) {
      when (currentStep) {
        // STEP 1: SELECT SERVICE
        BookingFlowStep.SELECT_SERVICE -> {
          SelectServiceStep(
            services = SampleData.services,
            selectedService = selectedService,
            onSelectService = { service ->
              selectedService = service
              currentStep = BookingFlowStep.SELECT_WORKER
            }
          )
        }

        // STEP 2: VIEW WORKERS & SMART MATCH
        BookingFlowStep.SELECT_WORKER -> {
          SelectWorkerStep(
            service = selectedService,
            workers = displayedWorkers,
            isSmartMatchActive = isSmartMatchActive,
            smartMatchWorker = smartMatchWorker,
            onToggleSmartMatch = { isSmartMatchActive = it },
            onSelectWorker = { worker ->
              selectedWorker = worker
              negotiatedPrice = worker.hourlyRate + selectedService.startingPrice / 2
              currentStep = BookingFlowStep.SERVICE_DETAILS
            }
          )
        }

        // STEP 3: SERVICE DETAILS (Problem Description)
        BookingFlowStep.SERVICE_DETAILS -> {
          ServiceDetailsStep(
            service = selectedService,
            worker = selectedWorker,
            problemDescription = problemDescription,
            onProblemDescriptionChange = { problemDescription = it },
            urgencyLevel = urgencyLevel,
            onUrgencyLevelChange = { urgencyLevel = it },
            onContinue = { currentStep = BookingFlowStep.DATE_TIME }
          )
        }

        // STEP 4: DATE & TIME
        BookingFlowStep.DATE_TIME -> {
          DateTimeStep(
            worker = selectedWorker,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it },
            selectedTimeSlot = selectedTimeSlot,
            onTimeSlotSelected = { selectedTimeSlot = it },
            onContinue = { currentStep = BookingFlowStep.ADDRESS }
          )
        }

        // STEP 5: ADDRESS
        BookingFlowStep.ADDRESS -> {
          AddressStep(
            flatNumber = flatNumber,
            onFlatNumberChange = { flatNumber = it },
            areaLocality = areaLocality,
            onAreaLocalityChange = { areaLocality = it },
            cityPincode = cityPincode,
            onCityPincodeChange = { cityPincode = it },
            contactPhone = contactPhone,
            onContactPhoneChange = { contactPhone = it },
            onContinue = { currentStep = BookingFlowStep.PRICE_NEGOTIATION }
          )
        }

        // STEP 6: PRICE NEGOTIATION
        BookingFlowStep.PRICE_NEGOTIATION -> {
          PriceNegotiationStep(
            worker = selectedWorker,
            service = selectedService,
            basePrice = basePrice,
            negotiatedPrice = negotiatedPrice,
            onPriceChanged = { negotiatedPrice = it },
            negotiationMode = negotiationMode,
            onModeChanged = { negotiationMode = it },
            adjustmentNote = priceAdjustmentNote,
            onNoteChanged = { priceAdjustmentNote = it },
            onContinue = { currentStep = BookingFlowStep.CONFIRMATION }
          )
        }

        // STEP 7: BOOKING CONFIRMATION & TRANSPARENT PRICE
        BookingFlowStep.CONFIRMATION -> {
          BookingConfirmationStep(
            service = selectedService,
            worker = selectedWorker,
            problemDescription = problemDescription,
            date = selectedDate,
            timeSlot = selectedTimeSlot,
            address = "$flatNumber, $areaLocality, $cityPincode",
            servicePrice = negotiatedPrice,
            workerEarnings = workerEarnings,
            cooperativeContribution = cooperativeContribution,
            platformFee = platformFee,
            totalPrice = totalPrice,
            onProceedToPayment = { currentStep = BookingFlowStep.PAYMENT }
          )
        }

        // STEP 8: PAYMENT PROTOTYPE
        BookingFlowStep.PAYMENT -> {
          PaymentPrototypeStep(
            totalAmount = totalPrice,
            workerEarnings = workerEarnings,
            cooperativeContribution = cooperativeContribution,
            platformFee = platformFee,
            selectedMethod = selectedPaymentMethod,
            onSelectMethod = { selectedPaymentMethod = it },
            upiProvider = upiProvider,
            onUpiProviderChange = { upiProvider = it },
            upiIdInput = upiIdInput,
            onUpiIdChange = { upiIdInput = it },
            cardNumber = cardNumber,
            onCardNumberChange = { cardNumber = it },
            cardExpiry = cardExpiry,
            onCardExpiryChange = { cardExpiry = it },
            cardCvv = cardCvv,
            onCardCvvChange = { cardCvv = it },
            isProcessing = isPaymentProcessing,
            onPay = {
              coroutineScope.launch {
                isPaymentProcessing = true
                delay(1200) // Simulated payment verification
                isPaymentProcessing = false
                transactionRefId = "TXN-HMZ-${(100000..999999).random()}"

                val newBooking = BookingItem(
                  id = "HMZ-${(1000..9999).random()}",
                  serviceName = selectedService.name,
                  category = selectedService.category,
                  customerName = currentUser.name,
                  customerAddress = "$flatNumber, $areaLocality, $cityPincode",
                  workerName = selectedWorker.name,
                  workerPhone = selectedWorker.phone,
                  date = selectedDate,
                  timeSlot = selectedTimeSlot,
                  price = totalPrice,
                  status = BookingStatus.IN_PROGRESS,
                  problemDescription = problemDescription,
                  workerEarnings = workerEarnings,
                  cooperativeContribution = cooperativeContribution,
                  platformFee = platformFee,
                  paymentMethod = selectedPaymentMethod,
                  paymentStatus = if (selectedPaymentMethod == "Cash") "COD (Handover on Completion)" else "Paid",
                  isWorkCompletedByWorker = false,
                  isCompletionVerified = false
                )
                createdBooking = newBooking
                currentStep = BookingFlowStep.PAYMENT_CONFIRMATION
              }
            }
          )
        }

        // STEP 8B: PAYMENT CONFIRMATION & DIGITAL INVOICE UI
        BookingFlowStep.PAYMENT_CONFIRMATION -> {
          val booking = createdBooking ?: BookingItem(
            id = "HMZ-8892",
            serviceName = selectedService.name,
            customerName = currentUser.name,
            customerAddress = "$flatNumber, $areaLocality",
            workerName = selectedWorker.name,
            date = selectedDate,
            timeSlot = selectedTimeSlot,
            price = totalPrice,
            status = BookingStatus.IN_PROGRESS,
            workerEarnings = workerEarnings,
            cooperativeContribution = cooperativeContribution,
            platformFee = platformFee
          )
          PaymentConfirmationAndInvoiceStep(
            booking = booking,
            transactionRef = transactionRefId,
            paymentMethod = selectedPaymentMethod,
            onProceedToTracker = {
              currentStep = BookingFlowStep.SERVICE_TRACKER
            }
          )
        }

        // STEP 9 & 10: WORK COMPLETION & COMPLETION VERIFICATION
        BookingFlowStep.SERVICE_TRACKER -> {
          val booking = createdBooking ?: BookingItem(
            id = "HMZ-8892",
            serviceName = selectedService.name,
            customerName = currentUser.name,
            customerAddress = "$flatNumber, $areaLocality",
            workerName = selectedWorker.name,
            date = selectedDate,
            timeSlot = selectedTimeSlot,
            price = totalPrice,
            status = if (isWorkMarkedCompleteByWorker) BookingStatus.COMPLETED else BookingStatus.IN_PROGRESS
          )

          WorkCompletionAndVerificationStep(
            booking = booking,
            worker = selectedWorker,
            isWorkMarkedCompleteByWorker = isWorkMarkedCompleteByWorker,
            isCompletionVerifiedByCustomer = isCompletionVerifiedByCustomer,
            isIssueReported = isIssueReported,
            reportedIssueReason = reportedIssueReason,
            onWorkerMarkCompleted = {
              isWorkMarkedCompleteByWorker = true
            },
            onCustomerVerifyCompleted = {
              isCompletionVerifiedByCustomer = true
              currentStep = BookingFlowStep.RATING
            },
            onReportIssueClick = {
              showReportIssueDialog = true
            }
          )
        }

        // STEP 11: RATING (Protected by False-Rating Mitigation)
        BookingFlowStep.RATING -> {
          RatingStep(
            worker = selectedWorker,
            service = selectedService,
            ratingScore = ratingScore,
            onRatingScoreChange = { ratingScore = it },
            comment = ratingComment,
            onCommentChange = { ratingComment = it },
            selectedCompliments = selectedCompliments,
            onToggleCompliment = { tag ->
              selectedCompliments = if (selectedCompliments.contains(tag)) {
                selectedCompliments - tag
              } else {
                selectedCompliments + tag
              }
            },
            onSubmitRating = {
              createdBooking?.let { b ->
                val finalBooking = b.copy(
                  status = BookingStatus.COMPLETED,
                  isWorkCompletedByWorker = true,
                  isCompletionVerified = true,
                  rating = ratingScore,
                  ratingComment = ratingComment
                )
                createdBooking = finalBooking
                onBookingFinished(finalBooking)
              }
              currentStep = BookingFlowStep.FINAL_SUCCESS
            }
          )
        }

        // STEP 12: FINAL SUCCESS
        BookingFlowStep.FINAL_SUCCESS -> {
          FinalSuccessStep(
            worker = selectedWorker,
            service = selectedService,
            booking = createdBooking,
            onViewBookings = {
              createdBooking?.let { onBookingFinished(it) }
              onNavigateBack()
            }
          )
        }
      }
    }
  }

  // Report Issue Modal Dialog
  if (showReportIssueDialog) {
    AlertDialog(
      onDismissRequest = { showReportIssueDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = HomezyError,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Report Service Issue", fontWeight = FontWeight.Bold)
        }
      },
      text = {
        Column {
          Text(
            text = "Tell us what went wrong. The Cooperative Ombudsman and Member Representative will inspect the work.",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = reportedIssueReason,
            onValueChange = { reportedIssueReason = it },
            placeholder = { Text("e.g. Work is partially incomplete or parts were missing") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Notice: Rating is paused while issue resolution is under review.",
            fontSize = 11.sp,
            color = HomezyPrimary,
            fontWeight = FontWeight.Medium
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            isIssueReported = true
            showReportIssueDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = HomezyError)
        ) {
          Text("Submit Grievance")
        }
      },
      dismissButton = {
        TextButton(onClick = { showReportIssueDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

// -------------------------------------------------------------
// SUB-STEPS IMPLEMENTATION
// -------------------------------------------------------------

@Composable
private fun SelectServiceStep(
  services: List<ServiceItem>,
  selectedService: ServiceItem,
  onSelectService: (ServiceItem) -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Choose a Supported Service",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "All services are carried out by verified member craftspeople with transparent rate cards.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    items(services) { service ->
      ServiceCard(
        service = service,
        onViewService = onSelectService
      )
    }
  }
}

@Composable
private fun SelectWorkerStep(
  service: ServiceItem,
  workers: List<WorkerProfile>,
  isSmartMatchActive: Boolean,
  smartMatchWorker: WorkerProfile,
  onToggleSmartMatch: (Boolean) -> Unit,
  onSelectWorker: (WorkerProfile) -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Select Cooperative Worker",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "For ${service.name} • Indiranagar & nearby",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Smart Match Toggle Card
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (isSmartMatchActive) HomezyPrimaryContainer else HomezySurfaceVariant
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
                .size(40.dp)
                .clip(CircleShape)
                .background(HomezyPrimary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Smart Match (Fair Allocation AI)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Text(
                text = "Auto-recommends by distance, availability & fair workload balance",
                fontSize = 11.sp,
                color = HomezyTextSecondary
              )
            }
          }
          Switch(
            checked = isSmartMatchActive,
            onCheckedChange = onToggleSmartMatch,
            modifier = Modifier.testTag("smart_match_toggle")
          )
        }

        if (isSmartMatchActive) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = HomezyPrimary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Top recommendation: ${smartMatchWorker.name} (Equity Score ${smartMatchWorker.fairAllocationScore}/100, ${smartMatchWorker.distanceKm} km away)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = HomezyPrimary
              )
            }
          }
        }
      }
    }

    items(workers) { worker ->
      WorkerCard(
        worker = worker,
        onBookWorker = onSelectWorker
      )
    }
  }
}

@Composable
private fun ServiceDetailsStep(
  service: ServiceItem,
  worker: WorkerProfile,
  problemDescription: String,
  onProblemDescriptionChange: (String) -> Unit,
  urgencyLevel: String,
  onUrgencyLevelChange: (String) -> Unit,
  onContinue: () -> Unit
) {
  val quickTags = when (service.id) {
    "srv_ac" -> listOf("Water dripping", "Low cooling", "Deep jet cleaning", "Foul odor", "Compressor noise", "Gas leak check")
    "srv_electrician" -> listOf("Switchboard sparking", "Ceiling fan repair", "MCB tripping", "Appliance wiring", "Power outage in 1 room")
    "srv_plumber" -> listOf("Tap leakage", "Low water pressure", "Pipe joint dripping", "Toilet flush issue", "Drain clog")
    else -> listOf("General inspection", "Repair", "Installation")
  }

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Describe Your Service Requirement",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "Help ${worker.name} bring the right tools and diagnostic parts.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Selected Worker & Service Banner
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(HomezyPrimaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.name.take(2).uppercase(),
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = worker.name,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
            Text(
              text = "${worker.trade} • ★ ${worker.rating} (${worker.completedJobs} jobs)",
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
          }
          HomezyBadge(
            text = "Co-op Partner",
            containerColor = HomezyPrimaryContainer,
            contentColor = HomezyPrimary
          )
        }
      }
    }

    // Problem Description Input
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "What seems to be the problem?",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = problemDescription,
          onValueChange = onProblemDescriptionChange,
          placeholder = { Text("Describe the issue in detail...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .testTag("booking_problem_description_input"),
          maxLines = 4
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Quick Issue Tags (Tap to add):",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = HomezyTextSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Quick Tag Pills
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          quickTags.chunked(3).forEach { rowTags ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              rowTags.forEach { tag ->
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (problemDescription.contains(tag, ignoreCase = true)) HomezyPrimaryContainer else HomezySurfaceVariant,
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (problemDescription.contains(tag, ignoreCase = true)) HomezyPrimary else HomezyBorder
                  ),
                  onClick = {
                    onProblemDescriptionChange(
                      if (problemDescription.isBlank()) tag else "$problemDescription • $tag"
                    )
                  }
                ) {
                  Text(
                    text = tag,
                    fontSize = 11.sp,
                    color = if (problemDescription.contains(tag, ignoreCase = true)) HomezyPrimary else HomezyText,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Urgency Preference
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Service Urgency",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          listOf("Standard Service", "Priority Dispatch").forEach { level ->
            val isSelected = urgencyLevel == level
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) HomezyPrimaryContainer else HomezyCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) HomezyPrimary else HomezyBorder
              ),
              onClick = { onUrgencyLevelChange(level) },
              modifier = Modifier.weight(1f)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                RadioButton(
                  selected = isSelected,
                  onClick = { onUrgencyLevelChange(level) },
                  colors = RadioButtonDefaults.colors(selectedColor = HomezyPrimary)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = level,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isSelected) HomezyPrimary else HomezyText
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      HomezyButton(
        text = "Continue to Date & Time",
        onClick = onContinue,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        testTag = "booking_continue_datetime_btn"
      )
    }
  }
}

@Composable
private fun DateTimeStep(
  worker: WorkerProfile,
  selectedDate: String,
  onDateSelected: (String) -> Unit,
  selectedTimeSlot: String,
  onTimeSlotSelected: (String) -> Unit,
  onContinue: () -> Unit
) {
  val dateOptions = listOf(
    "Today, 20 Sep",
    "Tomorrow, 21 Sep",
    "Monday, 22 Sep",
    "Tuesday, 23 Sep"
  )

  val timeSlots = listOf(
    "09:00 AM - 11:00 AM" to "Morning Slot",
    "11:30 AM - 01:30 PM" to "Noon Slot",
    "02:00 PM - 04:00 PM" to "Afternoon Slot",
    "04:30 PM - 06:30 PM" to "Evening Slot",
    "07:00 PM - 09:00 PM" to "Late Evening"
  )

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Select Appointment Slot",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "${worker.name} is available for direct dispatch in your area.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Date Picker Chips
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Preferred Date",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          dateOptions.forEach { date ->
            val isSelected = selectedDate == date
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) HomezyPrimaryContainer else HomezySurfaceVariant,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) HomezyPrimary else HomezyBorder
              ),
              onClick = { onDateSelected(date) },
              modifier = Modifier.weight(1f)
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = date.substringBefore(","),
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) HomezyPrimary else HomezyText
                )
                Text(
                  text = date.substringAfter(",").trim(),
                  fontSize = 10.sp,
                  color = HomezyTextSecondary
                )
              }
            }
          }
        }
      }
    }

    // Time Slot Chips
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Available Time Window",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          timeSlots.forEach { (slot, label) ->
            val isSelected = selectedTimeSlot == slot
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) HomezyPrimaryContainer else HomezyCard,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) HomezyPrimary else HomezyBorder
              ),
              onClick = { onTimeSlotSelected(slot) },
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = if (isSelected) HomezyPrimary else HomezyTextSecondary,
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(
                    text = slot,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) HomezyPrimary else HomezyText
                  )
                }
                Text(
                  text = label,
                  fontSize = 12.sp,
                  color = HomezyTextSecondary
                )
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      HomezyButton(
        text = "Continue to Address",
        onClick = onContinue,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        testTag = "booking_continue_address_btn"
      )
    }
  }
}

@Composable
private fun AddressStep(
  flatNumber: String,
  onFlatNumberChange: (String) -> Unit,
  areaLocality: String,
  onAreaLocalityChange: (String) -> Unit,
  cityPincode: String,
  onCityPincodeChange: (String) -> Unit,
  contactPhone: String,
  onContactPhoneChange: (String) -> Unit,
  onContinue: () -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Service Location",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "Enter where the worker will arrive.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Address Details",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = flatNumber,
          onValueChange = onFlatNumberChange,
          label = { Text("Flat / House / Building") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = areaLocality,
          onValueChange = onAreaLocalityChange,
          label = { Text("Area / Street / Landmark") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = cityPincode,
          onValueChange = onCityPincodeChange,
          label = { Text("City & Pincode") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = contactPhone,
          onValueChange = onContactPhoneChange,
          label = { Text("Contact Phone") },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      HomezyButton(
        text = "Continue to Fair Pricing",
        onClick = onContinue,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        testTag = "booking_continue_pricing_btn"
      )
    }
  }
}

@Composable
private fun PriceNegotiationStep(
  worker: WorkerProfile,
  service: ServiceItem,
  basePrice: Int,
  negotiatedPrice: Int,
  onPriceChanged: (Int) -> Unit,
  negotiationMode: String,
  onModeChanged: (String) -> Unit,
  adjustmentNote: String,
  onNoteChanged: (String) -> Unit,
  onContinue: () -> Unit
) {
  var offerInput by remember { mutableStateOf(negotiatedPrice.toString()) }
  var workerCounterOffer by remember { mutableStateOf<Int?>(550) }
  var isCounterActive by remember { mutableStateOf(false) }
  var isPriceAgreed by remember { mutableStateOf(false) }
  var isNegotiationRejected by remember { mutableStateOf(false) }

  val fairPriceSuggestion = remember(service.name, worker.trade, negotiatedPrice) {
    SampleData.calculateFairPriceSuggestion(
      serviceType = service.name,
      jobComplexity = "Standard",
      workerSkill = if (worker.experienceYears > 5) "Certified Senior" else "Specialist",
      location = "Indiranagar, Bengaluru",
      customerBudget = negotiatedPrice
    )
  }

  var negotiationHistory by remember {
    mutableStateOf(
      listOf(
        NegotiationHistoryEntry(
          id = "h-0",
          actor = NegotiationActor.AI_ASSISTANT,
          actorName = "AI Fair Price Assistant",
          actionType = NegotiationActionType.AI_SUGGESTION,
          amount = null,
          message = "Recommended Fair Range: ${fairPriceSuggestion.suggestedRangeText} based on cooperative benchmark rate cards.",
          timestamp = "10:14 AM"
        ),
        NegotiationHistoryEntry(
          id = "h-1",
          actor = NegotiationActor.CUSTOMER,
          actorName = "Customer Offer",
          actionType = NegotiationActionType.OFFER_SENT,
          amount = negotiatedPrice,
          message = "Initial proposal of ₹$negotiatedPrice submitted.",
          timestamp = "10:15 AM"
        )
      )
    )
  }

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize().testTag("price_negotiation_step")
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "AI Fair Price Assistant",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Democratic, transparent pricing without predatory aggregator margins.",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xFFEFF6FF)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = Color(0xFF2563EB),
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "AI-assisted",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1D4ED8)
            )
          }
        }
      }
    }

    // 1. AI Fair Price Range Card
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("ai_fair_price_card")) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Suggested Fair Price Range",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyTextSecondary
          )
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = Color(0xFFF1F5F9)
          ) {
            Text(
              text = "AI-assisted price suggestion",
              fontSize = 9.sp,
              fontWeight = FontWeight.Medium,
              color = HomezyTextSecondary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Text(
            text = fairPriceSuggestion.suggestedRangeText,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E3A8A)
          )

          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "Customer Offer",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
            Text(
              text = "₹$negotiatedPrice",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFFD97706) else Color(0xFF16A34A)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // AI Explanation Box
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFFFFFBEB) else Color(0xFFF0FDF4),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFFFDE68A) else Color(0xFFBBF7D0)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Icons.Default.Info else Icons.Default.CheckCircle,
              contentDescription = null,
              tint = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFFB45309) else Color(0xFF15803D),
              modifier = Modifier.size(16.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "AI explanation:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFFB45309) else Color(0xFF15803D)
              )
              Text(
                text = fairPriceSuggestion.explanation,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (negotiatedPrice < fairPriceSuggestion.suggestedMin) Color(0xFF92400E) else Color(0xFF166534),
                lineHeight = 16.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "AI is an assistant, not an autonomous decision maker. Configurable demo pricing based on cooperative baseline rate cards.",
          fontSize = 10.sp,
          color = HomezyTextSecondary,
          lineHeight = 14.sp
        )
      }
    }

    // 2. Final Agreed Price Card (If accepted)
    if (isPriceAgreed) {
      item {
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
          border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF10B981)),
          modifier = Modifier.fillMaxWidth().testTag("booking_final_agreed_card")
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
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Final Agreed Price",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF065F46)
                )
              }
              Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFD1FAE5)
              ) {
                Text(
                  text = "MUTUALLY AGREED",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF047857),
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = "₹$negotiatedPrice",
              fontSize = 28.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF047857)
            )

            Text(
              text = "Both you and ${worker.name} agreed to ₹$negotiatedPrice. Zero middleman cuts.",
              fontSize = 12.sp,
              color = Color(0xFF065F46)
            )
          }
        }
      }
    }

    // 3. Negotiation Interactive Control Box
    if (!isPriceAgreed && !isNegotiationRejected) {
      item {
        HomezyCard(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Your Offer & Negotiation",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(8.dp))

          // Worker Counter Offer Notification
          if (isCounterActive && workerCounterOffer != null) {
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
                  text = "${worker.name} reviewed your request and proposed ₹$workerCounterOffer.",
                  fontSize = 11.sp,
                  color = Color(0xFF166534)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Button(
                    onClick = {
                      onPriceChanged(workerCounterOffer!!)
                      isPriceAgreed = true
                      negotiationHistory = negotiationHistory + NegotiationHistoryEntry(
                        id = "h-${negotiationHistory.size}",
                        actor = NegotiationActor.CUSTOMER,
                        actorName = "Customer",
                        actionType = NegotiationActionType.ACCEPTED,
                        amount = workerCounterOffer,
                        message = "Customer accepted worker's counter offer of ₹$workerCounterOffer.",
                        timestamp = "10:18 AM"
                      )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).testTag("booking_accept_counter_btn")
                  ) {
                    Text("Accept (₹$workerCounterOffer)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }

                  OutlinedButton(
                    onClick = {
                      isNegotiationRejected = true
                      negotiationHistory = negotiationHistory + NegotiationHistoryEntry(
                        id = "h-${negotiationHistory.size}",
                        actor = NegotiationActor.CUSTOMER,
                        actorName = "Customer",
                        actionType = NegotiationActionType.REJECTED,
                        amount = null,
                        message = "Customer declined counter offer.",
                        timestamp = "10:19 AM"
                      )
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(0.7f)
                  ) {
                    Text("Reject", fontSize = 12.sp)
                  }
                }

                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                  onClick = { isCounterActive = false },
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text("Continue Negotiation (Revise Offer)", fontSize = 11.sp)
                }
              }
            }
          } else {
            // Customer enters offer
            OutlinedTextField(
              value = offerInput,
              onValueChange = {
                offerInput = it.filter { ch -> ch.isDigit() }
                val num = offerInput.toIntOrNull()
                if (num != null) onPriceChanged(num)
              },
              label = { Text("Customer Offer (₹)") },
              leadingIcon = { Text("₹", fontWeight = FontWeight.Bold, color = HomezyPrimary) },
              modifier = Modifier.fillMaxWidth().testTag("booking_offer_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick picks
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              listOf(500, fairPriceSuggestion.suggestedMin, 550, fairPriceSuggestion.suggestedMax).distinct().forEach { pick ->
                SuggestionChip(
                  onClick = {
                    offerInput = pick.toString()
                    onPriceChanged(pick)
                  },
                  label = { Text("₹$pick", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                val current = offerInput.toIntOrNull() ?: negotiatedPrice
                onPriceChanged(current)
                negotiationHistory = negotiationHistory + NegotiationHistoryEntry(
                  id = "h-${negotiationHistory.size}",
                  actor = NegotiationActor.CUSTOMER,
                  actorName = "Customer",
                  actionType = NegotiationActionType.OFFER_SENT,
                  amount = current,
                  message = "Sent offer of ₹$current.",
                  timestamp = "10:16 AM"
                )
                // Worker responds with counter offer
                isCounterActive = true
                workerCounterOffer = 550
                negotiationHistory = negotiationHistory + NegotiationHistoryEntry(
                  id = "h-${negotiationHistory.size + 1}",
                  actor = NegotiationActor.WORKER,
                  actorName = worker.name,
                  actionType = NegotiationActionType.COUNTER_OFFER,
                  amount = 550,
                  message = "${worker.name} received offer and countered with ₹550.",
                  timestamp = "10:17 AM"
                )
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier.fillMaxWidth().testTag("booking_send_offer_btn")
            ) {
              Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Send Offer to Worker (₹$negotiatedPrice)", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 4. Negotiation History
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("booking_negotiation_history_card")) {
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
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Negotiation History",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
          }
          Text(
            text = "${negotiationHistory.size} updates",
            fontSize = 11.sp,
            color = HomezyTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          negotiationHistory.forEach { entry ->
            val (badgeColor, textColor) = when (entry.actor) {
              NegotiationActor.CUSTOMER -> Pair(Color(0xFFEFF6FF), Color(0xFF1D4ED8))
              NegotiationActor.WORKER -> Pair(Color(0xFFF0FDF4), Color(0xFF15803D))
              NegotiationActor.AI_ASSISTANT -> Pair(Color(0xFFFAF5FF), Color(0xFF7E22CE))
            }

            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(badgeColor),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = when (entry.actor) {
                    NegotiationActor.CUSTOMER -> Icons.Default.Person
                    NegotiationActor.WORKER -> Icons.Default.Engineering
                    NegotiationActor.AI_ASSISTANT -> Icons.Default.AutoAwesome
                  },
                  contentDescription = null,
                  tint = textColor,
                  modifier = Modifier.size(13.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = entry.actorName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                  )
                  Text(
                    text = entry.timestamp,
                    fontSize = 10.sp,
                    color = HomezyTextSecondary
                  )
                }
                Text(
                  text = entry.message,
                  fontSize = 11.sp,
                  color = HomezyText,
                  lineHeight = 15.sp
                )
              }
            }
          }
        }
      }
    }

    // 5. Confirm & Continue Button
    item {
      Spacer(modifier = Modifier.height(6.dp))
      HomezyButton(
        text = if (isPriceAgreed) "Confirm Agreed Price (₹$negotiatedPrice) & Review" else "Confirm Price (₹$negotiatedPrice) & Review Booking",
        onClick = onContinue,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        testTag = "booking_confirm_price_btn"
      )
    }
  }
}

@Composable
private fun BookingConfirmationStep(
  service: ServiceItem,
  worker: WorkerProfile,
  problemDescription: String,
  date: String,
  timeSlot: String,
  address: String,
  servicePrice: Int,
  workerEarnings: Int,
  cooperativeContribution: Int,
  platformFee: Int,
  totalPrice: Int,
  onProceedToPayment: () -> Unit
) {
  var showPriceDetails by remember { mutableStateOf(false) }

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Booking Confirmation",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Review your booking details before proceeding.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Clear Booking Summary Card: Service, Worker, Date, Time, Final Price
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth().testTag("booking_confirmation_card")) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Service", fontSize = 13.sp, color = HomezyTextSecondary)
          Text(service.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Worker", fontSize = 13.sp, color = HomezyTextSecondary)
          Text("${worker.name} (★ ${worker.rating})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = HomezyPrimary)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Date", fontSize = 13.sp, color = HomezyTextSecondary)
          Text(date, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = HomezyText)
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Time", fontSize = 13.sp, color = HomezyTextSecondary)
          Text(timeSlot, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = HomezyText)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = HomezyBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Final Price", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          Text("₹$totalPrice", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // "View price details" expandable toggle
        Row(
          modifier = Modifier
            .clickable { showPriceDetails = !showPriceDetails }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (showPriceDetails) "Hide price details ▲" else "View price details ▼",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }

        AnimatedVisibility(visible = showPriceDetails) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Service Price", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$servicePrice", fontSize = 12.sp, color = HomezyText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Worker Earnings", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$workerEarnings", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HomezySecondary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Cooperative Contribution", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$cooperativeContribution", fontSize = 12.sp, color = HomezyText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Platform Fee", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$platformFee", fontSize = 12.sp, color = HomezyText)
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      Button(
        onClick = onProceedToPayment,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("booking_proceed_payment_btn")
      ) {
        Text(
          text = "Confirm Booking",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }
  }
}

@Composable
private fun PaymentPrototypeStep(
  totalAmount: Int,
  workerEarnings: Int,
  cooperativeContribution: Int,
  platformFee: Int,
  selectedMethod: String,
  onSelectMethod: (String) -> Unit,
  upiProvider: String,
  onUpiProviderChange: (String) -> Unit,
  upiIdInput: String,
  onUpiIdChange: (String) -> Unit,
  cardNumber: String,
  onCardNumberChange: (String) -> Unit,
  cardExpiry: String,
  onCardExpiryChange: (String) -> Unit,
  cardCvv: String,
  onCardCvvChange: (String) -> Unit,
  isProcessing: Boolean,
  onPay: () -> Unit
) {
  var showPaymentBreakdown by remember { mutableStateOf(false) }

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Payment",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = "Select payment method to complete booking.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Payment Total & Expandable Breakdown
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Total:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "₹$totalAmount",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // "View payment breakdown" expandable toggle
        Row(
          modifier = Modifier
            .clickable { showPaymentBreakdown = !showPaymentBreakdown }
            .padding(vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (showPaymentBreakdown) "Hide payment breakdown ▲" else "View payment breakdown ▼",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }

        AnimatedVisibility(visible = showPaymentBreakdown) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(HomezySurfaceVariant)
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Worker Earnings", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$workerEarnings", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = HomezySecondary)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Cooperative Contribution", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$cooperativeContribution", fontSize = 12.sp, color = HomezyText)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text("Platform/Service Fee", fontSize = 12.sp, color = HomezyTextSecondary)
              Text("₹$platformFee", fontSize = 12.sp, color = HomezyText)
            }
          }
        }
      }
    }

    // Payment Method Selection: UPI, Card, Cash
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Payment Mode", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        Spacer(modifier = Modifier.height(8.dp))

        listOf("UPI", "Card", "Cash").forEach { method ->
          val isSelected = selectedMethod == method
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) HomezyPrimaryContainer else HomezyCard,
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (isSelected) HomezyPrimary else HomezyBorder
            ),
            onClick = { onSelectMethod(method) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              RadioButton(
                selected = isSelected,
                onClick = { onSelectMethod(method) },
                colors = RadioButtonDefaults.colors(selectedColor = HomezyPrimary)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = when (method) {
                  "UPI" -> "UPI (GPay / PhonePe / Paytm)"
                  "Card" -> "Credit / Debit Card"
                  "Cash" -> "Cash on Service Completion"
                  else -> method
                },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 13.sp,
                color = if (isSelected) HomezyPrimary else HomezyText
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
      if (isProcessing) {
        Box(
          modifier = Modifier.fillMaxWidth().padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = HomezyPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "Processing payment authorization...",
              fontSize = 13.sp,
              color = HomezyTextSecondary
            )
          }
        }
      } else {
        Button(
          onClick = onPay,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("simulate_payment_btn")
        ) {
          Text(
            text = "Pay Now",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
private fun PaymentConfirmationAndInvoiceStep(
  booking: BookingItem,
  transactionRef: String,
  paymentMethod: String,
  onProceedToTracker: () -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    // Confirmation Badge
    item {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(Color(0xFFDCFCE7)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF16A34A),
            modifier = Modifier.size(38.dp)
          )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Payment & Booking Confirmed!",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Text(
          text = "Ref: $transactionRef • Paid via $paymentMethod",
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )
      }
    }

    // DIGITAL INVOICE UI
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "HOMEZY FEDERATION",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = HomezyPrimary
            )
            Text(
              text = "Tax Invoice & Service Certificate",
              fontSize = 11.sp,
              color = HomezyTextSecondary
            )
          }
          HomezyBadge(
            text = "ORIGINAL",
            containerColor = HomezySurfaceVariant,
            contentColor = HomezyText
          )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = HomezyBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Invoice No.", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("INV-${booking.id}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Customer", fontSize = 12.sp, color = HomezyTextSecondary)
          Text(booking.customerName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = HomezyText)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Craftsperson", fontSize = 12.sp, color = HomezyTextSecondary)
          Text(booking.workerName, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = HomezyPrimary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Service Date", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("${booking.date} (${booking.timeSlot})", fontSize = 12.sp, color = HomezyText)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = HomezyBorder)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Transparent Cost Structure", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        Spacer(modifier = Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Service Price", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("₹${booking.price}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Worker Earnings", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("₹${booking.workerEarnings}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF16A34A))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Cooperative Contribution", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("₹${booking.cooperativeContribution}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text("Platform/Service Fee", fontSize = 12.sp, color = HomezyTextSecondary)
          Text("₹${booking.platformFee}", fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(10.dp))
        Divider(color = HomezyBorder)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Total Amount Paid", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
          Text("₹${booking.price}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = HomezyPrimary)
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(10.dp))
      HomezyButton(
        text = "Proceed to Service Progress & Completion →",
        onClick = onProceedToTracker,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        testTag = "proceed_to_tracker_btn"
      )
    }
  }
}

@Composable
private fun WorkCompletionAndVerificationStep(
  booking: BookingItem,
  worker: WorkerProfile,
  isWorkMarkedCompleteByWorker: Boolean,
  isCompletionVerifiedByCustomer: Boolean,
  isIssueReported: Boolean,
  reportedIssueReason: String,
  onWorkerMarkCompleted: () -> Unit,
  onCustomerVerifyCompleted: () -> Unit,
  onReportIssueClick: () -> Unit
) {
  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      Text(
        text = "Live Service Status",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "Track execution and verify work completion.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // Service Status Timeline
    item {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        Text("Service Execution Timeline", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = HomezyText)
        Spacer(modifier = Modifier.height(12.dp))

        TimelineItem(
          title = "Booking & Payment Confirmed",
          subtitle = "Assigned to ${worker.name}",
          isDone = true,
          isActive = false
        )
        TimelineItem(
          title = "Worker Dispatched & Arrived",
          subtitle = "Indiranagar cooperative service cluster",
          isDone = true,
          isActive = false
        )
        TimelineItem(
          title = "Service in Progress",
          subtitle = booking.problemDescription,
          isDone = isWorkMarkedCompleteByWorker,
          isActive = !isWorkMarkedCompleteByWorker
        )
        TimelineItem(
          title = "Work Completion & Customer Verification",
          subtitle = if (isCompletionVerifiedByCustomer) "Verified by Customer" else if (isWorkMarkedCompleteByWorker) "Pending Customer Verification" else "Awaiting worker finish",
          isDone = isCompletionVerifiedByCustomer,
          isActive = isWorkMarkedCompleteByWorker && !isCompletionVerifiedByCustomer
        )
      }
    }

    // WORKER SIMULATION CONTROL
    // The requirement states: "Worker can mark booking as completed."
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezySurfaceVariant
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Engineering,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Worker Action Simulation:",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = if (isWorkMarkedCompleteByWorker) {
            "✓ Worker (${worker.name}) has finished the service and marked the booking completed."
          } else {
            "Simulate the worker completing the task on-site to test the completion verification and rating unlock flow."
          },
          fontSize = 12.sp,
          color = HomezyTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))
        if (!isWorkMarkedCompleteByWorker) {
          HomezyButton(
            text = "Worker Action: Mark Work as Completed",
            onClick = onWorkerMarkCompleted,
            variant = ButtonVariant.OUTLINE,
            fullWidth = true,
            icon = Icons.Default.Check,
            testTag = "worker_mark_completed_btn"
          )
        } else {
          HomezyBadge(
            text = "Work Marked Completed by Worker",
            containerColor = Color(0xFFDCFCE7),
            contentColor = Color(0xFF166534)
          )
        }
      }
    }

    // COMPLETION VERIFICATION SECTION
    // The requirement states:
    // Then customer sees:
    // "Was the work completed successfully?"
    // Buttons:
    // Yes, Work Completed
    // Report an Issue
    // Only after successful completion verification should rating be enabled.
    if (isWorkMarkedCompleteByWorker) {
      item {
        HomezyCard(
          modifier = Modifier.fillMaxWidth(),
          elevation = 2.dp
        ) {
          Text(
            text = "Was the work completed successfully?",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Inspect the repairs. To protect worker livelihoods and ensure fair evaluations, rating is only unlocked after you verify work completion.",
            fontSize = 12.sp,
            color = HomezyTextSecondary,
            lineHeight = 17.sp
          )

          if (isIssueReported) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFFEF2F2),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = HomezyError, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text("Grievance Registered with Cooperative Committee", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HomezyError)
                  Text("Ombudsman is investigating. Rating is currently locked.", fontSize = 11.sp, color = HomezyTextSecondary)
                }
              }
            }
          } else {
            Spacer(modifier = Modifier.height(14.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              HomezyButton(
                text = "Report an Issue",
                onClick = onReportIssueClick,
                variant = ButtonVariant.OUTLINE,
                modifier = Modifier.weight(1f).testTag("customer_report_issue_btn")
              )

              HomezyButton(
                text = "Yes, Work Completed",
                onClick = onCustomerVerifyCompleted,
                variant = ButtonVariant.PRIMARY,
                modifier = Modifier.weight(1.3f).testTag("customer_verify_work_completed_btn")
              )
            }
          }
        }
      }
    } else {
      item {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = HomezySurfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = HomezyTextSecondary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Completion Verification & Rating Locked",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = HomezyText
              )
              Text(
                text = "Once the craftsperson finishes work, you can verify and unlock the 5-star rating.",
                fontSize = 11.sp,
                color = HomezyTextSecondary
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TimelineItem(
  title: String,
  subtitle: String,
  isDone: Boolean,
  isActive: Boolean
) {
  Row(
    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    verticalAlignment = Alignment.Top
  ) {
    Box(
      modifier = Modifier
        .size(22.dp)
        .clip(CircleShape)
        .background(
          when {
            isDone -> HomezyPrimary
            isActive -> HomezySecondary
            else -> HomezyBorder
          }
        ),
      contentAlignment = Alignment.Center
    ) {
      if (isDone) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(13.dp)
        )
      } else if (isActive) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color.White))
      }
    }

    Spacer(modifier = Modifier.width(10.dp))

    Column {
      Text(
        text = title,
        fontSize = 13.sp,
        fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
        color = if (isActive) HomezySecondary else if (isDone) HomezyText else HomezyTextSecondary
      )
      Text(
        text = subtitle,
        fontSize = 11.sp,
        color = HomezyTextSecondary
      )
    }
  }
}

@Composable
private fun RatingStep(
  worker: WorkerProfile,
  service: ServiceItem,
  ratingScore: Int,
  onRatingScoreChange: (Int) -> Unit,
  comment: String,
  onCommentChange: (String) -> Unit,
  selectedCompliments: Set<String>,
  onToggleCompliment: (String) -> Unit,
  onSubmitRating: () -> Unit
) {
  val compliments = listOf(
    "Punctual",
    "Expert Craftsperson",
    "Clean Work",
    "Polite & Respectful",
    "Fair Pricing",
    "Co-op Standard Warranty"
  )

  LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier.fillMaxSize()
  ) {
    item {
      // False-Rating Mitigation Concept Banner
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = HomezyPrimaryContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "False-Rating Mitigation Enforced",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = HomezyPrimary
            )
            Text(
              text = "Rating unlocked: Only verified customers who confirmed completion can submit ratings, preventing predatory negative reviews.",
              fontSize = 11.sp,
              color = HomezyTextSecondary,
              lineHeight = 15.sp
            )
          }
        }
      }
    }

    item {
      Text(
        text = "Rate Your Experience with ${worker.name}",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
      Text(
        text = "For ${service.name} completed at your home.",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
    }

    // 5-Star Rating Card
    item {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
      ) {
        Text(
          text = "Select Star Rating (1 to 5 Stars):",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(14.dp))

        // 5 Stars Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          (1..5).forEach { star ->
            IconButton(
              onClick = { onRatingScoreChange(star) },
              modifier = Modifier.size(48.dp).testTag("rating_star_$star")
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "$star Stars",
                tint = if (star <= ratingScore) Color(0xFFF59E0B) else Color(0xFFE2E8F0),
                modifier = Modifier.size(38.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = when (ratingScore) {
            1 -> "Poor - Did not meet standards"
            2 -> "Below Expectation"
            3 -> "Average Service"
            4 -> "Very Good & Professional"
            5 -> "Exceptional - High Quality Work!"
            else -> ""
          },
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyPrimary,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = HomezyBorder)
        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Compliments for Craftsperson:",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Compliments Wrap
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          compliments.chunked(2).forEach { rowCompliments ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              rowCompliments.forEach { comp ->
                val isSelected = selectedCompliments.contains(comp)
                Surface(
                  shape = RoundedCornerShape(16.dp),
                  color = if (isSelected) HomezyPrimaryContainer else HomezySurfaceVariant,
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) HomezyPrimary else HomezyBorder
                  ),
                  onClick = { onToggleCompliment(comp) },
                  modifier = Modifier.weight(1f)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                  ) {
                    if (isSelected) {
                      Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = HomezyPrimary,
                        modifier = Modifier.size(13.dp)
                      )
                      Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                      text = comp,
                      fontSize = 11.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) HomezyPrimary else HomezyText
                    )
                  }
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Review Comment
        Text(
          text = "Written Feedback / Comment:",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = comment,
          onValueChange = onCommentChange,
          placeholder = { Text("Write a comment about the service experience...") },
          modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .testTag("rating_comment_input"),
          maxLines = 4
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))
      HomezyButton(
        text = "Submit Verified Rating",
        onClick = onSubmitRating,
        variant = ButtonVariant.PRIMARY,
        fullWidth = true,
        icon = Icons.Default.RateReview,
        testTag = "submit_rating_btn"
      )
    }
  }
}

@Composable
private fun FinalSuccessStep(
  worker: WorkerProfile,
  service: ServiceItem,
  booking: BookingItem?,
  onViewBookings: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(72.dp)
        .clip(CircleShape)
        .background(HomezyPrimaryContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Stars,
        contentDescription = null,
        tint = HomezyPrimary,
        modifier = Modifier.size(44.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Verified Review Published!",
      fontSize = 22.sp,
      fontWeight = FontWeight.Bold,
      color = HomezyText,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Your verified feedback has been credited to ${worker.name}'s cooperative portfolio and transparent welfare standing.",
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      textAlign = TextAlign.Center,
      lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(24.dp))

    HomezyCard(modifier = Modifier.fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Booking ID", fontSize = 13.sp, color = HomezyTextSecondary)
        Text(booking?.id ?: "HMZ-COMPLETED", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Service", fontSize = 13.sp, color = HomezyTextSecondary)
        Text(service.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
      }
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Member Craftsperson", fontSize = 13.sp, color = HomezyTextSecondary)
        Text(worker.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = HomezyPrimary)
      }
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Status", fontSize = 13.sp, color = HomezyTextSecondary)
        Text("Completed & Customer Verified ✓", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    HomezyButton(
      text = "View in My Bookings",
      onClick = onViewBookings,
      variant = ButtonVariant.PRIMARY,
      fullWidth = true,
      testTag = "final_view_bookings_btn"
    )
  }
}
