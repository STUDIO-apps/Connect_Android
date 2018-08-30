package uk.co.appsbystudio.connect.ui.server

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
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

}