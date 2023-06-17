package db_room.UserInterface

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model")
data class Model(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "position")
    var position: Int = 0,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,
    )