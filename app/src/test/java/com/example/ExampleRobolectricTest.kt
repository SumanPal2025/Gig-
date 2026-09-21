package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("HOMEZY", appName)
  }

  @Test
  fun `ai fair price suggestion produces expected range for ac service`() {
    val suggestion = SampleData.calculateFairPriceSuggestion(
      serviceType = "AC Service",
      jobComplexity = "Standard",
      workerSkill = "Certified Senior",
      location = "Indiranagar",
      customerBudget = 500
    )

    // Expected benchmark: ₹525–₹575 as per specification
    assertEquals(525, suggestion.suggestedMin)
    assertEquals(575, suggestion.suggestedMax)
    assertEquals("₹525–₹575", suggestion.suggestedRangeText)
    assertTrue(suggestion.explanation.contains("Your offer is slightly below the suggested range"))
    assertTrue(suggestion.explanation.contains("₹525 may be a reasonable starting offer"))
  }

  @Test
  fun `ai fair price explanation handles budget within range`() {
    val suggestion = SampleData.calculateFairPriceSuggestion(
      serviceType = "AC Service",
      jobComplexity = "Standard",
      workerSkill = "Certified Senior",
      location = "Indiranagar",
      customerBudget = 550
    )

    assertEquals(525, suggestion.suggestedMin)
    assertEquals(575, suggestion.suggestedMax)
    assertTrue(suggestion.explanation.contains("within the recommended range"))
  }
}

