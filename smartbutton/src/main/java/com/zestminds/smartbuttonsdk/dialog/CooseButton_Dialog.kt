package com.zestminds.smartbuttonsdk.dialog

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.RadioGroup
import android.widget.TextView
import com.zestminds.smartbuttonsdk.R
import com.zestminds.smartbuttonsdk.activity.Start_Activity


object CooseButton_Dialog{




    fun showExitDialog(context: Context) {

        var deviceType="smartwatch"
        var dialog_select = Dialog(context)
        dialog_select.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog_select.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_select.setContentView(R.layout.select_button_type)
        dialog_select.setCancelable(false)

        val yes = dialog_select.findViewById(R.id.tv_submit) as TextView
        val no = dialog_select.findViewById(R.id.tv_cancel) as TextView
        val rb_button=dialog_select.findViewById(R.id.radioGroup) as RadioGroup

        rb_button.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.rb_smartbutton ->{
                    deviceType="smartbutton"
                }
                R.id.rb_smartwatch ->{
                    deviceType="smartwatch"
                }
            }
        }

        no.setOnClickListener { dialog_select.dismiss() }

        yes.setOnClickListener {
            var intent=Intent(context,Start_Activity::class.java)
            if (deviceType.equals("smartbutton",true))
            {
                intent.putExtra("type","1")
                context.startActivity(intent)
            }
            else{
                intent.putExtra("type","2")
                context.startActivity(intent)
            }
            dialog_select.dismiss()

        }

        dialog_select.show()

    }




    fun blueToothTusrnOnDialog(context: Context) {

        var deviceType="smartwatch"
        var dialog_select = Dialog(context)
        dialog_select.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog_select.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_select.setContentView(R.layout.bluetooth_turnon_dialog)
        dialog_select.setCancelable(false)

        val yes = dialog_select.findViewById(R.id.tv_turnon) as TextView
        val no = dialog_select.findViewById(R.id.tv_cancel) as TextView



        no.setOnClickListener { dialog_select.dismiss() }

        yes.setOnClickListener {

            var adapter = BluetoothAdapter.getDefaultAdapter()
            if (adapter.isEnabled){
                adapter.disable()
            } else {
                adapter.enable()
            }
            dialog_select.dismiss()
        }

        dialog_select.show()

    }


}