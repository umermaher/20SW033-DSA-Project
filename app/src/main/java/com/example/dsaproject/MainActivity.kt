package com.example.dsaproject

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.accessibility.AccessibilityEventCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask

class MainActivity : AppCompatActivity() {
     private lateinit var mImageUri:Uri
    private lateinit var mImage:ImageView
    lateinit var storageReference: StorageReference
    lateinit var databaseReference: DatabaseReference
    lateinit var linearProgressBar: ProgressBar
    lateinit var progressBar: ProgressBar
    lateinit var fileNameText:TextInputLayout
    var uploadTask:UploadTask?=null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chooseFileBtn= findViewById<Button>(R.id.choose_file)
        val uploadBtn=findViewById<Button>(R.id.upload_image)
        fileNameText=findViewById(R.id.file_name_text)
        mImage=findViewById(R.id.image_view)
        linearProgressBar=findViewById(R.id.upload_progress_bar)
        progressBar=findViewById(R.id.progressBar)

        storageReference=FirebaseStorage.getInstance().getReference("uploads")
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads")

        chooseFileBtn.setOnClickListener{
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        uploadBtn.setOnClickListener {
            if(mImage.drawable==null){
                Toast.makeText(this,"Select image!",Toast.LENGTH_LONG).show()
            } else if(fileNameText.editText?.text.toString().isEmpty()){
                Toast.makeText(this,"Enter file name!",Toast.LENGTH_LONG).show()
            } else if(uploadTask!=null && uploadTask!!.isInProgress){
                Toast.makeText(this,"Upload in progress!",Toast.LENGTH_LONG).show()
            } else{
                uploadFile()
            }
        }
    }

//    private fun getFileExtension(uri:Uri): String? {
//        val cr:ContentResolver=this.contentResolver
//        val mime:MimeTypeMap= MimeTypeMap.getSingleton()
//        return mime.getExtensionFromMimeType(cr.getType(uri).toString())
//    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadFile() {
        if(mImageUri!=null){
            val fileReference: StorageReference=storageReference.child(
                "image_"+System.currentTimeMillis().toString()
            )
//            +"."+getFileExtension(mImageUri)
//            Toast.makeText(this,"image_"+System.currentTimeMillis().toString()+"."+getFileExtension(mImageUri),Toast.LENGTH_LONG).show()

            progressBar.visibility=View.VISIBLE
            uploadTask= fileReference.putFile(mImageUri)
                .addOnSuccessListener {
                    fileReference.downloadUrl.addOnSuccessListener {

                        progressBar.visibility=View.GONE
                        val task=it

                        Handler().postDelayed(Runnable {
                            linearProgressBar.setProgress(0,true)
                        },2000)

                        Toast.makeText(this,"Upload Successful!",Toast.LENGTH_LONG).show()

                        val model=Model(
                            fileNameText.editText?.text.toString(), task.toString()
                        )
                        val uploadId:String?=databaseReference.push().key

                        if (uploadId != null) {
                            databaseReference.child(uploadId).setValue(model).addOnSuccessListener {
                                startActivity(Intent(this,UploadImagesActivity::class.java))
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    progressBar.visibility=View.GONE
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
                .addOnProgressListener {
                    var progress:Double=(100.0*it.bytesTransferred/it.totalByteCount)
                    linearProgressBar.setProgress(progress.toInt(),true)
                } as UploadTask

        }else{
            Toast.makeText(this,"No file Selected!",Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            mImageUri = data?.data!!
            mImage.setImageURI(mImageUri)
        }catch (exception:Exception){
            Toast.makeText(this,"Please select image",Toast.LENGTH_LONG).show()
        }
    }

    fun showUploads(view: android.view.View) = startActivity(Intent(this,UploadImagesActivity::class.java))
}