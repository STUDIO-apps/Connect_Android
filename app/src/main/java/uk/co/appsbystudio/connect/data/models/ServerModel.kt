package uk.co.appsbystudio.connect.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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