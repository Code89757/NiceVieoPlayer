package com.xiao.nicevieoplayer.example

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import com.xiao.nicevieoplayer.R

class MainActivity : AppCompatActivity() {

    private var mNiceVideoPlayer: NiceVideoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        mNiceVideoPlayer = findViewById(R.id.nice_video_player) as NiceVideoPlayer
        mNiceVideoPlayer!!.setUp("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4", null)

        val controller = NiceVideoPlayerController(this)
        controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？")
        controller.setImage("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg")
        mNiceVideoPlayer!!.setController(controller)
    }

    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    fun enterTinyWindow(view: View) {
        if (mNiceVideoPlayer!!.isPlaying
                || mNiceVideoPlayer!!.isBufferingPlaying
                || mNiceVideoPlayer!!.isPaused
                || mNiceVideoPlayer!!.isBufferingPaused) {
            mNiceVideoPlayer!!.enterTinyWindow()
        } else {
            Toast.makeText(this, "要播放后才能进入小窗口", Toast.LENGTH_SHORT).show()
        }
    }

    fun showVideoList(view: View) {
        startActivity(Intent(this, RecyclerViewActivity::class.java))
    }
}
