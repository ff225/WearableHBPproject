package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "blood_pressure_table",
    indices = [Index(value = ["uid"], unique = true)]
)
data class BloodPressureEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid: String,    // id health connect
    val systolic: Double,
    val diastolic: Double,
    val time: Date,
    val timezone: Int,
    val bodyPosition: Int,
    val measurementLocation: Int,
    val description: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val synced: Boolean = false
)