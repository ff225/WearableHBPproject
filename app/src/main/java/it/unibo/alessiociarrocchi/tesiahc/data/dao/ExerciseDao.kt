package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.*
import it.unibo.alessiociarrocchi.tesiahc.data.model.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ExerciseEntity)

    @Update
    suspend fun update(item: ExerciseEntity)

    @Delete
    suspend fun delete(item: ExerciseEntity)

    @Query("SELECT * from exercise_table WHERE id = :id")
    suspend fun getItem(id: Int): ExerciseEntity

    @Query(
        "SELECT * FROM exercise_table" +
                " WHERE date(start_time / 1000,'unixepoch')>=date(:startDate / 1000,'unixepoch') AND date(start_time / 1000,'unixepoch')<=date(:endDate/ 1000,'unixepoch') ORDER BY start_time DESC"
    )
    fun getExerciseByDates(startDate: Date, endDate: Date): Flow<List<ExerciseEntity>>

    @Query("SELECT * from exercise_table WHERE synced=0 ORDER BY start_time ASC")
    suspend fun getExerciseUnsynced(): List<ExerciseEntity>
}