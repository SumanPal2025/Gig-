package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.BookingStatus
import com.example.ui.theme.*

enum class ButtonVariant {
  PRIMARY, SECONDARY, ACCENT, OUTLINE, DANGER, TEXT
}

@Composable
fun HomezyButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  variant: ButtonVariant = ButtonVariant.PRIMARY,
  icon: ImageVector? = null,
  enabled: Boolean = true,
  fullWidth: Boolean = false,
  testTag: String = "homezy_button"
) {
  val baseModifier = if (fullWidth) modifier.fillMaxWidth() else modifier
  val interactiveModifier = baseModifier
    .defaultMinSize(minHeight = 48.dp)
    .testTag(testTag)

  when (variant) {
    ButtonVariant.PRIMARY -> {
      Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = HomezyPrimary,
          contentColor = Color.White,
          disabledContainerColor = HomezyPrimary.copy(alpha = 0.4f),
          disabledContentColor = Color.White.copy(alpha = 0.8f)
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
    ButtonVariant.SECONDARY -> {
      Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = HomezySecondary,
          contentColor = Color.White
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
    ButtonVariant.ACCENT -> {
      Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = HomezyAccent,
          contentColor = HomezyText
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
    ButtonVariant.OUTLINE -> {
      OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, HomezyPrimary),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = HomezyPrimary
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
    ButtonVariant.DANGER -> {
      Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = HomezyError,
          contentColor = Color.White
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
    ButtonVariant.TEXT -> {
      TextButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
          contentColor = HomezyPrimary
        ),
        modifier = interactiveModifier
      ) {
        ButtonInnerContent(icon, text)
      }
    }
  }
}

@Composable
private fun ButtonInnerContent(icon: ImageVector?, text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
  ) {
    if (icon != null) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
    }
    Text(
      text = text,
      fontWeight = FontWeight.SemiBold,
      fontSize = 15.sp
    )
  }
}

@Composable
fun HomezyCard(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  elevation: Dp = 1.dp,
  backgroundColor: Color = HomezyCard,
  borderColor: Color = HomezyBorder,
  content: @Composable ColumnScope.() -> Unit
) {
  val shape = RoundedCornerShape(16.dp)
  val cardModifier = modifier
    .shadow(elevation, shape, clip = false)
    .clip(shape)
    .background(backgroundColor)
    .border(BorderStroke(1.dp, borderColor), shape)
    .then(
      if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    )
    .padding(16.dp)

  Column(
    modifier = cardModifier,
    content = content
  )
}

@Composable
fun HomezyBadge(
  text: String,
  modifier: Modifier = Modifier,
  containerColor: Color = HomezyPrimaryContainer,
  contentColor: Color = HomezyPrimary,
  icon: ImageVector? = null
) {
  Surface(
    shape = RoundedCornerShape(20.dp),
    color = containerColor,
    contentColor = contentColor,
    modifier = modifier
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
      if (icon != null) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
      }
      Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      )
    }
  }
}

@Composable
fun StatusBadge(
  status: BookingStatus,
  modifier: Modifier = Modifier
) {
  val (bgColor, textColor, icon) = when (status) {
    BookingStatus.PENDING -> Triple(HomezyAccentContainer, HomezyWarning, Icons.Default.Schedule)
    BookingStatus.ACCEPTED -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), Icons.Default.ThumbUp)
    BookingStatus.ASSIGNED -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), Icons.Default.Person)
    BookingStatus.IN_PROGRESS -> Triple(HomezyPrimaryContainer, HomezyPrimary, Icons.Default.Handyman)
    BookingStatus.COMPLETED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), Icons.Default.CheckCircle)
    BookingStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), Icons.Default.Cancel)
  }

  HomezyBadge(
    text = status.label,
    containerColor = bgColor,
    contentColor = textColor,
    icon = icon,
    modifier = modifier
  )
}

@Composable
fun HomezyInput(
  value: String,
  onValueChange: (String) -> Unit,
  label: String,
  modifier: Modifier = Modifier,
  placeholder: String = "",
  leadingIcon: ImageVector? = null,
  trailingIcon: ImageVector? = null,
  onTrailingIconClick: (() -> Unit)? = null,
  isError: Boolean = false,
  errorMessage: String? = null,
  singleLine: Boolean = true,
  testTag: String = "homezy_input"
) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = label,
      fontWeight = FontWeight.Medium,
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      modifier = Modifier.padding(bottom = 6.dp)
    )
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier
        .fillMaxWidth()
        .testTag(testTag),
      placeholder = {
        if (placeholder.isNotEmpty()) {
          Text(placeholder, color = HomezyTextSecondary.copy(alpha = 0.6f), fontSize = 14.sp)
        }
      },
      leadingIcon = leadingIcon?.let {
        {
          Icon(imageVector = it, contentDescription = null, tint = HomezySecondary)
        }
      },
      trailingIcon = trailingIcon?.let {
        {
          IconButton(onClick = { onTrailingIconClick?.invoke() }) {
            Icon(imageVector = it, contentDescription = null, tint = HomezyTextSecondary)
          }
        }
      },
      isError = isError,
      singleLine = singleLine,
      shape = RoundedCornerShape(12.dp),
      textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black, fontSize = 15.sp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedBorderColor = HomezyPrimary,
        unfocusedBorderColor = HomezyBorder,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        errorBorderColor = HomezyError,
        cursorColor = Color.Black,
        focusedLabelColor = HomezyPrimary,
        unfocusedLabelColor = HomezyTextSecondary
      )
    )
    if (isError && !errorMessage.isNullOrEmpty()) {
      Text(
        text = errorMessage,
        color = HomezyError,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
      )
    }
  }
}

