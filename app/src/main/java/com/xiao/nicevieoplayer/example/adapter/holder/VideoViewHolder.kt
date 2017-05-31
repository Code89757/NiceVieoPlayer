package com.xiao.nicevieoplayer.example.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View

import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevieoplayer.R
import com.xiao.nicevieoplayer.example.bean.Video

/**
 * Created by XiaoJianjun on 2017/5/21.
 */

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mController: NiceVideoPlayerController? = null
    private val mVideoPlayer: NiceVideoPlayer

    init {
        mVideoPlayer = itemView.findViewById(R.id.nice_video_player) as NiceVideoPlayer
    }

    fun setController(controller: NiceVideoPlayerController) {
        mController = controller
    }

    fun bindData(video: Video) {
        mController!!.setTitle(video.title!!)
        mController!!.setImage(video.imageUrl!!)
        mVideoPlayer.setController(mController!!)
        mVideoPlayer.setUp(video.videoUrl!!, null)
    }
}
