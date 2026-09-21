package com.example.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthUser
import com.example.ui.components.ButtonVariant
import com.example.ui.components.HomezyButton
import com.example.ui.components.HomezyCard
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(
  pendingUser: AuthUser?,
  onVerifySuccess: (AuthUser) -> Unit,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var otpCode by remember { mutableStateOf("123456") }
  var timerSeconds by remember { mutableStateOf(30) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isCodeSentFeedback by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current
  val scrollState = rememberScrollState()

  // Countdown timer
  LaunchedEffect(key1 = timerSeconds) {
    if (timerSeconds > 0) {
      delay(1000L)
      timerSeconds -= 1
    }
  }

  fun handleVerify() {
    errorMessage = null
    if (otpCode.trim().length < 6) {
      errorMessage = "Please enter the 6-digit OTP code"
      return
    }

    if (pendingUser != null) {
      onVerifySuccess(pendingUser)
    } else {
      // Fallback
      onVerifySuccess(
        AuthUser(
          id = "usr_${System.currentTimeMillis() % 10000}",
          name = "Verified Member",
          email = "member@homezy.demo",
          role = com.example.data.AppRole.CUSTOMER
        )
      )
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .verticalScroll(scrollState)
      .padding(20.dp)
      .testTag("otp_screen")
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onNavigateBack,
        modifier = Modifier.testTag("otp_back_btn")
      ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HomezyText)
      }
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Security Verification",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = HomezyText
      )
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Info card
    HomezyCard(
      modifier = Modifier.fillMaxWidth(),
      backgroundColor = HomezyPrimaryContainer
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.MarkEmailRead,
          contentDescription = null,
          tint = HomezyPrimary,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "OTP Code Sent",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = HomezyPrimary
          )
          Text(
            text = "Sent to ${pendingUser?.phone ?: "+91 98451 90812"} & ${pendingUser?.email ?: "member@homezy.demo"}",
            fontSize = 12.sp,
            color = HomezyText
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Main OTP Entry Card
    HomezyCard(modifier = Modifier.fillMaxWidth()) {
      Text(
        text = "Enter 6-Digit Verification Code",
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = HomezyText
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Enter the one-time passcode to confirm cooperative registration.",
        fontSize = 12.sp,
        color = HomezyTextSecondary
      )

      Spacer(modifier = Modifier.height(18.dp))

      // Visual 6-digit boxes
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        for (i in 0 until 6) {
          val charAt = otpCode.getOrNull(i)?.toString() ?: ""
          val isFocused = otpCode.length == i
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (charAt.isNotEmpty()) HomezySurfaceVariant else Color.White)
              .border(
                1.5.dp,
                if (isFocused) HomezyPrimary else if (charAt.isNotEmpty()) HomezySecondary else HomezyBorder,
                RoundedCornerShape(8.dp)
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = charAt,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyPrimary
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Direct text field for fast input & keyboard support
      OutlinedTextField(
        value = otpCode,
        onValueChange = { input ->
          if (input.length <= 6 && input.all { it.isDigit() }) {
            otpCode = input
            errorMessage = null
          }
        },
        label = { Text("6-Digit Code") },
        placeholder = { Text("123456") },
        leadingIcon = { Icon(Icons.Default.Pin, contentDescription = null, tint = HomezyPrimary) },
        singleLine = true,
        textStyle = androidx.compose.ui.text.TextStyle(
          color = Color.Black,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
          focusManager.clearFocus()
          handleVerify()
        }),
        modifier = Modifier.fillMaxWidth().testTag("otp_input_field"),
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

      Spacer(modifier = Modifier.height(14.dp))

      // Auto-fill demo button
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = HomezySurfaceVariant,
        onClick = {
          otpCode = "123456"
          errorMessage = null
        },
        modifier = Modifier.fillMaxWidth().testTag("otp_autofill_btn")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(Icons.Default.Bolt, contentDescription = null, tint = HomezyPrimary, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Auto-fill Demo Code (123456)",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezyPrimary
          )
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      HomezyButton(
        text = "Verify & Access Account",
        onClick = {
          focusManager.clearFocus()
          handleVerify()
        },
        variant = ButtonVariant.PRIMARY,
        icon = Icons.Default.CheckCircle,
        modifier = Modifier.fillMaxWidth().testTag("otp_verify_btn")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Resend Timer Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        if (timerSeconds > 0) {
          Text(
            text = "Resend OTP in ${timerSeconds}s",
            fontSize = 12.sp,
            color = HomezyTextSecondary
          )
        } else {
          Text(
            text = "Resend OTP Code",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyPrimary,
            modifier = Modifier
              .clickable {
                timerSeconds = 30
                isCodeSentFeedback = true
              }
              .padding(4.dp)
              .testTag("otp_resend_btn")
          )
        }
      }

      if (isCodeSentFeedback) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "✓ New OTP dispatched! (Use demo code 123456)",
          fontSize = 11.sp,
          color = HomezySecondary,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}
