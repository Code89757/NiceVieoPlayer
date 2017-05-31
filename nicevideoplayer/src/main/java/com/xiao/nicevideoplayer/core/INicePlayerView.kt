package com.xiao.nicevideoplayer.core

/**
 * 播放器视图
 * Created by wenyugang on 2017/5/31.
 */
interface INicePlayerView {
    fun showReady()
    fun showLoading()
    fun showPlaying()
    fun showError()
}