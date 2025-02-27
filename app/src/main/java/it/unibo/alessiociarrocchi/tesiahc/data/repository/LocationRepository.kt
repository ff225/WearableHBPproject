package it.unibo.alessiociarrocchi.tesiahc.data.repository

import it.unibo.alessiociarrocchi.tesiahc.data.dao.LocationDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate
import java.util.Date


class LocationRepository(
    private val locationDao: LocationDao
) {


    suspend fun getLocationsToday(today: String): List<LocationEntity> {
        return locationDao.getLocationsToday(today.toDate())
    }

    suspend fun getLocationsDates(dataInizio: String, dataFine: String): List<LocationEntity> {
        return locationDao.getLocationsDates(dataInizio.toDate(), dataFine.toDate())
    }

    suspend fun getLocation(id: Int): LocationEntity = locationDao.getLocation(id)

    suspend fun getLastLocation(): LocationEntity = locationDao.getLastLocation()

    suspend fun getLocationForMeasurement(data: Date): LocationEntity? {
        return locationDao.getLocationForMeasurement(data)
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

