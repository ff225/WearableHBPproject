package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.BloodOxygenDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodOxygenEntity

class BloodOxygenRepository(
    private val boDao: BloodOxygenDao
) {
    suspend fun getItem(id: Int) = boDao.getItem(id)

    suspend fun deleteItem(item: BloodOxygenEntity) = boDao.delete(item)

    suspend fun updateItem(item: BloodOxygenEntity) = boDao.update(item)

    suspend fun insertItem(item: BloodOxygenEntity) = boDao.insert(item)

    suspend fun getBOUnsynced() = boDao.getBOUnsynced()
}