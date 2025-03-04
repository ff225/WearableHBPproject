package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blood_pressure_table",
    indices = [Index(value = ["uid_health_connect"], unique = true)]
)
data class BloodPressureEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "uid_health_connect")
    val uid: String,
    @ColumnInfo(name = "systolic")
    val systolic: Double,
    @ColumnInfo(name = "diastolic")
    val diastolic: Double,
    @ColumnInfo("body_position")
    val bodyPosition: Int,
    @ColumnInfo("measurement_location")
    val measurementLocation: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "timezone")
    val timezone: Int,
    @ColumnInfo("description")
    val description: String? = null,
    @ColumnInfo("latitude")
    val latitude: Double = 0.0,
    @ColumnInfo("longitude")
    val longitude: Double = 0.0,
    @ColumnInfo("synced")
    val synced: Boolean = false
)