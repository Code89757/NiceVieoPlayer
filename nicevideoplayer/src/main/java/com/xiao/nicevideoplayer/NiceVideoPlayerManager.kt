package com.xiao.nicevideoplayer

/**
 * Created by XiaoJianjun on 2017/5/5.
 * 视频播放器管理器.
 */
class NiceVideoPlayerManager private constructor() {

    /**
     * 视频播放器
     */
    private var mVideoPlayer: NiceVideoPlayer? = null

    /**
     * 设置当前播放器
     */
    fun setCurrentNiceVideoPlayer(videoPlayer: NiceVideoPlayer?) {
        mVideoPlayer = videoPlayer
    }

    /**
     * 释放播放器
     */
    fun releaseNiceVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer!!.release()
            mVideoPlayer = null
        }
    }

    /**
     * 点击返回时触发
     */
    fun onBackPressed(): Boolean {
        if (mVideoPlayer != null) {
            if (mVideoPlayer!!.isFullScreen) {
                return mVideoPlayer!!.exitFullScreen()
            } else if (mVideoPlayer!!.isTinyWindow) {
                return mVideoPlayer!!.exitTinyWindow()
            } else {
                mVideoPlayer!!.release()
                return false
            }
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
