package com.devdiegomata90.nueva_vida_app.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class GetImagenUseCase {

    operator fun invoke(context: Context,bitmap: Bitmap): Uri? {

        // Importar la imagen

        // Guardar el bitmap como un archivo temporal
        val imageFolder = File(context.cacheDir, "images")
        var contentUri: Uri? = null
        try {
            imageFolder.mkdirs() // Crea el directorio si no existe
            val file = File(imageFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(
                context,
                "com.devdiegomata90.nueva_vida_app.fileprovider",
                file
            )
        } catch (e: Exception) {
            Log.e("ImpotanteErrorUC", "Error al intentar importar la imagen: ${e.message}")
        }

        return contentUri
    }


}