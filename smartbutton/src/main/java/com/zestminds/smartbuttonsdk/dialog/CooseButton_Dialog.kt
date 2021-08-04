package com.zestminds.smartbuttonsdk.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.RadioGroup
import android.widget.TextView
import com.zestminds.smartbuttonsdk.R


object CooseButton_Dialog{




    fun showExitDialog(context: Context) {

        var dialog_select = Dialog(context)
        dialog_select.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog_select.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_select.setContentView(R.layout.select_button_type)
        dialog_select.setCancelable(false)

        val yes = dialog_select.findViewById(R.id.tv_submit) as TextView
        val no = dialog_select.findViewById(R.id.tv_cancel) as TextView
        val rb_button=dialog_select.findViewById(R.id.radioGroup) as RadioGroup


        no.setOnClickListener { dialog_select.dismiss() }

        yes.setOnClickListener {
            dialog_select.dismiss()

        }

        dialog_select.show()

    }


}