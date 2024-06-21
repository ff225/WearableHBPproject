package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MySleepClassifyEventDao {
    @Query("SELECT * FROM sleep_classify_events_table ORDER BY time_stamp_seconds DESC")
    fun getAll(): Flow<List<MySleepClassifyEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleepClassifyEventEntity: MySleepClassifyEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sleepClassifyEventEntities: List<MySleepClassifyEventEntity>)

    @Delete
    suspend fun delete(sleepClassifyEventEntity: MySleepClassifyEventEntity)

    @Query("DELETE FROM sleep_classify_events_table")
    suspend fun deleteAll()
}