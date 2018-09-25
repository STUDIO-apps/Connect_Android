package uk.co.appsbystudio.connect.ui.server

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.launch
import uk.co.appsbystudio.connect.data.AppDatabase
import uk.co.appsbystudio.connect.data.models.ServerModel

class ServerViewModel(application: Application) : AndroidViewModel(application) {

    private var appDatabase = AppDatabase.getInstance(this.getApplication())
    lateinit var servers: MutableLiveData<List<ServerModel>>

    fun getServers(): LiveData<List<ServerModel>> {
        return appDatabase.serverDao().getAll()
    }

    fun addServer(serverModel: ServerModel) {
        launch {
            appDatabase.serverDao().insert(serverModel)
        }
    }

    fun setSelected(uid: Int) {
        launch {
            appDatabase.serverDao().deselectAll()
            appDatabase.serverDao().setSelected(uid, true)
        }
    }

    fun toggleFavourite(uid: Int, isFavourite: Boolean) {
        launch {
            appDatabase.serverDao().setFavourite(uid, isFavourite)
        }
    }

}