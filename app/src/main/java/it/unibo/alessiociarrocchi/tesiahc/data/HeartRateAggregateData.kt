package it.unibo.alessiociarrocchi.tesiahc.data

import java.time.Instant

data class HeartRateAggregateData (
    val hrStart: Instant,
    val hrEnd: Instant,
    val hrAVG: Long,
    val hrMIN: Long,
    val hrMAX: Long,
    val hrMC: Long,
)