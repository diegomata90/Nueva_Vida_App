package com.devdiegomata90.nueva_vida_app.util

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.TextView

object TypefaceUtil {

    fun asignarTipoLetra(
        context: Context,
        tipoLetraPath: String? = null,
        vararg elementosVista: View,
    ) {
        //si esta nullo usa el tipo ubuntu
        val typeface = if (tipoLetraPath != null) {
            Typeface.createFromAsset(context.assets, tipoLetraPath)
        } else {
            Typeface.createFromAsset(context.assets, "fons/Ubuntu.ttf")
        }

        for (elemento in elementosVista) {
            if (elemento is TextView) {
                elemento.typeface = typeface
            }
        }
    }
}