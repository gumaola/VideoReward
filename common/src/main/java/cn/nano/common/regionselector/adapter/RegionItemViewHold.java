package cn.nano.common.regionselector.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.nano.common.R;
import cn.nano.common.regionselector.bean.AreaListBean;

public class RegionItemViewHold extends RecyclerView.ViewHolder {
    private AreaListBean area;
    private RegionItemClickListener clickListener;

    private TextView areaName;
    private ImageView arrow;

    public RegionItemViewHold(@NonNull View itemView, RegionItemClickListener listener) {
        super(itemView);
        clickListener = listener;
        initView(itemView);
    }

    private void initView(View itemView) {
        areaName = itemView.findViewById(R.id.area_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(area);
                }
            }
        });
        arrow = itemView.findViewById(R.id.area_arrow);
    }

    public void bindData(AreaListBean data) {
        area = data;
        areaName.setText(data.getName());

        if (!data.isHasChildArea()) {
            arrow.setVisibility(View.GONE);
        } else {
            arrow.setVisibility(View.VISIBLE);
        }
    }

}
