package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context

class MyLocationManager private constructor(private val context: Context){

    companion object {
        @Volatile private var INSTANCE: MyLocationManager? = null

        fun getInstance(context: Context): MyLocationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyLocationManager(context).also { INSTANCE = it }
            }
        }
    }

}