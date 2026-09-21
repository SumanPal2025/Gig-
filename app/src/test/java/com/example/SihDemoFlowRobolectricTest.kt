package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.data.AuthScreen
import com.example.ui.theme.HomezyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SihDemoFlowRobolectricTest {

  @get:Rule
  val composeRule = createComposeRule()

  @Test
  fun test_customer_smart_match_and_fair_pricing_flow() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // 1. Customer Demo Login
    composeRule.onNodeWithTag("login_demo_customer_chip").performClick()
    composeRule.waitForIdle()

    // 2. Customer Home is displayed
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()

    // 3. Navigate to Services tab
    composeRule.onNodeWithTag("nav__customer_services").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_services_screen").assertIsDisplayed()

    // 4. Navigate to Bookings tab
    composeRule.onNodeWithTag("nav__customer_bookings").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_bookings_screen").assertIsDisplayed()

    // 5. Navigate to Insta Help tab
    composeRule.onNodeWithTag("nav__customer_insta-help").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_insta_help_screen").assertIsDisplayed()

    // 6. Navigate back to Home
    composeRule.onNodeWithTag("nav__customer_home").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()
  }

  @Test
  fun test_worker_dashboard_fair_allocation_and_jobs_flow() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // 1. Worker Demo Login
    composeRule.onNodeWithTag("login_demo_worker_chip").performClick()
    composeRule.waitForIdle()

    // 2. Verify Worker Dashboard
    composeRule.onNodeWithTag("worker_dashboard_screen").assertIsDisplayed()
    composeRule.onNodeWithText("Good morning, Rahul 👋").assertIsDisplayed()

    // 3. Navigate to Jobs tab
    composeRule.onNodeWithTag("worker_nav__worker_jobs").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("worker_jobs_screen").assertIsDisplayed()

    // 4. Navigate to Earnings tab
    composeRule.onNodeWithTag("worker_nav__worker_earnings").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("worker_earnings_screen").assertIsDisplayed()

    // 5. Navigate to Welfare tab
    composeRule.onNodeWithTag("worker_nav__worker_welfare").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("worker_welfare_screen").assertIsDisplayed()

    // 6. Navigate back to Dashboard
    composeRule.onNodeWithTag("worker_nav__worker_dashboard").performClick()
    composeRule.waitForIdle()
    composeRule.onNodeWithTag("worker_dashboard_screen").assertIsDisplayed()
  }

  @Test
  fun test_admin_dashboard_navigation_and_insights() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // 1. Admin Demo Login
    composeRule.onNodeWithTag("login_demo_admin_chip").performClick()
    composeRule.waitForIdle()

    // 2. Verify Admin Dashboard
    composeRule.onNodeWithTag("admin_dashboard_screen").assertIsDisplayed()

    // 3. Navigate to AI Forecast / Insights
    composeRule.onNodeWithTag("admin_tab_ai_forecast").performScrollTo().performClick()
    composeRule.waitForIdle()

    // 4. Verify AI Insights screen
    composeRule.onNodeWithTag("admin_ai_insights_screen").assertIsDisplayed()

    // 5. Navigate to Fair Allocation
    composeRule.onNodeWithTag("admin_tab_fair_allocation").performScrollTo().performClick()
    composeRule.waitForIdle()

    // 6. Verify Fair Allocation screen
    composeRule.onNodeWithTag("admin_fair_allocation_screen").assertIsDisplayed()

    // 7. Navigate to Workers
    composeRule.onNodeWithTag("admin_tab_workers").performScrollTo().performClick()
    composeRule.waitForIdle()

    // 8. Verify Workers screen
    composeRule.onNodeWithTag("admin_workers_screen").assertIsDisplayed()
  }
}



