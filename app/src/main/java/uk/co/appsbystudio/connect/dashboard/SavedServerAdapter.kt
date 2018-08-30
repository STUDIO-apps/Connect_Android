package uk.co.appsbystudio.connect.dashboard

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.server_list_item.view.*
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.models.ServerModel

class SavedServerAdapter(private var serverModelList: List<ServerModel>) : RecyclerView.Adapter<SavedServerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.server_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serverModel = serverModelList.get(position)
        holder.apply {
            name.text = serverModel.name
            address.text = "${serverModel.address}:${serverModel.port}"
            selected.isSelected = serverModel.selected
        }
    }

    fun addItem(serverModelList: List<ServerModel>?) {
        if (serverModelList != null) this.serverModelList = serverModelList
        notifyDataSetChanged()
    }

    override fun getItemCount() = serverModelList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val connectionStatus = itemView.image_connection_status_item
        val name = itemView.text_server_name_item
        val address = itemView.text_server_address_item
        val selected = itemView.radio_selected_item
    }
}