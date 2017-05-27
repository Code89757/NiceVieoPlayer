package com.xiao.nicevideoplayer

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.Toast

import java.io.IOException

/**
 * Created by XiaoJianjun on 2017/4/28.
 * 播放器
 */
class NiceVideoPlayer @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null) : FrameLayout(mContext, attrs), NiceVideoPlayerControl, TextureView.SurfaceTextureListener {

    private var mCurrentState = STATE_IDLE
    private var mPlayerState = PLAYER_NORMAL
    private var mContainer: FrameLayout? = null
    private var mTextureView: TextureView? = null
    private var mController: NiceVideoPlayerController? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mUrl: String? = null
    private var mHeaders: Map<String, String>? = null
    private var mMediaPlayer: MediaPlayer? = null

    override var bufferPercentage: Int = 0
        private set

    init {
        init()
    }

    private fun init() {
        mContainer = FrameLayout(mContext)
        mContainer!!.setBackgroundColor(Color.BLACK)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        this.addView(mContainer, params)
    }

    fun setUp(url: String, headers: Map<String, String>) {
        mUrl = url
        mHeaders = headers
    }

    fun setController(controller: NiceVideoPlayerController) {
        mController = controller
        mController!!.setNiceVideoPlayer(this)
        mContainer!!.removeView(mController)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContainer!!.addView(mController, params)
    }

    override fun start() {
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
        NiceVideoPlayerManager.instance().setCurrentNiceVideoPlayer(this)
        if (mCurrentState == STATE_IDLE
                || mCurrentState == STATE_ERROR
                || mCurrentState == STATE_COMPLETED) {
            initMediaPlayer()
            initTextureView()
            addTextureView()
        }
    }

    override fun restart() {
        if (mCurrentState == STATE_PAUSED) {
            mMediaPlayer!!.start()
            mCurrentState = STATE_PLAYING
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("STATE_PLAYING")
        }
        if (mCurrentState == STATE_BUFFERING_PAUSED) {
            mMediaPlayer!!.start()
            mCurrentState = STATE_BUFFERING_PLAYING
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("STATE_BUFFERING_PLAYING")
        }
    }

    override fun pause() {
        if (mCurrentState == STATE_PLAYING) {
            mMediaPlayer!!.pause()
            mCurrentState = STATE_PAUSED
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("STATE_PAUSED")
        }
        if (mCurrentState == STATE_BUFFERING_PLAYING) {
            mMediaPlayer!!.pause()
            mCurrentState = STATE_BUFFERING_PAUSED
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("STATE_BUFFERING_PAUSED")
        }
    }

