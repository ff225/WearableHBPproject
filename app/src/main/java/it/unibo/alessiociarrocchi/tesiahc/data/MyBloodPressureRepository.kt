package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import androidx.compose.material.ScaffoldState
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.showInfoSnackbar
import it.unibo.alessiociarrocchi.tesiahc.toDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MyBloodPressureRepository(
    private val myDB: MyLocalDatabase,
    private val executor: ExecutorService
) {

    private val bpDao = myDB.bpDao()

    fun getItem(id: Int): MyBloodPressureEntity = bpDao.getItem(id)

    fun getItemByExternalId(uid: String): MyBloodPressureEntity? = bpDao.getItemByExternalId(uid)

    fun getItemsToday(today: String): List<MyBloodPressureEntity>{
        return bpDao.getItemsToday(today.toDate())
    }

    fun getItemsByDates(dataInizio: String, dataFine: String): List<MyBloodPressureEntity>{
        return bpDao.getBPByDates(dataInizio.toDate(), dataFine.toDate())
    }

    fun getItemsUnsynced(): List<MyBloodPressureEntity>{
        return bpDao.getBPBUnsynced()
    }

    /*
    fun getAllItems(): List<MyBloodPressureEntity> = bpDao.getAllBP()

    fun getAllItemsStream(): Flow<List<MyBloodPressureEntity>> = bpDao.getAllBP()

    fun getItemStream(id: Int): Flow<MyBloodPressureEntity?> = bpDao.getFlowBP(id)
    */

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

fun updateBloodPressureDesc(bpId: Int, description: String,
                            context: Context, scaffoldState : ScaffoldState, scope: CoroutineScope
){
    if(bpId > 0){
        val bpRep = MyBloodPressureRepository.getInstance(context, Executors.newSingleThreadExecutor())
        val bpitem = bpRep.getItem(bpId)
        if (bpitem != null){
            if(bpitem.description != description){
                bpitem.synced = 0
            }
            bpitem.description = description
            bpRep.updateItem(bpitem)

            showInfoSnackbar(scaffoldState, scope, "Info salvate correttamente")
        }
        else{
            showInfoSnackbar(scaffoldState, scope, "Misurazione non valida. Uscire e riprovare!")
        }
    }
    else{
        showInfoSnackbar(scaffoldState, scope, "Errore id misurazione. Uscire e riprovare!")
    }
}