package com.example.bluetoothclient

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bluetoothclient.ui.theme.BluetoothClientTheme

class MainActivity : ComponentActivity() {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//    private val TAG = "Pimienta"
//    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//    val receiver = object : BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        override fun onReceive(context: Context, intent: Intent) {
//            val action: String = intent.action!!
//            when(action) {
//                BluetoothDevice.ACTION_FOUND -> {
//                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
//
//                    val info = if (device.name != null) "${device.name}: ${device.address}" else device.address // MAC address
//                    Log.e(TAG, info)
//                }
//            }
//        }
//    }

    @SuppressLint("MissingPermission")
    fun connect() {
//        val didItWork = bluetoothAdapter?.startDiscovery()
//        Log.i("Pimienta", "Did it work:  ${didItWork.toString()}")
        var device: BluetoothDevice? = null
        val devices = bluetoothAdapter?.bondedDevices
        devices?.map { if (it.address == "88:29:9C:04:52:CE") device = it }
        val connectThread = device?.let { ConnectThread(bluetoothAdapter!!, it) }

        connectThread?.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Esta aplicación requiere acceder a la ubicación y al Bluetooth")
            builder.setMessage("Por favor, otorga los permisos correspondientes para un desempeño correcto de la app.")
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ),
                    1
                )
            }
            builder.show()
        }

//        registerReceiver(receiver, filter)

        setContent {
            BluetoothClientTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Button(onClick = { connect() }) {
                        Text("Conectar")
                    }
                }
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(receiver)
//    }
}