    override fun seekTo(pos: Int) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(pos)
        }
    }

    override val isIdle: Boolean
        get() = mCurrentState == STATE_IDLE

    override val isPreparing: Boolean
        get() = mCurrentState == STATE_PREPARING

    override val isPrepared: Boolean
        get() = mCurrentState == STATE_PREPARED

    override val isBufferingPlaying: Boolean
        get() = mCurrentState == STATE_BUFFERING_PLAYING

    override val isBufferingPaused: Boolean
        get() = mCurrentState == STATE_BUFFERING_PAUSED

    override val isPlaying: Boolean
        get() = mCurrentState == STATE_PLAYING

    override val isPaused: Boolean
        get() = mCurrentState == STATE_PAUSED

    override val isError: Boolean
        get() = mCurrentState == STATE_ERROR

    override val isCompleted: Boolean
        get() = mCurrentState == STATE_COMPLETED

    override val isFullScreen: Boolean
        get() = mPlayerState == PLAYER_FULL_SCREEN

    override val isTinyWindow: Boolean
        get() = mPlayerState == PLAYER_TINY_WINDOW

    override val isNormal: Boolean
        get() = mPlayerState == PLAYER_NORMAL

    override val duration: Int
        get() = if (mMediaPlayer != null) mMediaPlayer!!.duration else 0

    override val currentPosition: Int
        get() = if (mMediaPlayer != null) mMediaPlayer!!.currentPosition else 0

    private fun initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()

            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer!!.setScreenOnWhilePlaying(true)

            mMediaPlayer!!.setOnPreparedListener(mOnPreparedListener)
            mMediaPlayer!!.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener)
            mMediaPlayer!!.setOnCompletionListener(mOnCompletionListener)
            mMediaPlayer!!.setOnErrorListener(mOnErrorListener)
            mMediaPlayer!!.setOnInfoListener(mOnInfoListener)
            mMediaPlayer!!.setOnBufferingUpdateListener(mOnBufferingUpdateListener)
        }
    }

    private fun initTextureView() {
        if (mTextureView == null) {
            mTextureView = TextureView(mContext)
            mTextureView!!.surfaceTextureListener = this
        }
    }

    private fun addTextureView() {
        mContainer!!.removeView(mTextureView)
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        mContainer!!.addView(mTextureView, 0, params)
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surfaceTexture
            openMediaPlayer()
        } else {
            mTextureView!!.surfaceTexture = mSurfaceTexture
        }
    }

    private fun openMediaPlayer() {
        try {
            mMediaPlayer!!.setDataSource(mContext.applicationContext, Uri.parse(mUrl), mHeaders)
            mMediaPlayer!!.setSurface(Surface(mSurfaceTexture))
            mMediaPlayer!!.prepareAsync()
            mCurrentState = STATE_PREPARING
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("STATE_PREPARING")
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtil.e("打开播放器发生错误", e)
        }

    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return mSurfaceTexture == null
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    private val mOnPreparedListener = MediaPlayer.OnPreparedListener { mp ->
        mp.start()
        mCurrentState = STATE_PREPARED
        mController!!.setControllerState(mPlayerState, mCurrentState)
        LogUtil.d("onPrepared ——> STATE_PREPARED")
    }

    private val mOnVideoSizeChangedListener = MediaPlayer.OnVideoSizeChangedListener { mp, width, height -> LogUtil.d("onVideoSizeChanged ——> width：$width，height：$height") }

    private val mOnCompletionListener = MediaPlayer.OnCompletionListener {
        mCurrentState = STATE_COMPLETED
        mController!!.setControllerState(mPlayerState, mCurrentState)
        LogUtil.d("onCompletion ——> STATE_COMPLETED")
        NiceVideoPlayerManager.instance().setCurrentNiceVideoPlayer(null)
    }

    private val mOnErrorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        mCurrentState = STATE_ERROR
        mController!!.setControllerState(mPlayerState, mCurrentState)
        LogUtil.d("onError ——> STATE_ERROR ———— what：" + what)
        false
    }

    private val mOnInfoListener = MediaPlayer.OnInfoListener { mp, what, extra ->
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            // 播放器渲染第一帧
            mCurrentState = STATE_PLAYING
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("onInfo ——> MEDIA_INFO_VIDEO_RENDERING_START：STATE_PLAYING")
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            // MediaPlayer暂时不播放，以缓冲更多的数据
            if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_BUFFERING_PAUSED) {
                mCurrentState = STATE_BUFFERING_PAUSED
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PAUSED")
            } else {
                mCurrentState = STATE_BUFFERING_PLAYING
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_START：STATE_BUFFERING_PLAYING")
            }
            mController!!.setControllerState(mPlayerState, mCurrentState)
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            // 填充缓冲区后，MediaPlayer恢复播放/暂停
            if (mCurrentState == STATE_BUFFERING_PLAYING) {
                mCurrentState = STATE_PLAYING
                mController!!.setControllerState(mPlayerState, mCurrentState)
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PLAYING")
            }
            if (mCurrentState == STATE_BUFFERING_PAUSED) {
                mCurrentState = STATE_PAUSED
                mController!!.setControllerState(mPlayerState, mCurrentState)
                LogUtil.d("onInfo ——> MEDIA_INFO_BUFFERING_END： STATE_PAUSED")
            }
        } else {
            LogUtil.d("onInfo ——> what：" + what)
        }
        true
    }

    private val mOnBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent -> bufferPercentage = percent }

    /**
     * 全屏，将mContainer(内部包含mTextureView和mController)从当前容器中移除，并添加到android.R.content中.
     */
    override fun enterFullScreen() {
        if (mPlayerState == PLAYER_FULL_SCREEN) return

        // 隐藏ActionBar、状态栏，并横屏
        NiceUtil.hideActionBar(mContext)
        NiceUtil.scanForActivity(mContext)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        this.removeView(mContainer)
        val contentView = NiceUtil.scanForActivity(mContext)!!
                .findViewById(android.R.id.content) as ViewGroup
        val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.addView(mContainer, params)

        mPlayerState = PLAYER_FULL_SCREEN
        mController!!.setControllerState(mPlayerState, mCurrentState)
        LogUtil.d("PLAYER_FULL_SCREEN")
    }

    /**
     * 退出全屏，移除mTextureView和mController，并添加到非全屏的容器中。

     * @return true退出全屏.
     */
    override fun exitFullScreen(): Boolean {
        if (mPlayerState == PLAYER_FULL_SCREEN) {
            NiceUtil.showActionBar(mContext)
            NiceUtil.scanForActivity(mContext)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            val contentView = NiceUtil.scanForActivity(mContext)!!
                    .findViewById(android.R.id.content) as ViewGroup
            contentView.removeView(mContainer)
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)

            mPlayerState = PLAYER_NORMAL
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("PLAYER_NORMAL")
            return true
        }
        return false
    }

    /**
     * 进入小窗口播放，小窗口播放的实现原理与全屏播放类似。
     */
    override fun enterTinyWindow() {
        if (mPlayerState == PLAYER_TINY_WINDOW) return
        this.removeView(mContainer)

        val contentView = NiceUtil.scanForActivity(mContext)!!
                .findViewById(android.R.id.content) as ViewGroup
        // 小窗口的宽度为屏幕宽度的60%，长宽比默认为16:9，右边距、下边距为8dp。
        val params = FrameLayout.LayoutParams(
                (NiceUtil.getScreenWidth(mContext) * 0.6f).toInt(),
                (NiceUtil.getScreenWidth(mContext).toFloat() * 0.6f * 9f / 16f).toInt())
        params.gravity = Gravity.BOTTOM or Gravity.END
        params.rightMargin = NiceUtil.dp2px(mContext, 8f)
        params.bottomMargin = NiceUtil.dp2px(mContext, 8f)

        contentView.addView(mContainer, params)

        mPlayerState = PLAYER_TINY_WINDOW
        mController!!.setControllerState(mPlayerState, mCurrentState)
        LogUtil.d("PLAYER_TINY_WINDOW")
    }

    /**
     * 退出小窗口播放
     */
    override fun exitTinyWindow(): Boolean {
        if (mPlayerState == PLAYER_TINY_WINDOW) {
            val contentView = NiceUtil.scanForActivity(mContext)!!
                    .findViewById(android.R.id.content) as ViewGroup
            contentView.removeView(mContainer)
            val params = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            this.addView(mContainer, params)

            mPlayerState = PLAYER_NORMAL
            mController!!.setControllerState(mPlayerState, mCurrentState)
            LogUtil.d("PLAYER_NORMAL")
            return true
        }
        return false
    }

    override fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
        mContainer!!.removeView(mTextureView)
        if (mSurfaceTexture != null) {
            mSurfaceTexture!!.release()
            mSurfaceTexture = null
        }
        if (mController != null) {
            mController!!.reset()
        }
        mCurrentState = STATE_IDLE
        mPlayerState = PLAYER_NORMAL
    }

    companion object {

        val STATE_ERROR = -1          // 播放错误
        val STATE_IDLE = 0            // 播放未开始
        val STATE_PREPARING = 1       // 播放准备中
        val STATE_PREPARED = 2        // 播放准备就绪
        val STATE_PLAYING = 3         // 正在播放
        val STATE_PAUSED = 4          // 暂停播放
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
         */
        val STATE_BUFFERING_PLAYING = 5
        /**
         * 正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停)
         */
        val STATE_BUFFERING_PAUSED = 6
        val STATE_COMPLETED = 7       // 播放完成

        val PLAYER_NORMAL = 10        // 普通播放器
        val PLAYER_FULL_SCREEN = 11   // 全屏播放器
        val PLAYER_TINY_WINDOW = 12   // 小窗口播放器
    }
}
