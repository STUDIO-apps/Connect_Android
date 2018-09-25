package uk.co.appsbystudio.connect.ui.server

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.ui.server.add.AddServerActivity
import uk.co.appsbystudio.connect.utils.OnSocketStateChangeListener

class ServerFragment : androidx.fragment.app.Fragment(), ServerAdapter.Callback, OnSocketStateChangeListener.SocketStateReceiverListener {

    private var onSocketStateChangeListener = OnSocketStateChangeListener()
    private var connected: Boolean? = false

    private lateinit var viewModel: ServerViewModel
    private var serverAdapter: ServerAdapter? = null

    companion object {
        fun newInstance(connected: Boolean) = ServerFragment().apply {
            arguments = Bundle().apply {
                putBoolean("connected", connected)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(ServerViewModel::class.java)

        connected = arguments?.getBoolean("connected")

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
            startActivity(Intent(context, AddServerActivity::class.java))
        }
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
