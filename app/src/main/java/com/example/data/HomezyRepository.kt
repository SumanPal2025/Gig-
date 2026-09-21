package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.data.firebase.FirebaseService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

object HomezyRepository {
  private const val TAG = "HomezyRepository"
  private const val PREFS_NAME = "homezy_persistent_storage"
  private const val KEY_SESSION_USER = "key_session_user"
  private const val KEY_BOOKINGS = "key_bookings"
  private const val KEY_MESSAGES = "key_messages"
  private const val KEY_WORKERS = "key_workers"
  private const val KEY_IS_SEEDED = "key_is_seeded_v1"

  private var prefs: SharedPreferences? = null
  private val scope = CoroutineScope(Dispatchers.IO)

  // Reactive UI States
  var currentUser by mutableStateOf<AuthUser?>(null)
    private set

  var bookings by mutableStateOf<List<BookingItem>>(emptyList())
    private set

  var workers by mutableStateOf<List<WorkerProfile>>(emptyList())
    private set

  var messages by mutableStateOf<List<ChatMessage>>(emptyList())
    private set

  private val _isOnline = MutableStateFlow(true)
  val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

  // 15 Canonical, Consistent Workers
  val canonicalWorkers = listOf(
    WorkerProfile(
      id = "wrk_1",
      name = "Rahul Sharma",
      trade = "Electrician & AC Specialist",
      cooperativeId = "COOP-KA-0429",
      experienceYears = 8,
      rating = 4.92f,
      completedJobs = 642,
      hourlyRate = 250,
      distanceKm = 1.2,
      isVerified = true,
      isAvailable = true,
      skills = listOf("AC Deep Clean", "Wiring", "Appliance Install", "Inverter Setup"),
      phone = "+91 98765 43210",
      locationArea = "Indiranagar, Sector 2",
      fairAllocationScore = 96
    ),
    WorkerProfile(
      id = "wrk_2",
      name = "Sunita Devi",
      trade = "Master Plumber & Pipe Fitter",
      cooperativeId = "COOP-KA-0118",
      experienceYears = 6,
      rating = 4.95f,
      completedJobs = 514,
      hourlyRate = 220,
      distanceKm = 1.8,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Pipe Leakage", "Sanitary Fitting", "Motor Overhaul", "RO Service"),
      phone = "+91 98451 22334",
      locationArea = "Koramangala, 5th Block",
      fairAllocationScore = 94
    ),
    WorkerProfile(
      id = "wrk_3",
      name = "Ramesh Patel",
      trade = "Senior HVAC & AC Tech",
      cooperativeId = "COOP-KA-0382",
      experienceYears = 10,
      rating = 4.88f,
      completedJobs = 890,
      hourlyRate = 280,
      distanceKm = 2.4,
      isVerified = true,
      isAvailable = false,
      skills = listOf("Split AC Gas Refill", "Duct Cleaning", "Compressor Repair"),
      phone = "+91 97312 98765",
      locationArea = "HSR Layout, Sector 1",
      fairAllocationScore = 91
    ),
    WorkerProfile(
      id = "wrk_4",
      name = "Amit Kumar",
      trade = "Residential Electrician",
      cooperativeId = "COOP-KA-0715",
      experienceYears = 4,
      rating = 4.85f,
      completedJobs = 310,
      hourlyRate = 200,
      distanceKm = 3.1,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Switchboard Repair", "Ceiling Fan", "MCB Tripping"),
      phone = "+91 96111 88442",
      locationArea = "Domlur Layout",
      fairAllocationScore = 98
    ),
    WorkerProfile(
      id = "wrk_5",
      name = "Priya Nambiar",
      trade = "Electrician & Appliance Specialist",
      cooperativeId = "COOP-KA-0512",
      experienceYears = 5,
      rating = 4.96f,
      completedJobs = 430,
      hourlyRate = 240,
      distanceKm = 1.5,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Smart Home Wiring", "Inverter Fitting", "Safety Earthing"),
      phone = "+91 98450 11223",
      locationArea = "Indiranagar 100ft Rd",
      fairAllocationScore = 97
    ),
    WorkerProfile(
      id = "wrk_6",
      name = "Vikram Singh",
      trade = "Plumber & Sanitation Expert",
      cooperativeId = "COOP-KA-0294",
      experienceYears = 7,
      rating = 4.89f,
      completedJobs = 380,
      hourlyRate = 210,
      distanceKm = 2.0,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Tap Leakage", "Bathroom Drainage", "Water Tank Cleaning"),
      phone = "+91 97401 55667",
      locationArea = "Koramangala 4th Block",
      fairAllocationScore = 93
    ),
    WorkerProfile(
      id = "wrk_7",
      name = "Manoj Gowda",
      trade = "AC Service & Jet Clean Tech",
      cooperativeId = "COOP-KA-0833",
      experienceYears = 8,
      rating = 4.91f,
      completedJobs = 520,
      hourlyRate = 260,
      distanceKm = 1.9,
      isVerified = true,
      isAvailable = true,
      skills = listOf("AC Jet Cleaning", "Coil Descaling", "Gas Leak Testing"),
      phone = "+91 99002 44556",
      locationArea = "Bellandur Outer Ring Rd",
      fairAllocationScore = 95
    ),
    WorkerProfile(
      id = "wrk_8",
      name = "Farhan Akhtar",
      trade = "Master Plumber & Pipe Specialist",
      cooperativeId = "COOP-KA-0621",
      experienceYears = 6,
      rating = 4.93f,
      completedJobs = 460,
      hourlyRate = 230,
      distanceKm = 1.4,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Pipe Replacement", "Pressure Pump Repair", "Sink Clog Removal"),
      phone = "+91 98863 77889",
      locationArea = "HAL 2nd Stage",
      fairAllocationScore = 96
    ),
    WorkerProfile(
      id = "wrk_9",
      name = "Deepak Verma",
      trade = "Inverter AC & Refrigeration Specialist",
      cooperativeId = "COOP-KA-0901",
      experienceYears = 5,
      rating = 4.87f,
      completedJobs = 340,
      hourlyRate = 250,
      distanceKm = 3.5,
      isVerified = true,
      isAvailable = true,
      skills = listOf("PCB Diagnostics", "Inverter AC Install", "Gas Charging"),
      phone = "+91 97411 33221",
      locationArea = "Electronic City Phase 1",
      fairAllocationScore = 92
    ),
    WorkerProfile(
      id = "wrk_10",
      name = "Ananya Joshi",
      trade = "Smart Home Electrician",
      cooperativeId = "COOP-KA-0429",
      experienceYears = 4,
      rating = 4.90f,
      completedJobs = 280,
      hourlyRate = 230,
      distanceKm = 2.8,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Automation Setup", "LED Profiles", "Distribution Box"),
      phone = "+91 98220 33445",
      locationArea = "Whitefield Inner Circle",
      fairAllocationScore = 95
    ),
    WorkerProfile(
      id = "wrk_11",
      name = "Kavita Sundaram",
      trade = "Plumbing Systems & Drainage Specialist",
      cooperativeId = "COOP-KA-0118",
      experienceYears = 5,
      rating = 4.94f,
      completedJobs = 395,
      hourlyRate = 220,
      distanceKm = 2.1,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Concealed Piping", "Toilet Cistern Fix", "Kitchen Drainage"),
      phone = "+91 97390 12345",
      locationArea = "BTM Layout 2nd Stage",
      fairAllocationScore = 94
    ),
    WorkerProfile(
      id = "wrk_12",
      name = "Rajeshwari Iyer",
      trade = "Commercial AC & Chiller Technician",
      cooperativeId = "COOP-KA-0382",
      experienceYears = 9,
      rating = 4.97f,
      completedJobs = 710,
      hourlyRate = 300,
      distanceKm = 2.9,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Commercial Chillers", "Cassette AC", "Duct Balancing"),
      phone = "+91 98440 98765",
      locationArea = "JP Nagar 4th Phase",
      fairAllocationScore = 97
    ),
    WorkerProfile(
      id = "wrk_13",
      name = "Suresh Nayak",
      trade = "Emergency Electrical Responder",
      cooperativeId = "COOP-KA-0715",
      experienceYears = 7,
      rating = 4.88f,
      completedJobs = 490,
      hourlyRate = 240,
      distanceKm = 1.7,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Short Circuit Recovery", "Overload Protection", "Generator Interlock"),
      phone = "+91 99801 22334",
      locationArea = "Old Airport Road",
      fairAllocationScore = 93
    ),
    WorkerProfile(
      id = "wrk_14",
      name = "Mohammed Tariq",
      trade = "Pipe Rerouting & Sanitary Plumber",
      cooperativeId = "COOP-KA-0294",
      experienceYears = 6,
      rating = 4.86f,
      completedJobs = 410,
      hourlyRate = 210,
      distanceKm = 2.6,
      isVerified = true,
      isAvailable = true,
      skills = listOf("Water Meter Install", "Geyser Connection", "Pressure Valves"),
      phone = "+91 96321 77665",
      locationArea = "Jayanagar 9th Block",
      fairAllocationScore = 91
    ),
    WorkerProfile(
      id = "wrk_15",
      name = "Pooja Hegde",
      trade = "Appliance & Air Conditioning Tech",
      cooperativeId = "COOP-KA-0833",
      experienceYears = 3,
      rating = 4.82f,
      completedJobs = 195,
      hourlyRate = 210,
      distanceKm = 3.2,
      isVerified = false,
      isAvailable = true,
      skills = listOf("Window AC Clean", "Filter Washing", "Exhaust Systems"),
      phone = "+91 99160 55443",
      locationArea = "Marathahalli Bridge",
      fairAllocationScore = 89
    )
  )

