package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import com.example.data.*
import com.example.ui.admin.*
import com.example.ui.auth.*
import com.example.ui.components.*
import com.example.ui.customer.*
import com.example.ui.theme.*
import com.example.ui.worker.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    HomezyRepository.initialize(applicationContext)
    enableEdgeToEdge()
    setContent {
      HomezyTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .testTag("homezy_root_surface"),
          color = HomezyBackground
        ) {
          HomezyApp()
        }
      }
    }
  }
}

@Composable
fun HomezyApp(
  initialUser: AuthUser? = null,
  initialAuthScreen: AuthScreen = AuthScreen.WELCOME
) {
  val isOnline by HomezyRepository.isOnline.collectAsState()
  val savedUser = HomezyRepository.currentUser
  var currentUser by remember {
    mutableStateOf<AuthUser?>(
      initialUser ?: (if (initialAuthScreen == AuthScreen.WELCOME) savedUser else null)
    )
  }
  var authScreen by remember { mutableStateOf(initialAuthScreen) }
  var pendingRegistrationUser by remember { mutableStateOf<AuthUser?>(null) }

  var customerRoute by remember { mutableStateOf(CustomerNavDestination.HOME.route) }
  var workerRoute by remember { mutableStateOf(WorkerNavDestination.DASHBOARD.route) }
  var adminRoute by remember { mutableStateOf(AdminNavDestination.DASHBOARD.route) }

  var bookingFlowService by remember { mutableStateOf<ServiceItem?>(null) }
  var bookingFlowWorker by remember { mutableStateOf<WorkerProfile?>(null) }

  var searchQuery by remember { mutableStateOf("") }
  val bookings = HomezyRepository.bookings
  var jobOffers by remember { mutableStateOf(SampleData.incomingJobs) }
  var instaHelpRequests by remember {
    mutableStateOf(
      listOf(
        InstaHelpRequest(
          id = "INSTA-8842",
          service = "Electrical Emergency",
          location = "Salt Lake, Sector V, Kolkata",
          latitude = 22.5850,
          longitude = 88.4312,
          searchRadiusKm = 5.0,
          workerId = "wrk_rahul_das",
          workerName = "Rahul Das",
          workerSkill = "Electrician",
          workerDistanceKm = 2.1,
          workerEtaMinutes = 12,
          estimatedPrice = 449,
          estimatedEarnings = 425,
          urgency = "Immediate / Priority 1",
          status = InstaHelpStatus.PENDING,
          timestamp = "Just now",
          customerName = "Priya Sundaram",
          customerPhone = "+91 98451 90812"
        )
      )
    )
  }
  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  fun performLogin(user: AuthUser) {
    currentUser = user
    HomezyRepository.login(user)
    when (user.role) {
      AppRole.CUSTOMER -> customerRoute = CustomerNavDestination.HOME.route
      AppRole.WORKER -> workerRoute = WorkerNavDestination.DASHBOARD.route
      AppRole.ADMIN -> adminRoute = AdminNavDestination.DASHBOARD.route
    }
  }

  fun performLogout() {
    currentUser = null
    HomezyRepository.logout()
    authScreen = AuthScreen.WELCOME
    coroutineScope.launch {
      snackbarHostState.showSnackbar("Logged out successfully.")
    }
  }

  // Role-based Access Control (RBAC) Guard
  fun handleRoleAccess(targetRole: AppRole) {
    val user = currentUser ?: run {
      authScreen = AuthScreen.LOGIN
      return
    }
    if (user.role == targetRole) {
      // User is already in their authorized role
      return
    }

    // Unauthorized route access attempt:
    // Display Access Denied feedback and enforce redirect to their role home
    coroutineScope.launch {
      snackbarHostState.showSnackbar(
        message = "Access Denied: ${user.role.label}s cannot access ${targetRole.label} area. Redirected to your home.",
        duration = SnackbarDuration.Short
      )
    }

    when (user.role) {
      AppRole.CUSTOMER -> customerRoute = CustomerNavDestination.HOME.route
      AppRole.WORKER -> workerRoute = WorkerNavDestination.DASHBOARD.route
      AppRole.ADMIN -> adminRoute = AdminNavDestination.DASHBOARD.route
    }
  }

  val activeUser = currentUser

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
      Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
        if (!isOnline) {
          Surface(
            color = Color(0xFFFEF3C7),
            contentColor = Color(0xFF92400E),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("offline_banner")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = "Offline indicator",
                modifier = Modifier.size(16.dp),
                tint = Color(0xFFD97706)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "You're offline — changes are saved locally and will sync when reconnected",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
        if (activeUser != null) {
          HomezyAuthenticatedTopBar(
            currentUser = activeUser,
            onLogout = { performLogout() }
          )
          if (activeUser.role == AppRole.ADMIN) {
            AdminNavigationBar(
              currentRoute = adminRoute,
              onNavigate = { destination -> adminRoute = destination.route }
            )
          }
        }
      }
    },
    bottomBar = {
      if (activeUser != null) {
        when (activeUser.role) {
          AppRole.CUSTOMER -> {
            if (customerRoute != "/customer/booking-flow") {
              CustomerBottomBar(
                currentRoute = customerRoute,
                onNavigate = { destination -> customerRoute = destination.route }
              )
            }
          }
          AppRole.WORKER -> {
            WorkerBottomBar(
              currentRoute = workerRoute,
              onNavigate = { destination -> workerRoute = destination.route }
            )
          }
          AppRole.ADMIN -> {
            // Admin uses top responsive navigation tabs
          }
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (activeUser == null) {
        // Authentication Screens
        Crossfade(targetState = authScreen, label = "auth_screen_crossfade") { screen ->
          when (screen) {
            AuthScreen.WELCOME -> {
              WelcomeScreen(
                onGetStarted = { authScreen = AuthScreen.REGISTER },
                onLogin = { authScreen = AuthScreen.LOGIN },
                onQuickDemoLogin = { demoUser -> performLogin(demoUser) }
              )
            }
            AuthScreen.LOGIN -> {
              LoginScreen(
                onLoginSuccess = { user -> performLogin(user) },
                onNavigateToRegister = { authScreen = AuthScreen.REGISTER },
                onNavigateToForgotPassword = { authScreen = AuthScreen.FORGOT_PASSWORD },
                onNavigateBack = { authScreen = AuthScreen.WELCOME }
              )
            }
            AuthScreen.REGISTER -> {
              RegisterScreen(
                onNavigateToOtp = { pendingUser ->
                  pendingRegistrationUser = pendingUser
                  authScreen = AuthScreen.OTP_VERIFY
                },
                onNavigateToLogin = { authScreen = AuthScreen.LOGIN },
                onNavigateBack = { authScreen = AuthScreen.WELCOME }
              )
            }
            AuthScreen.OTP_VERIFY -> {
              OtpVerificationScreen(
                pendingUser = pendingRegistrationUser,
                onVerifySuccess = { verifiedUser -> performLogin(verifiedUser) },
                onNavigateBack = { authScreen = AuthScreen.REGISTER }
              )
            }
            AuthScreen.FORGOT_PASSWORD -> {
              ForgotPasswordScreen(
                onNavigateBack = { authScreen = AuthScreen.LOGIN },
                onNavigateToLogin = { authScreen = AuthScreen.LOGIN }
              )
            }
          }
        }
      } else {
        // Authenticated Role-based Screens
        Crossfade(targetState = activeUser.role, label = "role_crossfade") { role ->
          when (role) {
            AppRole.CUSTOMER -> {
              when (customerRoute) {
                CustomerNavDestination.HOME.route -> {
                  CustomerHomeScreen(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onServiceSelect = { service ->
                      bookingFlowService = service
                      bookingFlowWorker = null
                      customerRoute = "/customer/booking-flow"
                    },
                    onWorkerSelect = { worker ->
                      bookingFlowWorker = worker
                      bookingFlowService = SampleData.services.firstOrNull { s ->
                        val term = if (worker.trade.contains("AC")) "AC" else if (worker.trade.contains("Plumb")) "Plumb" else "Electr"
                        s.name.contains(term, ignoreCase = true)
                      } ?: SampleData.services.first()
                      customerRoute = "/customer/booking-flow"
                    },
                    onInstaHelpClick = {
                      customerRoute = CustomerNavDestination.INSTA_HELP.route
                    },
                    onSmartMatchClick = {
                      customerRoute = "/customer/smart-match"
                    }
                  )
                }

                CustomerNavDestination.SERVICES.route -> {
                  CustomerServicesScreen(
                    onBookService = { service ->
                      bookingFlowService = service
                      bookingFlowWorker = null
                      customerRoute = "/customer/booking-flow"
                    }
                  )
                }

                CustomerNavDestination.BOOKINGS.route -> {
                  CustomerBookingsScreen(
                    bookings = bookings,
                    onChatWithWorker = { booking ->
                      customerRoute = CustomerNavDestination.MESSAGES.route
                    },
                    onExploreServices = {
                      bookingFlowService = null
                      bookingFlowWorker = null
                      customerRoute = "/customer/booking-flow"
                    },
                    onUpdateBooking = { updated ->
                      HomezyRepository.updateBooking(updated)
                    }
                  )
                }

                "/customer/booking-flow" -> {
                  CustomerBookingFlowScreen(
                    initialService = bookingFlowService,
                    initialWorker = bookingFlowWorker,
                    currentUser = activeUser,
                    onBookingFinished = { newOrUpdatedBooking ->
                      HomezyRepository.createBooking(newOrUpdatedBooking)
                      customerRoute = CustomerNavDestination.BOOKINGS.route
                    },
                    onNavigateBack = {
                      customerRoute = CustomerNavDestination.HOME.route
                    }
                  )
                }

                "/customer/smart-match" -> {
                  CustomerSmartMatchScreen(
                    onBack = { customerRoute = CustomerNavDestination.HOME.route },
                    onSelectWorkerForBooking = { worker, serviceName ->
                      bookingFlowWorker = worker
                      bookingFlowService = SampleData.services.find { it.name.contains(serviceName.split(" ")[0], ignoreCase = true) } ?: SampleData.services.first()
                      customerRoute = "/customer/booking-flow"
                    }
                  )
                }

                "/customer/fair-price" -> {
                  AiFairPriceNegotiationScreen(
                    initialService = bookingFlowService?.name ?: "AC Service",
                    initialWorkerName = bookingFlowWorker?.name ?: "Rahul Das (Certified Senior)",
                    initialWorkerTrade = bookingFlowWorker?.trade ?: "AC Service Specialist",
                    initialCustomerBudget = 500,
                    onBack = { customerRoute = CustomerNavDestination.HOME.route },
                    onPriceAgreed = { agreedPrice ->
                      customerRoute = CustomerNavDestination.HOME.route
                    }
                  )
                }

                CustomerNavDestination.INSTA_HELP.route -> {
                  CustomerInstaHelpScreen(
                    onDispatchConfirmed = { req ->
                      instaHelpRequests = listOf(req) + instaHelpRequests.filterNot { it.id == req.id }
                      val instantBooking = BookingItem(
                        id = req.id,
                        serviceName = "Insta Help: ${req.service}",
                        category = "Insta Help Emergency",
                        customerName = activeUser.name,
                        customerAddress = "${req.location} (Mock GPS)",
                        workerName = req.workerName,
                        status = BookingStatus.IN_PROGRESS,
                        date = "Immediate Dispatch",
                        timeSlot = "ETA: ${req.workerEtaMinutes} mins",
                        price = req.estimatedPrice,
                        isInstaHelp = true,
                        cooperativeDividendApplied = 35
                      )
                      HomezyRepository.createBooking(instantBooking)
                      coroutineScope.launch {
                        snackbarHostState.showSnackbar("Insta Help dispatched! ${req.workerName} arriving in ~${req.workerEtaMinutes}m")
                      }
                      customerRoute = CustomerNavDestination.BOOKINGS.route
                    },
                    onNavigateBack = {
                      customerRoute = CustomerNavDestination.HOME.route
                    }
                  )
                }

                CustomerNavDestination.MESSAGES.route -> {
                  CustomerMessagesScreen()
                }

                CustomerNavDestination.PROFILE.route -> {
                  CustomerProfileScreen(
                    onSwitchToWorker = { handleRoleAccess(AppRole.WORKER) },
                    onSwitchToAdmin = { handleRoleAccess(AppRole.ADMIN) },
                    onLogout = { performLogout() },
                    currentUser = activeUser
                  )
                }

                else -> {
                  CustomerHomeScreen(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onServiceSelect = { service ->
                      bookingFlowService = service
                      bookingFlowWorker = null
                      customerRoute = "/customer/booking-flow"
                    },
                    onWorkerSelect = { worker ->
                      bookingFlowWorker = worker
                      bookingFlowService = SampleData.services.firstOrNull { s ->
                        val term = if (worker.trade.contains("AC")) "AC" else if (worker.trade.contains("Plumb")) "Plumb" else "Electr"
                        s.name.contains(term, ignoreCase = true)
                      } ?: SampleData.services.first()
                      customerRoute = "/customer/booking-flow"
                    },
                    onInstaHelpClick = { customerRoute = CustomerNavDestination.INSTA_HELP.route },
                    onSmartMatchClick = { customerRoute = "/customer/smart-match" },
                    onFairPriceClick = { customerRoute = "/customer/fair-price" }
                  )
                }
              }
            }

            AppRole.WORKER -> {
              when (workerRoute) {
                WorkerNavDestination.DASHBOARD.route -> {
                  WorkerDashboardScreen(
                    incomingJobs = jobOffers,
                    instaHelpRequests = instaHelpRequests,
                    onAcceptJob = { job ->
                      jobOffers = jobOffers.map {
                        if (it.id == job.id) it.copy(status = JobOfferStatus.ACCEPTED) else it
                      }
                      workerRoute = WorkerNavDestination.JOBS.route
                    },
                    onDeclineJob = { job ->
                      jobOffers = jobOffers.map {
                        if (it.id == job.id) it.copy(status = JobOfferStatus.DECLINED) else it
                      }
                    },
                    onAcceptInstaHelp = { req ->
                      instaHelpRequests = instaHelpRequests.map {
                        if (it.id == req.id) it.copy(status = InstaHelpStatus.ACCEPTED) else it
                      }
                      coroutineScope.launch {
                        snackbarHostState.showSnackbar("Accepted Insta Help for ${req.service}! Customer notified.")
                      }
                      workerRoute = WorkerNavDestination.JOBS.route
                    },
                    onDeclineInstaHelp = { req ->
                      instaHelpRequests = instaHelpRequests.map {
                        if (it.id == req.id) it.copy(status = InstaHelpStatus.DECLINED) else it
                      }
                      coroutineScope.launch {
                        snackbarHostState.showSnackbar("Declined emergency request.")
                      }
                    },
                    onNavigateToFairAllocation = {
                      workerRoute = WorkerNavDestination.FAIR_ALLOCATION.route
                    }
                  )
                }

                WorkerNavDestination.JOBS.route -> {
                  WorkerJobsScreen()
                }

                WorkerNavDestination.FAIR_ALLOCATION.route -> {
                  WorkerFairAllocationScreen(
                    onBack = { workerRoute = WorkerNavDestination.DASHBOARD.route }
                  )
                }

                "/worker/fair-price" -> {
                  AiFairPriceNegotiationScreen(
                    initialService = "AC Service",
                    initialWorkerName = "Rahul Das (Certified Senior)",
                    initialWorkerTrade = "AC Service Specialist",
                    initialCustomerBudget = 500,
                    onBack = { workerRoute = WorkerNavDestination.DASHBOARD.route },
                    onPriceAgreed = { agreedPrice ->
                      workerRoute = WorkerNavDestination.DASHBOARD.route
                    }
                  )
                }

                WorkerNavDestination.EARNINGS.route -> {
                  WorkerEarningsScreen()
                }

                WorkerNavDestination.WELFARE.route -> {
                  WorkerWelfareScreen()
                }

                WorkerNavDestination.PROFILE.route -> {
                  WorkerProfileScreen(
                    onSwitchToCustomer = { handleRoleAccess(AppRole.CUSTOMER) },
                    onSwitchToAdmin = { handleRoleAccess(AppRole.ADMIN) },
                    onLogout = { performLogout() },
                    currentUser = activeUser
                  )
                }

                else -> {
                  WorkerDashboardScreen(
                    incomingJobs = jobOffers,
                    instaHelpRequests = instaHelpRequests,
                    onAcceptJob = { workerRoute = WorkerNavDestination.JOBS.route },
                    onDeclineJob = {},
                    onAcceptInstaHelp = { workerRoute = WorkerNavDestination.JOBS.route },
                    onDeclineInstaHelp = {},
                    onNavigateToFairAllocation = { workerRoute = WorkerNavDestination.FAIR_ALLOCATION.route },
                    onNavigateToFairPrice = { workerRoute = "/worker/fair-price" }
                  )
                }
              }
            }

            AppRole.ADMIN -> {
              when (adminRoute) {
                AdminNavDestination.DASHBOARD.route -> {
                  AdminDashboardScreen(
                    onNavigateToSection = { route -> adminRoute = route }
                  )
                }
                AdminNavDestination.WORKERS.route -> {
                  AdminWorkersScreen()
                }
                AdminNavDestination.BOOKINGS.route -> {
                  AdminBookingsScreen()
                }
                AdminNavDestination.FAIR_ALLOCATION.route -> {
                  AdminFairAllocationScreen()
                }
                AdminNavDestination.WELFARE.route -> {
                  AdminWelfareScreen()
                }
                AdminNavDestination.AI_INSIGHTS.route -> {
                  AdminAiInsightsScreen()
                }
                AdminNavDestination.PAYMENTS.route -> {
                  AdminPaymentsScreen()
                }
                AdminNavDestination.REPORTS.route -> {
                  AdminReportsScreen()
                }
                AdminNavDestination.SETTINGS.route -> {
                  AdminSettingsScreen(
                    onSwitchToCustomer = { handleRoleAccess(AppRole.CUSTOMER) },
                    onSwitchToWorker = { handleRoleAccess(AppRole.WORKER) },
                    onLogout = { performLogout() },
                    currentUser = activeUser
                  )
                }
                else -> {
                  AdminDashboardScreen(
                    onNavigateToSection = { route -> adminRoute = route }
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "HOMEZY: Trusted Local Services. Fair Opportunities. ($name)", modifier = modifier)
}

