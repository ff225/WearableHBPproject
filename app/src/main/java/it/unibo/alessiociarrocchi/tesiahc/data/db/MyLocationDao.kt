package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface MyLocationDao {

    @Query("SELECT * FROM my_location_table WHERE date(mydate / 1000,'unixepoch') = date(:today / 1000,'unixepoch') ORDER BY mydate DESC")
    fun getLocationsToday(today: Date): List<MyLocationEntity>

    @Query("SELECT * FROM my_location_table" +
            " WHERE date(mydate / 1000,'unixepoch')>=date(:dataInizio / 1000,'unixepoch') AND date(mydate / 1000,'unixepoch')<=date(:dataFine/ 1000,'unixepoch') ORDER BY mydate DESC")
    fun getLocationsDates(dataInizio: Date, dataFine: Date): List<MyLocationEntity>

    @Query("DELETE FROM my_location_table WHERE id=(:id)")
    fun deleteLocation(id: Int)

    @Query("SELECT * FROM my_location_table WHERE id=(:id)")
    fun getLocation(id: Int): MyLocationEntity

    @Query("SELECT * FROM my_location_table ORDER BY mydate DESC LIMIT 1")
    fun getLastLocation(): MyLocationEntity

    @Query("SELECT * FROM my_location_table WHERE date(mydate / 1000,'unixepoch') = date(:data / 1000,'unixepoch') ORDER BY mydate DESC LIMIT 1")
    fun getLocationForMeasurement(data: Date): MyLocationEntity?

    @Update
    fun updateLocation(myLocationEntity: MyLocationEntity)

    @Insert
    fun addLocation(myLocationEntity: MyLocationEntity)

    @Insert
    fun addLocations(myLocationEntities: List<MyLocationEntity>)
}
