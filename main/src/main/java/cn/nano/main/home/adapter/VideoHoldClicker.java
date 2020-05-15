package cn.nano.main.home.adapter;

import android.view.View;

import cn.nano.main.server.result.VideoListResult;

public interface VideoHoldClicker {
    void onClick(View v, VideoListResult.DataBean.VlistBean videoBean, VideoPlayerViewHold viewHold);
}