@Composable
fun HomezySearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  placeholder: String = "What service do you need?",
  onClear: () -> Unit = { onQueryChange("") },
  testTag: String = "homezy_search_bar"
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag)
      .shadow(1.dp, RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    color = HomezyCard,
    border = BorderStroke(1.dp, HomezyBorder)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search",
        tint = HomezyPrimary,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Box(modifier = Modifier.weight(1f)) {
        if (query.isEmpty()) {
          Text(
            text = placeholder,
            color = HomezyTextSecondary.copy(alpha = 0.7f),
            fontSize = 15.sp
          )
        }
        androidx.compose.foundation.text.BasicTextField(
          value = query,
          onValueChange = onQueryChange,
          singleLine = true,
          textStyle = androidx.compose.ui.text.TextStyle(
            color = Color.Black,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
          ),
          modifier = Modifier.fillMaxWidth()
        )
      }
      if (query.isNotEmpty()) {
        IconButton(
          onClick = onClear,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Clear search",
            tint = HomezyTextSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  modifier: Modifier = Modifier,
  subtext: String? = null,
  icon: ImageVector? = null,
  isPositive: Boolean? = null,
  badgeText: String? = null,
  containerColor: Color = HomezyCard
) {
  HomezyCard(
    modifier = modifier,
    backgroundColor = containerColor,
    elevation = 1.dp
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.Top
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium,
          color = HomezyTextSecondary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = value,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
      }
      if (icon != null) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(HomezyPrimaryContainer),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = HomezyPrimary,
            modifier = Modifier.size(22.dp)
          )
        }
      }
    }

    if (subtext != null || badgeText != null) {
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        if (badgeText != null) {
          HomezyBadge(
            text = badgeText,
            containerColor = if (isPositive == true) Color(0xFFDCFCE7) else HomezyPrimaryContainer,
            contentColor = if (isPositive == true) Color(0xFF15803D) else HomezyPrimary
          )
        }
        if (subtext != null) {
          Text(
            text = subtext,
            fontSize = 12.sp,
            color = HomezyTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }
  }
}

@Composable
fun LoadingState(
  message: String = "Loading cooperative data...",
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(40.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    CircularProgressIndicator(
      color = HomezyPrimary,
      strokeWidth = 3.dp,
      modifier = Modifier.size(42.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = message,
      fontSize = 14.sp,
      color = HomezyTextSecondary,
      textAlign = TextAlign.Center
    )
  }
}

@Composable
fun EmptyState(
  title: String,
  description: String,
  modifier: Modifier = Modifier,
  icon: ImageVector = Icons.Outlined.Inbox,
  actionText: String? = null,
  onAction: (() -> Unit)? = null
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(64.dp)
        .clip(CircleShape)
        .background(HomezyPrimaryContainer),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = HomezyPrimary,
        modifier = Modifier.size(32.dp)
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 17.sp,
      color = HomezyText,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = description,
      fontSize = 14.sp,
      color = HomezyTextSecondary,
      textAlign = TextAlign.Center,
      lineHeight = 20.sp
    )
    if (actionText != null && onAction != null) {
      Spacer(modifier = Modifier.height(20.dp))
      HomezyButton(
        text = actionText,
        onClick = onAction,
        variant = ButtonVariant.OUTLINE
      )
    }
  }
}

@Composable
fun ErrorState(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Box(
      modifier = Modifier
        .size(56.dp)
        .clip(CircleShape)
        .background(Color(0xFFFEE2E2)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = null,
        tint = HomezyError,
        modifier = Modifier.size(28.dp)
      )
    }
    Spacer(modifier = Modifier.height(14.dp))
    Text(
      text = "Something went wrong",
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = HomezyText
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = message,
      fontSize = 13.sp,
      color = HomezyTextSecondary,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    HomezyButton(
      text = "Retry Action",
      onClick = onRetry,
      variant = ButtonVariant.PRIMARY
    )
  }
}

@Composable
fun HomezyModal(
  visible: Boolean,
  onDismissRequest: () -> Unit,
  title: String,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  if (visible) {
    Dialog(onDismissRequest = onDismissRequest) {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = HomezyCard,
        border = BorderStroke(1.dp, HomezyBorder),
        shadowElevation = 8.dp,
        modifier = modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = title,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = HomezyText
            )
            IconButton(
              onClick = onDismissRequest,
              modifier = Modifier.size(32.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close dialog",
                tint = HomezyTextSecondary
              )
            }
          }
          Divider(
            color = HomezyBorder,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
          )
          content()
        }
      }
    }
  }
}
