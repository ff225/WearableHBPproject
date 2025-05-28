package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.unibo.alessiociarrocchi.tesiahc.data.dao.BloodOxygenDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.BloodPressureDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.ExerciseDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.HeartRateAggregateDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.LocationDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.SettingsDao
import it.unibo.alessiociarrocchi.tesiahc.data.dao.StepsDao
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodOxygenEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.ExerciseEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.MySettingsEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.StepsEntity

private const val DATABASE_NAME = "tesiahc_database"

@Database(
    entities =
    [
        BloodPressureEntity::class,
        HeartRateAggregateEntity::class,
        LocationEntity::class,
        StepsEntity::class,
        ExerciseEntity::class,
        BloodOxygenEntity::class,
        MySettingsEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun bloodPressureDao(): BloodPressureDao
    abstract fun heartRateAggregateDao(): HeartRateAggregateDao
    abstract fun locationDao(): LocationDao
    abstract fun settingsDao(): SettingsDao
    abstract fun stepsDao(): StepsDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun boDao(): BloodOxygenDao

    companion object {
        @Volatile
        private var Instance: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}