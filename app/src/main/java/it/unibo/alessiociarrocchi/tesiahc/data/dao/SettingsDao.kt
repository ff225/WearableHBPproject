package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.unibo.alessiociarrocchi.tesiahc.data.model.MySettingsEntity

@Dao
interface SettingsDao {

    @Query("SELECT * FROM my_settings_table WHERE id=(:id)")
    suspend fun getItem(id: Int): MySettingsEntity

    @Query("SELECT * FROM my_settings_table WHERE chiave=(:chiave)")
    suspend fun getItem(chiave: String): MySettingsEntity

    @Query("UPDATE my_settings_table SET valore=(:valore) WHERE chiave=(:chiave)")
    suspend fun updateItem(chiave: String, valore: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addItem(myItem: MySettingsEntity)

}
