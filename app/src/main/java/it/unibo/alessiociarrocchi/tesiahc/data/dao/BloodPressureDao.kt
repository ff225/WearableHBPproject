package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import java.util.Date

@Dao
interface BloodPressureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: BloodPressureEntity)

    @Update
    suspend fun update(item: BloodPressureEntity)

    @Delete
    suspend fun delete(item: BloodPressureEntity)


    @Query("SELECT * from my_blood_pressure_table WHERE id = :id")
    suspend fun getItem(id: Int): BloodPressureEntity

    @Query("SELECT * from my_blood_pressure_table WHERE uid =:uid")
    suspend fun getItemByExternalId(uid: String): BloodPressureEntity?

    @Query("SELECT * from my_blood_pressure_table ORDER BY time DESC")
    suspend fun getAllBP(): List<BloodPressureEntity>

    @Query(
        "SELECT * FROM my_blood_pressure_table" +
                " WHERE date(time / 1000,'unixepoch')>=date(:dataInizio / 1000,'unixepoch') AND date(time / 1000,'unixepoch')<=date(:dataFine/ 1000,'unixepoch') ORDER BY time DESC"
    )
    suspend fun getBPByDates(dataInizio: Date, dataFine: Date): List<BloodPressureEntity>

    @Query("SELECT * FROM my_blood_pressure_table WHERE date(time / 1000,'unixepoch') = date(:today / 1000,'unixepoch') ORDER BY time DESC")
    suspend fun getItemsToday(today: Date): List<BloodPressureEntity>

    @Query("SELECT * from my_blood_pressure_table WHERE synced=0 ORDER BY time ASC")
    suspend fun getBPBUnsynced(): List<BloodPressureEntity>
}