package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import java.util.ArrayList


class AudioListAdapter(
    private var audioList: List<Audio>,
    private val onClickListener: (Audio) -> Unit
) : RecyclerView.Adapter<AudioListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_audio2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val audioTitle: TextView = itemView.findViewById(R.id.audio_title)
        val btn_Play: Button = itemView.findViewById(R.id.btn_Play)
        //val audioPreview: ImageView = itemView.findViewById(R.id.audio_preview)


        fun bind(audio: Audio, onClickListener: (Audio) -> Unit){

            audioTitle.text = audio.titulo
            //audioPreview.setImageBitmap(audio.audioPrevious)

            btn_Play.setOnClickListener{onClickListener(audio)}

        }



    }
}