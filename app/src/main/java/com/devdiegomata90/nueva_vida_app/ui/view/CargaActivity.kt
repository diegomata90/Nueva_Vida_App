package com.devdiegomata90.nueva_vida_app.ui.view

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatImageView
import com.devdiegomata90.nueva_vida_app.R

class CargaActivity : AppCompatActivity() {

    //Declaracion del Objeto del XML
    private lateinit var app_name:TextView
    private lateinit var desarrollador:TextView
    private lateinit var ico_logoiglesia: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carga)
        initComponent()

        val duracion:Long  = 3000

        // Mostrar la actividad de carga
        Handler(Looper.getMainLooper()).postDelayed({
            // Ir a la siguiente actividad
            val intent = Intent(this@CargaActivity, MainActivityAdmin::class.java)
            startActivity(intent)
            finish()
        }, duracion)

        //Crear tipo letra personalizado
        val typefaceCaveat = Typeface.createFromAsset(this.assets,"fons/Caveat-Medium.ttf")
        val typefaceUbuntu =Typeface.createFromAsset(this.assets,"fons/Ubuntu.ttf")

        app_name.typeface = typefaceCaveat
        desarrollador.typeface = typefaceUbuntu

        //CAMBIAR ICONO MODO OSCURO
        val currentTheme = resources.configuration.uiMode

        if (currentTheme and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES){
            ico_logoiglesia.setImageResource(R.drawable.logoiglesiaw)
        }else  {
            ico_logoiglesia.setImageResource(R.drawable.logoiglesia)
        }

    }

    private fun initComponent(){
        app_name = findViewById(R.id.app_name)
        desarrollador = findViewById(R.id.desarrollador)
        ico_logoiglesia = findViewById(R.id.ico_logoiglesia)
    }


}





