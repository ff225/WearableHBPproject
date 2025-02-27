package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.HeartRateAggregateDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity

class HeartRateRepository(
    private val heartDao: HeartRateAggregateDao
) {

    suspend fun getItem(id: Int): HeartRateAggregateEntity = heartDao.getItem(id)

    suspend fun getItemByExternalId(uid: Int): HeartRateAggregateEntity? =
        heartDao.getHRA_ByBP(uid)

    suspend fun getItems(): List<HeartRateAggregateEntity> = heartDao.getAllHRA()


    suspend fun insertItem(item: HeartRateAggregateEntity) = heartDao.insert(item)
}