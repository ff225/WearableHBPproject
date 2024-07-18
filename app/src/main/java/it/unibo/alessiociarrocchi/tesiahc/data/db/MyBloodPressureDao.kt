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
    fun insert(item: MyBloodPressureEntity)

    @Update
    fun update(item: MyBloodPressureEntity)

    @Delete
    fun delete(item: MyBloodPressureEntity)

    @Query("SELECT * from my_blood_pressure_table WHERE id = :id")
    fun getItem(id: Int): MyBloodPressureEntity

    @Query("SELECT * from my_blood_pressure_table WHERE uid = :uid")
    fun getItemByExternalId(uid: String): MyBloodPressureEntity?

    @Query("SELECT * from my_blood_pressure_table WHERE id = :id")
    fun getFlowBP(id: Int): Flow<MyBloodPressureEntity>

    @Query("SELECT * from my_blood_pressure_table ORDER BY time DESC")
    fun getAllBP(): Flow<List<MyBloodPressureEntity>>
}