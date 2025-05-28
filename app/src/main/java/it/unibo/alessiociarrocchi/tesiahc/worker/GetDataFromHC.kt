package it.unibo.alessiociarrocchi.tesiahc.worker

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodOxygenEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.ExerciseEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.StepsEntity
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodOxygenRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.ExerciseRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.HeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.LocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.StepsRepository
import it.unibo.alessiociarrocchi.tesiahc.toDate
import java.time.Instant
import java.time.temporal.ChronoUnit

class GetDataFromHC(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    private val healthConnectManager = HealthConnectClient.getOrCreate(ctx)
    private val bloodPressureRepo =
        (ctx as WearableHBPApplication).appContainer.bloodPressureRepository
    private val heartRateRepository =
        (ctx as WearableHBPApplication).appContainer.heartRateRepository

    private val locationRepository = (ctx as WearableHBPApplication).appContainer.locationRepository

    private val stepsRepository = (ctx as WearableHBPApplication).appContainer.stepsRepository

    private val exerciseRepository = (ctx as WearableHBPApplication).appContainer.exerciseRepository

    private val boRepository = (ctx as WearableHBPApplication).appContainer.boRepository

    override suspend fun doWork(): Result {

        val startTime = Instant.now().minus(720, ChronoUnit.MINUTES)
        val endTime = Instant.now()

        val record = readBloodPressure(
            healthConnectManager,
            startTime,
            endTime,
            bloodPressureRepo,
            locationRepository
        )
        readAggregateHeartRate(
            healthConnectManager,
            //startTime,
            //endTime,
            heartRateRepository,
            record
        )
        readSteps(healthConnectManager, startTime, endTime, stepsRepository)
        readExercise(healthConnectManager, startTime, endTime, exerciseRepository)
        readBloodOxygen(healthConnectManager, startTime, endTime, boRepository)
        return Result.success()
    }
}

suspend fun readAggregateHeartRate(
    healthConnectClient: HealthConnectClient,
    //startTime: Instant,
    //endTime: Instant,
    heartRateRepository: HeartRateRepository,
    bloodsRecord: List<BloodPressureRecord>
) {

    bloodsRecord.forEach { bloodRecord ->
        try {
            val response =
                healthConnectClient.aggregate(
                    AggregateRequest(
                        setOf(
                            HeartRateRecord.BPM_MAX,
                            HeartRateRecord.BPM_MIN,
                            HeartRateRecord.BPM_AVG,
                            HeartRateRecord.MEASUREMENTS_COUNT
                        ),
                        timeRangeFilter = TimeRangeFilter.between(
                            bloodRecord.time.atZone(bloodRecord.zoneOffset).toInstant()
                                .minus(30, ChronoUnit.MINUTES),
                            bloodRecord.time.atZone(bloodRecord.zoneOffset).toInstant()

                        )
                    )
                )
            val minimumHeartRate = response[HeartRateRecord.BPM_MIN]
            val maximumHeartRate = response[HeartRateRecord.BPM_MAX]
            val averageHeartRate = response[HeartRateRecord.BPM_AVG]
            val measurementsCount = response[HeartRateRecord.MEASUREMENTS_COUNT]

            if (minimumHeartRate != null) {
                heartRateRepository.insertItem(
                    HeartRateAggregateEntity(
                        uidBloodPressureFK = bloodRecord.metadata.id,
                        hrStart = bloodRecord.time.atZone(bloodRecord.zoneOffset).toInstant()
                            .minus(30, ChronoUnit.MINUTES).toDate(),
                        hrEnd = bloodRecord.time.atZone(bloodRecord.zoneOffset).toInstant()
                            .toDate(),
                        hrMIN = minimumHeartRate,
                        hrMAX = maximumHeartRate ?: -1,
                        hrAVG = averageHeartRate ?: -1,
                        hrMC = measurementsCount ?: -1,
                        timezone = bloodRecord.zoneOffset?.totalSeconds ?: -1,
                        synced = false
                    )
                )
            }


        } catch (e: Exception) {
            Log.e("GetDataFromHC", "Error reading heart rate records", e)
        }

    }

}


