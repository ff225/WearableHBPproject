package it.unibo.alessiociarrocchi.tesiahc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val DATABASE_NAME = "tesiahc-database"

@Database(entities =
    [
        MyBloodPressureEntity::class,
        MyHeartRateAggregateEntity::class,
        MyLocationEntity::class
    ],
    version = 1,
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyLocalDatabase : RoomDatabase() {

    abstract fun bpDao(): MyBloodPressureDao
    abstract fun bpHRDao(): MyHeartRateAggregateDao
    abstract fun locationDao(): MyLocationDao

    companion object {
        @Volatile
        private var Instance: MyLocalDatabase? = null

        fun getDatabase(context: Context): MyLocalDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MyLocalDatabase::class.java,
                    DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build().also { Instance = it }
            }
        }
    }
}