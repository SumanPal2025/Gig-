package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.data.AuthScreen
import com.example.data.DemoAccounts
import com.example.ui.theme.HomezyTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AuthAndRbacRobolectricTest {

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun customer_login_displays_customer_home() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // Click demo customer chip in LoginScreen
    composeRule.onNodeWithTag("login_demo_customer_chip").performClick()
    composeRule.waitForIdle()

    // Verify Customer interface is active
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()
    composeRule.onNodeWithTag("admin_dashboard_screen").assertDoesNotExist()
  }

  @Test
  fun worker_login_displays_worker_dashboard() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // Click demo worker chip in LoginScreen
    composeRule.onNodeWithTag("login_demo_worker_chip").performClick()
    composeRule.waitForIdle()

    // Verify Worker Dashboard is active
    composeRule.onNodeWithTag("worker_dashboard_screen").assertIsDisplayed()
    composeRule.onNodeWithTag("admin_dashboard_screen").assertDoesNotExist()
  }

  @Test
  fun admin_login_displays_admin_dashboard() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialAuthScreen = AuthScreen.LOGIN)
      }
    }

    // Click demo admin chip in LoginScreen
    composeRule.onNodeWithTag("login_demo_admin_chip").performClick()
    composeRule.waitForIdle()

    // Verify Admin Dashboard is active
    composeRule.onNodeWithTag("admin_dashboard_screen").assertIsDisplayed()
  }

  @Test
  fun logout_returns_to_welcome_screen() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialUser = DemoAccounts.CUSTOMER)
      }
    }

    // Ensure customer home is displayed initially
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()

    // Click app logout button in top bar
    composeRule.onNodeWithTag("app_logout_btn").performClick()
    composeRule.waitForIdle()

    // Verify returned to welcome screen
    composeRule.onNodeWithTag("welcome_screen").assertIsDisplayed()
  }

  @Test
  fun customer_cannot_access_admin_redirects_to_home() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialUser = DemoAccounts.CUSTOMER)
      }
    }

    // Ensure Customer Home is active
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()

    // Tap on the Admin role tab
    composeRule.onNodeWithTag("role_tab_admin").performClick()
    composeRule.waitForIdle()

    // Verify user is NOT permitted to admin and remains safely on Customer Home
    composeRule.onNodeWithTag("customer_home_screen").assertIsDisplayed()
    composeRule.onNodeWithTag("admin_dashboard_screen").assertDoesNotExist()
  }

  @Test
  fun worker_cannot_access_admin_redirects_to_dashboard() {
    composeRule.setContent {
      HomezyTheme {
        HomezyApp(initialUser = DemoAccounts.WORKER)
      }
    }

    // Ensure Worker Dashboard is active
    composeRule.onNodeWithTag("worker_dashboard_screen").assertIsDisplayed()

    // Tap on Admin role tab
    composeRule.onNodeWithTag("role_tab_admin").performClick()
    composeRule.waitForIdle()

    // Verify worker remains safely on Worker Dashboard
    composeRule.onNodeWithTag("worker_dashboard_screen").assertIsDisplayed()
    composeRule.onNodeWithTag("admin_dashboard_screen").assertDoesNotExist()
  }
}

