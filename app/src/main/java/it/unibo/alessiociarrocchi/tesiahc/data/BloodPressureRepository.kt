package it.unibo.alessiociarrocchi.tesiahc.data

import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureDao
import kotlinx.coroutines.flow.Flow

class BloodPressureRepository(private val bpDao: MyBloodPressureDao) {
    fun getAllItemsStream(): Flow<List<MyBloodPressureEntity>> = bpDao.getAllBP()
    fun getItemStream(id: Int): Flow<MyBloodPressureEntity?> = bpDao.getBP(id)
    suspend fun insertItem(item: MyBloodPressureEntity) = bpDao.insert(item)
    suspend fun deleteItem(item: MyBloodPressureEntity) = bpDao.delete(item)
    suspend fun updateItem(item: MyBloodPressureEntity) = bpDao.update(item)
}