  // 10 Canonical Customers
  val canonicalCustomers = listOf(
    DemoAccounts.CUSTOMER,
    AuthUser("cust_2", "Ananya Sen", "ananya.sen@gmail.com", AppRole.CUSTOMER, "+91 98101 23456", null, "AS"),
    AuthUser("cust_3", "Rohit Verma", "rohit.verma@outlook.com", AppRole.CUSTOMER, "+91 98223 45678", null, "RV"),
    AuthUser("cust_4", "Meera Krishnan", "meera.k@gmail.com", AppRole.CUSTOMER, "+91 99401 23456", null, "MK"),
    AuthUser("cust_5", "Siddharth Rao", "siddharth.rao@gmail.com", AppRole.CUSTOMER, "+91 97412 34567", null, "SR"),
    AuthUser("cust_6", "Kavita Nair", "kavita.nair@yahoo.com", AppRole.CUSTOMER, "+91 96112 34567", null, "KN"),
    AuthUser("cust_7", "Arun Cherian", "arun.cherian@gmail.com", AppRole.CUSTOMER, "+91 98861 23456", null, "AC"),
    AuthUser("cust_8", "Sneha Kulkarni", "sneha.k@hotmail.com", AppRole.CUSTOMER, "+91 97311 23456", null, "SK"),
    AuthUser("cust_9", "Vikramaditya Bose", "vikram.bose@gmail.com", AppRole.CUSTOMER, "+91 99001 23456", null, "VB"),
    AuthUser("cust_10", "Divya Menon", "divya.menon@gmail.com", AppRole.CUSTOMER, "+91 98450 98765", null, "DM")
  )

