package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.*
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodOxygenEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface BloodOxygenDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: BloodOxygenEntity)

    @Update
    suspend fun update(item: BloodOxygenEntity)

    @Delete
    suspend fun delete(item: BloodOxygenEntity)

    @Query("SELECT * from blood_oxygen_table WHERE id = :id")
    suspend fun getItem(id: Int): BloodOxygenEntity

    @Query(
        "SELECT * FROM blood_oxygen_table" +
                " WHERE date(time / 1000,'unixepoch')>=date(:startDate / 1000,'unixepoch') AND date(time / 1000,'unixepoch')<=date(:endDate/ 1000,'unixepoch') ORDER BY time DESC"
    )
    fun getBOByDates(startDate: Date, endDate: Date): Flow<List<BloodOxygenEntity>>

    @Query("SELECT * from blood_oxygen_table WHERE synced=0 ORDER BY time ASC")
    suspend fun getBOUnsynced(): List<BloodOxygenEntity>
}