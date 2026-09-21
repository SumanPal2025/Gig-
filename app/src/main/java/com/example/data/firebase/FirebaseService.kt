package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.data.AppRole
import com.example.data.AuthUser
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FirebaseService {
  private const val TAG = "HomezyFirebase"

  // 11 Core Firestore Collections mandated by Phase 12
  const val COL_USERS = "Users"
  const val COL_WORKER_PROFILES = "WorkerProfiles"
  const val COL_COOPERATIVES = "Cooperatives"
  const val COL_SERVICES = "Services"
  const val COL_BOOKINGS = "Bookings"
  const val COL_PAYMENTS = "Payments"
  const val COL_RATINGS = "Ratings"
  const val COL_CERTIFICATIONS = "Certifications"
  const val COL_WELFARE = "Welfare"
  const val COL_NOTIFICATIONS = "Notifications"
  const val COL_MESSAGES = "Messages"

  private var isInitialized = false
  private var isConfiguredWithValidCredentials = false
  private var authInstance: FirebaseAuth? = null
  private var firestoreInstance: FirebaseFirestore? = null

  fun initialize(context: Context) {
    if (isInitialized) return

    try {
      // Check if Firebase was initialized automatically by GoogleServices plugin
      val apps = FirebaseApp.getApps(context)
      if (apps.isNotEmpty()) {
        val app = FirebaseApp.getInstance()
        val key = app.options.apiKey
        if (key.isNotBlank() && !key.contains("FakeKey", ignoreCase = true) && !key.contains("dummy", ignoreCase = true)) {
          authInstance = FirebaseAuth.getInstance(app)
          firestoreInstance = FirebaseFirestore.getInstance(app)

          try {
            val settings = FirebaseFirestoreSettings.Builder()
              .setPersistenceEnabled(true)
              .build()
            firestoreInstance?.firestoreSettings = settings
          } catch (e: Exception) {
            Log.w(TAG, "Could not set firestoreSettings: ${e.message}")
          }
          isConfiguredWithValidCredentials = true
          Log.i(TAG, "Firebase initialized successfully with valid Google Services credentials.")
        } else {
          Log.i(TAG, "Firebase credentials placeholder detected. Running in local persistent mode.")
        }
      } else {
        Log.i(TAG, "No google-services.json configured. Homezy running in local persistent mode.")
      }

      isInitialized = true
    } catch (e: Exception) {
      Log.w(TAG, "Firebase initialization notice (running in local-first persistent mode): ${e.message}")
      isInitialized = true
    }
  }

  fun getAuth(): FirebaseAuth? = authInstance
  fun getFirestore(): FirebaseFirestore? = firestoreInstance
  fun isReady(): Boolean = isInitialized && isConfiguredWithValidCredentials && authInstance != null && firestoreInstance != null
  fun hasValidRemoteConfig(): Boolean = isConfiguredWithValidCredentials

  // Save/sync a document to Firestore safely
  suspend fun saveDocument(collection: String, documentId: String, data: Map<String, Any?>): Boolean {
    val db = firestoreInstance ?: return false
    return try {
      db.collection(collection).document(documentId).set(data, SetOptions.merge()).await()
      true
    } catch (e: Exception) {
      Log.w(TAG, "Firestore write warning for $collection/$documentId: ${e.message}")
      false
    }
  }

  // Fetch all documents in a collection
  suspend fun getCollection(collection: String): List<Map<String, Any>> {
    val db = firestoreInstance ?: return emptyList()
    return try {
      val snapshot = db.collection(collection).get().await()
      snapshot.documents.mapNotNull { it.data }
    } catch (e: Exception) {
      Log.w(TAG, "Firestore read warning for $collection: ${e.message}")
      emptyList()
    }
  }

  // Get current Firebase Auth user
  fun getCurrentFirebaseUser(): FirebaseUser? {
    return authInstance?.currentUser
  }
}
