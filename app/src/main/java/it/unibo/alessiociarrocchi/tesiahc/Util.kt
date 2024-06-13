package it.unibo.alessiociarrocchi.tesiahc

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoField
import java.util.Date
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

/**
 * Shows details of a given throwable in the snackbar
 */
fun showExceptionSnackbar(
  scaffoldState: ScaffoldState,
  scope: CoroutineScope,
  throwable: Throwable?,
) {
  scope.launch {
    scaffoldState.snackbarHostState.showSnackbar(
      message = throwable?.localizedMessage ?: "Unknown exception",
      duration = SnackbarDuration.Short
    )
  }
}

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


//CONVERSIONE data ed ora per il db
fun instantToLong(myInstant : Instant): Long{
  return myInstant.getLong(ChronoField.INSTANT_SECONDS)
}

//CONVERSIONE data ed ora dal db
fun timestampToLocalTimeZone(myTime: Long, myTimeZone: Int): String{
  val myinstant = Instant.ofEpochMilli(myTime)
  val myLT = convertLongToDate(myinstant, myTimeZone)
  return localDateTimeToString(myLT)
}

fun convertLongToDate(time: Instant, tz: Int): LocalDateTime {
  return LocalDateTime.ofInstant(time, ZoneOffset.ofTotalSeconds(tz))
}

fun localDateTimeToString(_myZonedDT: LocalDateTime, _myFormat: String = ""): String{
  var myFormat = _myFormat;
  if (myFormat == ""){
    myFormat = "dd/MM/yyyy HH:mm:ss"
  }

  val formatter: SimpleDateFormat = SimpleDateFormat(myFormat, Locale.getDefault())
  return formatter.format(_myZonedDT)
}

fun Context.hasLocationPermission(): Boolean {
  return ContextCompat.checkSelfPermission(
    this, Manifest.permission.ACCESS_COARSE_LOCATION
  ) == PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
}

fun String?.toLong(): Long {
  val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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

fun Long?.toYear(): String {
  val dateFormat = SimpleDateFormat("y", Locale.getDefault())
  return try {
    dateFormat.format(this)
  } catch (t: Throwable) {
    t.printStackTrace()
    ""
  }
}

fun Long?.toTime(): String {
  val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
  return try {
    timeFormat.format(this)
  } catch (t: Throwable) {
    t.printStackTrace()
    ""
  }
}
