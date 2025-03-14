package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.IPFSDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.IPFSEntity

class IPFSRepository(private val ipfsDao: IPFSDao) {

    suspend fun addData(data: IPFSEntity) {
        ipfsDao.addData(data)
    }

    suspend fun deleteData(data: IPFSEntity) {
        ipfsDao.deleteData(data)
    }
}