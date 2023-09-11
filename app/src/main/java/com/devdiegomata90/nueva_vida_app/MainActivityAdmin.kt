package com.devdiegomata90.nueva_vida_app

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.devdiegomata90.nueva_vida_app.FragmentoAdmin.InicioAdmin
import com.devdiegomata90.nueva_vida_app.FragmentoAdmin.ListaAdmin
import com.devdiegomata90.nueva_vida_app.FragmentoAdmin.PerfilAdmin
import com.devdiegomata90.nueva_vida_app.FragmentoAdmin.RegistrarAdmin
import com.google.android.material.navigation.NavigationView


class MainActivityAdmin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        //Inicializar la barra de menu
        val toolbar = findViewById<Toolbar>(R.id.toobarA)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout_A)

        val navigationView: NavigationView = findViewById(R.id.nav_viewA)
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
                R.id.fragment_containerA,
                InicioAdmin()
            ).commit()
            navigationView.setCheckedItem(R.id.inicio_admin)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.inicio_admin -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_containerA,
                    InicioAdmin()
                ).commit()
            }
            R.id.perfil -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_containerA,
                    PerfilAdmin()
                ).commit()
            }

            R.id.registrar -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_containerA,
                    RegistrarAdmin()
                ).commit()
            }
            R.id.lista_admin -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_containerA,
                    ListaAdmin()
                ).commit()
            }
            R.id.salir -> Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
