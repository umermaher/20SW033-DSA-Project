package com.example.dsaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import androidx.appcompat.widget.SearchView

class UploadImagesActivity : AppCompatActivity() ,ImageAdapter.OnItemClickListener{
    lateinit var mAdapter: ImageAdapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var mFirebaseStorage: FirebaseStorage
    private lateinit var mUploadLists: LinkedList
    private lateinit var databaseReference: DatabaseReference
    private lateinit var valueEventListener: ValueEventListener
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_images)

        supportActionBar?.hide()
        val noInternetLayout=findViewById<RelativeLayout>(R.id.noInternetLayout)
        mRecyclerView=findViewById(R.id.recycler_view)
        progressBar=findViewById(R.id.deleteProgressBar)
        val searchView =findViewById<SearchView>(R.id.searchView)

        connectivityLiveData= ConnectivityLiveData(this)
        connectivityLiveData.observe(this,{
            if(it==false)
                noInternetLayout.visibility= View.VISIBLE
            else
                noInternetLayout.visibility=View.GONE
        })

        mRecyclerView.setHasFixedSize(true)
        val linearLayoutManager=LinearLayoutManager(this)
        linearLayoutManager.reverseLayout
        mRecyclerView.layoutManager=linearLayoutManager
        loadRecyclerView()
//        val uploadLists=ArrayList<Model>()
//        var uploadLists=LinkedList<Model>()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(p0: String?): Boolean {
                return false;
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                val i=mUploadLists?.search(p0)
                if(i!=-1)
                    Toast.makeText(this@UploadImagesActivity,"Item found at adapter position : $i",Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(this@UploadImagesActivity,"No item found!",Toast.LENGTH_LONG).show()
                return false;
            }
        })
    }

    private fun loadRecyclerView() {

        mUploadLists= LinkedList()

        mFirebaseStorage= FirebaseStorage.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads")
        valueEventListener=databaseReference.addValueEventListener(object:ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                for(postSnapShot in snapshot.children){
                    val name= postSnapShot.child("name").value as String?
                    val uri=postSnapShot.child("uri").value as String?
                    val key=postSnapShot.key
//                    uploadLists.add(Model(name,uri))
//                    linkedStack.push(Model(name,uri))
                    val model=Model(name,uri)
                    model.setKey(key.toString())
                    mUploadLists.addFirst(model)
                }

                mAdapter=ImageAdapter(this@UploadImagesActivity, mUploadLists!!)
                mRecyclerView.adapter=mAdapter
//                mAdapter.updateNews(uploadLists)
                mAdapter.setOnItemClickListener(this@UploadImagesActivity)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UploadImagesActivity,error.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this,"$position",Toast.LENGTH_SHORT).show()
    }

    override fun onDeleteClick(position: Int){
        progressBar.visibility=View.VISIBLE

        val selectedItem= mUploadLists.get(position)
        val selectedKey=selectedItem.getKey()

        val imgReference=mFirebaseStorage.getReferenceFromUrl(selectedItem?.uri.toString())

        imgReference.delete().addOnSuccessListener {

            databaseReference.child(selectedKey).removeValue()
                .addOnSuccessListener {
                    mUploadLists.clear()
                    loadRecyclerView()
                    progressBar.visibility=View.GONE
                    Toast.makeText(this,"Deleted",Toast.LENGTH_LONG).show()

                }

        }.addOnFailureListener {
            Toast.makeText(this,"Failed!",Toast.LENGTH_LONG).show()
            progressBar.visibility=View.GONE
        }
//        Toast.makeText(this,"Delete ${mUploadLists.get(position).getKey()}",Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference.removeEventListener(valueEventListener)
    }
}