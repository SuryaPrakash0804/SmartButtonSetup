package com.zestminds.smartbuttonsdk.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.zestminds.smartbuttonsdk.R
import kotlinx.android.synthetic.main.activity_switch_on_smart_button.*

class SwitchOnSmartButton_Activity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_on_smart_button)

        init()
    }


    private fun init(){

        clickOnView()
    }


        private fun clickOnView(){

            iv_info.setOnClickListener {
                showInfoDialog()
            }

            tv_continue.setOnClickListener {

            }
        }




    private fun showInfoDialog(){
        var dialog_select = Dialog(this)
        dialog_select.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog_select.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_select.setContentView(R.layout.button_info_dialog)
        dialog_select.setCancelable(false)
        val no = dialog_select.findViewById(R.id.iv_close) as ImageView
        no.setOnClickListener { dialog_select.dismiss() }
        dialog_select.show()
    }


}