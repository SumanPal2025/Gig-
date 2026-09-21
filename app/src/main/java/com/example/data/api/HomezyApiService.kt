package com.example.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HomezyApiService {

  // Auth
  @POST("api/auth/register")
  suspend fun register(
    @Body request: RegisterRequest
  ): Response<AuthResponse>

  @POST("api/auth/login")
  suspend fun login(
    @Body request: LoginRequest
  ): Response<AuthResponse>

  // Services
  @GET("api/services")
  suspend fun getServices(): Response<ApiResponse<List<ServiceDto>>>

  // Workers
  @GET("api/workers")
  suspend fun getWorkers(
    @Query("trade") trade: String? = null,
    @Query("search") search: String? = null
  ): Response<ApiResponse<List<WorkerProfileDto>>>

  @GET("api/workers/{id}")
  suspend fun getWorkerById(
    @Path("id") id: String
  ): Response<ApiResponse<WorkerProfileDto>>

  // Bookings
  @POST("api/bookings")
  suspend fun createBooking(
    @Body request: CreateBookingRequest
  ): Response<ApiResponse<BookingDto>>

  @GET("api/bookings")
  suspend fun getBookings(
    @Query("status") status: String? = null
  ): Response<ApiResponse<List<BookingDto>>>

  @PUT("api/bookings/{id}/status")
  suspend fun updateBookingStatus(
    @Path("id") id: String,
    @Body request: UpdateBookingStatusRequest
  ): Response<ApiResponse<BookingDto>>

  // Payments
  @POST("api/payments")
  suspend fun createPayment(
    @Body request: ProcessPaymentRequest
  ): Response<ApiResponse<Map<String, Any>>>

  // Ratings
  @POST("api/ratings")
  suspend fun submitRating(
    @Body request: SubmitRatingRequest
  ): Response<ApiResponse<Map<String, Any>>>

  // Admin
  @GET("api/admin/dashboard")
  suspend fun getAdminDashboard(): Response<ApiResponse<AdminDashboardDto>>

  // AI Smart Matching & Fair Allocation (Phase 5)
  @POST("api/ai/smart-match")
  suspend fun smartMatch(
    @Body request: SmartMatchRequest
  ): Response<ApiResponse<List<SmartMatchCandidateDto>>>

  @GET("api/ai/workforce-allocation")
  suspend fun getWorkforceAllocation(
    @Query("trade") trade: String? = null,
    @Query("area") area: String? = null
  ): Response<ApiResponse<WorkforceAllocationResponseDto>>

  // Phase 9: AI Demand Forecasting
  @GET("api/ai/demand-forecast")
  suspend fun getDemandForecast(): Response<ApiResponse<DemandForecastResponseDto>>
}
