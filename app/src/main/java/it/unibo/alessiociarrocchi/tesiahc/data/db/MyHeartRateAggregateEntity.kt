package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_heartrate_aggregate")
data class MyHeartRateAggregateEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val coll_bp_id: Int,
    val hrStart: Long,
    val hrEnd: Long,
    val timzone: Int,
    val hrAVG: Long,
    val hrMIN: Long,
    val hrMAX: Long,
    val hrMC: Long
)