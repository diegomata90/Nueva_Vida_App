package com.devdiegomata90.nueva_vida_app.core

import android.app.Activity
import android.app.AlertDialog
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.BibliaViewModel

class LoadingDialog(val mActivity: Activity) {
    private lateinit var isdialog:AlertDialog
    var mensaje:String = ""
    var setCancelable:Boolean = false

    fun starLoading(){
        //Setea la vista y se infla
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.loading_item, null)


        //setea Dialogo
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(setCancelable)
        builder.setMessage(mensaje)
        isdialog = builder.create() // crea la vista
        isdialog.show() // se muestra la vista
    }

    fun isDismiss(){
        isdialog.dismiss()
    }

}