  // Canonical initial bookings in all statuses
  val canonicalInitialBookings = listOf(
    BookingItem(
      id = "HMZ-8450",
      serviceName = "Ceiling Fan & Heavy Appliance Wiring",
      category = "Electrician",
      customerName = "Priya Sundaram",
      customerAddress = "Flat 402, Green Glen Layout, Bellandur, Bengaluru",
      workerName = "Amit Kumar",
      workerPhone = "+91 96111 88442",
      date = "Tomorrow, 21 Sep",
      timeSlot = "02:00 PM - 03:00 PM",
      price = 399,
      status = BookingStatus.PENDING,
      problemDescription = "Mount two BLDC energy-saving fans and replace bedroom switchboard",
      workerEarnings = 379,
      cooperativeContribution = 12,
      platformFee = 8,
      paymentStatus = "Escrow Secured (UPI Authorized)",
      isInstaHelp = false
    ),
    BookingItem(
      id = "HMZ-8435",
      serviceName = "AC Deep Foam Jet Clean",
      category = "AC Service",
      customerName = "Priya Sundaram",
      customerAddress = "Flat 402, Green Glen Layout, Bellandur, Bengaluru",
      workerName = "Rahul Sharma",
      workerPhone = "+91 98765 43210",
      date = "Today, 20 Sep",
      timeSlot = "03:30 PM - 04:30 PM",
      price = 549,
      status = BookingStatus.ACCEPTED,
      problemDescription = "Split AC cooling drop and coil antibacterial foam wash",
      workerEarnings = 522,
      cooperativeContribution = 17,
      platformFee = 10,
      paymentStatus = "Escrow Secured (Paid via UPI)",
      isInstaHelp = false
    ),
    BookingItem(
      id = "HMZ-8421",
      serviceName = "AC Service & Jet Clean",
      category = "Cooling & Appliances",
      customerName = "Priya Sundaram",
      customerAddress = "Flat 402, Green Glen Layout, Bellandur, Bengaluru",
      workerName = "Rahul Sharma",
      workerPhone = "+91 98765 43210",
      date = "Today, 20 Sep",
      timeSlot = "11:30 AM - 12:30 PM",
      price = 499,
      status = BookingStatus.IN_PROGRESS,
      problemDescription = "Cooling coil iced over, low airflow and minor water drip",
      workerEarnings = 474,
      cooperativeContribution = 15,
      platformFee = 10,
      paymentStatus = "Escrow Secured (Paid via UPI)",
      isInstaHelp = false
    ),
    BookingItem(
      id = "HMZ-8419",
      serviceName = "Emergency Tap Pipe Leak",
      category = "Plumbing",
      customerName = "Rohit Verma",
      customerAddress = "Indiranagar 100ft Road, Bengaluru",
      workerName = "Sunita Devi",
      workerPhone = "+91 98451 22334",
      date = "Today, 20 Sep",
      timeSlot = "09:00 AM",
      price = 249,
      status = BookingStatus.COMPLETED,
      problemDescription = "High pressure pipe burst under kitchen sink",
      workerEarnings = 236,
      cooperativeContribution = 8,
      platformFee = 5,
      paymentStatus = "Paid & Settled ✓",
      isInstaHelp = true
    ),
    BookingItem(
      id = "HMZ-8390",
      serviceName = "Main MCB & Switchboard Repair",
      category = "Electrical",
      customerName = "Ananya Sen",
      customerAddress = "Koramangala 4th Block, Bengaluru",
      workerName = "Amit Kumar",
      workerPhone = "+91 96111 88442",
      date = "Yesterday",
      timeSlot = "04:00 PM",
      price = 349,
      status = BookingStatus.COMPLETED,
      problemDescription = "Main MCB tripping repeatedly on heavy load",
      workerEarnings = 331,
      cooperativeContribution = 11,
      platformFee = 7,
      paymentStatus = "Paid & Settled ✓",
      isInstaHelp = false
    ),
    BookingItem(
      id = "HMZ-8310",
      serviceName = "Kitchen Drain & Trap Overhaul",
      category = "Plumbing",
      customerName = "Meera Krishnan",
      customerAddress = "HSR Layout, Sector 2, Bengaluru",
      workerName = "Farhan Akhtar",
      workerPhone = "+91 98863 77889",
      date = "18 Sep",
      timeSlot = "10:00 AM",
      price = 299,
      status = BookingStatus.CANCELLED,
      problemDescription = "Customer rescheduled for renovation next month",
      workerEarnings = 0,
      cooperativeContribution = 0,
      platformFee = 0,
      paymentStatus = "Refunded (100% Co-op Policy) ✓",
      isInstaHelp = false
    ),
    BookingItem(
      id = "HMZ-8491",
      serviceName = "AC Deep Cleaning & Filter Check",
      category = "Cooling & Appliances",
      customerName = "Ananya Sen",
      customerAddress = "Koramangala 4th Block, Bengaluru",
      workerName = "Rahul Sharma",
      workerPhone = "+91 98765 43210",
      date = "Today",
      timeSlot = "5:30 PM",
      status = BookingStatus.IN_PROGRESS,
      price = 650,
      workerEarnings = 618,
      cooperativeContribution = 20,
      platformFee = 12,
      problemDescription = "Deep clean filters and refrigerant pressure test",
      paymentStatus = "Escrow Secured"
    ),
    BookingItem(
      id = "HMZ-8492",
      serviceName = "Bathroom Tap Leakage Repair",
      category = "Plumbing",
      customerName = "Rohit Verma",
      customerAddress = "Indiranagar 100ft Road, Bengaluru",
      workerName = "Sunita Devi",
      workerPhone = "+91 98451 22334",
      date = "Today",
      timeSlot = "3:00 PM",
      status = BookingStatus.COMPLETED,
      price = 450,
      workerEarnings = 428,
      cooperativeContribution = 14,
      platformFee = 8,
      problemDescription = "Ceramic cartridge spindle replacement",
      paymentStatus = "Paid & Settled ✓"
    )
  )

