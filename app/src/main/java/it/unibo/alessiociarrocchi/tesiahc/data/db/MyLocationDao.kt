package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MyLocationDao {

    @Query("SELECT * FROM my_location_table ORDER BY date DESC")
    fun getLocations(): List<MyLocationEntity>
    //TODO lista filtrata per data

    @Query("DELETE FROM my_location_table WHERE id=(:id)")
    fun deleteLocation(id: Int)

    @Query("SELECT * FROM my_location_table WHERE id=(:id)")
    fun getLocation(id: Int): MyLocationEntity

    @Update
    fun updateLocation(myLocationEntity: MyLocationEntity)

    @Insert
    fun addLocation(myLocationEntity: MyLocationEntity)

    @Insert
    fun addLocations(myLocationEntities: List<MyLocationEntity>)
}
