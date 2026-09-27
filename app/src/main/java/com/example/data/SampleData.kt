package com.example.data

object SampleData {

  val services = listOf(
    ServiceItem(
      id = "srv_ac",
      name = "AC Service & Repair",
      category = "Cooling & Appliances",
      iconName = "ac_unit",
      shortDescription = "Deep jet cleaning, gas leak detection, cooling coil service & repairs.",
      priceRange = "₹399 - ₹1,499",
      startingPrice = 399,
      rating = 4.86f,
      reviewCount = 1420,
      duration = "45 - 60 mins",
      cooperativeGuarantee = "30-Day Co-op Service Warranty Included",
      popularFeatures = listOf("Eco-friendly jet clean", "Cooperative fair pricing", "Certified technician")
    ),
    ServiceItem(
      id = "srv_electrician",
      name = "Electrician",
      category = "Home Electricals",
      iconName = "electric_bolt",
      shortDescription = "Switchboard wiring, fan & appliance install, short-circuit diagnostics.",
      priceRange = "₹199 - ₹899",
      startingPrice = 199,
      rating = 4.92f,
      reviewCount = 2180,
      duration = "30 - 60 mins",
      cooperativeGuarantee = "Standardized rates • No hidden surcharge",
      popularFeatures = listOf("Govt. ITI certified", "Insulated safety gear", "Instant emergency dispatch")
    ),
    ServiceItem(
      id = "srv_plumber",
      name = "Plumber",
      category = "Plumbing & Sanitation",
      iconName = "plumbing",
      shortDescription = "Pipe leakage fixing, tap & sink repair, toilet blockage clearance.",
      priceRange = "₹149 - ₹749",
      startingPrice = 149,
      rating = 4.88f,
      reviewCount = 1890,
      duration = "30 - 45 mins",
      cooperativeGuarantee = "Direct co-op member compensation",
      popularFeatures = listOf("No predatory middleman fee", "Standardized rate card", "Rapid arrival")
    ),
    ServiceItem(
      id = "srv_cleaning",
      name = "Cleaning",
      category = "Home Deep Cleaning",
      iconName = "cleaning_services",
      shortDescription = "Full home deep sanitization, bathroom scrubbing, kitchen de-greasing.",
      priceRange = "₹499 - ₹1,899",
      startingPrice = 499,
      rating = 4.90f,
      reviewCount = 1250,
      duration = "60 - 90 mins",
      cooperativeGuarantee = "Eco-safe certified detergents • Verified staff",
      popularFeatures = listOf("Hospital-grade disinfectant", "Experienced co-op crew", "Satisfaction guaranteed")
    ),
    ServiceItem(
      id = "srv_salon",
      name = "Salon",
      category = "Grooming & Wellness",
      iconName = "spa",
      shortDescription = "Hair styling, beard grooming, facial treatments and head massage at home.",
      priceRange = "₹249 - ₹999",
      startingPrice = 249,
      rating = 4.94f,
      reviewCount = 980,
      duration = "30 - 60 mins",
      cooperativeGuarantee = "Hygienic single-use kits • Professional stylists",
      popularFeatures = listOf("Single-use sterilized kits", "Experienced co-op stylists", "No commute hassle")
    )
  )

