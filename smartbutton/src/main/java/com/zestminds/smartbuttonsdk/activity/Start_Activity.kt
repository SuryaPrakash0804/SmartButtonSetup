package com.zestminds.smartbuttonsdk.activity

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.zestminds.smartbuttonsdk.R
import com.zestminds.smartbuttonsdk.bluetoothService.BluetoothLeService
import com.zestminds.smartbuttonsdk.bluetoothService.SampleGattAttributes
import kotlinx.android.synthetic.main.activity_start.*

class Start_Activity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)


        init()
    }


    private fun init(){
        var type=""

        if (intent!=null && intent.getStringExtra("type")!=null){
            type=intent?.getStringExtra("type")!!

            if (type.equals("1",true)){
                iv_button.setImageResource(R.drawable.ic_smart_device_with_circle)
            }
            else{
                iv_button.setImageResource(R.drawable.ic_smart_watch)
            }
        }

        clickOnView()
    }


    private fun clickOnView(){
        tv_setup.setOnClickListener {
            
        }
    }





}