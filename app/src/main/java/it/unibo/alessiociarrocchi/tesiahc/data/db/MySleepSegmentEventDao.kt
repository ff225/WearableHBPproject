/*package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MySleepSegmentEventDao {
    @Query("SELECT * FROM sleep_segment_events_table ORDER BY start_time_millis DESC")
    fun getAll(): Flow<List<MySleepSegmentEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleepSegmentEventEntity: MySleepSegmentEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sleepSegmentEventEntities: List<MySleepSegmentEventEntity>)

    @Delete
    suspend fun delete(sleepSegmentEventEntity: MySleepSegmentEventEntity)

    @Query("DELETE FROM sleep_segment_events_table")
    suspend fun deleteAll()
}*/