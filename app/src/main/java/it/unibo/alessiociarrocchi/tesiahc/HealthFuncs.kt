package it.unibo.alessiociarrocchi.tesiahc

import android.app.Activity.ALARM_SERVICE
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.health.connect.client.records.BloodPressureRecord
import it.unibo.alessiociarrocchi.tesiahc.data.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationTracker
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
//import it.unibo.alessiociarrocchi.tesiahc.funcs.OnLocationChangeListener
//import it.unibo.alessiociarrocchi.tesiahc.funcs.getCurrentLocation
import it.unibo.alessiociarrocchi.tesiahc.funcs.hasLocationPermission
import it.unibo.alessiociarrocchi.tesiahc.presentation.MainActivity
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.concurrent.Executors


fun startHealthDataSync(context: Context){
    if(MainActivity.SERVIZIO_HEALTHDATA == 0) {
        val calendar: Calendar = Calendar.getInstance()
        val intent = Intent(context, HealthDataReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 2, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            (1000 * 60 * 2).toLong(), //15 min
            pendingIntent
        )
    }
    MainActivity.SERVIZIO_HEALTHDATA = 1
}

fun startHealthReminder(context: Context){
    if(MainActivity.SERVIZIO_HEALTHREM == 0){
        val calendar: Calendar = Calendar.getInstance()
        val intent = Intent(context, HourNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_MUTABLE)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            (1000 * 60 * 60 * 2).toLong(), //2H
            pendingIntent
        )
    }
    MainActivity.SERVIZIO_HEALTHREM = 1
}

@RequiresApi(Build.VERSION_CODES.S)
fun syncHeathData(healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager,
                  context: Context){

    //TODO controllare il db, se ha dei valori sincronizzare gli ultimi 30 minuti, altrimenti gli ultimi 30 giorni
    val periodStart = Instant.now().minus(30, ChronoUnit.DAYS)
    val periodEnd = Instant.now()

    // salvataggio dati pressione
    var elencoPressioni : List<BloodPressureRecord>? = null
    runBlocking {
        launch {
            elencoPressioni = healthConnectManager.readBloodPressureList(periodStart, periodEnd).reversed()
        }
    }
    if (elencoPressioni != null){
        if (elencoPressioni!!.count() > 0){
            val bpRep = MyBloodPressureRepository.getInstance(context, Executors.newSingleThreadExecutor())
            val locRep = MyLocationRepository.getInstance(context, Executors.newSingleThreadExecutor())

            for (item in elencoPressioni!!) {
                if (item.metadata.id != null && item.metadata.id != ""){
                    val checkItem = bpRep.getItemByExternalId(item.metadata.id)
                    if (checkItem == null || checkItem.id == 0){
                        var myoffset = 0
                        if(item.zoneOffset != null){
                            myoffset = item.zoneOffset!!.totalSeconds
                        }
                        var myuid = item.metadata.id

                        var myLocation = locRep.getLocationForMeasurement(timestampToLocalTimeZone(instantToLong(item.time), item.zoneOffset!!.totalSeconds))

                        if (myLocation != null){
                            var myLatitude =  myLocation!!.latitude
                            var myLongitude = myLocation.longitude

                            val myBP = MyBloodPressureEntity(
                                uid = myuid,
                                systolic = item.systolic.inMillimetersOfMercury,
                                diastolic = item.diastolic.inMillimetersOfMercury,
                                time = instantToLong(item.time),
                                timezone = myoffset,
                                bodyPosition = item.bodyPosition,
                                measurementLocation = item.measurementLocation,
                                description = "",
                                latitude = myLatitude,
                                longitude = myLongitude
                            )

                            bpRep.insertItem(myBP)

                            // TODO salvare dati HR
                            val savedItem = bpRep.getItemByExternalId(myuid)
                            if (savedItem != null){
                                if (savedItem.id > 0){
                                    var test = bpRep.getAllItemsStream()
                                    var bo = 0
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
    if(context.hasLocationPermission()){
        getCurrentLocation(context, object : OnLocationChangeListener {
            override fun onComplete(location: MyLocationTracker?) {
                if(location != null){
                    if (location.longitude != 0.0 && location.latitude != 0.0 && location.altitude != 0.0){

                    }
                }

            }
        })
    }
    */




    //TODO salvare dati sonno

}
