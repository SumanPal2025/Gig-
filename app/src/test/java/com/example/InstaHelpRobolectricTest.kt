package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.data.*
import com.example.ui.customer.CustomerInstaHelpScreen
import com.example.ui.theme.HomezyTheme
import com.example.ui.worker.WorkerDashboardScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class InstaHelpRobolectricTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun customer_insta_help_screen_displays_all_elements_and_service_options() {
    var dispatchedRequest: InstaHelpRequest? = null

    composeRule.setContent {
      HomezyTheme {
        CustomerInstaHelpScreen(
          onDispatchConfirmed = { dispatchedRequest = it }
        )
      }
    }

    // Verify header and prototype notice
    composeRule.onNodeWithText("Need help right now?").assertIsDisplayed()
    composeRule.onNodeWithText("Find the nearest available verified worker.").assertIsDisplayed()
    composeRule.onNodeWithTag("insta_help_prototype_notice").assertIsDisplayed()

    // Verify service options: Electrical Emergency, Water Leakage, AC Breakdown, Other Urgent Help
    composeRule.onNodeWithTag("insta_service_option_electrical_emergency").assertIsDisplayed()
    composeRule.onNodeWithTag("insta_service_option_water_leakage").assertIsDisplayed()
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("insta_service_option_ac_breakdown"))
    composeRule.onNodeWithTag("insta_service_option_ac_breakdown").assertIsDisplayed()
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("insta_service_option_other_urgent_help"))
    composeRule.onNodeWithTag("insta_service_option_other_urgent_help").assertIsDisplayed()

    // Verify location and radius cards
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("insta_location_radius_card"))
    composeRule.onNodeWithTag("insta_location_radius_card").assertIsDisplayed()
    composeRule.onNodeWithText("Salt Lake, Sector V, Kolkata").assertIsDisplayed()
    composeRule.onNodeWithTag("radius_chip_5km").assertIsDisplayed()

    // Verify worker results
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("worker_result_card_rahul_das"))
    composeRule.onNodeWithTag("worker_result_card_rahul_das").assertIsDisplayed()
    composeRule.onNodeWithText("Rahul Das").assertIsDisplayed()
    composeRule.onNodeWithText("2.1 km").assertIsDisplayed()
    composeRule.onNodeWithText("ETA: 12 min").assertIsDisplayed()
    composeRule.onNodeWithTag("request_insta_help_btn_rahul_das").assertIsDisplayed()
  }

  @Test
  fun customer_can_switch_services_and_see_customized_worker_results() {
    composeRule.setContent {
      HomezyTheme {
        CustomerInstaHelpScreen(
          onDispatchConfirmed = {}
        )
      }
    }

    // Switch to Water Leakage
    composeRule.onNodeWithTag("insta_service_option_water_leakage").performClick()
    composeRule.waitForIdle()

    // Verify Sunita Devi (Plumber) is listed
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("worker_result_card_sunita_devi"))
    composeRule.onNodeWithTag("worker_result_card_sunita_devi").assertIsDisplayed()
    composeRule.onNodeWithText("Sunita Devi").assertIsDisplayed()
    composeRule.onNodeWithText("1.8 km").assertIsDisplayed()
    composeRule.onNodeWithText("ETA: 10 min").assertIsDisplayed()
  }

  @Test
  fun customer_insta_help_confirmation_dialog_and_dispatch_flow() {
    var dispatchedRequest: InstaHelpRequest? = null

    composeRule.setContent {
      HomezyTheme {
        CustomerInstaHelpScreen(
          onDispatchConfirmed = { dispatchedRequest = it }
        )
      }
    }

    // Scroll and click Request Insta Help for Rahul Das
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("worker_result_card_rahul_das"))
    composeRule.onNodeWithTag("worker_result_card_rahul_das").performClick()
    composeRule.waitForIdle()

    // Verify confirmation modal
    composeRule.onNodeWithTag("insta_help_confirmation_dialog").assertIsDisplayed()
    composeRule.onNodeWithText("Confirm Insta Help").assertIsDisplayed()
    composeRule.onAllNodesWithText("₹449").onFirst().assertIsDisplayed()
    composeRule.onNodeWithText("Immediate Priority").assertIsDisplayed()

    // Confirm emergency request
    composeRule.onNodeWithTag("confirm_emergency_request_button").performScrollTo().performClick()
    composeRule.waitForIdle()

    // Verify dispatched payload
    assertNotNull(dispatchedRequest)
    assertEquals("Electrical Emergency", dispatchedRequest?.service)
    assertEquals("Rahul Das", dispatchedRequest?.workerName)
    assertEquals(2.1, dispatchedRequest?.workerDistanceKm ?: 0.0, 0.01)
    assertEquals(12, dispatchedRequest?.workerEtaMinutes)
  }

  @Test
  fun worker_receives_and_accepts_insta_help_request() {
    var accepted = false
    val testRequest = InstaHelpRequest(
      id = "TEST-INSTA-1",
      service = "Electrical Emergency",
      location = "Salt Lake, Sector V, Kolkata",
      workerName = "Rahul Das",
      workerDistanceKm = 2.1,
      workerEtaMinutes = 12,
      estimatedEarnings = 425,
      urgency = "Immediate / Priority 1",
      status = InstaHelpStatus.PENDING
    )

    composeRule.setContent {
      HomezyTheme {
        WorkerDashboardScreen(
          incomingJobs = emptyList(),
          instaHelpRequests = listOf(testRequest),
          onAcceptJob = {},
          onDeclineJob = {},
          onAcceptInstaHelp = { accepted = true },
          onDeclineInstaHelp = {}
        )
      }
    }

    // Verify Worker receives INSTA HELP REQUEST card with required fields
    composeRule.onNodeWithTag("worker_dashboard_screen").performScrollToNode(hasTestTag("worker_insta_help_card_TEST-INSTA-1"))
    composeRule.onNodeWithTag("worker_insta_help_card_TEST-INSTA-1").assertIsDisplayed()
    composeRule.onNodeWithText("INSTA HELP REQUEST").assertIsDisplayed()
    composeRule.onNodeWithText("Immediate / Priority 1").assertIsDisplayed()
    composeRule.onNodeWithText("Electrical Emergency").assertIsDisplayed()
    composeRule.onNodeWithText("2.1 km").assertIsDisplayed()
    composeRule.onNodeWithText("₹425").assertIsDisplayed()

    // Click Accept button
    composeRule.onNodeWithTag("accept_insta_help_TEST-INSTA-1").performClick()
    composeRule.waitForIdle()

    assertEquals(true, accepted)
  }

  @Test
  fun end_to_end_customer_to_worker_insta_help_flow() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // 1. Log in as Customer
    composeRule.onNodeWithTag("login_demo_customer_chip").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()

    // 2. Click prominent INSTA HELP CTA on customer home
    composeRule.onNodeWithTag("customer_home_screen").performScrollToNode(hasTestTag("insta_help_cta"))
    composeRule.onNodeWithTag("insta_help_cta").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_insta_help_screen").assertIsDisplayed()

    // 3. Request Insta Help on candidate
    composeRule.onNodeWithTag("customer_insta_help_screen").performScrollToNode(hasTestTag("worker_result_card_rahul_das"))
    composeRule.onNodeWithTag("worker_result_card_rahul_das").performClick()
    composeRule.waitForIdle()

    // 4. Confirm in dialog
    composeRule.onNodeWithTag("confirm_emergency_request_button").performScrollTo().performClick()
    composeRule.waitForIdle()

    // 5. Switches to Bookings screen showing the active emergency booking
    composeRule.onNodeWithTag("customer_bookings_screen").assertIsDisplayed()
    composeRule.onNodeWithText("Insta Help: Electrical Emergency").assertIsDisplayed()
  }
}

