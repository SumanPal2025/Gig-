package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.ServiceItem
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerServicesScreen(
  onBookService: (ServiceItem) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var activeModalService by remember { mutableStateOf<ServiceItem?>(null) }
  var searchQuery by remember { mutableStateOf("") }
  var servicesList by remember { mutableStateOf(SampleData.services) }
  var isLoading by remember { mutableStateOf(false) }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  val networkRepo = remember { com.example.data.api.NetworkRepository() }

  LaunchedEffect(Unit) {
    networkRepo.getServices().collect { result ->
      when (result) {
        is com.example.data.api.ApiResult.Loading -> {
          isLoading = true
          errorMessage = null
        }
        is com.example.data.api.ApiResult.Success -> {
          isLoading = false
          if (result.data.isNotEmpty()) {
            servicesList = result.data
          }
        }
        is com.example.data.api.ApiResult.Error -> {
          isLoading = false
          // Fall back to sample services gracefully
          servicesList = SampleData.services
        }
      }
    }
  }

  val categories = listOf("All", "Cooling & Appliances", "Home Electricals", "Plumbing & Sanitation")

  val filteredServices = servicesList.filter {
    (selectedCategory == "All" || it.category == selectedCategory) &&
    (searchQuery.isBlank() || it.name.contains(searchQuery, true) || it.shortDescription.contains(searchQuery, true))
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_services_screen")
  ) {
    LazyColumn(
      contentPadding = PaddingValues(bottom = 24.dp)
    ) {
      // Header
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .background(HomezyCard)
            .padding(16.dp)
        ) {
          Text(
            text = "Cooperative Services",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Standardized rate cards directly supporting cooperative workers",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
          Spacer(modifier = Modifier.height(14.dp))
          HomezySearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search AC, Electrician, Plumber..."
          )
          if (isLoading) {
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
              modifier = Modifier.fillMaxWidth(),
              color = HomezyPrimary,
              trackColor = HomezyPrimary.copy(alpha = 0.2f)
            )
          }
        }
      }

      // Filter category chips
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.forEach { category ->
            val isSelected = category == selectedCategory
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = category },
              label = {
                Text(
                  text = if (category == "Cooling & Appliances") "AC Service" else if (category == "Home Electricals") "Electrician" else if (category == "Plumbing & Sanitation") "Plumber" else category,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = HomezyPrimary,
                selectedLabelColor = Color.White,
                containerColor = HomezyCard,
                labelColor = HomezyText
              ),
              shape = RoundedCornerShape(20.dp)
            )
          }
        }
      }

      // Service items
      items(filteredServices) { service ->
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
          ServiceCard(
            service = service,
            onViewService = { activeModalService = it }
          )
        }
      }

      // Cooperative pledge card
      item {
        Box(modifier = Modifier.padding(16.dp)) {
          HomezyCard(
            backgroundColor = HomezyPrimaryContainer,
            borderColor = HomezyPrimary.copy(alpha = 0.3f)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = HomezyPrimary,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Zero Exploitation Promise",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = HomezyPrimary
                )
                Text(
                  text = "Traditional corporate apps cut 25-30% from workers. HOMEZY retains only 5% to maintain the cooperative server, returning all surplus as member dividends.",
                  fontSize = 12.sp,
                  color = HomezyText,
                  lineHeight = 16.sp
                )
              }
            }
          }
        }
      }
    }

    // Detailed Service Modal
    activeModalService?.let { service ->
      HomezyModal(
        visible = true,
        onDismissRequest = { activeModalService = null },
        title = service.name
      ) {
        Column {
          Text(
            text = service.shortDescription,
            fontSize = 14.sp,
            color = HomezyTextSecondary,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "What is included:",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(8.dp))

          service.popularFeatures.forEach { feature ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 3.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = HomezySecondary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = feature,
                fontSize = 13.sp,
                color = HomezyText
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(HomezySurfaceVariant, RoundedCornerShape(8.dp))
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Typical Duration", fontSize = 11.sp, color = HomezyTextSecondary)
              Text(service.duration, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = HomezyText)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("Starting From", fontSize = 11.sp, color = HomezyTextSecondary)
              Text("₹${service.startingPrice}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = HomezyPrimary)
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            HomezyButton(
              text = "Close",
              onClick = { activeModalService = null },
              variant = ButtonVariant.OUTLINE,
              modifier = Modifier.weight(1f)
            )
            HomezyButton(
              text = "Book Now",
              onClick = {
                val s = activeModalService
                activeModalService = null
                if (s != null) onBookService(s)
              },
              variant = ButtonVariant.PRIMARY,
              modifier = Modifier.weight(1f),
              testTag = "confirm_book_service"
            )
          }
        }
      }
    }
  }
}
