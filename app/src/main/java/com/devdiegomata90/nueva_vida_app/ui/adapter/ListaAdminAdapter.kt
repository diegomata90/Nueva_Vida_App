package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.User
import com.devdiegomata90.nueva_vida_app.ui.viewholder.ListaAdminViewHolder
import com.devdiegomata90.nueva_vida_app.ui.viewholder.TextBiblicoViewHolder

class ListaAdminAdapter(
    private var userList: List<User>,
    private val onClickListener: (User) -> Unit,
) : RecyclerView.Adapter<ListaAdminViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAdminViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_admin, parent, false)
        return ListaAdminViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ListaAdminViewHolder, position: Int) {
        val user = userList[position]

        holder.bind(user, onClickListener)
    }

    fun updateData(updateUserList: List<User>) {
        userList = updateUserList
        notifyDataSetChanged()
    }
}