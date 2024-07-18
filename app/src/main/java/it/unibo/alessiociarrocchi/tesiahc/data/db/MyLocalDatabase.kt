package it.unibo.alessiociarrocchi.tesiahc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "tesiahc_database"

@Database(entities =
    [
        MyBloodPressureEntity::class,
        MyHeartRateAggregateEntity::class,
        MyLocationEntity::class,
        MySleepSegmentEventEntity::class,
        MySleepClassifyEventEntity::class
    ],
    version = 4,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyLocalDatabase : RoomDatabase() {

    abstract fun bpDao(): MyBloodPressureDao
    abstract fun bpHRDao(): MyHeartRateAggregateDao
    abstract fun locationDao(): MyLocationDao
    abstract fun sleepSegmentEventDao(): MySleepSegmentEventDao
    abstract fun sleepClassifyEventDao(): MySleepClassifyEventDao

    companion object {
        @Volatile
        private var INSTANCE: MyLocalDatabase? = null

        fun getDatabase(context: Context): MyLocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MyLocalDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}