package uk.co.appsbystudio.connect.dashboard

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_dashboard.*

import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.models.ServerModel

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel

    private var serverAdapter: SavedServerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        serverAdapter = SavedServerAdapter(ArrayList())

        viewModel.getFavouriteServers().observe(this, Observer<List<ServerModel>>() {
            serverAdapter?.addItem(it)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_fave_dashboard.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = serverAdapter
        }
    }

}
