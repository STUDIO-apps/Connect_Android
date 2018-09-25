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
import uk.co.appsbystudio.connect.data.AppDatabase
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.ui.dashboard.DashboardFragment
import uk.co.appsbystudio.connect.ui.server.ServerFragment
import uk.co.appsbystudio.connect.ui.settings.SettingsFragment
import uk.co.appsbystudio.connect.utils.OnSocketStateChangeListener
import uk.co.appsbystudio.connect.utils.SocketService

class MainActivity : AppCompatActivity(), OnSocketStateChangeListener.SocketStateReceiverListener {

    private var connected = false

    private var onSocketStateChangeListener = OnSocketStateChangeListener()

    private var selectedItemId = R.id.dashboard
    private var selectedItem = R.id.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appDatabase = AppDatabase.getInstance(this.application)

        setSupportActionBar(toolbar)

        val service = Intent(this, SocketService::class.java)

        startService(service)

        appDatabase.serverDao().observerSelected().observe(this, Observer<ServerModel>() {
            stopService(service)
            startService(service)
            println(it?.uid)
        })

        supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment()).commit()

        bottom_navigation_main.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            toolbar.setTitle(R.string.app_name)
            if (selectedItem != it.itemId) {
                selectedItem = it.itemId
                when (it.itemId) {
                    R.id.dashboard -> {
                        selectedItemId = it.itemId
                        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, DashboardFragment.newInstance(connected)).commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.servers -> {
                        selectedItemId = it.itemId
                        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, ServerFragment.newInstance(connected)).commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.settings -> {
                        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, SettingsFragment()).commit()
                        bottom_navigation_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.exit_to_bottom))
                        bottom_navigation_main.visibility = View.GONE

                        toolbar.title = it.title
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        return@OnNavigationItemSelectedListener true
                    }
                }
            }
            false
        })

        registerReceiver(onSocketStateChangeListener, IntentFilter("socket.state"))
        onSocketStateChangeListener.addListener(this)

        /*connectButton.setOnClickListener {
            if (thread == null || !thread!!.isAlive) {
                clientThread = ClientThread(editTextAddress.text.toString(), Integer.valueOf(editTextPort.text.toString()))
                thread = Thread(clientThread)
                thread!!.start()
            } else
                println("Already connected!")
        }

        messageSend.setOnClickListener {
            val thread = Thread(Runnable { clientThread.sendMessage("This should work!") })
            thread.start()
        }

        disconnectButton.setOnClickListener { clientThread.closeConnection() }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onSocketStateChangeListener)
        onSocketStateChangeListener.removeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                settingsBack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        when (bottom_navigation_main.selectedItemId) {
            R.id.settings -> {
                settingsBack()
            }
            R.id.servers -> bottom_navigation_main.selectedItemId = R.id.dashboard
            else -> super.onBackPressed()
        }
    }

    private fun settingsBack() {
        bottom_navigation_main.selectedItemId = selectedItemId
        bottom_navigation_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom))
        bottom_navigation_main.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
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
