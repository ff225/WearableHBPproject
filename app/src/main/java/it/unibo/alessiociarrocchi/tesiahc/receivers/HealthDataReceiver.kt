package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.services.HealthDataService

class HealthDataReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "AHC_sync"
        const val CHANNEL_NAME = "Tesi Health Connect: sincronizzazione"
        const val NOTIFICATION_ID = 2
        const val REQUESTCODE = 2
    }

    @Override
    override fun onReceive(context: Context, intent: Intent) {
        /*val myIntent = Intent(context, HealthDataService::class.java)
        context.startForegroundService(myIntent)*/

        val myIntent = Intent(context, HealthDataService::class.java).apply {
            action = HealthDataService.ACTION_START
        }
        context.startForegroundService(myIntent)

        /*
        // notifica di inizio sincronizzazione
        val mywhen = System.currentTimeMillis();
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        val notificationIntent = Intent(context, HealthDataService::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, REQUESTCODE, notificationIntent, PendingIntent.FLAG_MUTABLE);

        val mNotifyBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setWhen(mywhen)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentTitle("HEALTH CONNECT")
            .setContentText("Sincronizzazione con Health Connect in corso...")

        notificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build())

        // sincronizza dati health
        runBlocking {
            launch {
                syncHeathData(context)
            }
        }

        // notifica di fine sincronizzazione
        val updatedNotification = mNotifyBuilder
            .setContentText("Sincronizzazione con Health Connect conclusa!")
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())
        //notificationManager.cancel(NOTIFICATION_ID)
         */

    }
}