package it.unibo.alessiociarrocchi.tesiahc

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.util.Date
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

fun longtimeToInstant(myTime: Long, myTimeZone: Int): Instant{
  val myinstant = Instant.ofEpochMilli(myTime)
  val myLT = convertLongToDate(myinstant, myTimeZone)
  return myLT.toInstant(ZoneOffset.ofTotalSeconds(myTimeZone))
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

fun convertToLocalDateViaMilisecond(dateToConvert: Date): LocalDate {
  return Instant.ofEpochMilli(dateToConvert.time)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()
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

fun String?.toLongDateStart(): Long {
  val startDate = convertToLocalDateViaMilisecond(this.toDate()).atStartOfDay()
  return startDate.toEpochSecond(ZoneOffset.UTC)
}

fun String?.toLongDateEnd(): Long {
  val endDate = convertToLocalDateViaMilisecond(this.toDate()).plusDays(1).atStartOfDay()
  return endDate.toEpochSecond(ZoneOffset.UTC)
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


/*
fun createNotificationChannel(context:Context, CHANNEL_ID: String, CHANNEL_NAME : String) : NotificationManager {
  val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
  val notificationManager = context.getSystemService(NotificationManager::class.java)
  notificationManager.createNotificationChannel(notificationChannel)

  return notificationManager
}

fun startForegroundMyNotification(context:Context, service: Service, CHANNEL_ID: String, NOTIFICATION_ID: Int, content: String, title:String, logo_small: Int, logo_big: Int) {
  val res: Resources = context.getResources()

  val notification = NotificationCompat.Builder(service, CHANNEL_ID)
    .setSmallIcon(logo_small)
    .setLargeIcon(BitmapFactory.decodeResource(res, logo_big))
    .setShowWhen(true)
    .setAutoCancel(true)
    .setContentTitle(title)
    .setContentText(content)
    .build()
  service.startForeground(NOTIFICATION_ID, notification)
}
 */

