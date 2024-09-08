package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.data.db.MySettingsEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService

class MySettingsRepository(
    myDB: MyLocalDatabase,
    private val executor: ExecutorService
) {

    private val setDao = myDB.settingsDao()

    fun getItem(id: Int): MySettingsEntity = setDao.getItem(id)
    fun getItem(chiave: String): MySettingsEntity = setDao.getItem(chiave)

    fun updateItem(item: MySettingsEntity){
        executor.execute {
            setDao.updateItem(item.chiave, item.valore)
        }
    }

    fun addItem(item: MySettingsEntity){
        executor.execute {
            setDao.addItem(item)
        }
    }


    companion object {
        @Volatile private var INSTANCE: MySettingsRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): MySettingsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySettingsRepository(
                    MyLocalDatabase.getDatabase(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }
}