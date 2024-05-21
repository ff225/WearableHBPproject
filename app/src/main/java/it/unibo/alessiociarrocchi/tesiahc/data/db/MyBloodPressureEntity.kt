package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_blood_pressure_table")
data class MyBloodPressureEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid: String,    // id health connect
    val systolic: Double,
    val diastolic: Double,
    val time: Long,
    val timzone: Int,
    val bodyPosition: Int,
    val measurementLocation: Int,
    val description: String
)