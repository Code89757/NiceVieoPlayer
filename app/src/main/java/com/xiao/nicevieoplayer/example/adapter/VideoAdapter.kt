package com.xiao.nicevieoplayer.example.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevieoplayer.R
import com.xiao.nicevieoplayer.example.adapter.holder.VideoViewHolder
import com.xiao.nicevieoplayer.example.bean.Video

/**
 * Created by XiaoJianjun on 2017/5/21.
 */

class VideoAdapter(private val mContext: Context, private val mVideoList: List<Video>) : RecyclerView.Adapter<VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false)
        val holder = VideoViewHolder(itemView)
        val controller = NiceVideoPlayerController(mContext)
        holder.setController(controller)
        return holder
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = mVideoList[position]
        holder.bindData(video)
    }

    override fun getItemCount(): Int {
        return mVideoList.size
    }
}
