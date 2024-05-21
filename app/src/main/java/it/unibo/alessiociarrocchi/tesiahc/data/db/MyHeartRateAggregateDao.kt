package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface MyHeartRateAggregateDao {

    @Query("SELECT * FROM my_heartrate_aggregate WHERE id=(:id)")
    fun getHRA_ByBP(id: Int): LiveData<List<MyHeartRateAggregateEntity>>

    @Query("SELECT * FROM my_heartrate_aggregate WHERE id=(:id)")
    fun getHRA(id: Int): LiveData<MyHeartRateAggregateEntity>

    @Update
    fun update(myhraEntity: MyHeartRateAggregateEntity)

    @Insert
    fun insert(myhraEntity: MyHeartRateAggregateEntity)

    @Insert
    fun insertMultiple(myhraEntity: List<MyHeartRateAggregateEntity>)
}
