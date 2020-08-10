package uk.co.appsbystudio.connect.utils

import android.content.Context
import android.content.Intent
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.InputStream
import java.io.ObjectOutputStream
import java.net.Socket

class SocketUtils {

    private var socket: Socket? = null
    var dataInputStream: DataInputStream? = null
    var objectOutputStream: ObjectOutputStream? = null

    fun openSocket(address: String, port: Int) {
        socket = Socket(address, port)
        dataInputStream = DataInputStream(BufferedInputStream(socket?.getInputStream()))
        objectOutputStream = ObjectOutputStream(socket?.getOutputStream())
    }

    fun closeSocket(context: Context) {
        socket?.close()
        dataInputStream?.close()
        objectOutputStream?.close()

        context.sendBroadcast(
                Intent().apply {
                    action = "socket.state"
                    putExtra("connection", false)
                }
        )
    }

}