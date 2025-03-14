package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import it.unibo.alessiociarrocchi.tesiahc.data.model.IPFSEntity

@Dao
interface IPFSDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addData(data: IPFSEntity)

    @Delete
    suspend fun deleteData(data: IPFSEntity)
}