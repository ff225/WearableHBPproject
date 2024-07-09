package it.unibo.alessiociarrocchi.tesiahc

import android.app.Activity.ALARM_SERVICE
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.presentation.MainActivity
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar

fun startHealthDataSync(context: Context){
    val calendar2: Calendar = Calendar.getInstance()
    val intent2 = Intent(context, HealthDataReceiver::class.java)
    val pendingIntent2 = PendingIntent.getBroadcast(context, 2, intent2, PendingIntent.FLAG_MUTABLE)
    val am2 = context.getSystemService(ALARM_SERVICE) as AlarmManager
    am2.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar2.getTimeInMillis(),
        (1000 * 60 * 15).toLong(), //15 min
        pendingIntent2
    )

    MainActivity.SERVIZIO_HEALTHDATA = "1"

}

fun startHealthReminder(context: Context){
    val calendar1: Calendar = Calendar.getInstance()
    val intent1 = Intent(context, HourNotificationReceiver::class.java)
    val pendingIntent1 = PendingIntent.getBroadcast(context, 1, intent1, PendingIntent.FLAG_MUTABLE)
    val am1 = context.getSystemService(ALARM_SERVICE) as AlarmManager
    am1.setRepeating(
        AlarmManager.RTC_WAKEUP,
        calendar1.getTimeInMillis(),
        (1000 * 60 * 60 * 2).toLong(), //2H
        pendingIntent1
    )
}

fun syncHeathData(healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager){
    //TODO controllare il db, se ha dei valori sincronizzare gli ultimi 30 minuti, altrimenti gli ultimi 30 giorni
    val periodStart = Instant.now().minus(30, ChronoUnit.DAYS)
    val periodEnd = Instant.now()

    runBlocking {
        launch {
            var test = healthConnectManager.readBloodPressureList(periodStart, periodEnd).reversed()
        }
    }

    //TODO salvare i dati

}
