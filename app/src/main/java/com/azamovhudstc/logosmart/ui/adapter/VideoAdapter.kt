package com.azamovhudstc.logosmart.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azamovhudstc.logosmart.R
import com.azamovhudstc.logosmart.data.model.Video
import com.squareup.picasso.Picasso

class VideoAdapter(private val context: Context, private val videoList: List<Video>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        val title: TextView = view.findViewById(R.id.videoTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video.title

        // Load YouTube thumbnail
        val thumbnailUrl = "https://img.youtube.com/vi/${video.youtubeId}/0.jpg"
        Picasso.get().load(thumbnailUrl).into(holder.thumbnail)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, com.azamovhudstc.logosmart.ui.screens.video.VideoDetailActivity::class.java)
            intent.putExtra("extra_title", video.title)
            intent.putExtra("extra_video_id", video.youtubeId)
            intent.putExtra("extra_description", video.text)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = videoList.size
}
