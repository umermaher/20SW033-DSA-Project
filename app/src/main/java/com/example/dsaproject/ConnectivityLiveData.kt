package com.example.dsaproject

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

class ConnectivityLiveData(private val context: Context): LiveData<Boolean>() {
    private val TAG="C-Manager"
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks : MutableSet<Network> = HashSet()

    private fun checkValidNetworks(){
        postValue(validNetworks.size>0)
    }

//    The onActive() method is called when the LiveData object has an active observer.
    override fun onActive() {
        networkCallback=createNetworkCallback()
        val networkRequest=NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback(){
        //called when a network is detected, if that network has internet, save it in the set.
        override fun onAvailable(network: Network) {
            Log.d(TAG, "onAvailable: $network")
            val networkCapabilities=connectivityManager.getNetworkCapabilities(network)
            val isInternet=networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            if(isInternet==true) {
                // check if this network actually has internet
                val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                if (hasInternet) {
                    Log.d(TAG, "onAvailable: adding network. $network")
                    validNetworks.add(network)
                    checkValidNetworks()
                }
            }
        }
        //if the callback was registered with registerNetworkCallback()
        //it will be called for each network which no longer satisfied the criteria of the callback
        override fun onLost(network: Network) {
            Log.d(TAG,"onLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    object DoesNetworkHaveInternet {
        fun execute(socketFactory: SocketFactory): Boolean {
            // Make sure to execute this on a background thread.
            return try {
                Log.d("C-Manager", "PINGING Google...")
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
                Log.d("C-Manager", "PING success.")
                true
            } catch (e: IOException) {
                Log.e("C-Manager", "No Internet Connection. $e")
                false
            }
        }
    }
}