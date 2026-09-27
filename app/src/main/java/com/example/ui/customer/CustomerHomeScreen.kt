package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingItem
import com.example.data.HomezyRepository
import com.example.data.SampleData
import com.example.data.ServiceItem
import com.example.data.WorkerProfile
import com.example.ui.components.*
import com.example.ui.theme.*

data class HomeServiceOption(
  val id: String,
  val name: String,
  val icon: ImageVector,
  val isUrgent: Boolean = false
)

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
  onViewAllServices: () -> Unit = {},
  onViewBookingDetails: ((BookingItem) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val allServices = SampleData.services
  val recentBookings = HomezyRepository.bookings

  // Exactly 6 service options as requested
  val sixServiceOptions = listOf(
    HomeServiceOption("srv_ac", "AC Service", Icons.Default.AcUnit),
    HomeServiceOption("srv_electrician", "Electrician", Icons.Default.ElectricBolt),
    HomeServiceOption("srv_plumber", "Plumber", Icons.Default.Plumbing),
    HomeServiceOption("srv_cleaning", "Cleaning", Icons.Default.CleaningServices),
    HomeServiceOption("srv_salon", "Salon", Icons.Default.Spa),
    HomeServiceOption("srv_insta_help", "Insta Help", Icons.Default.Bolt, isUrgent = true)
  )

  fun handleServiceOptionClick(option: HomeServiceOption) {
    if (option.isUrgent) {
      onInstaHelpClick()
    } else {
      val foundService = allServices.firstOrNull { it.id == option.id }
        ?: allServices.firstOrNull { it.name.contains(option.name, ignoreCase = true) }
        ?: allServices.first()
      onServiceSelect(foundService)
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(HomezyBackground)
      .testTag("customer_home_screen"),
    contentPadding = PaddingValues(bottom = 24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. TOP BAR: HOMEZY logo, location, notification icon
    item {
      HomezyTopBar(
        locationText = "Indiranagar, Bengaluru",
        onLocationClick = onLocationClick,
        onNotificationClick = onNotificationClick
      )
    }

    // 2. GREETING & PRIMARY ACTION: Search + "Book a Service" CTA
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = "Good morning 👋",
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "Verified cooperative household services",
          fontSize = 13.sp,
          color = HomezyTextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Large Search Bar
        HomezySearchBar(
          query = searchQuery,
          onQueryChange = onSearchQueryChange,
          placeholder = "What service do you need?"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Primary Call to Action
        Button(
          onClick = {
            val defaultService = allServices.firstOrNull() ?: SampleData.services.first()
            onServiceSelect(defaultService)
          },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("book_service_primary_cta")
        ) {
          Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Book a Service",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }

    // 3. SIX IMPORTANT SERVICE OPTIONS
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        Text(
          text = "Services",
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = HomezyText
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Grid 3 columns x 2 rows
        for (rowIndex in 0 until 2) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            for (colIndex in 0 until 3) {
              val itemIndex = rowIndex * 3 + colIndex
              if (itemIndex < sixServiceOptions.size) {
                val option = sixServiceOptions[itemIndex]
                Card(
                  onClick = { handleServiceOptionClick(option) },
                  shape = RoundedCornerShape(12.dp),
                  colors = CardDefaults.cardColors(
                    containerColor = if (option.isUrgent) Color(0xFFFEF3C7) else HomezyCard
                  ),
                  border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (option.isUrgent) Color(0xFFFDE68A) else HomezyBorder
                  ),
                  modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .testTag("service_option_${option.id}")
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxSize()
                      .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Box(
                      modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                          if (option.isUrgent) Color(0xFFF59E0B) else HomezyPrimaryContainer
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = option.icon,
                        contentDescription = option.name,
                        tint = if (option.isUrgent) Color.White else HomezyPrimary,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = option.name,
                      fontSize = 12.sp,
                      fontWeight = if (option.isUrgent) FontWeight.Bold else FontWeight.Medium,
                      color = if (option.isUrgent) Color(0xFF92400E) else HomezyText,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis,
                      textAlign = TextAlign.Center
                    )
                  }
                }
              } else {
                Spacer(modifier = Modifier.weight(1f))
              }
            }
          }
          if (rowIndex == 0) {
            Spacer(modifier = Modifier.height(10.dp))
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // "View all services" subtle action
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.CenterEnd
        ) {
          TextButton(
            onClick = {
              val firstService = allServices.firstOrNull() ?: SampleData.services.first()
              onServiceSelect(firstService)
            },
            modifier = Modifier.testTag("view_all_services_btn")
          ) {
            Text(
              text = "View all services →",
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              color = HomezyPrimary
            )
          }
        }
      }
    }

    // 4. INSTA HELP: One clearly visible but compact section
    item {
      Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        InstaHelpBanner(onGetHelpNow = onInstaHelpClick)
      }
    }

    // 5. ONE USEFUL PERSONALIZED/RECENT SECTION
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
      ) {
        val latestBooking = recentBookings.firstOrNull()

        if (latestBooking != null) {
          // "Recent Booking" card
          Text(
            text = "Recent Booking",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(10.dp))

          HomezyCard(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("recent_booking_card")
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.Top
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = latestBooking.serviceName,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Worker: ${latestBooking.workerName}",
                  fontSize = 12.sp,
                  color = HomezyTextSecondary
                )
                Text(
                  text = "Date: ${latestBooking.date} • ${latestBooking.timeSlot}",
                  fontSize = 12.sp,
                  color = HomezyTextSecondary
                )
              }

              Surface(
                shape = RoundedCornerShape(4.dp),
                color = HomezyPrimaryContainer
              ) {
                Text(
                  text = latestBooking.status.name.replace("_", " "),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = HomezyPrimary,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
              onClick = {
                onViewBookingDetails?.invoke(latestBooking) ?: run {
                  val matchedService = allServices.firstOrNull { it.name == latestBooking.serviceName }
                    ?: allServices.first()
                  onServiceSelect(matchedService)
                }
              },
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(containerColor = HomezyPrimary),
              modifier = Modifier
                .fillMaxWidth()
                .testTag("recent_booking_view_details_btn")
            ) {
              Text(
                text = "View Details",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
              )
            }
          }
        } else {
          // "Popular Near You" (2-3 service suggestions)
          Text(
            text = "Popular Near You",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = HomezyText
          )
          Spacer(modifier = Modifier.height(10.dp))

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            allServices.take(3).forEach { service ->
              HomezyCard(
                onClick = { onServiceSelect(service) },
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Box(
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HomezyPrimaryContainer),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = when (service.id) {
                          "srv_ac" -> Icons.Default.AcUnit
                          "srv_electrician" -> Icons.Default.ElectricBolt
                          "srv_plumber" -> Icons.Default.Plumbing
                          "srv_cleaning" -> Icons.Default.CleaningServices
                          else -> Icons.Default.Handyman
                        },
                        contentDescription = null,
                        tint = HomezyPrimary,
                        modifier = Modifier.size(18.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = service.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HomezyText
                      )
                      Text(
                        text = service.priceRange,
                        fontSize = 12.sp,
                        color = HomezyTextSecondary
                      )
                    }
                  }

                  TextButton(onClick = { onServiceSelect(service) }) {
                    Text(
                      text = "Book",
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Bold,
                      color = HomezyPrimary
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
