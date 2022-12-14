package com.example.bluetoothclient

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.bluetoothclient.data.Message
import java.io.IOException
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class ConnectThread(private val bluetoothAdapter: BluetoothAdapter, private val device: BluetoothDevice) : Thread() {
    private val TAG = "Pimienta"
    private val bClientS: BluetoothSocket? = try {
        device.createRfcommSocketToServiceRecord(UUID.fromString("f1473161-213d-4607-bd29-519b31206e63"))
    } catch (e: IOException) {
        Log.e(TAG, "Can't connect", e)
        null
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery()

        bClientS?.use { socket ->
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            try {
                socket.connect()
                if (socket.isConnected) {
                    Log.w(TAG,"Connected!")
                    val mmOutStream: OutputStream = socket.outputStream
                    val message = Message.newBuilder().setContent("Saludos desde la tablet!").build()
                    val bytes = message.toByteArray()

                    Log.i(TAG, "Tamaño del array de bytes por enviar: " + bytes.size.toString())
                    bytes.forEach { Log.i(TAG, it.toString()) }
                    mmOutStream.write(bytes)

//                    var i = 0
//
//                    while (i < 4) {
//                        mmOutStream.write(ByteArray(1))
//                        i++
//                    }
                } else {
                    Log.e(TAG,"Socket isn't connected")
                }

            } catch (e: IOException) {
                Log.e(TAG, "Can't connect", e)
            }


            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
//            manageMyConnectedSocket(socket)
        }
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            bClientS?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }
}