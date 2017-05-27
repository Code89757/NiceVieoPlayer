package com.xiao.nicevieoplayer.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import com.xiao.nicevieoplayer.R
import com.xiao.nicevieoplayer.example.adapter.VideoAdapter
import com.xiao.nicevieoplayer.example.bean.Video

import java.util.ArrayList

class RecyclerViewActivity : AppCompatActivity() {

    private var mRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        init()
    }

    private fun init() {
        mRecyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setHasFixedSize(true)
        val adapter = VideoAdapter(this, videoList)
        mRecyclerView!!.adapter = adapter
        mRecyclerView!!.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {

            }

            override fun onChildViewDetachedFromWindow(view: View) {
                val niceVideoPlayer = view.findViewById(R.id.nice_video_player) as NiceVideoPlayer
                niceVideoPlayer?.release()
            }
        })
    }

    val videoList: List<Video>
        get() {
            val videoList = ArrayList<Video>()
            videoList.add(Video("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4"))

            videoList.add(Video("小野在办公室用丝袜做茶叶蛋 边上班边看《外科风云》",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-09-58.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-20-26.mp4"))

            videoList.add(Video("花盆叫花鸡，怀念玩泥巴，过家家，捡根竹竿当打狗棒的小时候",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-03_12-52-08.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-03_13-02-41.mp4"))

            videoList.add(Video("针织方便面，这可能是史上最不方便的方便面",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-18-22.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4"))

            videoList.add(Video("宵夜的下午茶，办公室不只有KPI，也有诗和远方",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-26_10-00-28.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-26_10-06-25.mp4"))

            videoList.add(Video("可乐爆米花，嘭嘭嘭......收花的人说要把我娶回家",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4"))
            videoList.add(Video("可乐爆米花，嘭嘭嘭......收花的人说要把我娶回家",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4"))
            videoList.add(Video("可乐爆米花，嘭嘭嘭......收花的人说要把我娶回家",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4"))
            videoList.add(Video("可乐爆米花，嘭嘭嘭......收花的人说要把我娶回家",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4"))
            videoList.add(Video("可乐爆米花，嘭嘭嘭......收花的人说要把我娶回家",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-37-16.jpg",
                    "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4"))
            return videoList
        }

    override fun onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return
        }
        super.onBackPressed()
    }
}
