package com.devdiegomata90.nueva_vida_app.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devdiegomata90.nueva_vida_app.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplashActivity = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        screenSplashActivity.setKeepOnScreenCondition { true }
        // Ir a la actividad principal
        val intent = Intent(this, MainActivityAdmin::class.java)
        //val intent = Intent(this, CargaActivity::class.java)
        startActivity(intent)
        finish()


    }
}