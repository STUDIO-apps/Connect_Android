package uk.co.appsbystudio.connect

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import uk.co.appsbystudio.connect.models.ServerModel

@Dao
interface ServerDao {

    @Query("SELECT * FROM servers")
    fun getAll(): LiveData<List<ServerModel>>

    @Query("SELECT * FROM servers WHERE server_favourite = 1 LIMIT :limit")
    fun getFavourites(limit: Int): LiveData<List<ServerModel>>

    @Insert(onConflict = REPLACE)
    fun insert(server: ServerModel)

    @Query("UPDATE servers SET server_favourite = :favourite WHERE uid = :uid")
    fun setFavourite(uid: String, favourite: Boolean)

    @Delete
    fun delete(server: ServerModel)
}