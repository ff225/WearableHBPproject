package it.unibo.alessiociarrocchi.tesiahc.receivers

import  android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.services.HourNotificationService
//import it.unibo.alessiociarrocchi.tesiahc.services.LocationService

class HourNotificationReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "AHC_hour_notification"
        const val CHANNEL_NAME = "Tesi Health Connect: memo misurazione"
        const val NOTIFICATION_ID = 1
        const val REQUESTCODE = 1
    }

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

        val pendingIntent = PendingIntent.getActivity(context, REQUESTCODE, notificationIntent, PendingIntent.FLAG_MUTABLE);


        val mNotifyBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setWhen(mywhen)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentTitle("Samsung smartwatch")
            .setContentText("E' un buon momento per effettuare la misurazione della pressione arteriosa?")

        notificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build())

        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, notificationSound)
        r.play()
    }
}