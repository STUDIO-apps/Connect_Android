package uk.co.appsbystudio.connect.ui.dashboard

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import uk.co.appsbystudio.connect.data.AppDatabase
import uk.co.appsbystudio.connect.data.models.ServerModel

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private var appDatabase: AppDatabase = AppDatabase.getInstance(this.getApplication())
    lateinit var servers: MutableLiveData<List<ServerModel>>

    fun getFavouriteServers(): LiveData<List<ServerModel>> {
        return appDatabase.serverDao().getFavourites(3)
    }

    fun setFavouriteServers(uid: String, fave: Boolean) {
        appDatabase.serverDao().setFavourite(uid, fave)
    }

}