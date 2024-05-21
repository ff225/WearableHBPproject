package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_AVAILABLE
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.request.AggregateRequest

// The minimum android level that can use Health Connect
const val MIN_SUPPORTED_SDK = Build.VERSION_CODES.O_MR1

/**
 * Demonstrates reading and writing from Health Connect.
 */
class MyHealthConnectManager(private val context: Context) {
  private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }

  var availability = mutableStateOf(HealthConnectAvailability.NOT_SUPPORTED)
    private set

  init {
    checkAvailability()
  }

  private fun isSupported() = Build.VERSION.SDK_INT >= it.unibo.alessiociarrocchi.tesiahc.data.MIN_SUPPORTED_SDK

  fun checkAvailability() {
    availability.value = when {
      HealthConnectClient.getSdkStatus(context) == SDK_AVAILABLE -> HealthConnectAvailability.INSTALLED
      isSupported() -> HealthConnectAvailability.NOT_INSTALLED
      else -> HealthConnectAvailability.NOT_SUPPORTED
    }
  }

  /**
   * Determines whether all the specified permissions are already granted. It is recommended to
   * call [PermissionController.getGrantedPermissions] first in the permissions flow, as if the
   * permissions are already granted then there is no need to request permissions via
   * [PermissionController.createRequestPermissionResultContract].
   */
  suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
    return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
  }

  fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
    return PermissionController.createRequestPermissionResultContract()
  }

  /**  -------------------------------------[BloodPressureRecord]---------------------------------------------  */

  /**
   * Reads in existing [BloodPressureRecord]s
   */
  suspend fun readBloodPressureList(start: Instant, end: Instant): List<BloodPressureRecord>{
    val request = ReadRecordsRequest(
      recordType = BloodPressureRecord::class,
      timeRangeFilter = TimeRangeFilter.between(start, end)
    )
    val response = healthConnectClient.readRecords(request)
    return response.records
  }

  /**
   * Reads specific [BloodPressureRecord]
   */
  suspend fun readSpecificBloodPressure(
    uid: String,
  ): BloodPressureRecord {
    val myBP = healthConnectClient.readRecord(BloodPressureRecord::class, uid)

    return myBP.record
  }

  /**  -------------------------------------[HeartRateRecord]-----------------------------------------  */

  /**
   * Reads in existing [HeartRateRecord]s
   */
  suspend fun readHeartRateRecordList(start: Instant, end: Instant): List<HeartRateRecord>{
    val request = ReadRecordsRequest(
      recordType = HeartRateRecord::class,
      timeRangeFilter = TimeRangeFilter.between(start, end)
    )
    val response = healthConnectClient.readRecords(request)
    return response.records
  }

  /**
   * Reads specific [HeartRateRecord]
   */
  suspend fun readSpecificHeartRate(
    uid: String,
  ): HeartRateRecord {
    val myHR = healthConnectClient.readRecord(HeartRateRecord::class, uid)

    return myHR.record
  }

  suspend fun aggregateBPM_AVG(startTime: Instant, endTime: Instant): Long {
    val response = healthConnectClient.aggregate(
      AggregateRequest(
        metrics = setOf(HeartRateRecord.BPM_AVG),
        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
      )
    )

    return response[HeartRateRecord.BPM_AVG]?.toLong() ?: 0L
  }

  suspend fun aggregateBPM_Max(startTime: Instant, endTime: Instant): Long {
    val response = healthConnectClient.aggregate(
      AggregateRequest(
        metrics = setOf(HeartRateRecord.BPM_MAX),
        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
      )
    )

    return response[HeartRateRecord.BPM_MAX]?.toLong() ?: 0L
  }

  suspend fun aggregateBPM_Min(startTime: Instant, endTime: Instant): Long {
    val response = healthConnectClient.aggregate(
      AggregateRequest(
        metrics = setOf(HeartRateRecord.BPM_MIN),
        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
      )
    )

    return response[HeartRateRecord.BPM_MIN]?.toLong() ?: 0L
  }

  suspend fun aggregateMeasurements_Count(startTime: Instant, endTime: Instant): Long {
    val response = healthConnectClient.aggregate(
      AggregateRequest(
        metrics = setOf(HeartRateRecord.MEASUREMENTS_COUNT),
        timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
      )
    )

    return response[HeartRateRecord.MEASUREMENTS_COUNT]?.toLong() ?: 0L
  }


  /**  ----------------------------------------------------------------------------------------------------  */

}

/**
 * Health Connect requires that the underlying Health Connect APK is installed on the device.
 * [HealthConnectAvailability] represents whether this APK is indeed installed, whether it is not
 * installed but supported on the device, or whether the device is not supported (based on Android
 * version).
 */
enum class HealthConnectAvailability {
  INSTALLED,
  NOT_INSTALLED,
  NOT_SUPPORTED
}
