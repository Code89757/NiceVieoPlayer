package com.xiao.nicevideoplayer

/**
 * Created by XiaoJianjun on 2017/5/5.
 */

interface NiceVideoPlayerControl {

    fun start()
    fun restart()
    fun pause()
    fun seekTo(pos: Int)

    val isIdle: Boolean
    val isPreparing: Boolean
    val isPrepared: Boolean
    val isBufferingPlaying: Boolean
    val isBufferingPaused: Boolean
    val isPlaying: Boolean
    val isPaused: Boolean
    val isError: Boolean
    val isCompleted: Boolean

    val isFullScreen: Boolean
    val isTinyWindow: Boolean
    val isNormal: Boolean

    val duration: Int
    val currentPosition: Int
    val bufferPercentage: Int

    fun enterFullScreen()
    fun exitFullScreen(): Boolean
    fun enterTinyWindow()
    fun exitTinyWindow(): Boolean

    fun release()
}
