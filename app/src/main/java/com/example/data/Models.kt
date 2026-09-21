package com.example.data

enum class AppRole(val label: String, val badge: String) {
  CUSTOMER("Customer", "Home Care"),
  WORKER("Service Worker", "Co-op Member"),
  ADMIN("Federation Admin", "Governance")
}

data class ServiceItem(
  val id: String,
  val name: String,
  val category: String,
  val iconName: String,
  val shortDescription: String,
  val priceRange: String,
  val startingPrice: Int,
  val rating: Float,
  val reviewCount: Int,
  val duration: String,
  val cooperativeGuarantee: String,
  val popularFeatures: List<String>
)

data class WorkerProfile(
  val id: String,
  val name: String,
  val trade: String,
  val cooperativeId: String,
  val experienceYears: Int,
  val rating: Float,
  val completedJobs: Int,
  val hourlyRate: Int,
  val distanceKm: Double,
  val isVerified: Boolean,
  val isAvailable: Boolean,
  val skills: List<String>,
  val phone: String,
  val locationArea: String,
  val fairAllocationScore: Int // 0-100 score indicating equitable opportunity index
)

data class BookingItem(
  val id: String,
  val serviceName: String,
  val category: String = "Household Service",
  val customerName: String,
  val customerAddress: String,
  val workerName: String,
  val workerPhone: String = "+91 98765 43210",
  val date: String,
  val timeSlot: String,
  val price: Int,
  val status: BookingStatus,
  val isInstaHelp: Boolean = false,
  val cooperativeDividendApplied: Int = 20,
  val problemDescription: String = "Standard service & diagnostics",
  val workerEarnings: Int = 0,
  val cooperativeContribution: Int = 0,
  val platformFee: Int = 0,
  val paymentMethod: String = "UPI",
  val paymentStatus: String = "Paid",
  val isWorkCompletedByWorker: Boolean = false,
  val isCompletionVerified: Boolean = false,
  val isIssueReported: Boolean = false,
  val reportedIssueText: String? = null,
  val rating: Int? = null,
  val ratingComment: String? = null
)

enum class BookingStatus(val label: String) {
  PENDING("Pending"),
  ACCEPTED("Accepted"),
  ASSIGNED("Worker Assigned"),
  IN_PROGRESS("In Progress"),
  COMPLETED("Completed"),
  CANCELLED("Cancelled")
}

enum class WorkerJobStatus(val label: String, val stepIndex: Int) {
  ACCEPTED("Accepted", 0),
  ON_THE_WAY("On the Way", 1),
  ARRIVED("Arrived", 2),
  IN_PROGRESS("In Progress", 3),
  COMPLETED("Completed", 4)
}

data class WorkerActiveJob(
  val id: String,
  val service: String,
  val customerName: String,
  val customerPhone: String = "+91 98451 90812",
  val customerArea: String = "Koramangala",
  val address: String,
  val distance: String,
  val scheduledDate: String = "Today",
  val scheduledTime: String,
  val problemDescription: String,
  val agreedPrice: Int,
  val workerEarnings: Int,
  val cooperativeContribution: Int,
  val platformFee: Int,
  val paymentStatus: String = "Escrow Secured (Instant Payout)",
  val status: WorkerJobStatus = WorkerJobStatus.ACCEPTED,
  val checklist: List<String> = listOf(
    "Arrive on site and verify work location",
    "Show digital Co-op ID badge to resident",
    "Conduct technical diagnostics and safety check",
    "Execute repair / service with standard parts",
    "Customer inspection and digital sign-off"
  ),
  val completedChecklistIndices: Set<Int> = setOf(0, 1)
)

data class JobOffer(
  val id: String,
  val service: String,
  val customerName: String,
  val customerArea: String = "Indiranagar",
  val address: String,
  val distance: String,
  val date: String = "Today",
  val scheduledTime: String,
  val price: Int,
  val cooperativeFee: Int, // Just 5% platform fee
  val workerNet: Int,
  val aiMatchScore: Int,
  val problem: String = "Standard diagnosis and repair",
  val paymentStatus: String = "Co-op Escrow Secured",
  val urgency: String = "Standard",
  val status: JobOfferStatus = JobOfferStatus.NEW
)

enum class JobOfferStatus {
  NEW, ACCEPTED, DECLINED
}

data class WelfareBenefit(
  val id: String,
  val title: String,
  val category: String,
  val description: String,
  val coverageAmount: String,
  val status: String,
  val renewalDate: String
)

data class AdminMetric(
  val title: String,
  val value: String,
  val changePercentage: String,
  val isPositive: Boolean,
  val subtext: String
)

data class FairAllocationStat(
  val zoneName: String,
  val totalWorkers: Int,
  val avgJobsPerWorker: Double,
  val giniCoefficient: Double, // Measures equality: lower is better
  val equityScore: Int, // 1-100
  val status: String
)

data class ChatMessage(
  val id: String,
  val senderName: String,
  val isFromMe: Boolean,
  val text: String,
  val timestamp: String
)

data class SmartMatchScoreBreakdown(
  val skillMatchScore: Int,      // e.g. 28 / 30
  val skillMatchLabel: String,   // "Excellent"
  val distanceScore: Int,        // e.g. 23 / 25
  val distanceKm: Double,        // 2.1
  val availabilityScore: Int,    // e.g. 20 / 20
  val availabilityLabel: String, // "Available"
  val ratingScore: Int,          // e.g. 14 / 15
  val ratingValue: Float,        // 4.8
  val workloadScore: Int,        // e.g. 9 / 10
  val workloadLabel: String,     // "Low"
  val explanation: String
)

