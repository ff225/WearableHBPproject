package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate
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

    private val locationDao = myLocationDatabase.locationDao()

    fun getLocationsToday(today: String): List<MyLocationEntity>{
        return locationDao.getLocationsToday(today.toDate())
    }

    fun getLocationsDates(dataInizio: String, dataFine: String): List<MyLocationEntity>{
        return locationDao.getLocationsDates(dataInizio.toDate(), dataFine.toDate())
    }

    fun getLocation(id: Int): MyLocationEntity = locationDao.getLocation(id)

    fun getLastLocation(): MyLocationEntity = locationDao.getLastLocation()


    fun deleteLocation(id: Int){
        locationDao.deleteLocation(id)
    }

    fun updateLocation(myLocationEntity: MyLocationEntity) {
        executor.execute {
            locationDao.updateLocation(myLocationEntity)
        }
    }

    fun addLocation(myLocationEntity: MyLocationEntity) {
        executor.execute {
            locationDao.addLocation(myLocationEntity)
        }
    }

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