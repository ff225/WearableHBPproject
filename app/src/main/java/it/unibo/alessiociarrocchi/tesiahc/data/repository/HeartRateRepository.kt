package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.HeartRateAggregateDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity

class HeartRateRepository(
    private val heartDao: HeartRateAggregateDao
) {

    suspend fun getItem(id: Int): HeartRateAggregateEntity = heartDao.getItem(id)

    suspend fun getItemByExternalId(uid: String): HeartRateAggregateEntity? =
        heartDao.getHRAFromFK(uid)

    suspend fun getUnsyncedItems(): List<HeartRateAggregateEntity> =
        heartDao.getItemsUnsynced()

    suspend fun getItems(): List<HeartRateAggregateEntity> = heartDao.getAllHRA()


    suspend fun insertItem(item: HeartRateAggregateEntity) = heartDao.insert(item)
}