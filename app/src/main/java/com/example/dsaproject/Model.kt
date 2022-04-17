package com.example.dsaproject

import com.google.firebase.database.Exclude

data class Model(val name:String?=null,val uri:String?=null){

    @Exclude private lateinit var key:String
    @Exclude fun setKey(key:String){
        this.key=key
    }
    @Exclude fun getKey() : String = key
}