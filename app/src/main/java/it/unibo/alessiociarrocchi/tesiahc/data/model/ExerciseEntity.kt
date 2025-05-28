package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercise_table",
    indices = [Index(value = ["uid_health_connect"], unique = true)]
)
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "uid_health_connect")
    val uid: String,
    @ColumnInfo(name = "start_time")
    val startTime: Long,
    @ColumnInfo(name = "start_time_zone")
    val startTimeZone: Int,
    @ColumnInfo(name = "end_time")
    val endTime: Long,
    @ColumnInfo(name = "end_time_zone")
    val endTimeZone: Int,
    @ColumnInfo(name = "exercise_type")
    val exerciseType: String,
    @ColumnInfo(name = "synced")
    val synced: Boolean = false,
)