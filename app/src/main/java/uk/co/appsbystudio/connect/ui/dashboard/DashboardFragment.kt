package uk.co.appsbystudio.connect.ui.dashboard

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
import uk.co.appsbystudio.connect.data.models.ServerModel

class DashboardFragment : Fragment(), SavedServerAdapter.Callback {

    private lateinit var viewModel: DashboardViewModel

    private var serverAdapter: SavedServerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        serverAdapter = SavedServerAdapter(ArrayList(), this)

        viewModel.getFavouriteServers().observe(this, Observer<List<ServerModel>>() {
            serverAdapter?.addItem(it)
        })

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

    override fun setSelected(uid: Int) {
        viewModel.setSelected(uid)
    }

}
