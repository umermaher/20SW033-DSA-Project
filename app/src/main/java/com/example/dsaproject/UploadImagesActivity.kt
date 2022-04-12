package com.example.dsaproject

import android.location.GnssAntennaInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class UploadImagesActivity : AppCompatActivity() ,ImageAdapter.OnItemClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_images)

        val recyclerView=findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager=LinearLayoutManager(this)
        linearLayoutManager.reverseLayout
        recyclerView.layoutManager=linearLayoutManager
//        val uploadLists=ArrayList<Model>()
//        var uploadLists=LinkedList<Model>()

        val databaseReference=FirebaseDatabase.getInstance().getReference("uploads")
        databaseReference.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                var uploadLists=Node()
                for(postSnapShot in snapshot.children){
                    val name= postSnapShot.child("name").value as String?
                    val uri=postSnapShot.child("uri").value as String?
//                    uploadLists.add(Model(name,uri))
//                    linkedStack.push(Model(name,uri))
                    uploadLists=uploadLists.addFirst(Model(name,uri))
                }
                val adapter=ImageAdapter(this@UploadImagesActivity,uploadLists)
                recyclerView.adapter=adapter
                adapter.setOnItemClickListener(this@UploadImagesActivity)

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UploadImagesActivity,error.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"$position",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(position: Int) {

    }
}