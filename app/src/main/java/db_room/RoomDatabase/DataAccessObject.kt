package db_room.RoomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import db_room.UserInterface.Model
import kotlinx.coroutines.flow.Flow

@Dao
interface DataAccessObject {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(model: Model)

    @Update
    suspend fun update(model: Model)

    @Delete
    suspend fun delete(model: Model)

    @Query("SELECT * FROM model ORDER BY position DESC")
    fun getData(): Flow<List<Model>>

    @Query("SELECT * FROM model WHERE position =:position")
    fun getLoginDetails(position: Int?) : LiveData<Model>
}