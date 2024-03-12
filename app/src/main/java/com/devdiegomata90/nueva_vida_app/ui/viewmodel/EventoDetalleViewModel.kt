package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devdiegomata90.nueva_vida_app.domain.GetImagenUseCase

class EventoDetalleViewModel : ViewModel() {

    //Livedata
    val _url = MutableLiveData<Uri>()
    val url get() = _url


    //Casos de uso
    val getImagenUseCase = GetImagenUseCase()

    //Funciones

    fun shareImage(context: Context, imagenDetalle: ImageView, mensage:String) {
        val bitmap = imagenDetalle.drawable.toBitmap()

        val uri = getImagenUseCase(context, bitmap)

        val titulo = mensage.split("\n")[0]


        if (uri != null) {
            val sharedIntent = Intent(Intent.ACTION_SEND)
            sharedIntent.setType("image/jpeg")
            sharedIntent.putExtra(Intent.EXTRA_SUBJECT, "$titulo")
            sharedIntent.putExtra(Intent.EXTRA_STREAM, uri)
            sharedIntent.putExtra(Intent.EXTRA_TEXT, mensage)
            sharedIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(sharedIntent)
        } else {
            Log.e("ImpotanteError", "No se pudo compartir la imagen uri es nulo")
        }
    }


}
