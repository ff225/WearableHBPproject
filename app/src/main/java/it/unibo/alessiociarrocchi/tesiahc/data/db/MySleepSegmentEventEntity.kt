package it.unibo.alessiociarrocchi.tesiahc.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.location.SleepSegmentEvent

@Entity(tableName = "sleep_segment_events_table")
data class MySleepSegmentEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "start_time_millis")
    val startTimeMillis: Long,

    @ColumnInfo(name = "end_time_millis")
    val endTimeMillis: Long,

    @ColumnInfo(name = "status")
    val status: Int
) {
    companion object {
        fun from(sleepSegmentEvent: SleepSegmentEvent): MySleepSegmentEventEntity {
            return MySleepSegmentEventEntity(
                startTimeMillis = sleepSegmentEvent.startTimeMillis,
                endTimeMillis = sleepSegmentEvent.endTimeMillis,
                status = sleepSegmentEvent.status
            )
        }
    }
}