  fun initialize(context: Context) {
    if (prefs != null) return
    prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // 1. Initialize Firebase service
    FirebaseService.initialize(context)

    // 2. Setup network connectivity listener
    setupConnectivityListener(context)

    // 3. Load or seed data
    loadInitialData()
  }

  private fun setupConnectivityListener(context: Context) {
    try {
      val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
      if (cm != null) {
        val networkRequest = NetworkRequest.Builder()
          .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
          .build()

        cm.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
          override fun onAvailable(network: Network) {
            _isOnline.value = true
          }

          override fun onLost(network: Network) {
            _isOnline.value = false
          }
        })

        // Initial check
        val activeNet = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(activeNet)
        _isOnline.value = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
      }
    } catch (e: Exception) {
      Log.w(TAG, "Connectivity monitoring error: ${e.message}")
    }
  }

  private fun loadInitialData() {
    val p = prefs ?: return

    // Restore saved session user
    val userJson = p.getString(KEY_SESSION_USER, null)
    if (userJson != null) {
      try {
        currentUser = deserializeUser(JSONObject(userJson))
      } catch (e: Exception) {
        Log.w(TAG, "Failed to restore session user: ${e.message}")
      }
    }

    // Workers: Use persistent or canonical
    workers = canonicalWorkers

    // Bookings: Restore from SharedPreferences or seed canonical
    val bookingsJson = p.getString(KEY_BOOKINGS, null)
    if (bookingsJson != null) {
      try {
        val arr = JSONArray(bookingsJson)
        val list = mutableListOf<BookingItem>()
        for (i in 0 until arr.length()) {
          list.add(deserializeBooking(arr.getJSONObject(i)))
        }
        bookings = list
      } catch (e: Exception) {
        Log.w(TAG, "Failed to restore bookings from prefs: ${e.message}")
        bookings = canonicalInitialBookings
      }
    } else {
      bookings = canonicalInitialBookings
      saveBookingsToPrefs(bookings)
    }

    // Messages: Restore from SharedPreferences or initial
    val messagesJson = p.getString(KEY_MESSAGES, null)
    if (messagesJson != null) {
      try {
        val arr = JSONArray(messagesJson)
        val list = mutableListOf<ChatMessage>()
        for (i in 0 until arr.length()) {
          val obj = arr.getJSONObject(i)
          list.add(
            ChatMessage(
              id = obj.getString("id"),
              senderName = obj.getString("senderName"),
              isFromMe = obj.getBoolean("isFromMe"),
              text = obj.getString("text"),
              timestamp = obj.getString("timestamp")
            )
          )
        }
        messages = list
      } catch (e: Exception) {
        messages = SampleData.initialChatMessages
      }
    } else {
      messages = SampleData.initialChatMessages
      saveMessagesToPrefs(messages)
    }

    // Seed Firestore asynchronously if connected
    scope.launch {
      syncSeedToFirestore()
    }
  }

  private suspend fun syncSeedToFirestore() {
    if (!FirebaseService.isReady()) return

    // Sync Workers to Firestore collection "WorkerProfiles"
    canonicalWorkers.forEach { w ->
      val map = mapOf(
        "id" to w.id,
        "name" to w.name,
        "trade" to w.trade,
        "cooperativeId" to w.cooperativeId,
        "experienceYears" to w.experienceYears,
        "rating" to w.rating.toDouble(),
        "completedJobs" to w.completedJobs,
        "hourlyRate" to w.hourlyRate,
        "isVerified" to w.isVerified,
        "isAvailable" to w.isAvailable,
        "phone" to w.phone,
        "locationArea" to w.locationArea,
        "fairAllocationScore" to w.fairAllocationScore
      )
      FirebaseService.saveDocument(FirebaseService.COL_WORKER_PROFILES, w.id, map)
    }

    // Sync Services to Firestore collection "Services"
    SampleData.services.forEach { s ->
      val map = mapOf(
        "id" to s.id,
        "name" to s.name,
        "category" to s.category,
        "startingPrice" to s.startingPrice,
        "rating" to s.rating.toDouble(),
        "reviewCount" to s.reviewCount,
        "duration" to s.duration
      )
      FirebaseService.saveDocument(FirebaseService.COL_SERVICES, s.id, map)
    }

    // Sync Bookings to Firestore collection "Bookings"
    bookings.forEach { b ->
      val map = mapOf(
        "id" to b.id,
        "serviceName" to b.serviceName,
        "category" to b.category,
        "customerName" to b.customerName,
        "workerName" to b.workerName,
        "date" to b.date,
        "timeSlot" to b.timeSlot,
        "price" to b.price,
        "status" to b.status.name,
        "workerEarnings" to b.workerEarnings,
        "paymentStatus" to b.paymentStatus
      )
      FirebaseService.saveDocument(FirebaseService.COL_BOOKINGS, b.id, map)
    }

    // Sync Users to Firestore collection "Users"
    canonicalCustomers.forEach { u ->
      val map = mapOf(
        "id" to u.id,
        "name" to u.name,
        "email" to u.email,
        "role" to u.role.name,
        "phone" to u.phone
      )
      FirebaseService.saveDocument(FirebaseService.COL_USERS, u.id, map)
    }

    // Sync Welfare to Firestore collection "Welfare"
    SampleData.welfareBenefits.forEach { wb ->
      val map = mapOf(
        "id" to wb.id,
        "title" to wb.title,
        "category" to wb.category,
        "coverageAmount" to wb.coverageAmount,
        "status" to wb.status,
        "renewalDate" to wb.renewalDate
      )
      FirebaseService.saveDocument(FirebaseService.COL_WELFARE, wb.id, map)
    }
  }

  // Authentication: Real Firebase Auth + Session Persistence
  fun login(user: AuthUser) {
    currentUser = user
    val p = prefs ?: return
    try {
      p.edit().putString(KEY_SESSION_USER, serializeUser(user).toString()).apply()
    } catch (e: Exception) {
      Log.w(TAG, "Failed to save user session: ${e.message}")
    }

    // Real Firebase Auth attempt (only when valid remote configuration is active)
    if (FirebaseService.isReady()) {
      scope.launch {
        val auth = FirebaseService.getAuth()
        if (auth != null) {
          try {
            val defaultPass = "homezyPass123!"
            auth.signInWithEmailAndPassword(user.email, defaultPass)
          } catch (e: Exception) {
            // If user doesn't exist, create it in Firebase Auth
            try {
              auth.createUserWithEmailAndPassword(user.email, "homezyPass123!")
            } catch (e2: Exception) {
              Log.d(TAG, "FirebaseAuth info: ${e2.message}")
            }
          }
        }
      }
    }
  }

  fun logout() {
    currentUser = null
    prefs?.edit()?.remove(KEY_SESSION_USER)?.apply()
    if (FirebaseService.isReady()) {
      try {
        FirebaseService.getAuth()?.signOut()
      } catch (e: Exception) {
        Log.d(TAG, "FirebaseAuth signOut: ${e.message}")
      }
    }
  }

  // Bookings CRUD: Persist locally & sync with Firestore
  fun createBooking(booking: BookingItem) {
    val updated = listOf(booking) + bookings.filter { it.id != booking.id }
    bookings = updated
    saveBookingsToPrefs(updated)

    if (FirebaseService.isReady()) {
      scope.launch {
        val map = mapOf(
          "id" to booking.id,
          "serviceName" to booking.serviceName,
          "category" to booking.category,
          "customerName" to booking.customerName,
          "customerAddress" to booking.customerAddress,
          "workerName" to booking.workerName,
          "workerPhone" to booking.workerPhone,
          "date" to booking.date,
          "timeSlot" to booking.timeSlot,
          "price" to booking.price,
          "status" to booking.status.name,
          "problemDescription" to booking.problemDescription,
          "workerEarnings" to booking.workerEarnings,
          "paymentStatus" to booking.paymentStatus,
          "isInstaHelp" to booking.isInstaHelp
        )
        FirebaseService.saveDocument(FirebaseService.COL_BOOKINGS, booking.id, map)
      }
    }
  }

  fun updateBooking(updatedBooking: BookingItem) {
    val updatedList = bookings.map { if (it.id == updatedBooking.id) updatedBooking else it }
    bookings = updatedList
    saveBookingsToPrefs(updatedList)

    if (FirebaseService.isReady()) {
      scope.launch {
        val map = mapOf(
          "id" to updatedBooking.id,
          "status" to updatedBooking.status.name,
          "price" to updatedBooking.price,
          "workerEarnings" to updatedBooking.workerEarnings,
          "paymentStatus" to updatedBooking.paymentStatus,
          "rating" to (updatedBooking.rating ?: 0),
          "ratingComment" to (updatedBooking.ratingComment ?: "")
        )
        FirebaseService.saveDocument(FirebaseService.COL_BOOKINGS, updatedBooking.id, map)
      }
    }
  }

  // Messages CRUD: Persist locally & sync with Firestore
  fun addMessage(message: ChatMessage) {
    val updated = messages + message
    messages = updated
    saveMessagesToPrefs(updated)

    if (FirebaseService.isReady()) {
      scope.launch {
        val map = mapOf(
          "id" to message.id,
          "senderName" to message.senderName,
          "isFromMe" to message.isFromMe,
          "text" to message.text,
          "timestamp" to message.timestamp
        )
        FirebaseService.saveDocument(FirebaseService.COL_MESSAGES, message.id, map)
      }
    }
  }

  // Serialization Helpers
  private fun saveBookingsToPrefs(list: List<BookingItem>) {
    val p = prefs ?: return
    try {
      val arr = JSONArray()
      list.forEach { arr.put(serializeBooking(it)) }
      p.edit().putString(KEY_BOOKINGS, arr.toString()).apply()
    } catch (e: Exception) {
      Log.w(TAG, "Failed saving bookings to prefs: ${e.message}")
    }
  }

  private fun saveMessagesToPrefs(list: List<ChatMessage>) {
    val p = prefs ?: return
    try {
      val arr = JSONArray()
      list.forEach {
        val obj = JSONObject()
        obj.put("id", it.id)
        obj.put("senderName", it.senderName)
        obj.put("isFromMe", it.isFromMe)
        obj.put("text", it.text)
        obj.put("timestamp", it.timestamp)
        arr.put(obj)
      }
      p.edit().putString(KEY_MESSAGES, arr.toString()).apply()
    } catch (e: Exception) {
      Log.w(TAG, "Failed saving messages to prefs: ${e.message}")
    }
  }

  private fun serializeUser(user: AuthUser): JSONObject {
    return JSONObject().apply {
      put("id", user.id)
      put("name", user.name)
      put("email", user.email)
      put("role", user.role.name)
      put("phone", user.phone)
      put("trade", user.trade ?: "")
      put("avatarInitials", user.avatarInitials)
    }
  }

  private fun deserializeUser(obj: JSONObject): AuthUser {
    val roleStr = obj.optString("role", "CUSTOMER")
    val role = try {
      AppRole.valueOf(roleStr)
    } catch (e: Exception) {
      AppRole.CUSTOMER
    }
    return AuthUser(
      id = obj.optString("id", "usr_01"),
      name = obj.optString("name", "User"),
      email = obj.optString("email", "user@homezy.demo"),
      role = role,
      phone = obj.optString("phone", "+91 98451 90812"),
      trade = obj.optString("trade").takeIf { it.isNotEmpty() },
      avatarInitials = obj.optString("avatarInitials", "HM")
    )
  }

  private fun serializeBooking(b: BookingItem): JSONObject {
    return JSONObject().apply {
      put("id", b.id)
      put("serviceName", b.serviceName)
      put("category", b.category)
      put("customerName", b.customerName)
      put("customerAddress", b.customerAddress)
      put("workerName", b.workerName)
      put("workerPhone", b.workerPhone)
      put("date", b.date)
      put("timeSlot", b.timeSlot)
      put("price", b.price)
      put("status", b.status.name)
      put("problemDescription", b.problemDescription)
      put("workerEarnings", b.workerEarnings)
      put("cooperativeContribution", b.cooperativeContribution)
      put("platformFee", b.platformFee)
      put("paymentStatus", b.paymentStatus)
      put("isInstaHelp", b.isInstaHelp)
      put("rating", b.rating ?: -1)
      put("ratingComment", b.ratingComment ?: "")
    }
  }

  private fun deserializeBooking(obj: JSONObject): BookingItem {
    val statusStr = obj.optString("status", "PENDING")
    val status = try {
      BookingStatus.valueOf(statusStr)
    } catch (e: Exception) {
      BookingStatus.PENDING
    }
    val ratingVal = obj.optInt("rating", -1)
    val commentVal = obj.optString("ratingComment")

    return BookingItem(
      id = obj.optString("id", "HMZ-1000"),
      serviceName = obj.optString("serviceName", "Service"),
      category = obj.optString("category", "General"),
      customerName = obj.optString("customerName", "Customer"),
      customerAddress = obj.optString("customerAddress", "Address"),
      workerName = obj.optString("workerName", "Worker"),
      workerPhone = obj.optString("workerPhone", "+91 98765 43210"),
      date = obj.optString("date", "Today"),
      timeSlot = obj.optString("timeSlot", "10:00 AM"),
      price = obj.optInt("price", 399),
      status = status,
      problemDescription = obj.optString("problemDescription", "General service"),
      workerEarnings = obj.optInt("workerEarnings", 350),
      cooperativeContribution = obj.optInt("cooperativeContribution", 15),
      platformFee = obj.optInt("platformFee", 10),
      paymentStatus = obj.optString("paymentStatus", "Paid"),
      isInstaHelp = obj.optBoolean("isInstaHelp", false),
      rating = if (ratingVal >= 0) ratingVal else null,
      ratingComment = if (commentVal.isNotEmpty()) commentVal else null
    )
  }
}
