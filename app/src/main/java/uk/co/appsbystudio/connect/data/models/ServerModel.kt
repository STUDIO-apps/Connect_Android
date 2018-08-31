package uk.co.appsbystudio.connect.data.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "servers")
data class ServerModel(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") val uid: Int? = null,
        @ColumnInfo(name = "server_name") val name: String,
        @ColumnInfo(name = "server_address") val address: String,
        @ColumnInfo(name = "server_port") val port: Int,
        @ColumnInfo(name = "server_selected") val selected: Boolean,
        @ColumnInfo(name = "server_favourite") val favourite: Boolean,
        @ColumnInfo(name = "server_date") val timestamp: Long = System.currentTimeMillis()
)