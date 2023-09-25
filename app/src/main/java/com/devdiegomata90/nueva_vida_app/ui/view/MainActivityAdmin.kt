package com.devdiegomata90.nueva_vida_app.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin.InicioAdmin
import com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin.ListaAdmin
import com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin.PerfilAdmin
import com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin.RegistrarAdmin
import com.devdiegomata90.nueva_vida_app.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivityAdmin : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var firebaseAuth: FirebaseAuth
    var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin)

        //INICIALIZAR INSTANCIA BASEDATOS
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser


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
            R.id.salir -> {
                CerraSession()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun ComprabarInicioSession() {
        //Validar que exista el usuario
        if (currentUser != null) {
            Toast.makeText(this, "Session iniciada con correo " + currentUser?.email, Toast.LENGTH_LONG).show()
        } else {
            //Si no se ha inciado sesion, es porque el usuario es un cliente
            startActivity(Intent(this@MainActivityAdmin, MainActivity::class.java))
            finish()
        }
    }

    private fun CerraSession(){
        firebaseAuth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Sesion cerrada exitosamente", Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
        ComprabarInicioSession()
    }
}