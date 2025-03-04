package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.LocationDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity
import java.util.Date


class LocationRepository(
    private val locationDao: LocationDao
) {


    suspend fun getLocationsToday(today: Long): List<LocationEntity> {
        return locationDao.getLocationsToday(today)
    }

    /*
    suspend fun getLocationsDates(dataInizio: String, dataFine: String): List<LocationEntity> {
        return locationDao.getLocationsDates(dataInizio.toDate(), dataFine.toDate())
    }*/

    suspend fun getLocation(id: Int): LocationEntity = locationDao.getLocation(id)

    suspend fun getLastLocation(): LocationEntity = locationDao.getLastLocation()

    suspend fun getLocationForMeasurement(date: Long): LocationEntity? {
        return locationDao.getLocationForMeasurement(date)
    }

    suspend fun deleteLocation(id: Int) {
        locationDao.deleteLocation(id)
    }

    suspend fun updateLocation(locationEntity: LocationEntity) {
        locationDao.updateLocation(locationEntity)
    }

    suspend fun addLocation(locationEntity: LocationEntity) {

        locationDao.addLocation(locationEntity)

    }

    suspend fun addLocations(myLocationEntities: List<LocationEntity>) {
        locationDao.addLocations(myLocationEntities)
    }


}

