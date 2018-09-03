package uk.co.appsbystudio.connect.ui.server

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.server_list_item.view.*
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel

class ServerAdapter(private var serverModelList: List<ServerModel>, private var callback: Callback) : RecyclerView.Adapter<ServerAdapter.ViewHolder>() {

    interface Callback {
        fun setSelected(uid: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.server_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val serverModel = serverModelList[holder.adapterPosition]

        holder.apply {
            name.text = serverModel.name
            address.text = "${serverModel.address}:${serverModel.port}"
            selected.isChecked = serverModel.selected

            selected.setOnClickListener {
                callback.setSelected(serverModel.uid!!)
            }
        }

        holder.view.setOnLongClickListener {
            holder.apply {
                checkbox.isChecked = true
            }
            return@setOnLongClickListener true
        }
    }

    fun addItem(serverModelList: List<ServerModel>?) {
        if (serverModelList != null) this.serverModelList = serverModelList
        notifyDataSetChanged()
    }

    override fun getItemCount() = serverModelList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val connectionStatus: ImageView = itemView.image_connection_status_item
        val name: TextView = itemView.text_server_name_item
        val address: TextView = itemView.text_server_address_item
        val selected: RadioButton = itemView.radio_selected_item

        val checkbox: CheckBox = itemView.checkbox_selected_item
        val star: ImageView = itemView.image_star_item

        init {
            view.setOnLongClickListener {
                checkbox.visibility = View.VISIBLE
                star.visibility = View.VISIBLE

                connectionStatus.visibility = View.INVISIBLE
                selected.visibility = View.INVISIBLE

                return@setOnLongClickListener true
            }
        }
    }
}