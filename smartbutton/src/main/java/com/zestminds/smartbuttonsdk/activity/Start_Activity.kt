package com.zestminds.smartbuttonsdk.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zestminds.smartbuttonsdk.R
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


            if (type.equals("smartbutton",true))
            {
                iv_button.setImageResource(R.drawable.ic_smart_device_with_circle)
            }
            else{
                iv_button.setImageResource(R.drawable.ic_smart_watch)
            }


//            if (type.equals("1",true)){
//                iv_button.setImageResource(R.drawable.ic_smart_device_with_circle)
//            }
//            else{
//                iv_button.setImageResource(R.drawable.ic_smart_watch)
//            }
        }

        clickOnView()
    }


    private fun clickOnView(){
        tv_setup.setOnClickListener {
            startActivity(Intent(this,TermsCondition_Activity::class.java))
        }
    }





}