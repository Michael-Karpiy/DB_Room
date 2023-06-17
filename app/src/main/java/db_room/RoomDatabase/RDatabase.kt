package db_room.RoomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import db_room.UserInterface.Model

@Database(entities = [Model::class], version = 1, exportSchema = true)

abstract class RDatabase : RoomDatabase() {

    abstract fun contactDao(): DataAccessObject

    companion object {
        @Volatile
        private var INSTANCE: RDatabase? = null

        fun getDatabase(context: Context): RDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): RDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RDatabase::class.java,
                "notes_database"
            ).build()
        }
    }
}