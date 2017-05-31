package com.xiao.nicevideoplayer

import android.content.Context
import android.os.CountDownTimer
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import java.util.*

/**
 * Created by XiaoJianjun on 2017/4/28.
 * 播放器控制器.
 */
class NiceVideoPlayerController(private val mContext: Context) : FrameLayout(mContext), View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private var mNiceVideoPlayer: NiceVideoPlayerControl? = null
    private var mImage: ImageView? = null
    private var mCenterStart: ImageView? = null
    private var mTop: LinearLayout? = null
    private var mBack: ImageView? = null
    private var mTitle: TextView? = null
    private var mBottom: LinearLayout? = null
    private var mRestartPause: ImageView? = null
    private var mPosition: TextView? = null
    private var mDuration: TextView? = null
    private var mSeek: SeekBar? = null
    private var mFullScreen: ImageView? = null
    private var mLoading: LinearLayout? = null
    private var mLoadText: TextView? = null
    private var mError: LinearLayout? = null
    private var mRetry: TextView? = null
    private var mCompleted: LinearLayout? = null
    private var mReplay: TextView? = null
    private var mShare: TextView? = null

    private var mUpdateProgressTimer: Timer? = null
    private var mUpdateProgressTimerTask: TimerTask? = null
    private var topBottomVisible: Boolean = false
    private var mDismissTopBottomCountDownTimer: CountDownTimer? = null

    init {
        init()
    }

    private fun init() {
        LayoutInflater.from(mContext).inflate(R.layout.nice_video_palyer_controller, this, true)
        mCenterStart = findViewById(R.id.center_start) as ImageView
        mImage = findViewById(R.id.image) as ImageView

        mTop = findViewById(R.id.top) as LinearLayout
        mBack = findViewById(R.id.back) as ImageView
        mTitle = findViewById(R.id.title) as TextView

        mBottom = findViewById(R.id.bottom) as LinearLayout
        mRestartPause = findViewById(R.id.restart_or_pause) as ImageView
        mPosition = findViewById(R.id.position) as TextView
        mDuration = findViewById(R.id.duration) as TextView
        mSeek = findViewById(R.id.seek) as SeekBar
        mFullScreen = findViewById(R.id.full_screen) as ImageView

        mLoading = findViewById(R.id.loading) as LinearLayout
        mLoadText = findViewById(R.id.load_text) as TextView

        mError = findViewById(R.id.error) as LinearLayout
        mRetry = findViewById(R.id.retry) as TextView

        mCompleted = findViewById(R.id.completed) as LinearLayout
        mReplay = findViewById(R.id.replay) as TextView
        mShare = findViewById(R.id.share) as TextView

        mCenterStart!!.setOnClickListener(this)
        mBack!!.setOnClickListener(this)
        mRestartPause!!.setOnClickListener(this)
        mFullScreen!!.setOnClickListener(this)
        mRetry!!.setOnClickListener(this)
        mReplay!!.setOnClickListener(this)
        mShare!!.setOnClickListener(this)
        mSeek!!.setOnSeekBarChangeListener(this)

        this.setOnClickListener(this)
    }


    fun setTitle(title: String) {
        mTitle!!.text = title
    }

    fun setImage(imageUrl: String) {
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.img_default)
                .crossFade()
                .into(mImage!!)
    }

    fun setImage(@DrawableRes resId: Int) {
        mImage!!.setImageResource(resId)
    }

    fun setNiceVideoPlayer(niceVideoPlayer: NiceVideoPlayerControl) {
        mNiceVideoPlayer = niceVideoPlayer
        if (mNiceVideoPlayer!!.isIdle) {
            mBack!!.visibility = View.GONE
            mTop!!.visibility = View.VISIBLE
            mBottom!!.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        if (v === mCenterStart) {
            if (mNiceVideoPlayer!!.isIdle) {
                mNiceVideoPlayer!!.start()
            }
        } else if (v === mBack) {
            if (mNiceVideoPlayer!!.isFullScreen) {
                mNiceVideoPlayer!!.exitFullScreen()
            } else if (mNiceVideoPlayer!!.isTinyWindow) {
                mNiceVideoPlayer!!.exitTinyWindow()
            }
        } else if (v === mRestartPause) {
            if (mNiceVideoPlayer!!.isPlaying || mNiceVideoPlayer!!.isBufferingPlaying) {
                mNiceVideoPlayer!!.pause()
            } else if (mNiceVideoPlayer!!.isPaused || mNiceVideoPlayer!!.isBufferingPaused) {
                mNiceVideoPlayer!!.restart()
            }
        } else if (v === mFullScreen) {
            if (mNiceVideoPlayer!!.isNormal) {
                mNiceVideoPlayer!!.enterFullScreen()
            } else if (mNiceVideoPlayer!!.isFullScreen) {
                mNiceVideoPlayer!!.exitFullScreen()
            }
        } else if (v === mRetry) {
            mNiceVideoPlayer!!.release()
            mNiceVideoPlayer!!.start()
        } else if (v === mReplay) {
            mRetry!!.performClick()
        } else if (v === mShare) {
            Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show()
        } else if (v === this) {
            if (mNiceVideoPlayer!!.isPlaying
                    || mNiceVideoPlayer!!.isPaused
                    || mNiceVideoPlayer!!.isBufferingPlaying
                    || mNiceVideoPlayer!!.isBufferingPaused) {
                setTopBottomVisible(!topBottomVisible)
            }
        }
    }

    private fun setTopBottomVisible(visible: Boolean) {
        mTop!!.visibility = if (visible) View.VISIBLE else View.GONE
        mBottom!!.visibility = if (visible) View.VISIBLE else View.GONE
        topBottomVisible = visible
        if (visible) {
            if (!mNiceVideoPlayer!!.isPaused && !mNiceVideoPlayer!!.isBufferingPaused) {
                startDismissTopBottomTimer()
            }
        } else {
            cancelDismissTopBottomTimer()
        }
    }

    fun setControllerState(playerState: Int, playState: Int) {
        when (playerState) {
            NiceVideoPlayer.PLAYER_NORMAL -> {
                mBack!!.visibility = View.GONE
                mFullScreen!!.visibility = View.VISIBLE
                mFullScreen!!.setImageResource(R.drawable.ic_player_enlarge)
            }
            NiceVideoPlayer.PLAYER_FULL_SCREEN -> {
                mBack!!.visibility = View.VISIBLE
                mFullScreen!!.visibility = View.VISIBLE
                mFullScreen!!.setImageResource(R.drawable.ic_player_shrink)
            }
            NiceVideoPlayer.PLAYER_TINY_WINDOW -> mFullScreen!!.visibility = View.GONE
        }
        when (playState) {
            NiceVideoPlayer.STATE_IDLE -> {
            }
            NiceVideoPlayer.STATE_PREPARING -> {
                // 只显示准备中动画，其他不显示
                mImage!!.visibility = View.GONE
                mLoading!!.visibility = View.VISIBLE
                mLoadText!!.text = "正在准备..."
                mError!!.visibility = View.GONE
                mCompleted!!.visibility = View.GONE
                mTop!!.visibility = View.GONE
                mCenterStart!!.visibility = View.GONE
            }
            NiceVideoPlayer.STATE_PREPARED -> startUpdateProgressTimer()
            NiceVideoPlayer.STATE_PLAYING -> {
                mLoading!!.visibility = View.GONE
                mRestartPause!!.setImageResource(R.drawable.ic_player_pause)
                startDismissTopBottomTimer()
            }
            NiceVideoPlayer.STATE_PAUSED -> {
                mLoading!!.visibility = View.GONE
                mRestartPause!!.setImageResource(R.drawable.ic_player_start)
                cancelDismissTopBottomTimer()
            }
            NiceVideoPlayer.STATE_BUFFERING_PLAYING -> {
                mLoading!!.visibility = View.VISIBLE
                mRestartPause!!.setImageResource(R.drawable.ic_player_pause)
                mLoadText!!.text = "正在缓冲..."
                startDismissTopBottomTimer()
            }
            NiceVideoPlayer.STATE_BUFFERING_PAUSED -> {
                mLoading!!.visibility = View.VISIBLE
                mRestartPause!!.setImageResource(R.drawable.ic_player_start)
                mLoadText!!.text = "正在缓冲..."
                cancelDismissTopBottomTimer()
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                mImage!!.visibility = View.VISIBLE
                mCompleted!!.visibility = View.VISIBLE
                if (mNiceVideoPlayer!!.isFullScreen) {
                    mNiceVideoPlayer!!.exitFullScreen()
                }
                if (mNiceVideoPlayer!!.isTinyWindow) {
                    mNiceVideoPlayer!!.exitTinyWindow()
                }
            }
            NiceVideoPlayer.STATE_COMPLETED -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                mImage!!.visibility = View.VISIBLE
                mCompleted!!.visibility = View.VISIBLE
                if (mNiceVideoPlayer!!.isFullScreen) {
                    mNiceVideoPlayer!!.exitFullScreen()
                }
                if (mNiceVideoPlayer!!.isTinyWindow) {
                    mNiceVideoPlayer!!.exitTinyWindow()
                }
            }
            NiceVideoPlayer.STATE_ERROR -> {
                cancelUpdateProgressTimer()
                setTopBottomVisible(false)
                mTop!!.visibility = View.VISIBLE
                mError!!.visibility = View.VISIBLE
            }
        }
    }

    private fun startUpdateProgressTimer() {
        cancelUpdateProgressTimer()
        if (mUpdateProgressTimer == null) {
            mUpdateProgressTimer = Timer()
        }
        if (mUpdateProgressTimerTask == null) {
            mUpdateProgressTimerTask = object : TimerTask() {
                override fun run() {
                    this@NiceVideoPlayerController.post { updateProgress() }
                }
            }
        }
        mUpdateProgressTimer!!.schedule(mUpdateProgressTimerTask, 0, 300)
    }

    private fun updateProgress() {
        val position = mNiceVideoPlayer!!.currentPosition
        val duration = mNiceVideoPlayer!!.duration
        val bufferPercentage = mNiceVideoPlayer!!.bufferPercentage
        mSeek!!.secondaryProgress = bufferPercentage
        val progress = (100f * position / duration).toInt()
        mSeek!!.progress = progress
        mPosition!!.text = NiceUtil.formatTime(position)
        mDuration!!.text = NiceUtil.formatTime(duration)
    }

    private fun cancelUpdateProgressTimer() {
        if (mUpdateProgressTimer != null) {
            mUpdateProgressTimer!!.cancel()
            mUpdateProgressTimer = null
        }
        if (mUpdateProgressTimerTask != null) {
            mUpdateProgressTimerTask!!.cancel()
            mUpdateProgressTimerTask = null
        }
    }

    private fun startDismissTopBottomTimer() {
        cancelDismissTopBottomTimer()
        if (mDismissTopBottomCountDownTimer == null) {
            mDismissTopBottomCountDownTimer = object : CountDownTimer(8000, 8000) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    setTopBottomVisible(false)
                }
            }
        }
        mDismissTopBottomCountDownTimer!!.start()
    }


    private fun cancelDismissTopBottomTimer() {
        if (mDismissTopBottomCountDownTimer != null) {
            mDismissTopBottomCountDownTimer!!.cancel()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        cancelDismissTopBottomTimer()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (mNiceVideoPlayer!!.isBufferingPaused || mNiceVideoPlayer!!.isPaused) {
            mNiceVideoPlayer!!.restart()
        }
        val position = (mNiceVideoPlayer!!.duration * seekBar.progress / 100f).toInt()
        mNiceVideoPlayer!!.seekTo(position)
        startDismissTopBottomTimer()
    }

    /**
     * 控制器恢复到初始状态
     */
    fun reset() {
        topBottomVisible = false
        cancelUpdateProgressTimer()
        cancelDismissTopBottomTimer()
        mSeek!!.progress = 0
        mSeek!!.secondaryProgress = 0

        mCenterStart!!.visibility = View.VISIBLE
        mImage!!.visibility = View.VISIBLE

        mBottom!!.visibility = View.GONE
        mFullScreen!!.setImageResource(R.drawable.ic_player_enlarge)

        mTop!!.visibility = View.VISIBLE
        mBack!!.visibility = View.GONE

        mLoading!!.visibility = View.GONE
        mError!!.visibility = View.GONE
        mCompleted!!.visibility = View.GONE
    }
}
