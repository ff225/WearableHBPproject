package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "my_location_table")
data class MyLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val foreground: Boolean = true,
    val date: Date = Date()
)
