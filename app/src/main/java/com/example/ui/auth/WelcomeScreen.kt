package com.example.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AuthUser
import com.example.ui.theme.*

@Composable
fun WelcomeScreen(
  onGetStarted: () -> Unit,
  onLogin: () -> Unit,
  onQuickDemoLogin: ((AuthUser) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  // Brand colors specified for HOMEZY
  val deepGreen = Color(0xFF146B4A)  // #146B4A
  val teal = Color(0xFF18A67A)       // #18A67A
  val amber = Color(0xFFF4B942)      // #F4B942
  val bgOffWhite = Color(0xFFF7FAF8) // #F7FAF8

  // Subtle breathing scale animation for central house illustration
  val infiniteTransition = rememberInfiniteTransition(label = "welcome_breathing")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.98f,
    targetValue = 1.02f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "illustration_pulse"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(bgOffWhite)
      .windowInsetsPadding(WindowInsets.safeDrawing)
      .padding(horizontal = 28.dp, vertical = 24.dp)
      .testTag("welcome_screen"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top Section: HOMEZY Logo Center
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(top = 12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(deepGreen),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Handshake,
            contentDescription = "HOMEZY Emblem",
            tint = amber,
            modifier = Modifier.size(24.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "HOMEZY",
          fontSize = 26.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 2.sp,
          color = deepGreen,
          modifier = Modifier.testTag("welcome_title")
        )
      }
    }

    // Middle Section: Simple Modern House Illustration & Text
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .padding(vertical = 16.dp)
    ) {
      // Modern Minimalist House Illustration
      Box(
        modifier = Modifier
          .scale(pulseScale)
          .size(180.dp),
        contentAlignment = Alignment.Center
      ) {
        // Soft outer ambient radial glow
        Box(
          modifier = Modifier
            .size(170.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                colors = listOf(
                  teal.copy(alpha = 0.15f),
                  deepGreen.copy(alpha = 0.05f),
                  Color.Transparent
                )
              )
            )
        )

        // Middle soft card badge container
        Box(
          modifier = Modifier
            .size(124.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White)
            .border(1.dp, deepGreen.copy(alpha = 0.10f), RoundedCornerShape(32.dp))
            .shadow(8.dp, RoundedCornerShape(32.dp), spotColor = deepGreen.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(RoundedCornerShape(24.dp))
              .background(
                Brush.linearGradient(
                  colors = listOf(
                    deepGreen,
                    teal
                  )
                )
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.HomeWork,
              contentDescription = "Trusted Local House Services",
              tint = Color.White,
              modifier = Modifier.size(48.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(28.dp))

      // Heading
      Text(
        text = "Trusted Local Services.\nFair Opportunities.",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = deepGreen,
        textAlign = TextAlign.Center,
        lineHeight = 32.sp,
        modifier = Modifier.testTag("welcome_tagline")
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Subtitle
      Text(
        text = "Connect with verified local workers.",
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF51645B),
        textAlign = TextAlign.Center
      )
    }

    // Bottom Section: Get Started & Login Action Buttons
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // [ Get Started ]
      Button(
        onClick = onGetStarted,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .shadow(6.dp, RoundedCornerShape(27.dp), spotColor = deepGreen.copy(alpha = 0.25f))
          .testTag("welcome_get_started_btn"),
        shape = RoundedCornerShape(27.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = deepGreen,
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "Get Started",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      // [ Login ]
      OutlinedButton(
        onClick = onLogin,
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("welcome_login_btn"),
        shape = RoundedCornerShape(27.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, deepGreen),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = deepGreen
        ),
        contentPadding = PaddingValues(horizontal = 24.dp)
      ) {
        Text(
          text = "Login",
          fontSize = 16.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}

