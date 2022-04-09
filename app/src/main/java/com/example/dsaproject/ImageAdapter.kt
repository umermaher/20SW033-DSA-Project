package com.example.dsaproject

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ImageAdapter(val context: Context, private val items:ArrayList<Model>) : Adapter<ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem=items[position]
        holder.textView.text = currentItem.name
        Picasso.get().load(currentItem.uri).fit().centerCrop().into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size

}

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView =itemView.findViewById(R.id.image_name)
    val imageView: ImageView =itemView.findViewById(R.id.image_view_upload)
}