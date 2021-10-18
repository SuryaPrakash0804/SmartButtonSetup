package com.zestminds.smartbuttonsdk.activity

import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zestminds.smartbuttonsdk.R
import com.zestminds.smartbuttonsdk.dialog.ChooseButton_Dialog.blueToothTurnOnDialog
import com.zestminds.smartbuttonsdk.dialog.showToast
import kotlinx.android.synthetic.main.activity_terms_condition.*


class TermsCondition_Activity : AppCompatActivity() {

    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)



        init()
    }



    fun init(){


        showpermissionDialog()

        clickOnView()
    }


    fun clickOnView(){

        tv_submit.setOnClickListener {
            if (!cb_agree.isChecked)
                showToast(getString(R.string.please_accept_our_terms_conditions))
            else startActivity(Intent(this,SwitchOnSmartButton_Activity::class.java))
        }
    }



    private fun isBlueToothOpen():Boolean{

        return mBluetoothAdapter.isEnabled

    }



    private fun showpermissionDialog() {


        var dialog_select = Dialog(this)
        dialog_select.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog_select.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_select.setContentView(R.layout.allow_physical_activity_dialog)
        dialog_select.setCancelable(false)

        val yes = dialog_select.findViewById(R.id.tv_submit) as TextView
        val no = dialog_select.findViewById(R.id.tv_cancel) as TextView



        no.setOnClickListener { dialog_select.dismiss() }

        yes.setOnClickListener {

            if (!isBlueToothOpen())
                blueToothTurnOnDialog(this)
            dialog_select.dismiss()
        }

        dialog_select.show()

    }

}