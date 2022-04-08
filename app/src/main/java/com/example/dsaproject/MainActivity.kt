package com.example.dsaproject

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    lateinit var mImageUri:Uri
    private lateinit var mImage:ImageView
    lateinit var storageReference: StorageReference
    lateinit var databaseReferance: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chooseFileBtn= findViewById<Button>(R.id.choose_file)
        val uploadBtn=findViewById<Button>(R.id.upload_image)
        val fileNameText=findViewById<TextInputLayout>(R.id.file_name_text)
        mImage=findViewById<ImageView>(R.id.image_view)


        storageReference=FirebaseStorage.getInstance().getReference("uploads")
        databaseReferance= FirebaseDatabase.getInstance().getReference("uploads")

        chooseFileBtn.setOnClickListener{
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        uploadBtn.setOnClickListener {
            uploadFile()
        }
    }

    private fun getFileExtension(uri:Uri): String? {
        var cr:ContentResolver=contentResolver
        var mime:MimeTypeMap= MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    private fun uploadFile() {
        if(mImageUri!=null){
            var fileRefence: StorageReference=storageReference.child(
                System.currentTimeMillis().toString()+"."+getFileExtension(mImageUri)
            )
            fileRefence.putFile(mImageUri)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
                .addOnProgressListener {

                }
        }else{
            Toast.makeText(this,"No file Selected!",Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mImageUri= data?.data!!
        mImage.setImageURI(mImageUri)
    }
}