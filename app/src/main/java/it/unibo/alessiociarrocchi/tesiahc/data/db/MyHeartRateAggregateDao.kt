package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.util.UUID

@Dao
interface MyHeartRateAggregateDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(myhraEntity: MyHeartRateAggregateEntity)

    @Query("SELECT * FROM my_heartrate_aggregate")
    fun getAllHRA(): List<MyHeartRateAggregateEntity>

    @Query("SELECT * FROM my_heartrate_aggregate WHERE coll_bp_id=(:coll_bp_id)")
    fun getHRA_ByBP(coll_bp_id: Int): MyHeartRateAggregateEntity?

    @Query("SELECT * FROM my_heartrate_aggregate WHERE id=(:id)")
    fun getItem(id: Int): MyHeartRateAggregateEntity

}
