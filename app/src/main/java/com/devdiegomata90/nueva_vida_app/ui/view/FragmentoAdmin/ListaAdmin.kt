package com.devdiegomata90.nueva_vida_app.ui.view.FragmentoAdmin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.databinding.FragmentListaAdminBinding
import com.devdiegomata90.nueva_vida_app.ui.adapter.ListaAdminAdapter
import com.devdiegomata90.nueva_vida_app.ui.viewmodel.ListAdminViewModel
import kotlinx.coroutines.launch



class ListaAdmin : Fragment() {

    private lateinit var binding: FragmentListaAdminBinding
    private lateinit var adapter: ListaAdminAdapter

    private val listAdminVM: ListAdminViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentListaAdminBinding.inflate(inflater, container, false)
        val view = binding.root


        //Se inicializa el RecyclerView
        initRecyclerView(view)

        eventos()

        //Inicializar el onCreate del ViewModel
        listAdminVM.onCreate()


        //Escuchar los cambio en el reciclerView
        // Observa cambios en el StateFlow de los Usuarios
        lifecycleScope.launch {
            listAdminVM.users.collect { users ->

                Log.d("ListAdminV","Datos ${users}")
                // Recupera los audios y agragar la lista al reciclerView
                adapter.updateData(users as List<User>)
            }
        }

        return view
    }

    private fun eventos(){

        binding.name.apply {

            //escuchar los cambios del EditText
            doOnTextChanged { name, _, _, _ ->
               // listAdminVM.
                listAdminVM.searchUser(name.toString())

            }
        }
    }

    private fun initRecyclerView(view: View) {
        // Configurar el adaptador de la lista
        adapter = ListaAdminAdapter(
            userList = emptyList(),
            onClickListener = { user -> getUser(user) }
        )

        binding.rvListaAdmin.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        binding.rvListaAdmin.adapter = adapter

    }

    private fun getUser(user: User){
        Toast.makeText(requireContext(),"Haz elegido: ${user.NOMBRES} ${user.APELLIDOS}",Toast.LENGTH_SHORT).show()
    }


}

