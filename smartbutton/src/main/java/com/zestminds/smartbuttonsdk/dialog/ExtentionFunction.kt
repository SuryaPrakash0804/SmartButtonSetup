package com.zestminds.smartbuttonsdk.dialog

import android.content.Context
import android.view.View
import android.widget.Toast


fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}


fun View.visible()
{
    this.visibility= View.VISIBLE
}

fun View.hide(){
    this.visibility= View.GONE
}


