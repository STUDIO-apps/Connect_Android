package uk.co.appsbystudio.connect

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_server.*
import uk.co.appsbystudio.connect.ui.dashboard.DashboardFragment
import uk.co.appsbystudio.connect.utils.ClientThread
import uk.co.appsbystudio.connect.ui.server.ServerFragment
import uk.co.appsbystudio.connect.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var clientThread: ClientThread
    private var thread: Thread? = null

    private var selectedItemId = R.id.dashboard
    private var selectedItem = R.id.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().replace(R.id.container, DashboardFragment()).commit()

        bottom_navigation_main.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener {
            toolbar.setTitle(R.string.app_name)
            if (selectedItem != it.itemId) {
                selectedItem = it.itemId
                when (it.itemId) {
                    R.id.dashboard -> {
                        selectedItemId = it.itemId
                        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, DashboardFragment()).commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.servers -> {
                        selectedItemId = it.itemId
                        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, ServerFragment()).commit()
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
                bottom_navigation_main.selectedItemId = selectedItemId
                bottom_navigation_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom))
                bottom_navigation_main.visibility = View.VISIBLE
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        /*if (bottom_navigation_main.selectedItemId == R.id.settings) {
            bottom_navigation_main.selectedItemId = selectedItemId
            bottom_navigation_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom))
            bottom_navigation_main.visibility = View.VISIBLE
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            super.onBackPressed()
        }*/

        when (bottom_navigation_main.selectedItemId) {
            R.id.settings -> {
                bottom_navigation_main.selectedItemId = selectedItemId
                bottom_navigation_main.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_from_bottom))
                bottom_navigation_main.visibility = View.VISIBLE
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
            R.id.servers -> bottom_navigation_main.selectedItemId = R.id.dashboard
            else -> super.onBackPressed()
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
