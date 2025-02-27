package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity

@Dao
interface HeartRateAggregateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(myhraEntity: HeartRateAggregateEntity)

    @Query("SELECT * FROM my_heartrate_aggregate")
    suspend fun getAllHRA(): List<HeartRateAggregateEntity>

    @Query("SELECT * FROM my_heartrate_aggregate WHERE coll_bp_id=(:coll_bp_id)")
    suspend fun getHRA_ByBP(coll_bp_id: Int): HeartRateAggregateEntity?

    @Query("SELECT * FROM my_heartrate_aggregate WHERE id=(:id)")
    suspend fun getItem(id: Int): HeartRateAggregateEntity

}
