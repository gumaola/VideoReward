package org.yczbj.ycvideoplayerlib.inter.listener;

/**
 * 监听视频播放进度
 */
public interface OnProgressChangeListener {
    /**
     * 0-100
     *
     * @param progress
     */
    void onProgress(int progress);
}
