package uk.co.appsbystudio.connect.ui.server

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_server.*
import kotlinx.android.synthetic.main.fragment_server.button_add_server
import kotlinx.android.synthetic.main.fragment_server.entry_address_server
import kotlinx.android.synthetic.main.fragment_server.entry_name_server
import kotlinx.android.synthetic.main.fragment_server.entry_port_server
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.utils.OnSocketStateChangeListener

class ServerFragment : androidx.fragment.app.Fragment(), ServerAdapter.Callback, OnSocketStateChangeListener.SocketStateReceiverListener {

    private var onSocketStateChangeListener = OnSocketStateChangeListener()
    private var connected: Boolean? = false

    private lateinit var viewModel: ServerViewModel
    private var serverAdapter: ServerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(ServerViewModel::class.java)

        serverAdapter = ServerAdapter(ArrayList(), connected, this)

        viewModel.getServers().observe(this, Observer<List<ServerModel>>() {
            serverAdapter?.addItem(it)
        })

        context?.registerReceiver(onSocketStateChangeListener, IntentFilter("socket.state"))
        onSocketStateChangeListener.addListener(this)

        return inflater.inflate(R.layout.fragment_server, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_servers_server.apply {
            setHasFixedSize(true)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = serverAdapter
        }

        fab_add_server.setOnClickListener {
            val finalRadius = Math.max(layout_add_server_container.width, layout_add_server_container.height) * 1.1f
            val y = constraint_fragment_server.height - layout_add_server_container.y - (fab_add_server.paddingBottom + (fab_add_server.height /2))
            val anim = ViewAnimationUtils.createCircularReveal(layout_add_server_container, fab_add_server.left + (fab_add_server.width / 2), y.toInt(), (fab_add_server.width / 2).toFloat(), finalRadius)

            layout_add_server.visibility = View.VISIBLE
            fab_add_server.visibility = View.INVISIBLE

            anim.start()
        }

        layout_add_server.setOnClickListener {
            closeAddServer()
        }

        button_close.setOnClickListener {
            closeAddServer()
        }

        button_add_server.setOnClickListener {
            val serverModel = ServerModel(
                    name = entry_name_server.text.toString(),
                    address = entry_address_server.text.toString(),
                    port = entry_port_server.text.toString().toInt(),
                    selected = false,
                    favourite = false)

            viewModel.addServer(serverModel)
        }
    }

    private fun closeAddServer() {
        val finalRadius = Math.max(layout_add_server_container.width, layout_add_server_container.height) * 1.1f
        val y = constraint_fragment_server.height - layout_add_server_container.y - (fab_add_server.paddingBottom + (fab_add_server.height /2))
        val anim = ViewAnimationUtils.createCircularReveal(layout_add_server_container, fab_add_server.left + (fab_add_server.width / 2), y.toInt(), finalRadius, (fab_add_server.width / 2).toFloat())

        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                layout_add_server.visibility = View.INVISIBLE
                fab_add_server.visibility = View.VISIBLE
            }

        })

        anim.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(onSocketStateChangeListener)
        onSocketStateChangeListener.removeListener(this)
    }

    override fun setSelected(uid: Int) {
        viewModel.setSelected(uid)
    }

    override fun toggleFavourite(uid: Int, isFavourite: Boolean) {
        viewModel.toggleFavourite(uid, isFavourite)
    }

    override fun socketConnected() {
        serverAdapter?.setConnectionState(true)
    }

    override fun socketDisconnected() {
        serverAdapter?.setConnectionState(false)
    }
}
