package uk.co.appsbystudio.connect

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_server.*
import uk.co.appsbystudio.connect.ui.dashboard.DashboardFragment
import uk.co.appsbystudio.connect.utils.ClientThread
import uk.co.appsbystudio.connect.ui.server.ServerFragment
import uk.co.appsbystudio.connect.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var clientThread: ClientThread
    private var thread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment()).commit()

        bottom_navigation_main.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            toolbar.title = it.title
            when (it.itemId) {
                R.id.dashboard -> {
                    toolbar.title = it.title
                    supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).replace(R.id.container, DashboardFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.servers -> {
                    supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.container, ServerFragment()).commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment()).addToBackStack(null).commit()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    bottom_navigation_main.visibility = View.GONE
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                bottom_navigation_main.visibility = View.VISIBLE
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        println(supportFragmentManager.backStackEntryCount)
        if (supportFragmentManager.backStackEntryCount == 0) {
            bottom_navigation_main.visibility = View.VISIBLE
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (thread != null) {
            clientThread.closeConnection()
        }
    }

    override fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().add(R.id.constraint_main, fragment).addToBackStack(null).commit()
    }
}
