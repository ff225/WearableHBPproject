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
import it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager
import it.unibo.alessiociarrocchi.tesiahc.data.MyHeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationTracker
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
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
            (1000 * 60 * 15).toLong(), //15 min
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

fun syncHeathData(context: Context){

    val healthConnectManager = MyHealthConnectManager(context)

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
            val hraRep = MyHeartRateRepository.getInstance(context, Executors.newSingleThreadExecutor())
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

                        var myLocation = locRep.getLocationForMeasurement(item.time.toDate())

                        var myLatitude : Double = 0.0
                        var myLongitude : Double = 0.0
                        if (myLocation != null){
                            var myLatitude =  myLocation!!.latitude
                            var myLongitude = myLocation.longitude
                        }

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
                                val hrStart = longtimeToInstant(savedItem.time, myBP.timezone).minus(30, ChronoUnit.MINUTES)
                                val hrEnd = longtimeToInstant(savedItem.time, myBP.timezone)
                                var hrAVG: Long = 0
                                var hrMIN: Long = 0
                                var hrMAX: Long = 0
                                var hrMC: Long = 0
                                runBlocking {
                                    launch {
                                        hrAVG = healthConnectManager.aggregateBPM_AVG(hrStart, hrEnd)
                                        hrMIN = healthConnectManager.aggregateBPM_Min(hrStart, hrEnd)
                                        hrMAX = healthConnectManager.aggregateBPM_Max(hrStart, hrEnd)
                                        hrMC = healthConnectManager.aggregateMeasurements_Count(hrStart, hrEnd)

                                    }
                                }

                                if (hrAVG > 0 && hrMIN > 0 && hrMAX > 0 && hrMC > 0){
                                    val myHRA = MyHeartRateAggregateEntity(
                                        coll_bp_id = savedItem.id,
                                        hrStart = instantToLong(hrStart),
                                        hrEnd = instantToLong(hrEnd),
                                        timzone = myBP.timezone,
                                        hrAVG = hrAVG,
                                        hrMIN = hrMIN,
                                        hrMAX = hrMAX,
                                        hrMC = hrMC
                                    )

                                    hraRep.insertItem(myHRA)
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
