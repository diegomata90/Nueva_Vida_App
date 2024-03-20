package com.devdiegomata90.nueva_vida_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.data.model.Video
import com.devdiegomata90.nueva_vida_app.ui.viewholder.VideoViewHolder

class VideoAdapter(
    private var videos: List<Video>,
    private val onClickListener: (Video) -> Unit
): RecyclerView.Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position],onClickListener)
    }

    fun updateData(newVideoList: List<Video>) {
        videos = newVideoList
        notifyDataSetChanged()
    }

}