package com.xiao.nicevideoplayer.core

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import com.xiao.nicevideoplayer.model.NiceVideoStates
import com.xiao.nicevideoplayer.utils.LogUtil

/**
 * 播放器引擎
 * Created by wenyugang on 2017/6/1.
 */
class NicePlayerEngine(override val mVideoStates: NiceVideoStates, val mVideoView: INicePlayerView, val mVideoManager: NiceVideoPlayerManager) : INicePlayer {

    private var mMediaPlayer: MediaPlayer = MediaPlayer()

    private val mOnVideoSizeChangedListener = MediaPlayer.OnVideoSizeChangedListener {
        mediaPlayer, width, height ->
        LogUtil.d("onVideoSizeChanged ——> width：$width，height：$height")
    }

    private val mOnPreparedListener = MediaPlayer.OnPreparedListener { mediaPlayer ->
        mediaPlayer.start()
        mVideoStates.state = NiceVideoStates.STATE_PREPARED
        mVideoView.showPlaying();
        LogUtil.d("onPrepared ——> STATE_PREPARED")
    }

    private val mOnCompletionListener = MediaPlayer.OnCompletionListener {
        mVideoStates.state = NiceVideoStates.STATE_COMPLETED
        mVideoView.showReady()
        LogUtil.d("onCompletion ——> STATE_COMPLETED")
        mVideoManager.setCurrentNiceVideoPlayer(null)
    }

    private val mOnErrorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        mVideoStates.state = NiceVideoStates.STATE_ERROR
        mVideoView.showReady()

        LogUtil.d("onError ——> STATE_ERROR ———— what：" + what)
        false
    }

    private val mOnInfoListener = MediaPlayer.OnInfoListener { mediaPlayer, what, extra ->
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            // 播放器渲染第一帧
            mVideoStates.state = NiceVideoStates.STATE_PLAYING
            mVideoView.showPlaying()
            LogUtil.d("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING")

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            // MediaPlayer暂时不播放，以缓冲更多的数据
            if (mVideoStates.state == NiceVideoStates.STATE_PAUSED || mVideoStates.state == NiceVideoStates.STATE_BUFFERING_PAUSED) {
                mVideoStates.state = NiceVideoStates.STATE_BUFFERING_PAUSED
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED")
            } else {
                mVideoStates.state = NiceVideoStates.STATE_BUFFERING_PLAYING
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING")
            }
            mVideoView.showLoading()

        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            // 填充缓冲区后，MediaPlayer恢复播放/暂停
            if (mVideoStates.state == NiceVideoStates.STATE_BUFFERING_PLAYING) {
                mVideoStates.state = NiceVideoStates.STATE_PLAYING
                mVideoView.showPlaying()
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING")
            }
            if (mVideoStates.state == NiceVideoStates.STATE_BUFFERING_PAUSED) {
                mVideoStates.state = NiceVideoStates.STATE_PAUSED
                mVideoView.showPause();
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED")
            }
        } else {
            LogUtil.d("onInfo ——> what：" + what)
        }

        true
    }

    private val mOnBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent -> mVideoStates.progress = percent }


    init {
        mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer!!.setScreenOnWhilePlaying(true)

        mMediaPlayer!!.setOnPreparedListener(mOnPreparedListener)
        mMediaPlayer!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener)
        mMediaPlayer!!.setOnCompletionListener(mOnCompletionListener)
        mMediaPlayer!!.setOnErrorListener(mOnErrorListener)
        mMediaPlayer!!.setOnInfoListener(mOnInfoListener)
        mMediaPlayer!!.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
    }

    /**
     * 设置播放器
     */
    override fun setup(uri: Uri) {
        mVideoStates.uri = uri
        mVideoStates.state = NiceVideoStates.STATE_IDLE
        mMediaPlayer.setSurface(mVideoView.attachSurface())
    }

    override fun seek(progress: Int) {
        mVideoStates.progress = progress
    }

    override fun play() {
        if (!checkState(mVideoStates)) {
            mVideoManager.setCurrentNiceVideoPlayer(this)
            return
        }
    }

    override fun stop() {
        TODO("not implemented")
    }

    override fun pause() {
        TODO("not implemented")
    }

    override fun destroy() {
        TODO("not implemented")
    }

    private fun checkState(videoStates: NiceVideoStates): Boolean = videoStates.uri != null
}