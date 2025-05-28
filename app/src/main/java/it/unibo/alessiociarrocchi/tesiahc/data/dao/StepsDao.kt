package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.*
import it.unibo.alessiociarrocchi.tesiahc.data.model.StepsEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface StepsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: StepsEntity)

    @Update
    suspend fun update(item: StepsEntity)

    @Delete
    suspend fun delete(item: StepsEntity)

    @Query("SELECT * from steps_table WHERE id = :id")
    suspend fun getItem(id: Int): StepsEntity

    @Query(
        "SELECT * FROM steps_table" +
                " WHERE date(start_time / 1000,'unixepoch')>=date(:startDate / 1000,'unixepoch') AND date(start_time / 1000,'unixepoch')<=date(:endDate/ 1000,'unixepoch') ORDER BY start_time DESC"
    )
    fun getStepsByDates(startDate: Date, endDate: Date): Flow<List<StepsEntity>>

    @Query("SELECT * from steps_table WHERE synced=0 ORDER BY start_time ASC")
    suspend fun getStepsUnsynced(): List<StepsEntity>
}