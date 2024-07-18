package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase

interface AppContainer {
    //val bpRepository: MyBloodPressureRepository
}

class AppDataContainer(/*private val context: Context*/) : AppContainer {
    //private val myLocDb = MyLocalDatabase.getDatabase(context)

    /*
    override val bpRepository: MyBloodPressureRepository by lazy {
        MyBloodPressureRepository(myLocDb.bpDao())
    }
    */
}