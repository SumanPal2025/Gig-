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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ButtonVariant
import com.example.ui.components.HomezyButton
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*

@Composable
fun ForgotPasswordScreen(
  onNavigateBack: () -> Unit,
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  var email by remember { mutableStateOf("customer@homezy.demo") }
  var isSent by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  fun handleSendReset() {
    errorMessage = null
    if (email.trim().isEmpty() || !email.contains("@")) {
      errorMessage = "Please enter a valid registered email"
      return
    }
    isSent = true
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("forgot_password_screen")
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier.testTag("forgot_password_back_btn")
      ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomezyText)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Reset Password",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Enter your cooperative registered email to receive a password reset link and temporary recovery OTP.",
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      lineHeight = 18.sp
    )

    Spacer(modifier = Modifier.height(20.dp))

    if (isSent) {
      HomezyCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = HomezyPrimaryContainer
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.MarkEmailRead,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Reset Instructions Sent!",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = HomezyPrimary
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "A secure reset link and development OTP have been sent to $email. Please check your inbox.",
            fontSize = 13.sp,
            color = HomezyText,
            lineHeight = 18.sp
          )
          Spacer(modifier = Modifier.height(16.dp))
          HomezyButton(
            text = "Return to Login",
            onClick = onNavigateToLogin,
            variant = ButtonVariant.PRIMARY,
            icon = Icons.Default.Login,
            modifier = Modifier.fillMaxWidth().testTag("forgot_return_to_login_btn")
          )
        }
      }
    } else {
      HomezyCard(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = email,
          onValueChange = {
            email = it
            errorMessage = null
          },
          label = { Text("Registered Email Address") },
          placeholder = { Text("customer@homezy.demo") },
          leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = HomezyPrimary) },
          singleLine = true,
          textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 15.sp),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            handleSendReset()
          }),
          modifier = Modifier.fillMaxWidth().testTag("forgot_password_email_input"),
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
          text = "Send Recovery Email & OTP",
          onClick = {
            focusManager.clearFocus()
            handleSendReset()
          },
          variant = ButtonVariant.PRIMARY,
          icon = Icons.Default.Send,
          modifier = Modifier.fillMaxWidth().testTag("forgot_password_submit_btn")
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Remembered your password? ",
        fontSize = 13.sp,
        color = HomezyTextSecondary
      )
      Text(
        text = "Back to Sign In",
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyPrimary,
        modifier = Modifier
          .clickable { onNavigateToLogin() }
          .padding(4.dp)
          .testTag("forgot_back_to_login_text")
      )
    }
  }
}
