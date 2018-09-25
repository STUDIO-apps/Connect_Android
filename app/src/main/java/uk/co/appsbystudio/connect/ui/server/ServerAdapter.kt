package uk.co.appsbystudio.connect.ui.server

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import kotlinx.android.synthetic.main.server_list_item.view.*
import kotlinx.android.synthetic.main.server_list_item_selected.view.*
import uk.co.appsbystudio.connect.R
import uk.co.appsbystudio.connect.data.models.ServerModel

class ServerAdapter(private var serverModelList: List<ServerModel>, connected: Boolean?, private var callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private var isSelected = false
    private var itemSelected: Int? = null

    private var connected: Boolean = false

    interface Callback {
        fun setSelected(uid: Int)
        fun toggleFavourite(uid: Int, isFavourite: Boolean)
    }

    init {
        if (connected != null) this.connected = connected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == R.layout.server_list_item_selected) return ViewHolderSelected(LayoutInflater.from(parent.context).inflate(R.layout.server_list_item_selected, parent, false))
        return ViewHolderDefault(LayoutInflater.from(parent.context).inflate(R.layout.server_list_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val serverModel = serverModelList[holder.adapterPosition]

        when (holder.itemViewType) {
            R.layout.server_list_item -> {
                val holderDefault: ViewHolderDefault = holder as ViewHolderDefault

                holderDefault.apply {
                    name.text = serverModel.name
                    address.text = "${serverModel.address}:${serverModel.port}"
                    selected.isChecked = serverModel.selected

                    connectionStatus.setImageResource(if (connected && serverModel.selected) R.drawable.ic_signal_wifi_4_bar_black_24dp else R.drawable.ic_signal_wifi_off_black_24dp)

                    selected.setOnClickListener {
                        callback.setSelected(serverModel.uid!!)
                    }
                }

                holderDefault.view.setOnLongClickListener {
                    holderDefault.apply {
                        itemSelected = adapterPosition
                        isSelected = true
                        notifyDataSetChanged()
                    }
                    return@setOnLongClickListener true
                }
            }
            R.layout.server_list_item_selected -> {
                val holderDefault: ViewHolderSelected = holder as ViewHolderSelected

                holderDefault.apply {
                    name.text = serverModel.name
                    address.text = "${serverModel.address}:${serverModel.port}"
                    star.setImageResource(if (serverModel.favourite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp)

                    star.setOnClickListener {
                        callback.toggleFavourite(serverModel.uid!!, !serverModel.favourite)
                        star.setImageResource(if (serverModel.favourite) R.drawable.ic_star_border_black_24dp else R.drawable.ic_star_black_24dp)
                    }
                }

                holderDefault.view.setOnLongClickListener {
                    holderDefault.apply {
                        if (adapterPosition == itemSelected) checkbox.isChecked = true
                        isSelected = true
                        notifyDataSetChanged()
                    }
                    return@setOnLongClickListener true
                }
            }
        }
    }

    fun addItem(serverModelList: List<ServerModel>?) {
        if (serverModelList != null) this.serverModelList = serverModelList
        notifyDataSetChanged()
    }

    fun setConnectionState(connected: Boolean) {
        this.connected = connected
        notifyDataSetChanged()
    }

    override fun getItemCount() = serverModelList.size

    override fun getItemViewType(position: Int): Int {
        return if (!isSelected) R.layout.server_list_item else R.layout.server_list_item_selected
    }

    class ViewHolderDefault(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val connectionStatus = itemView.image_connection_status_item
        val name: TextView = itemView.text_server_name_item
        val address: TextView = itemView.text_server_address_item
        val selected: RadioButton = itemView.radio_selected_item
    }

    class ViewHolderSelected(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val view = itemView

        val name: TextView = itemView.text_server_name_item_selected
        val address: TextView = itemView.text_server_address_item_selected

        val checkbox: CheckBox = itemView.checkbox_selected_item_selected
        val star: ImageView = itemView.image_star_item_selected
    }
}