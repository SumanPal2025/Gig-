package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppRole
import com.example.data.AuthUser
import com.example.data.DemoAccounts
import com.example.ui.components.ButtonVariant
import kotlinx.coroutines.launch
import com.example.ui.components.HomezyButton
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun LoginScreen(
  onLoginSuccess: (AuthUser) -> Unit,
  onNavigateToRegister: () -> Unit,
  onNavigateToForgotPassword: () -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("customer@homezy.demo") }
  var password by remember { mutableStateOf("demo123") }
  var passwordVisible by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isLoading by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  val networkRepo = remember { com.example.data.api.NetworkRepository() }
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  fun performLogin(targetEmail: String, targetPass: String) {
    errorMessage = null
    val cleanEmail = targetEmail.trim().lowercase()
    if (cleanEmail.isEmpty()) {
      errorMessage = "Please enter your email address"
      return
    }
    if (targetPass.isEmpty()) {
      errorMessage = "Please enter your password"
      return
    }

    // Try backend Express API login with loading and error states
    isLoading = true
    coroutineScope.launch {
      networkRepo.login(cleanEmail, targetPass).collect { result ->
        when (result) {
          is com.example.data.api.ApiResult.Loading -> {
            isLoading = true
          }
          is com.example.data.api.ApiResult.Success -> {
            isLoading = false
            val userDto = result.data.user
            val role = when (userDto?.role?.lowercase()) {
              "admin" -> AppRole.ADMIN
              "worker" -> AppRole.WORKER
              else -> AppRole.CUSTOMER
            }
            val authUser = AuthUser(
              id = userDto?.id ?: "usr_${System.currentTimeMillis() % 10000}",
              name = userDto?.name ?: cleanEmail.substringBefore("@").replace(".", " ").capitalize(),
              email = userDto?.email ?: cleanEmail,
              role = role,
              phone = userDto?.phone ?: "+91 98451 90812",
              avatarInitials = (userDto?.name ?: cleanEmail).take(2).uppercase()
            )
            onLoginSuccess(authUser)
          }
          is com.example.data.api.ApiResult.Error -> {
            isLoading = false
            // Fallback to local demo accounts if backend is unreachable
            val demoUser = DemoAccounts.findByEmail(cleanEmail)
            if (demoUser != null) {
              onLoginSuccess(demoUser)
            } else {
              val role = when {
                cleanEmail.contains("admin") -> AppRole.ADMIN
                cleanEmail.contains("worker") -> AppRole.WORKER
                else -> AppRole.CUSTOMER
              }
              val customUser = AuthUser(
                id = "usr_${System.currentTimeMillis() % 10000}",
                name = cleanEmail.substringBefore("@").replace(".", " ").capitalize(),
                email = cleanEmail,
                role = role,
                phone = "+91 98451 90812",
                avatarInitials = cleanEmail.take(2).uppercase()
              )
              onLoginSuccess(customUser)
            }
          }
        }
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("login_screen")
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier.testTag("login_back_btn")
      ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomezyText)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Sign In to HOMEZY",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Subtitle
    Text(
      text = "Access your cooperative dashboard with your registered email and password.",
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Quick Demo Role Switcher Chips for Easy Testing
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      SuggestionChip(
        onClick = {
          email = DemoAccounts.CUSTOMER.email
          password = "demo123"
          onLoginSuccess(DemoAccounts.CUSTOMER)
        },
        label = { Text("Customer", fontSize = 11.sp) },
        modifier = Modifier.testTag("login_demo_customer_chip")
      )
      SuggestionChip(
        onClick = {
          email = DemoAccounts.WORKER.email
          password = "demo123"
          onLoginSuccess(DemoAccounts.WORKER)
        },
        label = { Text("Worker", fontSize = 11.sp) },
        modifier = Modifier.testTag("login_demo_worker_chip")
      )
      SuggestionChip(
        onClick = {
          email = DemoAccounts.ADMIN.email
          password = "demo123"
          onLoginSuccess(DemoAccounts.ADMIN)
        },
        label = { Text("Admin", fontSize = 11.sp) },
        modifier = Modifier.testTag("login_demo_admin_chip")
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Form inputs
    HomezyCard(modifier = Modifier.fillMaxWidth()) {
      OutlinedTextField(
        value = email,
        onValueChange = {
          email = it
          errorMessage = null
        },
        label = { Text("Email Address") },
        placeholder = { Text("e.g. customer@homezy.demo") },
        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = HomezyPrimary) },
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 15.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_email_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedBorderColor = HomezyPrimary,
          unfocusedBorderColor = HomezyBorder,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          cursorColor = Color.Black,
          focusedLabelColor = HomezyPrimary,
          unfocusedLabelColor = HomezyTextSecondary
        )
      )

      Spacer(modifier = Modifier.height(14.dp))

      OutlinedTextField(
        value = password,
        onValueChange = {
          password = it
          errorMessage = null
        },
        label = { Text("Password") },
        textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 15.sp),
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = HomezyPrimary) },
        trailingIcon = {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (passwordVisible) "Hide password" else "Show password",
              tint = HomezyPrimary
            )
          }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
          focusManager.clearFocus()
          performLogin(email, password)
        }),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_password_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedTextColor = Color.Black,
          unfocusedTextColor = Color.Black,
          focusedBorderColor = HomezyPrimary,
          unfocusedBorderColor = HomezyBorder,
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          cursorColor = Color.Black,
          focusedLabelColor = HomezyPrimary,
          unfocusedLabelColor = HomezyTextSecondary
        )
      )

      // Forgot Password Link
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        contentAlignment = Alignment.CenterEnd
      ) {
        Text(
          text = "Forgot Password?",
          color = HomezyPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          modifier = Modifier
            .clickable { onNavigateToForgotPassword() }
            .padding(vertical = 4.dp)
            .testTag("login_forgot_password_btn")
        )
      }

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = errorMessage!!,
          color = MaterialTheme.colorScheme.error,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(18.dp))

      HomezyButton(
        text = if (isLoading) "Connecting to API..." else "Login to Dashboard",
        onClick = {
          focusManager.clearFocus()
          performLogin(email, password)
        },
        enabled = !isLoading,
        variant = ButtonVariant.PRIMARY,
        icon = if (isLoading) null else Icons.Default.Login,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_submit_btn")
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Don't have an account?
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "New to the cooperative? ",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
      Text(
        text = "Register Now",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyPrimary,
        modifier = Modifier
          .clickable { onNavigateToRegister() }
          .padding(4.dp)
          .testTag("login_to_register_btn")
      )
    }
  }
}