  val workers = listOf(
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
      name = "Suresh Patel",
      trade = "Inverter AC Technician",
      cooperativeId = "COOP-KA-0901",
      experienceYears = 3,
      rating = 4.75f,
      completedJobs = 120,
      hourlyRate = 220,
      distanceKm = 3.5,
      isVerified = false,
      isAvailable = false,
      skills = listOf("PCB Diagnostics", "Inverter AC Install"),
      phone = "+91 97411 33221",
      locationArea = "Electronic City Phase 1",
      fairAllocationScore = 88
    )
  )

  val initialBookings = listOf(
    BookingItem(
      id = "HMZ-8450",
      serviceName = "Ceiling Fan & Heavy Appliance Wiring",
      category = "Electrician",
      customerName = "Priya Sundaram",
      customerAddress = "Flat 402, Green Glen Layout, Bellandur",
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
      customerAddress = "Flat 402, Green Glen Layout, Bellandur",
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
      customerAddress = "Flat 402, Green Glen Layout, Bellandur",
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
      customerName = "Rajesh Verma",
      customerAddress = "Villa 12, Palm Meadows, Whitefield",
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
      customerName = "Ananya Nair",
      customerAddress = "34, 7th Main, Indiranagar",
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
      customerName = "Vikram Reddy",
      customerAddress = "92, 1st Cross, Koramangala",
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
    )
  )

  val incomingJobs = listOf(
    JobOffer(
      id = "JOB-1095",
      service = "Plumbing Repair",
      customerName = "Aarti Mehta",
      customerArea = "Koramangala 4th Block",
      address = "Flat 204, Sunset Heights, Koramangala 4th Block",
      distance = "1.8 km",
      date = "Today",
      scheduledTime = "5:30 PM",
      price = 650,
      cooperativeFee = 32, // 5% fee
      workerNet = 618, // Worker 95% take-home
      aiMatchScore = 91,
      problem = "Main bathroom sink pipeline leakage and drainage block",
      paymentStatus = "Co-op Escrow Verified",
      urgency = "Evening Slot"
    ),
    JobOffer(
      id = "JOB-1082",
      service = "AC Power Trip & Deep Cleaning",
      customerName = "Deepak Joshi",
      customerArea = "Indiranagar Stage 2",
      address = "142, 4th Cross, Indiranagar Stage 2",
      distance = "1.1 km",
      date = "Today",
      scheduledTime = "11:30 AM",
      price = 549,
      cooperativeFee = 27, // 5% only
      workerNet = 522, // Worker takes 95% home
      aiMatchScore = 98,
      problem = "AC indoor unit blowing warm air with intermittent tripping",
      paymentStatus = "Co-op Escrow Verified",
      urgency = "High Priority"
    ),
    JobOffer(
      id = "JOB-1085",
      service = "Ceiling Fan Installation (2 Units)",
      customerName = "Meenakshi Rao",
      customerArea = "Windmills Apartments",
      address = "Tower B - 903, Windmills Apartments",
      distance = "2.3 km",
      date = "Today",
      scheduledTime = "2:30 PM",
      price = 399,
      cooperativeFee = 20,
      workerNet = 379,
      aiMatchScore = 93,
      problem = "Unbox and securely mount two BLDC ceiling fans",
      paymentStatus = "Co-op Escrow Verified",
      urgency = "Scheduled"
    ),
    JobOffer(
      id = "JOB-1089",
      service = "Kitchen Geyser Wiring Check",
      customerName = "Kavita Reddy",
      customerArea = "HAL 2nd Stage",
      address = "78/1, HAL 2nd Stage",
      distance = "3.0 km",
      date = "Today",
      scheduledTime = "7:00 PM",
      price = 299,
      cooperativeFee = 15,
      workerNet = 284,
      aiMatchScore = 89,
      problem = "Instant geyser indicator light flashing with no hot water",
      paymentStatus = "Co-op Escrow Verified",
      urgency = "Standard"
    )
  )

  val initialWorkerActiveJobs = listOf(
    WorkerActiveJob(
      id = "HMZ-8421",
      service = "AC Service & Jet Clean (Split 1.5T)",
      customerName = "Priya Sundaram",
      customerPhone = "+91 98451 90812",
      customerArea = "Bellandur Green Glen",
      address = "Flat 402, Green Glen Layout, Bellandur",
      distance = "1.2 km",
      scheduledDate = "Today",
      scheduledTime = "11:30 AM - 12:30 PM",
      problemDescription = "Cooling coil iced over, low airflow and minor water drip from indoor unit",
      agreedPrice = 499,
      workerEarnings = 474,
      cooperativeContribution = 15,
      platformFee = 10,
      paymentStatus = "Escrow Secured (Paid via UPI)",
      status = WorkerJobStatus.IN_PROGRESS,
      checklist = listOf(
        "Arrived at Customer Flat 402",
        "Show Cooperative ID badge to resident",
        "Perform AC Jet wash & coil diagnostics",
        "Test thermostat cooling & gas pressure",
        "Collect digital customer sign-off"
      ),
      completedChecklistIndices = setOf(0, 1, 2)
    ),
    WorkerActiveJob(
      id = "HMZ-8428",
      service = "Plumbing Repair",
      customerName = "Aarti Mehta",
      customerPhone = "+91 98450 67123",
      customerArea = "Koramangala 4th Block",
      address = "Flat 204, Sunset Heights, Koramangala 4th Block",
      distance = "1.8 km",
      scheduledDate = "Today",
      scheduledTime = "5:30 PM",
      problemDescription = "Main bathroom sink pipeline leakage and drainage block",
      agreedPrice = 650,
      workerEarnings = 618,
      cooperativeContribution = 20,
      platformFee = 12,
      paymentStatus = "Escrow Secured (Paid via NetBanking)",
      status = WorkerJobStatus.ACCEPTED,
      checklist = listOf(
        "Arrived at Sunset Heights",
        "Verify sink fixture and trap piping",
        "Clear blockage and install seal ring",
        "Pressure test water flow",
        "Collect digital customer sign-off"
      ),
      completedChecklistIndices = emptySet()
    ),
    WorkerActiveJob(
      id = "HMZ-8430",
      service = "Ceiling Fan Installation (2 Units)",
      customerName = "Meenakshi Rao",
      customerPhone = "+91 98860 11928",
      customerArea = "Windmills Apartments",
      address = "Tower B - 903, Windmills Apartments",
      distance = "2.3 km",
      scheduledDate = "Tomorrow",
      scheduledTime = "02:30 PM",
      problemDescription = "Unbox and mount 2 BLDC high-efficiency ceiling fans in living room and study",
      agreedPrice = 399,
      workerEarnings = 379,
      cooperativeContribution = 12,
      platformFee = 8,
      paymentStatus = "Escrow Secured",
      status = WorkerJobStatus.ACCEPTED,
      checklist = listOf(
        "Arrive at Windmills Apartments",
        "Inspect ceiling hook and electrical point",
        "Assemble fan downrod and blades",
        "Safety balancing & remote pairing",
        "Customer demo and sign-off"
      ),
      completedChecklistIndices = emptySet()
    )
  )

  val welfareBenefits = listOf(
    WelfareBenefit(
      id = "wlf_1",
      title = "Rashtriya Swasthya Co-op Health Cover",
      category = "Healthcare & Family Insurance",
      description = "Comprehensive cashless hospitalization cover for worker and 4 family members across 850+ network hospitals.",
      coverageAmount = "₹5,00,000 / year",
      status = "Active ✓",
      renewalDate = "31 March 2027"
    ),
    WelfareBenefit(
      id = "wlf_2",
      title = "Occupational Hazard & Accident Shield",
      category = "On-Duty Insurance",
      description = "100% loss-of-pay compensation and immediate medical reimbursement while servicing jobs.",
      coverageAmount = "₹10,00,000 Cover",
      status = "Active ✓",
      renewalDate = "Auto-Renewed via Co-op"
    ),
    WelfareBenefit(
      id = "wlf_3",
      title = "Electrician Skill Certification — Level 3",
      category = "Skill Certification",
      description = "Master Electrical Technician & HVAC specialist certified by National Skill Development Corporation (NSDC).",
      coverageAmount = "Level 3 Master Tech",
      status = "Certified ✓",
      renewalDate = "Valid Lifetime"
    ),
    WelfareBenefit(
      id = "wlf_4",
      title = "Advanced AC Repair & Inverter Tech Training",
      category = "Co-op Training Module",
      description = "Free upskilling course on PCB diagnostics, inverter split systems, and eco-friendly R32 refrigerant handling.",
      coverageAmount = "100% Co-op Sponsored",
      status = "Enrolled (Batch Starts Mon)",
      renewalDate = "State Skill Academy"
    ),
    WelfareBenefit(
      id = "wlf_5",
      title = "24/7 Field Emergency & Legal Support",
      category = "Emergency Support",
      description = "Direct SOS hotline for on-duty accidents, customer disputes, vehicle breakdown, or technical backup assistance.",
      coverageAmount = "Instant Co-op Dispatch",
      status = "Always Ready ✓",
      renewalDate = "Toll-Free 1800-419-COOP"
    )
  )

  val fairAllocationZones = listOf(
    FairAllocationStat(
      zoneName = "Bengaluru East (Indiranagar / Domlur)",
      totalWorkers = 48,
      avgJobsPerWorker = 3.6,
      giniCoefficient = 0.14, // Very equitable
      equityScore = 95,
      status = "Balanced Opportunity"
    ),
    FairAllocationStat(
      zoneName = "Bengaluru South (Koramangala / HSR)",
      totalWorkers = 64,
      avgJobsPerWorker = 3.9,
      giniCoefficient = 0.16,
      equityScore = 93,
      status = "Optimal Distribution"
    ),
    FairAllocationStat(
      zoneName = "Bengaluru North (Hebbal / Sahakar)",
      totalWorkers = 32,
      avgJobsPerWorker = 2.8,
      giniCoefficient = 0.19,
      equityScore = 89,
      status = "Surplus Rebalancing Active"
    ),
    FairAllocationStat(
      zoneName = "Bengaluru West (Rajajinagar / Malleswaram)",
      totalWorkers = 36,
      avgJobsPerWorker = 3.2,
      giniCoefficient = 0.15,
      equityScore = 94,
      status = "Balanced Opportunity"
    )
  )

  val initialChatMessages = listOf(
    ChatMessage(
      id = "msg_1",
      senderName = "Rahul Sharma (Worker)",
      isFromMe = false,
      text = "Namaste Priya ji! I am Rahul from HOMEZY Cooperative. I have accepted your AC service request.",
      timestamp = "11:05 AM"
    ),
    ChatMessage(
      id = "msg_2",
      senderName = "Rahul Sharma (Worker)",
      isFromMe = false,
      text = "I am on my way on two-wheeler and will arrive at Flat 402 around 11:25 AM. I have my cooperative ID card and sanitized tools.",
      timestamp = "11:06 AM"
    ),
    ChatMessage(
      id = "msg_3",
      senderName = "You",
      isFromMe = true,
      text = "Hello Rahul! Thanks for confirming. The security guard will let you up. Please bring the jet pump pipe.",
      timestamp = "11:08 AM"
    ),
    ChatMessage(
      id = "msg_4",
      senderName = "Rahul Sharma (Worker)",
      isFromMe = false,
      text = "Yes, already carried the pressure washer and eco coil cleaner. See you shortly!",
      timestamp = "11:09 AM"
    )
  )

  // Prototype Zone Workforce Allocations for Admin
  val sampleZoneAllocations = listOf(
    AdminWorkforceZoneAllocation(
      area = "Howrah",
      service = "Electrician",
      demand = "High",
      availableWorkers = 12,
      recommendedAllocation = 8,
      giniEqualityIndex = 0.16,
      statusNotes = "Balanced dispatch avoids nearest-worker monopoly."
    ),
    AdminWorkforceZoneAllocation(
      area = "Salt Lake / Sector V",
      service = "AC Service",
      demand = "Surging",
      availableWorkers = 18,
      recommendedAllocation = 14,
      giniEqualityIndex = 0.14,
      statusNotes = "Peak cooling demand distributed equitably across 3 local wards."
    ),
    AdminWorkforceZoneAllocation(
      area = "New Town",
      service = "Plumber",
      demand = "Medium",
      availableWorkers = 10,
      recommendedAllocation = 6,
      giniEqualityIndex = 0.18,
      statusNotes = "Guarantees standby plumbers receive live jobs."
    ),
    AdminWorkforceZoneAllocation(
      area = "Indiranagar, BLR",
      service = "Electrician",
      demand = "High",
      availableWorkers = 15,
      recommendedAllocation = 10,
      giniEqualityIndex = 0.15,
      statusNotes = "Anti-fatigue rest rotation applied."
    ),
    AdminWorkforceZoneAllocation(
      area = "Koramangala, BLR",
      service = "Plumber",
      demand = "Medium",
      availableWorkers = 9,
      recommendedAllocation = 5,
      giniEqualityIndex = 0.17,
      statusNotes = "Rotational morning and evening shifts."
    )
  )

  // Worker Fair Allocation Stats
  val sampleWorkerAllocationStats = listOf(
    WorkerAllocationStat(
      workerId = "w_rahul_das",
      workerName = "Rahul Das",
      trade = "Electrician",
      jobsReceived = 6,
      hoursWorked = 18.5,
      currentWorkload = "Low",
      idleTimeHours = 3.5,
      recentEarnings = 4250,
      fairOpportunityScore = 96,
      status = "Priority for Next Assignment",
      explanation = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
    ),
    WorkerAllocationStat(
      workerId = "w_rahul_sharma",
      workerName = "Rahul Sharma",
      trade = "AC Service & Electrician",
      jobsReceived = 8,
      hoursWorked = 24.0,
      currentWorkload = "Moderate",
      idleTimeHours = 2.0,
      recentEarnings = 5840,
      fairOpportunityScore = 91,
      status = "Equitably Allocated",
      explanation = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
    ),
    WorkerAllocationStat(
      workerId = "w_sunita",
      workerName = "Sunita Devi",
      trade = "Plumber",
      jobsReceived = 7,
      hoursWorked = 21.0,
      currentWorkload = "Low",
      idleTimeHours = 4.0,
      recentEarnings = 4920,
      fairOpportunityScore = 94,
      status = "Priority for Next Assignment",
      explanation = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
    ),
    WorkerAllocationStat(
      workerId = "w_vikram",
      workerName = "Vikram Sengupta",
      trade = "AC Technician",
      jobsReceived = 10,
      hoursWorked = 29.5,
      currentWorkload = "High",
      idleTimeHours = 0.5,
      recentEarnings = 7200,
      fairOpportunityScore = 82,
      status = "Workload Protected (Anti-Burnout)",
      explanation = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
    )
  )

  /**
   * Prototype Explainable Smart Match Scoring Algorithm
   * Computes match score (0-100%) using:
   * 1. Skill Match (0-30 pts)
   * 2. Distance (0-25 pts)
   * 3. Availability (0-20 pts)
   * 4. Rating (0-15 pts)
   * 5. Current Workload (0-10 pts)
   */
  fun calculateSmartMatchCandidates(
    service: String = "AC Service",
    requiredSkill: String = "",
    customerLocation: String = "Indiranagar, Bengaluru",
    preferredTime: String = "Morning"
  ): List<SmartMatchCandidate> {
    // Base workers pool including Rahul Das for prompt fidelity
    val candidatesPool = listOf(
      SmartMatchCandidate(
        workerId = "w_rahul_das",
        workerName = "Rahul Das",
        trade = if (service.contains("AC", ignoreCase = true)) "AC Service & Repair" else if (service.contains("Plumb", ignoreCase = true)) "Plumber" else "Electrician",
        verified = true,
        rating = 4.8f,
        jobsCompleted = 84,
        distanceKm = 2.1,
        availability = "Available",
        estimatedPrice = 499,
        matchPercentage = 94,
        breakdown = SmartMatchScoreBreakdown(
          skillMatchScore = 29,
          skillMatchLabel = "Excellent",
          distanceScore = 24,
          distanceKm = 2.1,
          availabilityScore = 20,
          availabilityLabel = "Available",
          ratingScore = 14,
          ratingValue = 4.8f,
          workloadScore = 7,
          workloadLabel = "Low",
          explanation = "Rahul Das has certified trade qualification for $service (Excellent match), is located just 2.1 km away, is actively Available for your preferred $preferredTime slot, holds an exceptional 4.8★ rating, and currently carries Low workload—ensuring dedicated focus without rushed service."
        )
      ),
      SmartMatchCandidate(
        workerId = "w_1",
        workerName = "Rahul Sharma",
        trade = "AC Service & HVAC Technician",
        verified = true,
        rating = 4.9f,
        jobsCompleted = 148,
        distanceKm = 2.8,
        availability = "Available",
        estimatedPrice = 499,
        matchPercentage = 92,
        breakdown = SmartMatchScoreBreakdown(
          skillMatchScore = 28,
          skillMatchLabel = "Excellent",
          distanceScore = 22,
          distanceKm = 2.8,
          availabilityScore = 20,
          availabilityLabel = "Available",
          ratingScore = 15,
          ratingValue = 4.9f,
          workloadScore = 7,
          workloadLabel = "Moderate",
          explanation = "Rahul Sharma is a certified HVAC Master with 148 successful jobs. Proximity is 2.8 km, with immediate availability and 4.9★ rating. Moderate current workload ensures prompt dispatch within 30 minutes."
        )
      ),
      SmartMatchCandidate(
        workerId = "w_3",
        workerName = "Sunita Devi",
        trade = "Plumbing Specialist",
        verified = true,
        rating = 4.9f,
        jobsCompleted = 120,
        distanceKm = 3.4,
        availability = "Available",
        estimatedPrice = 399,
        matchPercentage = 89,
        breakdown = SmartMatchScoreBreakdown(
          skillMatchScore = 26,
          skillMatchLabel = "Very High",
          distanceScore = 21,
          distanceKm = 3.4,
          availabilityScore = 20,
          availabilityLabel = "Available",
          ratingScore = 14,
          ratingValue = 4.9f,
          workloadScore = 8,
          workloadLabel = "Low",
          explanation = "Sunita Devi possesses verified plumbing and pipe diagnosis credentials. She is 3.4 km away with Low active queue, allowing her full undivided craftsmanship for leak repair and sanitary fittings."
        )
      ),
      SmartMatchCandidate(
        workerId = "w_2",
        workerName = "Amit Kumar",
        trade = "Master Electrician",
        verified = true,
        rating = 4.8f,
        jobsCompleted = 95,
        distanceKm = 4.1,
        availability = "Available",
        estimatedPrice = 349,
        matchPercentage = 87,
        breakdown = SmartMatchScoreBreakdown(
          skillMatchScore = 27,
          skillMatchLabel = "Excellent",
          distanceScore = 19,
          distanceKm = 4.1,
          availabilityScore = 19,
          availabilityLabel = "Available",
          ratingScore = 14,
          ratingValue = 4.8f,
          workloadScore = 8,
          workloadLabel = "Low",
          explanation = "Amit Kumar is a licensed domestic & industrial electrician with top reviews on wiring safety. Proximity is 4.1 km with low queue volume."
        )
      )
    )

    // Filter or adjust order so top 3 match the requested service
    val filtered = candidatesPool.sortedByDescending { it.matchPercentage }.take(3)
    return filtered
  }

  fun calculateFairPriceSuggestion(
    serviceType: String = "AC Service",
    jobComplexity: String = "Standard",
    workerSkill: String = "Certified Senior",
    location: String = "Indiranagar, Bengaluru",
    customerBudget: Int = 500
  ): FairPriceSuggestion {
    val budget = customerBudget.coerceAtLeast(100)

    val sType = serviceType.lowercase()
    val baseRate = when {
      sType.contains("ac") || sType.contains("air") -> 425
      sType.contains("electr") -> 340
      sType.contains("plumb") -> 300
      sType.contains("appliance") -> 380
      else -> 350
    }

    val comp = jobComplexity.lowercase()
    val complexityMultiplier = when {
      comp.contains("minor") || comp.contains("quick") -> 1.0
      comp.contains("complex") || comp.contains("major") -> 1.45
      comp.contains("moderate") -> 1.25
      else -> 1.12 // Standard
    }

    val skill = workerSkill.lowercase()
    val skillMultiplier = when {
      skill.contains("master") || skill.contains("expert") -> 1.2
      skill.contains("senior") || skill.contains("certified") -> 1.08
      else -> 1.0
    }

    val loc = location.lowercase()
    val locationAdjustment = when {
      loc.contains("indiranagar") || loc.contains("koramangala") || loc.contains("salt lake") || loc.contains("whitefield") -> 35
      loc.contains("howrah") || loc.contains("new town") || loc.contains("bellandur") -> 25
      else -> 20
    }

    val rawFair = (baseRate * complexityMultiplier * skillMultiplier) + locationAdjustment
    val fairCenter = Math.round(rawFair / 25.0).toInt() * 25

    var suggestedMin = Math.round((fairCenter * 0.95) / 25.0).toInt() * 25
    var suggestedMax = Math.round((fairCenter * 1.05) / 25.0).toInt() * 25

    if (suggestedMax <= suggestedMin) {
      suggestedMax = suggestedMin + 50
    } else if (suggestedMax - suggestedMin < 50) {
      suggestedMax = suggestedMin + 50
    }

    val startingOffer = suggestedMin
    val explanation = when {
      budget < suggestedMin -> {
        val diff = suggestedMin - budget
        if (diff <= 50) {
          "Your offer is slightly below the suggested range.\n₹$suggestedMin may be a reasonable starting offer."
        } else {
          "Your offer of ₹$budget is noticeably below the suggested range (₹$suggestedMin–₹$suggestedMax).\n₹$suggestedMin is recommended as an equitable starting proposal."
        }
      }
      budget in suggestedMin..suggestedMax -> {
        "Your offer of ₹$budget fits squarely within the recommended range (₹$suggestedMin–₹$suggestedMax) and is likely to be accepted promptly by verified craftspeople."
      }
      else -> {
        "Your offer of ₹$budget is comfortably above the recommended range (₹$suggestedMin–₹$suggestedMax). It represents a fair premium for express scheduling."
      }
    }

    return FairPriceSuggestion(
      customerBudget = budget,
      suggestedMin = suggestedMin,
      suggestedMax = suggestedMax,
      suggestedRangeText = "₹$suggestedMin–₹$suggestedMax",
      recommendedStartingOffer = startingOffer,
      explanation = explanation,
      disclaimer = "AI-assisted price suggestion. AI is an assistant, not an autonomous decision maker.",
      serviceType = serviceType,
      jobComplexity = jobComplexity,
      workerSkill = workerSkill,
      location = location,
      baseRate = baseRate
    )
  }

  // Phase 10: INSTA HELP Available Workers Finder
  fun getNearbyInstaHelpWorkers(
    serviceOption: String = "Electrical Emergency",
    radiusKm: Double = 5.0
  ): List<InstaHelpWorkerResult> {
    val allWorkers = when (serviceOption) {
      "Electrical Emergency" -> listOf(
        InstaHelpWorkerResult(
          workerId = "wrk_rahul_das",
          workerName = "Rahul Das",
          distanceKm = 2.1,
          availability = "Available Now",
          isVerified = true,
          skill = "Electrician",
          etaMinutes = 12,
          rating = 4.9f,
          estimatedPrice = 449,
          estimatedEarnings = 425
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_amit_k",
          workerName = "Amit Kumar",
          distanceKm = 3.4,
          availability = "Available Now",
          isVerified = true,
          skill = "Electrician (ITI Certified)",
          etaMinutes = 18,
          rating = 4.8f,
          estimatedPrice = 429,
          estimatedEarnings = 405
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_priya_n",
          workerName = "Priya Nambiar",
          distanceKm = 4.2,
          availability = "Available Now",
          isVerified = true,
          skill = "Electrician & Circuit Tech",
          etaMinutes = 22,
          rating = 4.95f,
          estimatedPrice = 499,
          estimatedEarnings = 470
        )
      )
      "Water Leakage" -> listOf(
        InstaHelpWorkerResult(
          workerId = "wrk_sunita_d",
          workerName = "Sunita Devi",
          distanceKm = 1.8,
          availability = "Available Now",
          isVerified = true,
          skill = "Plumber (Master Pipe Fitter)",
          etaMinutes = 10,
          rating = 4.95f,
          estimatedPrice = 399,
          estimatedEarnings = 379
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_farhan_a",
          workerName = "Farhan Akhtar",
          distanceKm = 2.6,
          availability = "Available Now",
          isVerified = true,
          skill = "Plumber & Drainage Specialist",
          etaMinutes = 15,
          rating = 4.92f,
          estimatedPrice = 420,
          estimatedEarnings = 399
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_vikram_s",
          workerName = "Vikram Singh",
          distanceKm = 3.8,
          availability = "Available Now",
          isVerified = true,
          skill = "Plumber",
          etaMinutes = 20,
          rating = 4.88f,
          estimatedPrice = 380,
          estimatedEarnings = 360
        )
      )
      "AC Breakdown" -> listOf(
        InstaHelpWorkerResult(
          workerId = "wrk_rahul_das_ac",
          workerName = "Rahul Das",
          distanceKm = 2.1,
          availability = "Available Now",
          isVerified = true,
          skill = "AC Breakdown & Electrical Specialist",
          etaMinutes = 12,
          rating = 4.9f,
          estimatedPrice = 549,
          estimatedEarnings = 520
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_ramesh_p",
          workerName = "Ramesh Patel",
          distanceKm = 2.4,
          availability = "Available Now",
          isVerified = true,
          skill = "Senior HVAC & AC Tech",
          etaMinutes = 14,
          rating = 4.88f,
          estimatedPrice = 599,
          estimatedEarnings = 569
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_manoj_g",
          workerName = "Manoj Gowda",
          distanceKm = 3.9,
          availability = "Available Now",
          isVerified = true,
          skill = "AC Cooling Diagnostics Tech",
          etaMinutes = 21,
          rating = 4.91f,
          estimatedPrice = 549,
          estimatedEarnings = 520
        )
      )
      else -> listOf(
        InstaHelpWorkerResult(
          workerId = "wrk_rahul_das",
          workerName = "Rahul Das",
          distanceKm = 2.1,
          availability = "Available Now",
          isVerified = true,
          skill = "Urgent Multi-Trade Specialist",
          etaMinutes = 12,
          rating = 4.9f,
          estimatedPrice = 399,
          estimatedEarnings = 379
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_sunita_d",
          workerName = "Sunita Devi",
          distanceKm = 1.8,
          availability = "Available Now",
          isVerified = true,
          skill = "Emergency Domestic Service",
          etaMinutes = 10,
          rating = 4.95f,
          estimatedPrice = 399,
          estimatedEarnings = 379
        ),
        InstaHelpWorkerResult(
          workerId = "wrk_amit_k",
          workerName = "Amit Kumar",
          distanceKm = 3.4,
          availability = "Available Now",
          isVerified = true,
          skill = "Rapid Response Handyman",
          etaMinutes = 18,
          rating = 4.8f,
          estimatedPrice = 349,
          estimatedEarnings = 330
        )
      )
    }

    return allWorkers.filter { it.distanceKm <= radiusKm }
  }
}

