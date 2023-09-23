package com.devdiegomata90.nueva_vida_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

import com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente.AcercaDeCliente
import com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente.CompartirCliente
import com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente.InicioCliente
import com.devdiegomata90.nueva_vida_app.ui.FragmentoCliente.LoginAdmin
import com.devdiegomata90.nueva_vida_app.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Inicializar la barra de menu
        val toolbar = findViewById<Toolbar>(R.id.toobar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //fragmento por default
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                InicioCliente()
            ).commit()
            navigationView.setCheckedItem(R.id.inicio_cliente)
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio_cliente -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    InicioCliente()
                ).commit()
            }
            R.id.acercade -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    AcercaDeCliente()
                ).commit()
            }
            R.id.compartir -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    CompartirCliente()
                ).commit()
            }
            R.id.administrador -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    LoginAdmin()
                ).commit()
            }
            R.id.salir -> {
                Toast.makeText(this,"Salir",Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}