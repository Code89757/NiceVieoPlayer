package com.xiao.nicevideoplayer

/**
 * Created by XiaoJianjun on 2017/5/5.
 * 视频播放器管理器.
 */
class NiceVideoPlayerManager private constructor() {

    private var mVideoPlayer: NiceVideoPlayer? = null

    fun setCurrentNiceVideoPlayer(videoPlayer: NiceVideoPlayer) {
        mVideoPlayer = videoPlayer
    }

    fun releaseNiceVideoPlayer() {
        if (mVideoPlayer != null) {
            mVideoPlayer!!.release()
            mVideoPlayer = null
        }
    }

    fun onBackPressd(): Boolean {
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

    companion object {

        private var sInstance: NiceVideoPlayerManager? = null

        @Synchronized fun instance(): NiceVideoPlayerManager {
            if (sInstance == null) {
                sInstance = NiceVideoPlayerManager()
            }
            return sInstance
        }
    }
}
