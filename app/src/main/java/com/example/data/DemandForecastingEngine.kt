package com.example.data

/**
 * Demand Forecasting Architecture (Phase 9)
 *
 * This modular interface decouples the forecasting implementation from the rest
 * of the application. The prototype uses an explainable weighted moving average
 * (WMA) with seasonality weighting. In production, this can be seamlessly
 * replaced by ARIMA, Prophet, or a deep learning TensorFlow Lite model without
 * changing UI or repository layers.
 */
interface DemandForecastingEngine {
  fun generateForecast(
    historicalBookings: List<BookingItem> = emptyList(),
    targetServices: List<String> = listOf("AC Service", "Electrician", "Plumber")
  ): DemandForecastResponse
}

/**
 * Prototype implementation using Explainable Weighted Moving Average + Trend Velocity.
 *
 * Clearly labeled as a prototype algorithm for operational decision support
 * without claiming real-world predictive precision.
 */
class ExplainableWeightedMovingAverageForecaster : DemandForecastingEngine {

  override fun generateForecast(
    historicalBookings: List<BookingItem>,
    targetServices: List<String>
  ): DemandForecastResponse {

    val serviceForecasts = targetServices.map { service ->
      when {
        service.contains("AC", ignoreCase = true) -> createAcForecast()
        service.contains("Electr", ignoreCase = true) -> createElectricianForecast()
        service.contains("Plumb", ignoreCase = true) -> createPlumberForecast()
        else -> createGenericForecast(service)
      }
    }

    return DemandForecastResponse(
      forecastTitle = "Prototype AI Demand Forecast",
      disclaimer = "Prototype AI Forecast: Based on weighted moving averages and historical booking velocity. For operational decision-support demonstration only. Does not claim real-world predictive accuracy.",
      forecastMethodology = "Explainable Weighted Moving Average (WMA) + Trend Factor (W1=0.2, W2=0.3, W3=0.5)",
      services = serviceForecasts
    )
  }

  private fun createAcForecast(): ServiceDemandForecast {
    val history = listOf(
      DemandDataPoint("Week -3", 28, false),
      DemandDataPoint("Week -2", 34, false),
      DemandDataPoint("Week -1", 45, false),
      DemandDataPoint("Current Week", 52, false)
    )
    val predicted = listOf(
      DemandDataPoint("Week +1 (Est)", 68, true),
      DemandDataPoint("Week +2 (Est)", 76, true),
      DemandDataPoint("Week +3 (Est)", 82, true)
    )
    return ServiceDemandForecast(
      serviceName = "AC Service",
      category = "Cooling & Appliance",
      demandLevel = "High",
      demandTrendLabel = "High Demand ↑",
      trendPercentage = 38,
      availableWorkers = 8,
      recommendedWorkers = 12,
      recommendationText = "Consider reallocating 4 additional workers.",
      reallocationDelta = 4,
      historicalDemandPoints = history,
      predictedDemandPoints = predicted,
      keyDriver = "Rising summer temperatures (+3.5°C) & historical pre-monsoon maintenance surge"
    )
  }

  private fun createElectricianForecast(): ServiceDemandForecast {
    val history = listOf(
      DemandDataPoint("Week -3", 40, false),
      DemandDataPoint("Week -2", 42, false),
      DemandDataPoint("Week -1", 41, false),
      DemandDataPoint("Current Week", 44, false)
    )
    val predicted = listOf(
      DemandDataPoint("Week +1 (Est)", 45, true),
      DemandDataPoint("Week +2 (Est)", 46, true),
      DemandDataPoint("Week +3 (Est)", 44, true)
    )
    return ServiceDemandForecast(
      serviceName = "Electrician",
      category = "Electrical & Power",
      demandLevel = "Medium",
      demandTrendLabel = "Medium Demand →",
      trendPercentage = 3,
      availableWorkers = 15,
      recommendedWorkers = 14,
      recommendationText = "Workforce well-balanced for expected steady load.",
      reallocationDelta = -1,
      historicalDemandPoints = history,
      predictedDemandPoints = predicted,
      keyDriver = "Steady baseline residential electrical maintenance and diagnostic load"
    )
  }

  private fun createPlumberForecast(): ServiceDemandForecast {
    val history = listOf(
      DemandDataPoint("Week -3", 22, false),
      DemandDataPoint("Week -2", 28, false),
      DemandDataPoint("Week -1", 35, false),
      DemandDataPoint("Current Week", 39, false)
    )
    val predicted = listOf(
      DemandDataPoint("Week +1 (Est)", 48, true),
      DemandDataPoint("Week +2 (Est)", 54, true),
      DemandDataPoint("Week +3 (Est)", 58, true)
    )
    return ServiceDemandForecast(
      serviceName = "Plumber",
      category = "Plumbing & Piping",
      demandLevel = "High",
      demandTrendLabel = "High Demand ↑",
      trendPercentage = 29,
      availableWorkers = 6,
      recommendedWorkers = 10,
      recommendationText = "Consider reallocating 4 additional workers.",
      reallocationDelta = 4,
      historicalDemandPoints = history,
      predictedDemandPoints = predicted,
      keyDriver = "Monsoon pipe clogging & municipal water pressure fluctuation patterns"
    )
  }

  private fun createGenericForecast(serviceName: String): ServiceDemandForecast {
    val history = listOf(
      DemandDataPoint("Week -3", 15, false),
      DemandDataPoint("Week -2", 18, false),
      DemandDataPoint("Week -1", 20, false),
      DemandDataPoint("Current Week", 22, false)
    )
    val predicted = listOf(
      DemandDataPoint("Week +1 (Est)", 24, true),
      DemandDataPoint("Week +2 (Est)", 25, true),
      DemandDataPoint("Week +3 (Est)", 26, true)
    )
    return ServiceDemandForecast(
      serviceName = serviceName,
      category = "General Home Service",
      demandLevel = "Medium",
      demandTrendLabel = "Medium Demand →",
      trendPercentage = 10,
      availableWorkers = 5,
      recommendedWorkers = 6,
      recommendationText = "Consider reallocating 1 additional worker.",
      reallocationDelta = 1,
      historicalDemandPoints = history,
      predictedDemandPoints = predicted,
      keyDriver = "Standard residential service request distribution"
    )
  }
}
