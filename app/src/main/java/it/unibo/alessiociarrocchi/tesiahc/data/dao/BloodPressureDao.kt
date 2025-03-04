package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BloodPressureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: BloodPressureEntity)

    @Update
    suspend fun update(item: BloodPressureEntity)

    @Delete
    suspend fun delete(item: BloodPressureEntity)


    @Query("SELECT * from blood_pressure_table WHERE id = :id")
    suspend fun getItem(id: Int): BloodPressureEntity

    @Query("SELECT * from blood_pressure_table WHERE uid_health_connect =:uid")
    suspend fun getItemByExternalId(uid: String): BloodPressureEntity?

    @Query("SELECT * from blood_pressure_table ORDER BY time DESC")
    suspend fun getAllBP(): List<BloodPressureEntity>

    // TODO work with timestamp instead of Date
    @Query(
        "SELECT * FROM blood_pressure_table" +
                " WHERE date(time / 1000,'unixepoch')>=date(:dataInizio / 1000,'unixepoch') AND date(time / 1000,'unixepoch')<=date(:dataFine/ 1000,'unixepoch') ORDER BY time DESC"
    )
    fun getBPByDates(dataInizio: Date, dataFine: Date): Flow<List<BloodPressureEntity>>

    @Query("SELECT * FROM blood_pressure_table WHERE date = :today")
    suspend fun getItemsToday(today: Long): List<BloodPressureEntity>

    @Query("SELECT * from blood_pressure_table WHERE synced=0 ORDER BY time ASC")
    suspend fun getBPBUnsynced(): List<BloodPressureEntity>
}