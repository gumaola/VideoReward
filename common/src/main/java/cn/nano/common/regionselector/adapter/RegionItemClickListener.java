package cn.nano.common.regionselector.adapter;

import cn.nano.common.regionselector.bean.AreaListBean;

public interface RegionItemClickListener {
    void onHeadClick(String address);

    void onItemClick(AreaListBean bean);
}
