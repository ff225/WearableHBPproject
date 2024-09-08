
package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "my_settings_table",
    indices = [Index(value = ["chiave"], unique = true)]
)
data class MySettingsEntity(

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "chiave")
    val chiave: String= "",

    @ColumnInfo(name = "valore")
    val valore: String= "",

)

fun GetKey_FilterDataInzio() : String{
    return "FilterDataInzio"
}
fun GetKey_FilterDataFine() : String{
    return "FilterDataFine"
}

