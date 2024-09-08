package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface MySettingsDao {

    @Query("SELECT * FROM my_settings_table WHERE id=(:id)")
    fun getItem(id: Int): MySettingsEntity

    @Query("SELECT * FROM my_settings_table WHERE chiave=(:chiave)")
    fun getItem(chiave: String): MySettingsEntity

    @Query("UPDATE my_settings_table SET valore=(:valore) WHERE chiave=(:chiave)")
    fun updateItem(chiave: String, valore: String)

    @Insert
    fun addItem(myItem: MySettingsEntity)

}
