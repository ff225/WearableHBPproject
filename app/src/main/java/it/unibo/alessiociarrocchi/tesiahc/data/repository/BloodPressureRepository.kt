package it.unibo.alessiociarrocchi.tesiahc.data.repository

import android.util.Log
import it.unibo.alessiociarrocchi.tesiahc.data.dao.BloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate
import kotlinx.coroutines.flow.Flow

class BloodPressureRepository(
    private val bloodPressureDao: BloodPressureDao
) {


    suspend fun getItem(id: Int): BloodPressureEntity = bloodPressureDao.getItem(id)

    suspend fun getItemByExternalId(uid: String): BloodPressureEntity? =
        bloodPressureDao.getItemByExternalId(uid)

    suspend fun getItemsToday(today: Long): List<BloodPressureEntity> {
        Log.d("BloodPressureRepository", "getItemsToday: ${today}")
        return bloodPressureDao.getItemsToday(today)
    }

    fun getItemsByDates(
        dataInizio: String,
        dataFine: String
    ): Flow<List<BloodPressureEntity>> {
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