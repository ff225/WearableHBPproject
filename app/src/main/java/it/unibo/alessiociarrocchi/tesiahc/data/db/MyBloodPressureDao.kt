package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MyBloodPressureDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: MyBloodPressureEntity)

    @Update
    suspend fun update(item: MyBloodPressureEntity)

    @Delete
    suspend fun delete(item: MyBloodPressureEntity)

    @Query("SELECT * from my_location_table WHERE id = :id")
    fun getBP(id: Int): Flow<MyBloodPressureEntity>

    @Query("SELECT * from my_location_table ORDER BY id DESC")
    fun getAllBP(): Flow<List<MyBloodPressureEntity>>
}