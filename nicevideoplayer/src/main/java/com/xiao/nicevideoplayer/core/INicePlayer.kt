package com.xiao.nicevideoplayer.core

import android.net.Uri
import com.xiao.nicevideoplayer.model.NiceVideoStates

/**
 * 播放器
 * Created by wenyugang on 2017/5/31.
 */
interface INicePlayer {
    val videoStates: NiceVideoStates

    fun seek(progress: Int)
    fun play()
    fun stop()
    fun pause()
    fun setup(uri: Uri)
}