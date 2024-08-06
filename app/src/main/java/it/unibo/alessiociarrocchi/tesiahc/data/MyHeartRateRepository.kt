package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.toDate
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService

class MyHeartRateRepository(
    private val myDB: MyLocalDatabase,
    private val executor: ExecutorService
) {

    private val hrDao = myDB.bpHRDao()

    fun getItem(id: Int): MyHeartRateAggregateEntity = hrDao.getItem(id)

    fun getItemByExternalId(uid: Int): MyHeartRateAggregateEntity? = hrDao.getHRA_ByBP(uid)

    fun getItems(): List<MyHeartRateAggregateEntity>{
        return hrDao.getAllHRA()
    }

    fun insertItem(item: MyHeartRateAggregateEntity){
        executor.execute {
            hrDao.insert(item)
        }
    }


    companion object {
        @Volatile private var INSTANCE: MyHeartRateRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): MyHeartRateRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyHeartRateRepository(
                    MyLocalDatabase.getDatabase(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }
}