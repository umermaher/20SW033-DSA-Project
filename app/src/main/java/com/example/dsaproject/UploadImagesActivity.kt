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

class UploadImagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_images)

        val recyclerView=findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this)

        val uploadLists=ArrayList<Model>()
        var adapter:ImageAdapter

        val databaseReference=FirebaseDatabase.getInstance().getReference("uploads")
        databaseReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapShot in snapshot.children){
                    val name= postSnapShot.child("name").value as String?
                    val uri=postSnapShot.child("uri").value as String?
                    uploadLists.add(Model(name,uri))
                }
                adapter=ImageAdapter(this@UploadImagesActivity,uploadLists)
                recyclerView.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UploadImagesActivity,error.message,Toast.LENGTH_LONG).show()
            }
        })
    }
}