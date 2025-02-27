package it.unibo.alessiociarrocchi.tesiahc.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "my_settings_table",
    indices = [Index(value = ["chiave"], unique = true)]
)
data class MySettingsEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "chiave")
    val key: String = "",

    @ColumnInfo(name = "valore")
    val valore: String = "",

    )

fun GetKey_FilterDataInzio(): String {
    return "FilterDataInzio"
}

fun GetKey_FilterDataFine(): String {
    return "FilterDataFine"
}

