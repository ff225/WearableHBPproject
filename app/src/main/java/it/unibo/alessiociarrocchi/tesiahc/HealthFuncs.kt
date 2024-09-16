package it.unibo.alessiociarrocchi.tesiahc

import android.app.Activity.ALARM_SERVICE
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.ScaffoldState
import androidx.health.connect.client.records.BloodPressureRecord
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import it.unibo.alessiociarrocchi.tesiahc.data.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager
import it.unibo.alessiociarrocchi.tesiahc.data.MyHeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationTracker
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
//import it.unibo.alessiociarrocchi.tesiahc.funcs.OnLocationChangeListener
//import it.unibo.alessiociarrocchi.tesiahc.funcs.getCurrentLocation
import it.unibo.alessiociarrocchi.tesiahc.presentation.MainActivity
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.concurrent.Executors

// avvia il serivio di sincronizzazione con Health Connect
fun startHealthDataSync(context: Context){
    if(MainActivity.SERVIZIO_HEALTHDATA == 0) {
        val calendar: Calendar = Calendar.getInstance()
        val intent = Intent(context, HealthDataReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, HealthDataReceiver.REQUESTCODE, intent, PendingIntent.FLAG_MUTABLE)
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

// avvia il servizio di notifica "esegui misurazione"
fun startHealthReminder(context: Context){
    if(MainActivity.SERVIZIO_HEALTHREM == 0){
        val calendar: Calendar = Calendar.getInstance()
        val intent = Intent(context, HourNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, HourNotificationReceiver.REQUESTCODE, intent, PendingIntent.FLAG_MUTABLE)
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

// esegue la sincronizzazione con Health Connect
fun syncHealthData(context: Context){

    val healthConnectManager = MyHealthConnectManager(context)

    // controllo gli ultimi 30 giorni, il massimo permesso da Health Connect
    val periodStart = Instant.now().minus(30, ChronoUnit.DAYS)
    val periodEnd = Instant.now()

    // salvataggio dati pressione
    var elencoPressioni : List<BloodPressureRecord>? = null
    runBlocking {
        launch {
            elencoPressioni = healthConnectManager.readBloodPressureList(periodStart, periodEnd)
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
                            myLatitude =  myLocation!!.latitude
                            myLongitude = myLocation.longitude
                        }

                        val myBP = MyBloodPressureEntity(
                            uid = myuid,
                            systolic = item.systolic.inMillimetersOfMercury,
                            diastolic = item.diastolic.inMillimetersOfMercury,
                            time = item.time.toDate(),
                            timezone = myoffset,
                            bodyPosition = item.bodyPosition,
                            measurementLocation = item.measurementLocation,
                            description = "",
                            latitude = myLatitude,
                            longitude = myLongitude,
                            synced = 0
                        )

                        bpRep.insertItem(myBP)

                        // dati HR
                        var savedItem: MyBloodPressureEntity? = null
                        runBlocking {
                            launch {
                                savedItem = bpRep.getItemByExternalId(myuid)
                            }
                        }

                        var test = hraRep.getItems()

                        if (savedItem != null){
                            if (savedItem!!.id > 0){
                                val hrStart =item.time.minus(30, ChronoUnit.MINUTES)
                                val hrEnd = item.time
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

                                val myHRA = MyHeartRateAggregateEntity(
                                    coll_bp_id = savedItem!!.id,
                                    hrStart = hrStart.toDate(),
                                    hrEnd = hrEnd.toDate(),
                                    timezone = myBP.timezone,
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

            // invio dati BP a firestore
            sendAllHealthData(context)
        }
    }

    //TODO salvare dati sonno
}


// invia tutte le misurazioni presenti nel db e non sincronizzate a firestore
fun sendAllHealthData(context: Context){
    if(isOnline(context)){
        val bpRep = MyBloodPressureRepository.getInstance(context, Executors.newSingleThreadExecutor())
        var elencoPressioni : List<MyBloodPressureEntity>? = null
        runBlocking {
            launch {
                elencoPressioni = bpRep.getItemsUnsynced()
            }
        }

        if (elencoPressioni != null) {
            if (elencoPressioni!!.count() > 0) {
                val hraRep = MyHeartRateRepository.getInstance(context, Executors.newSingleThreadExecutor())
                for (bpitem in elencoPressioni!!) {
                    firestoreDocument(context, bpitem, bpRep, hraRep)
                }
            }
        }
    }
}

// invia la singola misurazione a firestore
fun sendSingleHealthData(bpId: Int,
                         context: Context, scaffoldState : ScaffoldState, scope: CoroutineScope,
                         showMsg: Boolean) : Boolean{
    var myout : Boolean = false
    if(bpId > 0){
        val bpRep = MyBloodPressureRepository.getInstance(context, Executors.newSingleThreadExecutor())
        val bpitem = bpRep.getItem(bpId)
        if (bpitem != null){
            if (bpitem.synced == 0){
                if(isOnline(context)){
                    runBlocking {
                        launch {
                            firestoreDocument(context, bpitem, bpRep)
                        }
                    }
                    myout = true
                    if(showMsg){
                        showInfoSnackbar(scaffoldState, scope, "Misurazione inviata correttamente")
                    }
                }
                else{
                    if(showMsg){
                        showInfoSnackbar(scaffoldState, scope, "Internet non disponibile. Riprovare!")
                    }
                }
            }
            else{
                if(showMsg){
                    showInfoSnackbar(scaffoldState, scope, "Misurazione già sincronizzata")
                }
            }
        }
        else{
            if(showMsg){
                showInfoSnackbar(scaffoldState, scope, "Misurazione non valida. Uscire e riprovare!")
            }
        }
    }
    else{
        if(showMsg){
            showInfoSnackbar(scaffoldState, scope, "Errore id misurazione. Uscire e riprovare!")
        }
    }

    return myout
}

// esegue la chiamata a firestore, inviando la misurazione e controllando che  l'id assegnato da health connect sia univoco
private fun firestoreDocument(
    context: Context, bpitem: MyBloodPressureEntity,
    bpRep: MyBloodPressureRepository, hraRep: MyHeartRateRepository? = null){

    val collezione = "bloodpressure"

    if(isOnline(context)){
        val db = Firebase.firestore

        // controllo se la misurazione è già presente su firebase
        db.collection(collezione)
            .whereEqualTo("metadataid", bpitem.uid)
            .get()
            .addOnSuccessListener { documents ->
                var canAdd = true;

                if(documents != null){
                    if(documents.count() > 0){
                        canAdd = false;

                        for(document in documents){
                            if (document.getString("metadataid") == bpitem.uid &&
                                document.getString("user_description") != bpitem.description){
                                // aggiorno la descrizione della misurazione
                                document.reference.update("user_description", bpitem.description)
                            }
                        }

                        // aggiorno il flag nel db locale
                        bpitem.synced = 1
                        bpRep.updateItem(bpitem)
                    }
                }

                if(canAdd){
                    // salvo la misurazione
                    var myHR : MyHeartRateAggregateEntity?
                    var myHRRep = hraRep
                    if (myHRRep == null){
                        myHRRep = MyHeartRateRepository.getInstance(context, Executors.newSingleThreadExecutor())
                    }
                    myHR = myHRRep.getItemByExternalId(bpitem.id)
                    if(myHR == null){
                        myHR = MyHeartRateAggregateEntity(
                            coll_bp_id = 0,
                            hrStart = "1970-1-1".toDate(),
                            hrEnd =  "1970-1-1".toDate(),
                            timezone = 0,
                            hrAVG = 0,
                            hrMIN = 0,
                            hrMAX = 0,
                            hrMC = 0
                        )
                    }

                    val bpToSync = hashMapOf(
                        "metadataid" to bpitem.uid,
                        "systolic" to bpitem.systolic,
                        "diastolic" to bpitem.diastolic,
                        "datetime" to bpitem.time,
                        "timezone" to bpitem.timezone,
                        "bodyPosition" to bpitem.bodyPosition,
                        "measurementLocation" to bpitem.measurementLocation,
                        "user_description" to bpitem.description,
                        "latitude" to bpitem.latitude,
                        "longitude" to bpitem.longitude,
                        "hrStart" to myHR.hrStart,
                        "hrEnd" to myHR.hrEnd,
                        "hrTimezone" to myHR.timezone,
                        "hrAVG" to myHR.hrAVG,
                        "hrMIN" to myHR.hrMIN,
                        "hrMAX" to myHR.hrMAX,
                        "hrMC" to myHR.hrMC,
                    )

                    if(isOnline(context)){
                        db.collection(collezione)
                            .add(bpToSync)
                            .addOnSuccessListener { documentReference ->
                                bpitem.synced = 1
                                bpRep.updateItem(bpitem)
                            }
                            .addOnFailureListener { e ->

                            }
                    }

                }

            }
            .addOnFailureListener { exception ->

            }
    }
}
