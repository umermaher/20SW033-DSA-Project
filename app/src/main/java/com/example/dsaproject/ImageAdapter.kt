package com.example.dsaproject

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.*
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.util.*

class ImageAdapter(
    private var listener: OnItemClickListener,private var items:Node) : Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem= items.get(position)
        if(currentItem!=null){
            holder.textView.text = currentItem.name
            Picasso.get().load(currentItem.uri).fit().centerCrop().into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = items.size()

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener=listener
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        var textView: TextView = itemView.findViewById(R.id.image_name)
        var imageView:ImageView = itemView.findViewById(R.id.image_view_upload)
        init {
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onClick(p0: View?) {
            if(listener!=null){
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            contextMenu: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Select Action")
            val item:MenuItem?=menu?.add(Menu.NONE,1,1,"Delete")

            item?.setOnMenuItemClickListener(this)
        }

        override fun onMenuItemClick(p0: MenuItem?): Boolean {
            if(listener!=null){
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    when(p0?.itemId){
                        1->{
                            listener.onDeleteClick(position)
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}
