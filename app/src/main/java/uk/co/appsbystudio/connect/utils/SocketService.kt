package uk.co.appsbystudio.connect.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.experimental.launch
import uk.co.appsbystudio.connect.data.AppDatabase

class SocketService : Service() {

    var clientThread: ClientThread? = null
    var thread: Thread? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        val appDatabase = AppDatabase.getInstance(this.application)

        launch {
            val server = appDatabase.serverDao().getSelected()

            if (server != null) openConnection(server.address, server.port)
        }
    }

    private fun openConnection(address: String, port: Int) {
        if (clientThread != null) if (clientThread!!.isConnected) clientThread!!.closeConnection()

        clientThread = ClientThread(applicationContext, address, port)

        if (thread == null || !thread!!.isAlive) {
            thread = Thread(clientThread)
            thread?.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clientThread?.closeConnection()
    }
}
