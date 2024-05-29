package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import java.util.concurrent.ExecutorService

/**
 * Access point for database (MyLocation data) and location APIs (start/stop location updates and
 * checking location update status).
 */
class MyLocationRepository private constructor(
    private val myLocationDatabase: MyLocalDatabase,
    private val myLocationManager: MyLocationManager,
    private val executor: ExecutorService
) {

    // Database related fields/methods:
    private val locationDao = myLocationDatabase.locationDao()

    /**
     * Returns all recorded locations from database.
     */
    fun getLocations(): List<MyLocationEntity> = locationDao.getLocations()

    //TODO locations filtrate per data

    // Not being used now but could in future versions.
    /**
     * Returns specific location in database.
     */
    fun getLocation(id: Int): MyLocationEntity = locationDao.getLocation(id)

    fun deleteLocation(id: Int){
        locationDao.deleteLocation(id)
    }

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

    companion object {
        @Volatile private var INSTANCE: MyLocationRepository? = null

        fun getInstance(context: Context, executor: ExecutorService): MyLocationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyLocationRepository(
                    MyLocalDatabase.getDatabase(context),
                    MyLocationManager.getInstance(context),
                    executor)
                    .also { INSTANCE = it }
            }
        }
    }

}