suspend fun readBloodPressure(
    healthConnectClient: HealthConnectClient,
    startTime: Instant,
    endTime: Instant,
    bloodPressureRepo: BloodPressureRepository,
    locationRepository: LocationRepository
): List<BloodPressureRecord> {
    return try {

        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    BloodPressureRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

        response.records.forEach {

            val time = it.time.truncatedTo(ChronoUnit.DAYS).toEpochMilli()
            val locationEntity =
                locationRepository.getLocationForMeasurement(
                    time
                )

            Log.d("GetDataFromHC", "Reading blood pressure record: ${it.metadata.id}")
            bloodPressureRepo.insertItem(
                BloodPressureEntity(
                    uid = it.metadata.id,
                    systolic = it.systolic.inMillimetersOfMercury,
                    diastolic = it.diastolic.inMillimetersOfMercury,
                    date = it.time.atZone(it.zoneOffset).truncatedTo(ChronoUnit.DAYS)
                        .toEpochSecond(),
                    time = it.time.toEpochMilli(),
                    timezone = it.zoneOffset?.totalSeconds ?: -1,
                    bodyPosition = it.bodyPosition,
                    measurementLocation = it.measurementLocation,
                    latitude = locationEntity?.latitude ?: -1.0,
                    longitude = locationEntity?.longitude ?: -1.0,
                    synced = false
                )
            )
        }

        response.records
    } catch (e: Exception) {
        Log.e("GetDataFromHC", "Error reading blood pressure records", e)
        emptyList()
    }

}

suspend fun readSteps(healthConnectClient: HealthConnectClient,
                      startTime: Instant,
                      endTime: Instant,
                      stepsRepo: StepsRepository,
): List<StepsRecord> {
    return try {
        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

        response.records.forEach {

            stepsRepo.insertItem(
                StepsEntity(
                    uid = it.metadata.id,
                    startTime = it.startTime.toEpochMilli(),
                    startTimeZone = it.startZoneOffset?.totalSeconds ?: -1,
                    endTime = it.endTime.toEpochMilli(),
                    endTimeZone = it.endZoneOffset?.totalSeconds ?: -1,
                    count = it.count,
                    synced = false,
                )
            )
        }

        response.records
    } catch (e: Exception) {
        Log.e("GetDataFromHC", "Error reading steps records", e)
        emptyList()
    }
}

suspend fun readExercise(healthConnectClient: HealthConnectClient,
                      startTime: Instant,
                      endTime: Instant,
                      exerciseRepo: ExerciseRepository,
): List<ExerciseSessionRecord> {
    return try {
        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

        response.records.forEach {

            exerciseRepo.insertItem(
                ExerciseEntity(
                    uid = it.metadata.id,
                    startTime = it.startTime.toEpochMilli(),
                    startTimeZone = it.startZoneOffset?.totalSeconds ?: -1,
                    endTime = it.endTime.toEpochMilli(),
                    endTimeZone = it.endZoneOffset?.totalSeconds ?: -1,
                    exerciseType = when(it.exerciseType) {
                        8 -> "Biking"
                        56 -> "Running"
                        else -> "Exercise not recognized"
                    },
                    synced = false,
                )
            )
        }

        response.records
    } catch (e: Exception) {
        Log.e("GetDataFromHC", "Error reading exercise records", e)
        emptyList()
    }
}

suspend fun readBloodOxygen(healthConnectClient: HealthConnectClient,
                         startTime: Instant,
                         endTime: Instant,
                         boRepo: BloodOxygenRepository,
): List<OxygenSaturationRecord> {
    return try {
        val response =
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    OxygenSaturationRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

        response.records.forEach {

            boRepo.insertItem(
                BloodOxygenEntity(
                    uid = it.metadata.id,
                    time = it.time.toEpochMilli(),
                    timeZone = it.zoneOffset?.totalSeconds ?: -1,
                    percentage = it.percentage.value,
                    synced = false,
                )
            )
        }

        response.records
    } catch (e: Exception) {
        Log.e("GetDataFromHC", "Error reading exercise records", e)
        emptyList()
    }
}