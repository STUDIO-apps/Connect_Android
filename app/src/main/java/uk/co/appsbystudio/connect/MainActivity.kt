package uk.co.appsbystudio.connect

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.appsbystudio.connect.ui.dashboard.DashboardFragment
import uk.co.appsbystudio.connect.utils.ClientThread
import uk.co.appsbystudio.connect.ui.server.ServerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var clientThread: ClientThread
    private var thread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment()).commit()

        bottom_navigation_main.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.dashboard -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment()).addToBackStack(null).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.servers -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, ServerFragment()).addToBackStack(null).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

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
        if (thread != null) {
            clientThread.closeConnection()
        }
    }
}
