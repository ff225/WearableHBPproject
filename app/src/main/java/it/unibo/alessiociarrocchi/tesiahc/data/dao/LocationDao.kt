package it.unibo.alessiociarrocchi.tesiahc.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity

@Dao
interface LocationDao {

    @Query("SELECT * FROM my_location_table WHERE date =:today ORDER BY :today DESC")
    suspend fun getLocationsToday(today: Long): List<LocationEntity>

    /*
    @Query(
        "SELECT * FROM my_location_table" +
                " WHERE date(mydate / 1000,'unixepoch')>=date(:dataInizio / 1000,'unixepoch') AND date(mydate / 1000,'unixepoch')<=date(:dataFine/ 1000,'unixepoch') ORDER BY mydate DESC"
    )
    suspend fun getLocationsDates(dataInizio: Date, dataFine: Date): List<LocationEntity>
*/
    @Query("DELETE FROM my_location_table WHERE id=(:id)")
    suspend fun deleteLocation(id: Int)

    @Query("SELECT * FROM my_location_table WHERE id=(:id)")
    suspend fun getLocation(id: Int): LocationEntity

    @Query("SELECT * FROM my_location_table ORDER BY date DESC LIMIT 1")
    suspend fun getLastLocation(): LocationEntity

    @Query("SELECT * FROM my_location_table WHERE date <= :data ORDER BY date DESC LIMIT 1")
    suspend fun getLocationForMeasurement(data: Long): LocationEntity?

    @Update
    suspend fun updateLocation(locationEntity: LocationEntity)

    @Insert
    suspend fun addLocation(locationEntity: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLocations(myLocationEntities: List<LocationEntity>)
}
