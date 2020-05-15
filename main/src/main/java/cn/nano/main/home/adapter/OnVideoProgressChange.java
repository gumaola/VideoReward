package cn.nano.main.home.adapter;

import cn.nano.main.server.result.VideoListResult;

public interface OnVideoProgressChange {
    void onProgressChange(VideoListResult.DataBean.VlistBean bean, int progress);
}