data class SmartMatchCandidate(
  val workerId: String,
  val workerName: String,
  val trade: String,
  val verified: Boolean = true,
  val rating: Float,
  val jobsCompleted: Int,
  val distanceKm: Double,
  val availability: String,
  val estimatedPrice: Int,
  val matchPercentage: Int, // 0 - 100%
  val breakdown: SmartMatchScoreBreakdown
)

data class WorkerAllocationStat(
  val workerId: String,
  val workerName: String,
  val trade: String,
  val jobsReceived: Int,
  val hoursWorked: Double,
  val currentWorkload: String, // "Low" / "Moderate" / "High"
  val idleTimeHours: Double,
  val recentEarnings: Int,
  val fairOpportunityScore: Int = 94,
  val status: String = "Priority for Next Assignment",
  val explanation: String = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
)

data class AdminWorkforceZoneAllocation(
  val area: String,
  val service: String,
  val demand: String, // "High", "Surging", "Medium"
  val availableWorkers: Int,
  val recommendedAllocation: Int,
  val giniEqualityIndex: Double = 0.16,
  val statusNotes: String = "Balanced dispatch prevents nearest-worker monopoly."
)

data class FairPriceSuggestion(
  val customerBudget: Int = 500,
  val suggestedMin: Int = 525,
  val suggestedMax: Int = 575,
  val suggestedRangeText: String = "₹525–₹575",
  val recommendedStartingOffer: Int = 525,
  val explanation: String = "Your offer is slightly below the suggested range.\n₹525 may be a reasonable starting offer.",
  val disclaimer: String = "AI-assisted price suggestion. AI is an assistant, not an autonomous decision maker.",
  val serviceType: String = "AC Service",
  val jobComplexity: String = "Standard",
  val workerSkill: String = "Certified Senior",
  val location: String = "Indiranagar, Bengaluru",
  val baseRate: Int = 425
)

enum class NegotiationActor {
  CUSTOMER,
  WORKER,
  AI_ASSISTANT
}

enum class NegotiationActionType {
  OFFER_SENT,
  COUNTER_OFFER,
  ACCEPTED,
  REJECTED,
  AI_SUGGESTION
}

data class NegotiationHistoryEntry(
  val id: String,
  val actor: NegotiationActor,
  val actorName: String,
  val actionType: NegotiationActionType,
  val amount: Int?,
  val message: String,
  val timestamp: String
)

// Phase 9: AI Demand Forecasting Models
data class DemandDataPoint(
  val periodLabel: String,
  val count: Int,
  val isProjected: Boolean = false
)

data class ServiceDemandForecast(
  val serviceName: String, // "AC Service", "Electrician", "Plumber"
  val category: String,
  val demandLevel: String, // "High", "Medium", "Low"
  val demandTrendLabel: String, // "High Demand ↑", "Medium Demand →", "High Demand ↑"
  val trendPercentage: Int, // e.g. +38, +3, +29
  val availableWorkers: Int,
  val recommendedWorkers: Int,
  val recommendationText: String, // "Consider reallocating 4 additional workers."
  val reallocationDelta: Int, // e.g. +4, 0, -1
  val historicalDemandPoints: List<DemandDataPoint>,
  val predictedDemandPoints: List<DemandDataPoint>,
  val keyDriver: String = "Seasonal surge & historical 3-week velocity"
)

data class DemandForecastResponse(
  val forecastTitle: String = "Prototype AI Demand Forecast",
  val disclaimer: String = "Prototype AI Forecast: Based on weighted moving averages and historical booking velocity. For operational decision-support demonstration only. Does not claim real-world predictive accuracy.",
  val forecastMethodology: String = "Explainable Weighted Moving Average (WMA) + Trend Factor",
  val services: List<ServiceDemandForecast>
)

// Phase 10: INSTA HELP Models
enum class InstaHelpStatus {
  PENDING,
  ACCEPTED,
  DECLINED,
  EN_ROUTE,
  ARRIVED,
  COMPLETED,
  CANCELLED
}

data class InstaHelpWorkerResult(
  val workerId: String,
  val workerName: String,
  val distanceKm: Double,
  val availability: String = "Available Now",
  val isVerified: Boolean = true,
  val skill: String,
  val etaMinutes: Int,
  val rating: Float = 4.85f,
  val estimatedPrice: Int = 449,
  val estimatedEarnings: Int = 425
)

data class InstaHelpRequest(
  val id: String = "INSTA-8842",
  val service: String = "Electrical Emergency",
  val location: String = "Salt Lake, Sector V, Kolkata",
  val latitude: Double = 22.5850,
  val longitude: Double = 88.4312,
  val searchRadiusKm: Double = 5.0,
  val workerId: String = "wrk_rahul_das",
  val workerName: String = "Rahul Das",
  val workerSkill: String = "Electrician",
  val workerDistanceKm: Double = 2.1,
  val workerEtaMinutes: Int = 12,
  val estimatedPrice: Int = 449,
  val estimatedEarnings: Int = 425,
  val urgency: String = "Immediate / Priority 1",
  val status: InstaHelpStatus = InstaHelpStatus.PENDING,
  val timestamp: String = "Just now",
  val customerName: String = "Priya Sundaram",
  val customerPhone: String = "+91 98451 90812"
)




