package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService

class MyBloodPressureRepository(
    private val myDB: MyLocalDatabase,
    private val executor: ExecutorService
) {

    private val bpDao = myDB.bpDao()

    fun getItem(id: Int): MyBloodPressureEntity = bpDao.getItem(id)

    fun getItemByExternalId(uid: String): MyBloodPressureEntity? = bpDao.getItemByExternalId(uid)

    fun getAllItemsStream(): Flow<List<MyBloodPressureEntity>> = bpDao.getAllBP()

    fun getItemStream(id: Int): Flow<MyBloodPressureEntity?> = bpDao.getFlowBP(id)

    fun insertItem(item: MyBloodPressureEntity){
        executor.execute {
            bpDao.insert(item)
        }
    }

    fun deleteItem(item: MyBloodPressureEntity){
        executor.execute {
            bpDao.delete(item)
        }
    }

    fun updateItem(item: MyBloodPressureEntity){
        bpDao.update(item)
    }

    companion object {
        @Volatile private var INSTANCE: MyBloodPressureRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): MyBloodPressureRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyBloodPressureRepository(
                    MyLocalDatabase.getDatabase(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }
}