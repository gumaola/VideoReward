package cn.nano.main.tribe.adapter;

import android.view.View;

import cn.nano.main.server.result.LevelListResult;

public interface LevelItemClicker {
    void onItemClck(LevelListResult.DataBean.LevelListBean bean, View content);
}
