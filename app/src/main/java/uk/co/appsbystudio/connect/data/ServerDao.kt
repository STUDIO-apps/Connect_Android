package uk.co.appsbystudio.connect.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import uk.co.appsbystudio.connect.data.models.ServerModel

@Dao
interface ServerDao {

    @Query("SELECT * FROM servers ORDER BY server_date DESC")
    fun getAll(): LiveData<List<ServerModel>>

    @Query("SELECT * FROM servers WHERE server_selected = 1")
    fun observerSelected(): LiveData<ServerModel>

    @Query("SELECT * FROM servers WHERE server_selected = 1")
    fun getSelected(): ServerModel

    @Query("SELECT * FROM servers WHERE server_favourite = 1 ORDER BY server_date DESC LIMIT :limit")
    fun getFavourites(limit: Int): LiveData<List<ServerModel>>

    @Insert(onConflict = REPLACE)
    fun insert(server: ServerModel)

    @Query("UPDATE servers SET server_favourite = :favourite WHERE uid = :uid")
    fun setFavourite(uid: Int, favourite: Boolean)

    @Query("UPDATE servers SET server_selected = 0 WHERE server_selected = 1")
    fun deselectAll()

    @Query("UPDATE servers SET server_selected = :selected WHERE uid = :uid")
    fun setSelected(uid: Int, selected: Boolean)

    @Delete
    fun delete(server: ServerModel)
}