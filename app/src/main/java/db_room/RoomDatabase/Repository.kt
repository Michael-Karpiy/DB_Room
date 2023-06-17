package db_room.RoomDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import db_room.UserInterface.Model

class Repository {

    companion object {

        var loginDatabase: RDatabase? = null

        var loginTableModel: LiveData<Model>? = null

        fun initializeDB(context: Context): RDatabase {
            return RDatabase.getDatabase(context)
        }

        fun getLoginDetails(context: Context, position: Int): LiveData<Model>? {

            loginDatabase = initializeDB(context)

            loginTableModel = loginDatabase!!.contactDao().getLoginDetails(position)

            return loginTableModel
        }
    }
}