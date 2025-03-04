package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_location_table")
data class LocationEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "latitude")
    val latitude: Double = -1.0,

    @ColumnInfo(name = "longitude")
    val longitude: Double = -1.0,

    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "time")
    val time: Long
)

