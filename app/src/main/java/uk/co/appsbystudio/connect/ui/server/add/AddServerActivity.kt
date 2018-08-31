package uk.co.appsbystudio.connect.ui.server.add

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_server.*

import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel
import uk.co.appsbystudio.connect.ui.server.ServerViewModel

class AddServerActivity : AppCompatActivity() {

    private lateinit var viewModel: ServerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_server)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(ServerViewModel::class.java)

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

}
