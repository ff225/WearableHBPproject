package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.SettingsDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.MySettingsEntity

class SettingsRepository(
    private val settingsDao: SettingsDao
) {


    suspend fun getItem(id: Int): MySettingsEntity = settingsDao.getItem(id)
    suspend fun getItem(chiave: String): MySettingsEntity = settingsDao.getItem(chiave)

    suspend fun updateItem(chiave: String, valore: String) = settingsDao.updateItem(chiave, valore)

    suspend fun addItem(item: MySettingsEntity) = settingsDao.addItem(item)

}