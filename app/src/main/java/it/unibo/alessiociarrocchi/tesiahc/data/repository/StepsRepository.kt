package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.StepsDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.StepsEntity

class StepsRepository(
    private val stepsDao: StepsDao,
) {
    suspend fun getItem(id: Int) = stepsDao.getItem(id)

    suspend fun deleteItem(item: StepsEntity) = stepsDao.delete(item)

    suspend fun updateItem(item: StepsEntity) = stepsDao.update(item)

    suspend fun insertItem(item: StepsEntity) = stepsDao.insert(item)

    suspend fun getStepsUnsynced() = stepsDao.getStepsUnsynced()
}