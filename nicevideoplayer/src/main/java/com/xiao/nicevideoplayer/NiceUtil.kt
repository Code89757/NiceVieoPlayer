package com.xiao.nicevideoplayer

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ContextThemeWrapper
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager

import java.util.Formatter
import java.util.Locale

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
    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * Get AppCompatActivity from context

     * @param context
     * *
     * @return AppCompatActivity if it's not null
     */
    fun getAppCompActivity(context: Context?): AppCompatActivity? {
        if (context == null) return null
        if (context is AppCompatActivity) {
            return context
        } else if (context is ContextThemeWrapper) {
            return getAppCompActivity(context.baseContext)
        }
        return null
    }

    fun showActionBar(context: Context) {
        val ab = getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.show()
        }
        scanForActivity(context)!!
                .window
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun hideActionBar(context: Context) {
        val ab = getAppCompActivity(context)!!.supportActionBar
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false)
            ab.hide()
        }
        scanForActivity(context)!!
                .window
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 获取屏幕宽度

     * @param context
     * *
     * @return width of the screen.
     */
    fun getScreenWidth(context: Context): Int {
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
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics).toInt()
    }

    /**
     * 将毫秒数格式化为"##:##"的时间

     * @param milliseconds 毫秒数
     * *
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
        val mFormatter = Formatter(stringBuilder, Locale.getDefault())
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }
}
