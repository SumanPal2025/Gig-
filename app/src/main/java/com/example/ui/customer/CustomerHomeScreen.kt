package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.data.ServiceItem
import com.example.data.WorkerProfile
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CustomerHomeScreen(
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onServiceSelect: (ServiceItem) -> Unit,
  onWorkerSelect: (WorkerProfile) -> Unit,
  onInstaHelpClick: () -> Unit,
  onSmartMatchClick: () -> Unit = {},
  onFairPriceClick: () -> Unit = {},
  onLocationClick: () -> Unit = {},
  onNotificationClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val services = SampleData.services
  val workers = SampleData.workers

  val filteredServices = remember(searchQuery) {
    if (searchQuery.isBlank()) services
    else services.filter {
      it.name.contains(searchQuery, ignoreCase = true) ||
      it.shortDescription.contains(searchQuery, ignoreCase = true) ||
      it.category.contains(searchQuery, ignoreCase = true)
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_home_screen"),
    contentPadding = PaddingValues(bottom = 24.dp)
  ) {
    // Top Bar with Location and Notifications
    item {
      HomezyTopBar(
        locationText = "Indiranagar 100ft Rd, Bengaluru",
        onLocationClick = onLocationClick,
        onNotificationClick = onNotificationClick
      )
    }

    // Hero Greeting & Brand Header
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp)
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Good morning 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Connecting homes with verified cooperative workers.",
            fontSize = 13.sp,
            color = HomezyTextSecondary
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        HomezySearchBar(
          query = searchQuery,
          onQueryChange = onSearchQueryChange,
          placeholder = "What service do you need?"
        )
      }
    }

    // Main Emergency Feature: INSTA HELP
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        InstaHelpBanner(onGetHelpNow = onInstaHelpClick)
      }
    }

    // AI Smart Matching Prototype Banner
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        Card(
          onClick = onSmartMatchClick,
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_smart_match_card")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF2563EB)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Smart Match",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF1E3A8A)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFDBEAFE)
                ) {
                  Text(
                    text = "AI PROTOTYPE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D4ED8),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "HOMEZY finds suitable workers using skill, distance, availability, rating and workload.",
                fontSize = 11.sp,
                color = Color(0xFF1E40AF),
                lineHeight = 15.sp
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open Smart Match",
              tint = Color(0xFF1D4ED8),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // AI Fair Price Assistant Banner
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Card(
          onClick = onFairPriceClick,
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("home_fair_price_card")
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF16A34A)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Handshake,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Fair Price Assistant",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF14532D)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFFDCFCE7)
                ) {
                  Text(
                    text = "AI-ASSISTED",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF15803D),
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                  )
                }
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Explainable pricing suggestions (e.g. ₹525–₹575) and democratic 2-way negotiation.",
                fontSize = 11.sp,
                color = Color(0xFF166534),
                lineHeight = 15.sp
              )
            }
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Open Fair Price",
              tint = Color(0xFF15803D),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // Main Services Section
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp, bottom = 8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Main Services",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Text(
            text = "Cooperative Rates",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HomezySecondary
          )
        }

        Spacer(modifier = Modifier.height(10.dp))
      }
    }

    // Service Cards (AC Service, Electrician, Plumber)
    items(filteredServices) { service ->
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        ServiceCard(
          service = service,
          onViewService = onServiceSelect
        )
      }
    }

    // Popular Near You (Verified cooperative workers)
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 20.dp, bottom = 8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Popular Near You",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = HomezyText
            )
            Text(
              text = "Verified cooperative craftspeople ready to assist",
              fontSize = 12.sp,
              color = HomezyTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
      }
    }

    items(workers.take(4)) { worker ->
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
        WorkerCard(
          worker = worker,
          onBookWorker = onWorkerSelect
        )
      }
    }

    // Why HOMEZY?
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        WhyHomezyCards()
      }
    }
  }
}
