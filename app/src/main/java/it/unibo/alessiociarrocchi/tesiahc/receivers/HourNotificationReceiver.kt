package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.annotation.SuppressLint
import  android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.services.HourNotificationService
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService

class HourNotificationReceiver: BroadcastReceiver() {

    @Override
    override fun onReceive(context: Context, intent: Intent) {

        /*val myIntent = Intent(context, HourNotificationService::class.java).apply {
            action = HourNotificationService.ACTION_START
        }
        context.startForegroundService(myIntent)*/

        val mywhen = System.currentTimeMillis();
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        val notificationIntent = Intent(context, HourNotificationService::class.java)
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);


        val mNotifyBuilder = NotificationCompat.Builder(context, "AHC_hour_notification")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setWhen(mywhen)
            .setContentIntent(pendingIntent)
            .setContentTitle("Tesi Android Health Connect")
            .setContentText("E' un buon momento per effettuare la misurazione della pressione arteriosa?")

        notificationManager.notify(2, mNotifyBuilder.build())

    }
}