package cn.nano.common.regionselector.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.common.R;
import cn.nano.common.regionselector.bean.AreaListBean;

public class RegionAdapter<T extends AreaListBean> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private boolean needShowHead;
    private List<T> areas;

    private RegionItemClickListener clickListener;

    //记录head view
    private RegionHeadViewHold headViewHold;

    public RegionAdapter(boolean needHead, RegionItemClickListener listener) {
        needShowHead = needHead;
        clickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_region_head, parent, false);
            headViewHold = new RegionHeadViewHold(headView, clickListener);
            return headViewHold;
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_region_item, parent, false);
        return new RegionItemViewHold(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {//head
            return;
        }

        AreaListBean bean = areas.get(translateToRealPos(position));
        ((RegionItemViewHold) holder).bindData(bean);
    }

    @Override
    public int getItemCount() {
        int headSize = needShowHead ? 1 : 0;
        int listSize = areas == null ? 0 : areas.size();
        return headSize + listSize;
    }

    private int translateToRealPos(int position) {
        int headSize = needShowHead ? 1 : 0;
        return position - headSize;
    }

    @Override
    public int getItemViewType(int position) {
        if (needShowHead && position == 0) {
            return 0;//0是顶头view样式
        }
        return 1;//1是默认item样式
    }


    //对外暴露方法
    public void addData(List<T> list) {
        areas = list;
        notifyDataSetChanged();
    }

    public void requestLocation() {
        if (headViewHold != null) {
            headViewHold.rquestLocation();
        }
    }
}
