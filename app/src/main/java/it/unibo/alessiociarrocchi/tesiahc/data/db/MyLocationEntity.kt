package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "my_location_table")
data class MyLocationEntity(

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "latitude")
    val latitude: Double= 0.0,

    @ColumnInfo(name = "longitude")
    val longitude: Double= 0.0,

    @ColumnInfo(name = "mydate")
    val mydate: Date= Date()
)