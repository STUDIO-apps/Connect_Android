package uk.co.appsbystudio.connect.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.launch
import uk.co.appsbystudio.connect.data.AppDatabase
import uk.co.appsbystudio.connect.data.models.ServerModel

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private var appDatabase: AppDatabase = AppDatabase.getInstance(this.getApplication())
    lateinit var servers: MutableLiveData<List<ServerModel>>

    fun getFavouriteServers(): LiveData<List<ServerModel>> {
        return appDatabase.serverDao().getFavourites(3)
    }

    fun setFavouriteServers(uid: Int, fave: Boolean) {
        appDatabase.serverDao().setFavourite(uid, fave)
    }

    fun setSelected(uid: Int) {
        launch {
            appDatabase.serverDao().deselectAll()
            appDatabase.serverDao().setSelected(uid, true)
        }
    }

}