package com.xiao.nicevideoplayer

/**
 *
 */
interface NiceVideoPlayerControl {

    /**
     * 空闲的时候
     */
    val isIdle: Boolean
    /**
     * 准备中
     */
    val isPreparing: Boolean
    /**
     * 准备晚餐
     */
    val isPrepared: Boolean
    /**
     * 缓冲中
     */
    val isBufferingPlaying: Boolean
    /**
     * 缓冲已暂停
     */
    val isBufferingPaused: Boolean
    /**
     * 播放中
     */
    val isPlaying: Boolean
    /**
     * 暂停
     */
    val isPaused: Boolean
    /**
     * 错误
     */
    val isError: Boolean
    /**
     * 播放完成
     */
    val isCompleted: Boolean
    /**
     * 全屏
     */
    val isFullScreen: Boolean
    /**
     * 小屏
     */
    val isTinyWindow: Boolean
    /**
     * 正常状态
     */
    val isNormal: Boolean
    /**
     * 时长
     */
    val duration: Int
    /**
     * 当前位置
     */
    val currentPosition: Int
    /**
     * 缓冲百分比
     */
    val bufferPercentage: Int

    /**
     * 播放视频
     */
    fun start()

    /**
     * 从头开始播放视频
     */
    fun restart()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 从指定位置开始播放视频
     */
    fun seekTo(pos: Int)

    /**
     * 进入全屏
     */
    fun enterFullScreen()

    /**
     * 退出全屏
     *
     * @return  Boolean 是否退出
     */
    fun exitFullScreen(): Boolean

    /**
     * 进入小屏幕状态
     */
    fun enterTinyWindow()

    /**
     * 退出小屏幕状态
     *
     * @return Boolean 是否退出
     */
    fun exitTinyWindow(): Boolean

    /**
     * 释放
     */
    fun release()
}
