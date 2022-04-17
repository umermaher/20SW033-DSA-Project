package com.example.dsaproject

import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class ImageAdapter(private var listener: OnItemClickListener,private var items:LinkedList=LinkedList()) :
    Adapter<ImageAdapter.ImageViewHolder>() {

    private  var fullItems:LinkedList= LinkedList()


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

    override fun getItemCount(): Int = items.size

    fun updateNews(updatedList:LinkedList){
        items.clear()
        items.addAll(updatedList)
        fullItems.clear()
        fullItems.addAll(updatedList)
        //reloading the recycler view
        notifyDataSetChanged()
    }

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

//    override fun getFilter(): Filter = filter
//
//    private val filter=object:Filter(){
//        override fun performFiltering(p0: CharSequence?): FilterResults {
//            var filteredList=LinkedList()
//            if(p0==null || p0.isEmpty()){
//                filteredList.addAll(fullItems)
//            }else{
//                val filterPattern= p0.toString().lowercase(Locale.getDefault()).trim()
//                for(i in 0 until fullItems.size){
//                    if(fullItems.get(i).name?.contains(filterPattern) == true){
//                        filteredList.addFirst(fullItems.get(i))
//                    }
//                }
//            }
//            val result=FilterResults()
//            result.values =filteredList
//            return result
//        }
//
//        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//            items.clear()
//            items.addAll(p1?.values as LinkedList)
//            notifyDataSetChanged()
//        }
//    }
}
