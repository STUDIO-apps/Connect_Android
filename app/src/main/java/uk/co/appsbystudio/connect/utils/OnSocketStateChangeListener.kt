package uk.co.appsbystudio.connect.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OnSocketStateChangeListener : BroadcastReceiver() {

    private val listeners: MutableSet<SocketStateReceiverListener> = HashSet()
    private var connected: Boolean = false

    interface SocketStateReceiverListener {
        fun socketConnected()
        fun socketDisconnected()
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.extras == null) return

        connected = intent.getBooleanExtra("connection", false)

        notifyStateToAll()
    }

    private fun notifyStateToAll() {
        for (listener in listeners) {
            notifyState(listener)
        }
    }

    private fun notifyState(listener: SocketStateReceiverListener) {
        if (connected) {
            listener.socketConnected()
        } else {
            listener.socketDisconnected()
        }
    }

    fun addListener(listener: SocketStateReceiverListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: SocketStateReceiverListener) {
        listeners.remove(listener)
    }

}