package com.xiao.nicevideoplayer

import com.xiao.nicevideoplayer.core.INicePlayer

/**
 * 视频播放器管理器,保存唯一一个正在播放的播放器
 * Created by wenyugang on 2017/5/5.
 */
class NiceVideoPlayerManager private constructor() {

    /**
     * 视频播放器
     */
    private var mVideoPlayer: INicePlayer? = null

    /**
     * 设置当前播放器
     */
    fun setCurrentNiceVideoPlayer(videoPlayer: INicePlayer?) {
        mVideoPlayer = videoPlayer
    }

    /**
     * 释放播放器
     */
    fun releaseNiceVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer!!.destroy()
            mVideoPlayer = null
        }
    }

    /**
     * 点击返回时触发
     */
    fun onBackPressed(): Boolean {
        if (mVideoPlayer != null) {
            mVideoPlayer!!.destroy()
            return false
        }
        return false
    }

    /**
     * 单例
     */
    companion object {

        private var sInstance: NiceVideoPlayerManager? = null

        @Synchronized fun instance(): NiceVideoPlayerManager {
            if (sInstance == null) {
                sInstance = NiceVideoPlayerManager()
            }
            return sInstance!!
        }
    }
}
