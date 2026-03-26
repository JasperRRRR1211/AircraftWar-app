package edu.hitsz.Music;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 迁移到 Android 后，实际上不再需要继承 Thread，
 * 因为 MediaPlayer 本身就是异步在后台播放的。
 * 为了兼容你之前的调用代码，保留了 start() 和 stopPlay() 等方法名。
 */
public class MusicThread {
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean loop;

    // 注意：在 Android 中加载资源通常需要 Context，并且资源变成了 int 类型的 ID (如 R.raw.bgm)
    // 之前你传入的是 String filename，现在改传资源 ID
    public MusicThread(Context context, int resId, boolean loop) {
        this.context = context;
        this.loop = loop;

        // 创建 MediaPlayer 实例并加载音频文件
        mediaPlayer = MediaPlayer.create(context, resId);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(loop); // 设置是否循环
        }
    }

    /**
     * 替代之前的 Thread.start()
     */
    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 停止播放并释放内存
     */
    public void stopPlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            // 在 Android 中，不再使用 MediaPlayer 时必须 release() 释放底层资源
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}