package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import java.util.UUID
import java.util.concurrent.ExecutorService

/**
 * Access point for database (MyLocation data) and location APIs (start/stop location updates and
 * checking location update status).
 */
class LocationRepository private constructor(
    private val myLocationDatabase: MyLocalDatabase,
    private val executor: ExecutorService
) {

    // Database related fields/methods:
    private val locationDao = myLocationDatabase.locationDao()

    /**
     * Returns all recorded locations from database.
     */
    fun getLocations(): LiveData<List<MyLocationEntity>> = locationDao.getLocations()

    // Not being used now but could in future versions.
    /**
     * Returns specific location in database.
     */
    fun getLocation(id: Int): LiveData<MyLocationEntity> = locationDao.getLocation(id)

    // Not being used now but could in future versions.
    /**
     * Updates location in database.
     */
    fun updateLocation(myLocationEntity: MyLocationEntity) {
        executor.execute {
            locationDao.updateLocation(myLocationEntity)
        }
    }

    /**
     * Adds location to the database.
     */
    fun addLocation(myLocationEntity: MyLocationEntity) {
        executor.execute {
            locationDao.addLocation(myLocationEntity)
        }
    }

    /**
     * Adds list of locations to the database.
     */
    fun addLocations(myLocationEntities: List<MyLocationEntity>) {
        executor.execute {
            locationDao.addLocations(myLocationEntities)
        }
    }

}