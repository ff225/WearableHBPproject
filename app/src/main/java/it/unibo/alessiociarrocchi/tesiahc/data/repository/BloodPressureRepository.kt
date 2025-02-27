package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.BloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate

class MyBloodPressureRepository(
    private val bloodPressureDao: BloodPressureDao
) {


    suspend fun getItem(id: Int): BloodPressureEntity = bloodPressureDao.getItem(id)

    suspend fun getItemByExternalId(uid: String): BloodPressureEntity? =
        bloodPressureDao.getItemByExternalId(uid)

    suspend fun getItemsToday(today: String): List<BloodPressureEntity> {
        return bloodPressureDao.getItemsToday(today.toDate())
    }

    suspend fun getItemsByDates(dataInizio: String, dataFine: String): List<BloodPressureEntity> {
        return bloodPressureDao.getBPByDates(dataInizio.toDate(), dataFine.toDate())
    }

    suspend fun getItemsUnsynced(): List<BloodPressureEntity> {
        return bloodPressureDao.getBPBUnsynced()
    }


    suspend fun getAllItems(): List<BloodPressureEntity> = bloodPressureDao.getAllBP()
    /*
    fun getAllItemsStream(): Flow<List<MyBloodPressureEntity>> = bloodPressureDao.getAllBP()

    fun getItemStream(id: Int): Flow<MyBloodPressureEntity?> = bloodPressureDao.getFlowBP(id)
    */

    suspend fun insertItem(item: BloodPressureEntity) {
        bloodPressureDao.insert(item)
    }

    suspend fun deleteItem(item: BloodPressureEntity) {

        bloodPressureDao.delete(item)

    }

    suspend fun updateItem(item: BloodPressureEntity) {
        bloodPressureDao.update(item)
    }

}

/* TODO remove
fun updateBloodPressureDesc(
    bpId: Int, description: String,
    context: Context, scaffoldState: ScaffoldState, scope: CoroutineScope
) {
    if (bpId > 0) {
        val bpRep =
            MyBloodPressureRepository.getInstance(context, Executors.newSingleThreadExecutor())
        val bpitem = bpRep.getItem(bpId)
        if (bpitem != null) {
            if (bpitem.description != description) {
                bpitem.synced = 0
            }
            bpitem.description = description
            bpRep.updateItem(bpitem)

            showInfoSnackbar(scaffoldState, scope, "Info salvate correttamente")
        } else {
            showInfoSnackbar(scaffoldState, scope, "Misurazione non valida. Uscire e riprovare!")
        }
    } else {
        showInfoSnackbar(scaffoldState, scope, "Errore id misurazione. Uscire e riprovare!")
    }
}

 */