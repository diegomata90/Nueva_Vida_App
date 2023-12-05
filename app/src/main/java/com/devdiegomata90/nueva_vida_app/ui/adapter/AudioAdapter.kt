package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.model.Verse
import com.devdiegomata90.nueva_vida_app.ui.viewholder.AudioViewHolder

class AudioAdapter(
    private var audios: List<Audio>,
    private val onClickListener: (Audio) -> Unit
) : RecyclerView.Adapter<AudioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audio = audios[position]
        holder.bind(audio, onClickListener)
    }

    override fun getItemCount() = audios.size

    fun updateData(newAudioList: List<Audio>) {
        audios = newAudioList
        notifyDataSetChanged()
    }
}