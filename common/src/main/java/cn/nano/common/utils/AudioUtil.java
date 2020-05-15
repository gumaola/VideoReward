package cn.nano.common.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class AudioUtil {
    public static int getVolume(Context context) {
        return ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE))
                .getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    /**
     * @param context
     * @param isAlways isAlways标记是否在系统关闭声音的前提下震动
     * @param isStrong
     */
    public static void onVibrator(Context context, boolean isAlways,
                                  boolean isStrong) {
        long[] weakPattern = {0L, 100L, 150L, 100L};
        long[] strongPattern = {0L, 200L, 250L, 200L};
        long[] pattern = weakPattern;
        if (isStrong) {
            pattern = strongPattern;
        }
        if (isAlways) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(pattern,
                    -1);
        } else if (getVolume(context) == 0) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(pattern,
                    -1);
        }
    }

    /**
     * 自拍模式下，可以采用声音提示用户
     *
     * @param context
     * @param mediaPlayer
     */
    public static void playMusic(Context context, MediaPlayer mediaPlayer) {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            mediaPlayer.seekTo(0);
            // int volumeValue = getVolume(context);
            // mediaPlayer.setVolume(volumeValue, volumeValue);// 设置声音
            mediaPlayer.start();
        }

    }

    /**
     * 停止播放声音
     */
    public static void stopMusic(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            // if (mediaPlayer.isPlaying()) {
            // mediaPlayer.stop();
            // }
            mediaPlayer.pause();
        }

    }

    /**
     * 释放mediaPlayer对象
     */
    public static void releasePlayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mediaPlayer = null;
            }
        }
    }
}
