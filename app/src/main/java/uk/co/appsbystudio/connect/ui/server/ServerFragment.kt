package uk.co.appsbystudio.connect.ui.server

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_server.*

import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.models.ServerModel

class ServerFragment : Fragment() {

    private lateinit var viewModel: ServerViewModel
    private var serverAdapter: ServerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(ServerViewModel::class.java)

        serverAdapter = ServerAdapter(ArrayList())

        viewModel.getServers().observe(this, Observer<List<ServerModel>>() {
            serverAdapter?.addItem(it)
        })

        return inflater.inflate(R.layout.fragment_server, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_servers_server.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = serverAdapter
        }

        button_add_server.setOnClickListener {
            val serverModel = ServerModel(
                    name = "My Amazing PC",
                    address = editTextAddress.text.toString(),
                    port = editTextPort.text.toString().toInt(),
                    selected = false,
                    favourite = true)

            viewModel.addServer(serverModel)

        }
    }
}
