package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.ui.viewholder.AudioListViewHolder
import java.util.ArrayList


class AudioListAdapter(
    private var audioList: List<Audio>,
    private val onClickListener: (Audio) -> Unit,
) : RecyclerView.Adapter<AudioListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioListViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_audio_list, parent, false)
        return AudioListViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioListViewHolder, position: Int) {

        val audio = audioList[position]
        holder.bind(audio, onClickListener)

    }

    override fun getItemCount(): Int {
        return audioList.size
    }

    fun updateData(newAudioList: ArrayList<Audio>) {
        audioList = newAudioList
        notifyDataSetChanged()
    }


}