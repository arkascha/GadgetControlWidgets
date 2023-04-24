package org.rustygnome.gadgetcontrolwidgets.widget.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.rustygnome.gadgetcontrolwidgets.R

internal class Adapter (
    private val list: List<Item>
) : RecyclerView.Adapter<Adapter.GadgetView?>() {

    inner class GadgetView(view: View) : RecyclerView.ViewHolder(view) {

        var itemContainer: LinearLayout
        var itemIcon: ImageView
        var itemName: TextView

        init {
            with(view) {
                itemContainer = findViewById(R.id.bluetoothWidget_itemFull_itemContainer)
                itemIcon = findViewById(R.id.bluetoothWidget_itemFull_itemIcon)
                itemName = findViewById(R.id.bluetoothWidget_itemFull_itemName)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GadgetView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.bluetooth_gadget_item_full,
                parent,
                false
            )
        return GadgetView(itemView)
    }

    override fun onBindViewHolder(
        holder: GadgetView,
        position: Int
    ) {
        holder.itemIcon.setImageResource(list[position].icon)
        holder.itemName.text = list[position].name
    }

    override fun getItemCount() = list.size
}