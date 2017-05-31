package com.xiao.nicevideoplayer.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * 播放器状态器
 *
 * @param uri 播放地址
 * @param state 播放状态
 * @param progress 播放进度
 *
 * Created by wenyugang on 2017/5/31.
 */
public data class NiceVideoStates(var uri: Uri, var state: Int, var progress: Int) : Parcelable {

    override fun describeContents(): Int {
        TODO("not implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(uri.toString())
        dest!!.writeInt(state)
        dest!!.writeInt(progress)
    }

    companion object {
        val STATE_ERROR = -1          // 播放错误
        val STATE_IDLE = 0            // 播放未开始
        val STATE_PREPARING = 1       // 播放准备中
        val STATE_PREPARED = 2        // 播放准备就绪
        val STATE_PLAYING = 3         // 正在播放
        val STATE_PAUSED = 4          // 暂停播放
        val STATE_BUFFERING_PLAYING = 5 //正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
        val STATE_BUFFERING_PAUSED = 6 //正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停)
        val STATE_COMPLETED = 7       // 播放完成
        val PLAYER_NORMAL = 10        // 普通播放器
        val PLAYER_FULL_SCREEN = 11   // 全屏播放器
        val PLAYER_TINY_WINDOW = 12   // 小窗口播放器
    }
}