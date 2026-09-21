package com.example.data

data class AuthUser(
  val id: String,
  val name: String,
  val email: String,
  val role: AppRole,
  val phone: String = "+91 98451 90812",
  val trade: String? = null,
  val avatarInitials: String = "HM"
)

object DemoAccounts {
  val CUSTOMER = AuthUser(
    id = "usr_cust_01",
    name = "Priya Sundaram",
    email = "customer@homezy.demo",
    role = AppRole.CUSTOMER,
    phone = "+91 98451 90812",
    avatarInitials = "PS"
  )

  val WORKER = AuthUser(
    id = "usr_work_01",
    name = "Rahul Sharma",
    email = "worker@homezy.demo",
    role = AppRole.WORKER,
    phone = "+91 98765 43210",
    trade = "Licensed Electrician",
    avatarInitials = "RS"
  )

  val ADMIN = AuthUser(
    id = "usr_admin_01",
    name = "Kavita Rao",
    email = "admin@homezy.demo",
    role = AppRole.ADMIN,
    phone = "+91 98111 22334",
    avatarInitials = "KR"
  )

  val customer get() = CUSTOMER
  val worker get() = WORKER
  val admin get() = ADMIN

  val all = listOf(CUSTOMER, WORKER, ADMIN)

  fun findByEmail(email: String): AuthUser? {
    val clean = email.trim().lowercase()
    return all.find { it.email.lowercase() == clean }
  }
}

enum class AuthScreen {
  WELCOME,
  LOGIN,
  REGISTER,
  OTP_VERIFY,
  FORGOT_PASSWORD
}
