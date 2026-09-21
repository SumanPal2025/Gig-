package com.example.data.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class ApiResult<out T> {
  object Loading : ApiResult<Nothing>()
  data class Success<out T>(val data: T) : ApiResult<T>()
  data class Error(val message: String, val exception: Throwable? = null) : ApiResult<Nothing>()
}

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
  val success: Boolean,
  val message: String? = null,
  val data: T? = null,
  val token: String? = null,
  val count: Int? = null
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
  val email: String,
  val password: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
  val name: String,
  val email: String,
  val phone: String,
  val password: String,
  val role: String = "customer",
  val trade: String? = null,
  val address: String? = null
)

@JsonClass(generateAdapter = true)
data class UserDto(
  val id: String,
  val name: String,
  val email: String,
  val phone: String,
  val role: String,
  val avatar: String? = null
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
  val success: Boolean,
  val message: String? = null,
  val token: String? = null,
  val user: UserDto? = null
)

@JsonClass(generateAdapter = true)
data class ServiceDto(
  @Json(name = "_id") val id: String,
  val name: String,
  val category: String,
  val description: String,
  val basePrice: Int,
  val estimatedDurationMinutes: Int = 60,
  val popular: Boolean = false,
  val icon: String? = null,
  val includedTasks: List<String> = emptyList(),
  val standardCoopFeePercentage: Double = 5.0
)

@JsonClass(generateAdapter = true)
data class WorkerProfileDto(
  @Json(name = "_id") val id: String,
  val name: String,
  val email: String? = null,
  val phone: String? = null,
  val trade: String,
  val skills: List<String> = emptyList(),
  val bio: String? = null,
  val rating: Float = 5.0f,
  val totalReviews: Int = 0,
  val completedJobs: Int = 0,
  val availability: String = "available",
  val currentWorkload: Int = 0,
  val monthlyCap: Int = 35,
  val fairAllocationScore: Int = 95,
  val totalEarnings: Int = 0,
  val verificationStatus: String = "verified"
)

@JsonClass(generateAdapter = true)
data class CreateBookingRequest(
  val workerId: String,
  val serviceId: String,
  val scheduledDate: String,
  val timeSlot: String,
  val customerAddress: String,
  val notes: String = "",
  val customerPhone: String = ""
)

@JsonClass(generateAdapter = true)
data class BookingDto(
  @Json(name = "_id") val id: String,
  val customer: String,
  val worker: String,
  val service: String,
  val scheduledDate: String,
  val timeSlot: String,
  val status: String,
  val amount: Int,
  val platformFee: Int,
  val workerEarnings: Int,
  val customerAddress: String,
  val notes: String = "",
  val workerName: String? = null,
  val workerTrade: String? = null,
  val serviceName: String? = null,
  val customerName: String? = null
)

@JsonClass(generateAdapter = true)
data class UpdateBookingStatusRequest(
  val status: String
)

@JsonClass(generateAdapter = true)
data class ProcessPaymentRequest(
  val bookingId: String,
  val amount: Int,
  val paymentMethod: String = "upi"
)

@JsonClass(generateAdapter = true)
data class SubmitRatingRequest(
  val bookingId: String,
  val workerId: String,
  val rating: Int,
  val comment: String = ""
)

@JsonClass(generateAdapter = true)
data class AdminDashboardDto(
  val federationName: String,
  val platformFeePercentage: Double,
  val totalWorkers: Int,
  val activeWorkers: Int,
  val totalCustomers: Int,
  val totalBookings: Int,
  val totalGMV: Int,
  val cooperativeFeesRetained: Int,
  val welfareFundDisbursed: Int,
  val emergencyFundReserve: Int,
  val avgWorkerSatisfactionRate: Double,
  val fairRoutingIndex: Double
)

@JsonClass(generateAdapter = true)
data class SmartMatchRequest(
  val service: String,
  val requiredSkill: String = "",
  val customerLocation: String = "Indiranagar, Bengaluru",
  val preferredTime: String = "Morning"
)

@JsonClass(generateAdapter = true)
data class SmartMatchScoreBreakdownDto(
  val skillScore: Int = 28,
  val skillLabel: String = "Excellent",
  val distanceScore: Int = 23,
  val distanceKm: Double = 2.1,
  val availabilityScore: Int = 20,
  val availabilityLabel: String = "Available",
  val ratingScore: Int = 14,
  val ratingValue: Float = 4.8f,
  val workloadScore: Int = 9,
  val workloadLabel: String = "Low",
  val explanation: String = ""
)

@JsonClass(generateAdapter = true)
data class SmartMatchCandidateDto(
  val workerId: String,
  val workerName: String,
  val trade: String,
  val verified: Boolean = true,
  val rating: Float = 4.8f,
  val jobsCompleted: Int = 42,
  val distanceKm: Double = 2.1,
  val availability: String = "Available",
  val estimatedPrice: Int = 499,
  val matchPercentage: Int = 94,
  val breakdown: SmartMatchScoreBreakdownDto? = null
)

@JsonClass(generateAdapter = true)
data class WorkerAllocationStatDto(
  val workerId: String,
  val workerName: String,
  val trade: String,
  val jobsReceived: Int,
  val hoursWorked: Double,
  val currentWorkload: String,
  val idleTimeHours: Double,
  val recentEarnings: Int,
  val fairOpportunityScore: Int = 94,
  val status: String = "Priority for Next Assignment",
  val explanation: String = "HOMEZY considers workload and availability so qualified workers receive fair opportunities."
)

@JsonClass(generateAdapter = true)
data class AdminWorkforceZoneAllocationDto(
  val area: String,
  val service: String,
  val demand: String,
  val availableWorkers: Int,
  val recommendedAllocation: Int,
  val giniEqualityIndex: Double = 0.16,
  val statusNotes: String = ""
)

@JsonClass(generateAdapter = true)
data class WorkforceAllocationResponseDto(
  val algorithmName: String = "Prototype Fair Allocation Algorithm",
  val disclaimer: String = "SIH Prototype - Explainable heuristics avoiding unfair job concentration",
  val zoneAllocations: List<AdminWorkforceZoneAllocationDto> = emptyList(),
  val workerFairnessStats: List<WorkerAllocationStatDto> = emptyList()
)

// Phase 9: AI Demand Forecasting DTOs
@JsonClass(generateAdapter = true)
data class DemandDataPointDto(
  val periodLabel: String,
  val count: Int,
  val isProjected: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ServiceDemandForecastDto(
  val serviceName: String,
  val category: String,
  val demandLevel: String,
  val demandTrendLabel: String,
  val trendPercentage: Int,
  val availableWorkers: Int,
  val recommendedWorkers: Int,
  val recommendationText: String,
  val reallocationDelta: Int,
  val historicalDemandPoints: List<DemandDataPointDto> = emptyList(),
  val predictedDemandPoints: List<DemandDataPointDto> = emptyList(),
  val keyDriver: String = ""
)

@JsonClass(generateAdapter = true)
data class DemandForecastResponseDto(
  val forecastTitle: String = "Prototype AI Demand Forecast",
  val disclaimer: String = "Prototype AI Forecast: Based on weighted moving averages and historical booking velocity. For operational decision-support demonstration only. Does not claim real-world predictive accuracy.",
  val forecastMethodology: String = "Explainable Weighted Moving Average (WMA) + Trend Factor",
  val services: List<ServiceDemandForecastDto> = emptyList()
)


