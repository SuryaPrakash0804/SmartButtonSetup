package com.zestminds.smartbuttonsdk.activity

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.zestminds.smartbuttonsdk.R
import com.zestminds.smartbuttonsdk.bean.DevicesList
import com.zestminds.smartbuttonsdk.dialog.hide
import com.zestminds.smartbuttonsdk.dialog.showToast
import com.zestminds.smartbuttonsdk.dialog.visible
import kotlinx.android.synthetic.main.activity_paring.*

class Paring_Activity : AppCompatActivity() {



    private  var mBluetoothAdapter: BluetoothAdapter?=null
    val RECORD_REQUEST_CODE=1
    private var mScanning = false
    private var mHandler: Handler? = null
    private var devicelist=ArrayList<DevicesList>()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paring)



        var permission= arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_EXTERNAL_STORAGE)

        if (!hasPermissions(permission))
        {
            askPermission()
        }


        init()

    }


    fun init(){
        cv_step1.visible()
        cv_step2.hide()
        cv_try_again.hide()
        mHandler= Handler(Looper.getMainLooper())



        clickOnView()

    }



    private fun clickOnView(){

        tv_continue.setOnClickListener {
            cv_step1.hide()
            cv_step2.visible()
        }

        tv_continuestep2.setOnClickListener {

        }

        tv_tryagain.setOnClickListener {
            cv_step2.hide()
            cv_try_again.hide()
            cv_step1.visible()
        }

    }

    fun hasPermissions(permissions: Array<String>): Boolean {
        if (permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission!!) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    private fun askPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("The Location permission is required for the core functionality")
                } else {
                    init()
                    if (mBluetoothAdapter?.isEnabled!!) {
                        scanLeDevice(true)
                     //   loading.visibility= View.VISIBLE

                    }
                }
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scanLeDevice(enable: Boolean) {
        val bluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler!!.postDelayed({
                mScanning = false
                bluetoothLeScanner.stopScan(mLeScanCallback)
            }, SCAN_PERIOD)
            mScanning = true
            bluetoothLeScanner.startScan(mLeScanCallback)
        } else {
            mScanning = false
            bluetoothLeScanner.stopScan(mLeScanCallback)
        }
    }





    private val mLeScanCallback: ScanCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
        //    loading.visibility=View.GONE
            if (!devicelist.isNullOrEmpty())
            {
                var isinlist=false
                for (de in devicelist) {
                    if (de.device.equals(result?.device))
                        isinlist = true

                }
                if (!isinlist)
                    devicelist.add(DevicesList(result?.device!!,false))
            }
            else{
                devicelist.add(DevicesList(result?.device!!,false))
            }

            if (!devicelist.isNullOrEmpty())
             //   myadapter.setData(devicelist,true)
            else{
                showToast("No Device Found")
            }

        }

        override fun onBatchScanResults(results: List<ScanResult?>?) {
            super.onBatchScanResults(results)
        //    loading.visibility=View.GONE

        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            showToast("Scan failed")
         //   loading.visibility=View.GONE
        }
    }




    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun pairDevice(devicesList: DevicesList) {

        val device: BluetoothDevice = devicesList.device ?: return
//        val intent = Intent(this, DeviceControlActivity::class.java)
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.name)
//        intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.address)
        if (mScanning) {
            val bluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
            bluetoothLeScanner.stopScan(mLeScanCallback)
            mScanning = false
        }
        startActivity(intent)
    }


}