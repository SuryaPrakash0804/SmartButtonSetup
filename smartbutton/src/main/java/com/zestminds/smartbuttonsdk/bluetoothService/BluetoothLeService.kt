package com.zestminds.smartbuttonsdk.bluetoothService

import android.app.Service
import android.bluetooth.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.*
import java.util.*


class BluetoothLeService :Service(){

    var byteArrayOutputStream=ByteArrayOutputStream()
    private var mBluetoothManager: BluetoothManager?=null
    private var mBluetoothAdapter: BluetoothAdapter?=null
    private var mBluetoothDeviceAddress:String?=null
    private var mBluetoothGatt: BluetoothGatt?=null
    private var mConnectionState = STATE_DISCONNECTED
    // Implements callback methods for GATT events that the app cares about. For example,
    // connection change and services discovered.
    private val mGattCallback = @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt:BluetoothGatt, status:Int, newState:Int) {
            val intentAction:String
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                intentAction = ACTION_GATT_CONNECTED
                mConnectionState = STATE_CONNECTED
                broadcastUpdate(intentAction)
                Log.i(TAG, "Connected to GATT server.")
                // Attempts to discover services after successful connection.
                Log.i(TAG, ("Attempting to start service discovery:" + mBluetoothGatt?.discoverServices()))
            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                intentAction = ACTION_GATT_DISCONNECTED
                mConnectionState = STATE_DISCONNECTED
                Log.i(TAG, "Disconnected from GATT server.")
                broadcastUpdate(intentAction)
            }
        }
        override fun onServicesDiscovered(gatt:BluetoothGatt, status:Int) {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
            }
            else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status)
            }
        }

        override fun onCharacteristicRead(gatt:BluetoothGatt,
                                          characteristic:BluetoothGattCharacteristic,
                                          status:Int) {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
            }
        }

        override fun onCharacteristicChanged(gatt:BluetoothGatt,
                                             characteristic:BluetoothGattCharacteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
        }




    }




    private val mBinder = LocalBinder()

    val supportedGattServices: List<BluetoothGattService>?
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        get() {
            if (mBluetoothGatt == null) return null
            return mBluetoothGatt?.services!!
        }
    private fun broadcastUpdate(action:String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun broadcastUpdate(action:String,
                                characteristic:BluetoothGattCharacteristic) {
        val intent = Intent(action)
        // This is special handling for the Heart Rate Measurement profile. Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_MEASUREMENT.equals(characteristic.uuid))
        {
            val flag = characteristic.properties
            var format = -1
            if ((flag and 0x01) != 0)
            {
                format = BluetoothGattCharacteristic.FORMAT_UINT16
                Log.d(TAG, "Heart rate format UINT16.")
            }
            else
            {
                format = BluetoothGattCharacteristic.FORMAT_UINT8
                Log.d(TAG, "Heart rate format UINT8.")
            }
            val heartRate = characteristic.getIntValue(format, 1)
            Log.d(TAG, String.format("Received heart rate: %d", heartRate))
            intent.putExtra(EXTRA_DATA, (heartRate).toString())
        }
        else
        {
            // For all other profiles, writes the data formatted in HEX.
            val data = characteristic.value
            if (data != null && data.isNotEmpty())
            {

            }
        }
        sendBroadcast(intent)
    }













    inner class LocalBinder: Binder() {
        internal val service:BluetoothLeService
            get() {
                return this@BluetoothLeService
            }
    }
    override fun onBind(intent:Intent): IBinder {
        return mBinder
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onUnbind(intent:Intent):Boolean {

        close()
        return super.onUnbind(intent)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun initialize():Boolean {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null)
        {
            mBluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            if (mBluetoothManager == null)
            {
                Log.e(TAG, "Unable to initialize BluetoothManager.")
                return false
            }
        }
        mBluetoothAdapter = mBluetoothManager?.adapter
        if (mBluetoothAdapter == null)
        {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun connect(address:String):Boolean {
        if (mBluetoothAdapter == null || address == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.")
            return false
        }
        // Previously connected device. Try to reconnect.
        if ((mBluetoothDeviceAddress != null && address == mBluetoothDeviceAddress
                    && mBluetoothGatt != null))
        {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.")
            return if (mBluetoothGatt?.connect()!!) {
                mConnectionState = STATE_CONNECTING
                true
            } else {
                false
            }
        }
        val device = mBluetoothAdapter?.getRemoteDevice(address)
        if (device == null)
        {
            Log.w(TAG, "Device not found. Unable to connect.")
            return false
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback)
        Log.d(TAG, "Trying to create a new connection.")
        mBluetoothDeviceAddress = address
        mConnectionState = STATE_CONNECTING
        return true
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt?.disconnect()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun close() {
        if (mBluetoothGatt == null)
        {
            return
        }
        mBluetoothGatt?.close()
        mBluetoothGatt = null
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun readCharacteristic(characteristic:BluetoothGattCharacteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null)
        {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt?.readCharacteristic(characteristic)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic,
                                      enabled: Boolean) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)

        // This is specific to Heart Rate Measurement.
        if (UUID_MEASUREMENT == characteristic.uuid) {
            val descriptor = characteristic.getDescriptor(
                UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mBluetoothGatt!!.writeDescriptor(descriptor)
        }
    }    companion object {
        private val TAG = BluetoothLeService::class.java.simpleName
        private val STATE_DISCONNECTED = 0
        private val STATE_CONNECTING = 1
        private val STATE_CONNECTED = 2
        val ACTION_GATT_CONNECTED = "ACTION_GATT_CONNECTED"
        val ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED"
        val ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED"
        val ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE"
        val EXTRA_DATA = "EXTRA_DATA"
        val UUID_MEASUREMENT = UUID.fromString(SampleGattAttributes.Generic_Attribute)
    }

}