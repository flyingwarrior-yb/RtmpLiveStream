package com.jutong.live.jni;

import android.util.Log;

import com.androidyuan.publisher.LiveStateChangeListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PusherNative {



    public static final int  MSG_START=100;//开始
    public static final int  MSG_STOP=101;//停止

    public static final int  MSG_ERROR_PREVIEW=-100;//预览失败
    public static final int  MSG_ERROR_AUDIO_RECORD=-101;//音频录制失败
    public static final int  MSG_ERROR_SETUP_AUDIO_CODEC=-102;//设置音频编码器
    public static final int  MSG_ERROR_SETUP_VIDEO_CODEC=-103;//设置视频编码器
    public static final int  MSG_ERROR_NOT_CONNECTSERVER=-104;//无法连接到服务器

    //使用FIFO 方式的线程池
    ExecutorService mVideoPushDataExecutor = Executors.newSingleThreadExecutor();
    ExecutorService mAudioPushDataExecutor = Executors.newSingleThreadExecutor();


    private LiveStateChangeListener mListener;

    public PusherNative() {
    }

    public void setLiveStateChangeListener(LiveStateChangeListener listener) {
        mListener = listener;
    }

    public void onPostNativeError(int code) {
        Log.d("PusherNative", code + "");
        if (null != mListener) {
            mListener.onErrorPusher(code);
        }
    }

    //jni c call java
    public void onPostNativeState(int state) {
        if (state == MSG_START) {
            if (mListener != null) {
                mListener.onStartPusher();
            }
        } else if (state == MSG_STOP) {
            if (mListener != null) {
                mListener.onStopPusher();
            }
        }
    }

    public native void setVideoOptions(int width, int height, int bitrate,
            int fps);

    public native void setAudioOptions(int sampleRate, int channel);

    public void pushVideo(final byte[] buffer)
    {
        mVideoPushDataExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fireVideo(buffer);
            }
        });
    }

    public void pushAudio(final byte[] buffer, final int len)
    {
        mAudioPushDataExecutor.execute(new Runnable() {
            @Override
            public void run() {
                fireAudio(buffer,len);
            }
        });
    }

    private native void fireVideo(byte[] buffer);

    private native void fireAudio(byte[] buffer, int len);

    public native int getInputSamples();

    public native boolean startPusher(String url);

    public native void stopPusher();

    public native void release();
}
