package db_room.UserInterface

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import db_room.R

class Adapter(val context : Context) : ListAdapter<Model, Adapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private lateinit var listener: RecyclerClickListener
    fun setItemListener(listener: RecyclerClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        val viewHolder = ViewHolder(v)

        val delete = viewHolder.itemView.findViewById<CardView>(R.id.delete)
        delete.setOnClickListener {
            listener.onItemRemoveClick(viewHolder.adapterPosition)
        }

        val cardview = viewHolder.itemView.findViewById<CardView>(R.id.cardview)
        cardview.setOnClickListener {
            listener.onItemClick(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        val position = holder.itemView.findViewById<TextView>(R.id.position)
        val name = holder.itemView.findViewById<TextView>(R.id.name)
        val id = holder.itemView.findViewById<TextView>(R.id.id)
        position.text = currentItem.position.toString()
        name.text = currentItem.name
        id.text = currentItem.id
    }

    class DiffCallback : DiffUtil.ItemCallback<Model>() {
        override fun areItemsTheSame(oldItem: Model, newItem: Model) =
            oldItem.position == newItem.position

        override fun areContentsTheSame(oldItem: Model, newItem: Model) =
            oldItem == newItem
    }
}