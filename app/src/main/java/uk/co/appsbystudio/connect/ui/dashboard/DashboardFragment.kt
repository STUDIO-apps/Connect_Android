package uk.co.appsbystudio.connect.ui.dashboard

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.utils.OnSocketStateChangeListener

class DashboardFragment : androidx.fragment.app.Fragment(), SavedServerAdapter.Callback, OnSocketStateChangeListener.SocketStateReceiverListener {

    private var onSocketStateChangeListener = OnSocketStateChangeListener()
    private var connected: Boolean? = false

    private lateinit var viewModel: DashboardViewModel
    private var serverAdapter: SavedServerAdapter? = null

    companion object {
        fun newInstance(connected: Boolean) = DashboardFragment().apply {
            arguments = Bundle().apply {
                putBoolean("connected", connected)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        serverAdapter = SavedServerAdapter(ArrayList(), connected, this)

        viewModel.getFavouriteServers().observe(this, Observer<List<ServerModel>>() {
            serverAdapter?.addItem(it)
        })

        context?.registerReceiver(onSocketStateChangeListener, IntentFilter("socket.state"))
        onSocketStateChangeListener.addListener(this)

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_fave_dashboard.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = serverAdapter
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

    override fun socketConnected() {
        serverAdapter?.setConnectionState(true)
    }

    override fun socketDisconnected() {
        serverAdapter?.setConnectionState(false)
    }
}
