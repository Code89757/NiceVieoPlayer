package com.xiao.nicevideoplayer.utils

import java.util.*

/**
 * Created by XiaoJianjun on 2017/5/8.
 * 工具类.
 */
object NiceUtil {
    /**
     * Get activity from context object

     * @param context something
     * *
     * @return object of Activity or null if it is not Activity
     */
    fun scanForActivity(context: android.content.Context?): android.app.Activity? {
        if (context == null) return null
        if (context is android.app.Activity) {
            return context
        } else if (context is android.content.ContextWrapper) {
            return com.xiao.nicevideoplayer.utils.NiceUtil.scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * Get AppCompatActivity from context

     * @param context
     * *
     * @return AppCompatActivity if it's not null
     */
    fun getAppCompActivity(context: android.content.Context?): android.support.v7.app.AppCompatActivity? {
        if (context == null) return null
        if (context is android.support.v7.app.AppCompatActivity) {
            return context
        } else if (context is android.support.v7.view.ContextThemeWrapper) {
            return com.xiao.nicevideoplayer.utils.NiceUtil.getAppCompActivity(context.baseContext)
        }
        return null
    }

    @android.annotation.SuppressLint("RestrictedApi")
    fun showActionBar(context: android.content.Context) {
        val ab = com.xiao.nicevideoplayer.utils.NiceUtil.getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.show()
        }
        com.xiao.nicevideoplayer.utils.NiceUtil.scanForActivity(context)!!
                .window
                .clearFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    @android.annotation.SuppressLint("RestrictedApi")
    fun hideActionBar(context: android.content.Context) {
        val ab = com.xiao.nicevideoplayer.utils.NiceUtil.getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.hide()
        }
        com.xiao.nicevideoplayer.utils.NiceUtil.scanForActivity(context)!!
                .window
                .setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取屏幕宽度

     * @param context
     * *
     * @return width of the screen.
     */
    fun getScreenWidth(context: android.content.Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * dp转px

     * @param context
     * *
     * @param dpVal   dp value
     * *
     * @return px value
     */
    fun dp2px(context: android.content.Context, dpVal: Float): Int {
        return android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics).toInt()
    }

    /**
     * 将毫秒数格式化为"##:##"的时间

     * @param milliseconds 毫秒数
     *
     * @return ##:##
     */
    fun formatTime(milliseconds: Int): String {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00"
        }
        val totalSeconds = milliseconds / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val stringBuilder = StringBuilder()
        val mFormatter = java.util.Formatter(stringBuilder, Locale.getDefault())
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }
}
