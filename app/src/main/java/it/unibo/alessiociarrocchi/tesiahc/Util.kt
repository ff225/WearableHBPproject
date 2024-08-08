package it.unibo.alessiociarrocchi.tesiahc

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import android.graphics.Color as AndroidColor

fun showInfoSnackbar(
  scaffoldState: ScaffoldState,
  scope: CoroutineScope,
  message: String,
) {
  scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      message = message,
      duration = SnackbarDuration.Short
    )
  }
}

fun convertToLocalDateViaMilisecond(dateToConvert: Date, tz: Int): LocalDateTime {
  return Instant.ofEpochMilli(dateToConvert.time)
    .atZone(ZoneOffset.ofTotalSeconds(tz))
    .toLocalDateTime()
}

fun Instant.toDate(): Date{
  val myDate = Date.from(this)
  return myDate
}

fun String?.toLong(): Long {
  val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
  val date = formatter.parse(this)
  return date!!.time
}

fun String?.toDate(): Date {
  val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
  return formatter.parse(this)
}

fun Long?.toDate(): String {
  val dateFormat = SimpleDateFormat("d MMM, y", Locale.getDefault())
  return try {
    dateFormat.format(this)
  } catch (t: Throwable) {
    t.printStackTrace()
    ""
  }
}

fun isOnline(context: Context): Boolean {
  val connectivityManager =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  if (connectivityManager != null) {
    val capabilities =
      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
      if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

        return true
      } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

        return true
      }
    }
  }
  return false
}

fun Color.Companion.fromHex(hex: String): Color {
  return Color(AndroidColor.parseColor(hex))
}