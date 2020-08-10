package uk.co.appsbystudio.connect

import androidx.lifecycle.Observer
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.appsbystudio.connect.data.AppDatabase
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.ui.dashboard.DashboardFragment
import uk.co.appsbystudio.connect.ui.server.ServerFragment
import uk.co.appsbystudio.connect.ui.settings.SettingsFragment
import uk.co.appsbystudio.connect.utils.OnSocketStateChangeListener
import uk.co.appsbystudio.connect.utils.SocketService
import uk.co.appsbystudio.connect.utils.SocketUtils

class MainActivity : AppCompatActivity(), OnSocketStateChangeListener.SocketStateReceiverListener {

    private var connected = false

    private var socketUtils: SocketUtils? = null
    private var onSocketStateChangeListener = OnSocketStateChangeListener()

    lateinit var service: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appDatabase = AppDatabase.getInstance(this.application)

        setSupportActionBar(toolbar)

        socketUtils = SocketUtils()

        service = Intent(this, SocketService::class.java)

        startService(service)

        appDatabase.serverDao().observerSelected().observe(this, Observer<ServerModel>() {
            stopService(service)
            startService(service)
            println(it?.uid)
        })

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        bottom_navigation_main.setupWithNavController(navController)

        registerReceiver(onSocketStateChangeListener, IntentFilter("socket.state"))
        onSocketStateChangeListener.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        socketUtils?.closeSocket(this)

        unregisterReceiver(onSocketStateChangeListener)
        onSocketStateChangeListener.removeListener(this)
    }

    override fun socketConnected() {
        connected = true
        Toast.makeText(this, "Connected to server", Toast.LENGTH_SHORT).show()

    }

    override fun socketDisconnected() {
        connected = false
        Toast.makeText(this, "Disconnected from server", Toast.LENGTH_SHORT).show()
    }
}
