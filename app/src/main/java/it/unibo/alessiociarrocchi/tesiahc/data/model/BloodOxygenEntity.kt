package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blood_oxygen_table",
    indices = [Index(value = ["uid_health_connect"], unique = true)]
)
data class BloodOxygenEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "uid_health_connect")
    val uid: String,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "time_zone")
    val timeZone: Int,
    @ColumnInfo(name = "percentage")
    val percentage: Double,
    @ColumnInfo(name = "synced")
    val synced: Boolean = false,
)