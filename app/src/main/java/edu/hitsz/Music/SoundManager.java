package edu.hitsz.Music;


public class SoundManager {
    private static boolean musicOn = true; // 默认开启音乐
    private static MusicThread currentMusic = null;

    public static boolean isMusicOn() {
        return musicOn;
    }

    public static void setMusicOn(boolean on) {
        musicOn = on;
        if (!on && currentMusic != null) {
            stopBackground();
        }
    }

    // 播放背景音乐（普通BGM）
    public static void playBackground(String path, boolean loop) {
        if (!musicOn) return; // 若音乐关闭，不播放
        stopBackground(); // 避免多线程重叠
        currentMusic = new MusicThread(path, loop);
        currentMusic.start();
    }

    // 停止音乐
    public static void stopBackground() {
        if (currentMusic != null) {
            currentMusic.stopPlay();
            currentMusic = null;
        }
    }

    // 播放一次性音效（比如吃道具、爆炸等）
    public static void playEffect(String path) {
        if (!musicOn) return;
        new MusicThread(path, false).start();
    }
}