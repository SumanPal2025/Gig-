package com.example.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

  // 10.0.2.2 is Android emulator loopback to host machine; 127.0.0.1 for local JVM
  private const val BASE_URL = "http://10.0.2.2:5001/"

  @Volatile
  var authToken: String? = null

  private val authInterceptor = Interceptor { chain ->
    val original = chain.request()
    val builder = original.newBuilder()

    authToken?.let { token ->
      if (token.isNotBlank()) {
        builder.header("Authorization", "Bearer $token")
      }
    }

    builder.header("Accept", "application/json")
    chain.proceed(builder.build())
  }

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .addInterceptor(loggingInterceptor)
    .connectTimeout(5, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()

  private val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

  val apiService: HomezyApiService by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .build()
      .create(HomezyApiService::class.java)
  }
}
