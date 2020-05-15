package cn.nano.main.tribe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.main.R;
import cn.nano.main.server.result.LevelListResult;

public class TribeLevelListAdapter extends RecyclerView.Adapter<TribeLevelViewHold> {
    private List<LevelListResult.DataBean.LevelListBean> levelList;
    private LevelItemClicker itemClicker;

    @NonNull
    @Override
    public TribeLevelViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level,
                parent, false);

        TribeLevelViewHold hold = new TribeLevelViewHold(itemView);
        hold.setItemClickListener(itemClicker);
        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull TribeLevelViewHold holder, int position) {
        LevelListResult.DataBean.LevelListBean level = levelList.get(position);
        holder.bindLevelData(level);
    }

    @Override
    public int getItemCount() {
        return levelList != null ? levelList.size() : 0;
    }

    public void bindList(List<LevelListResult.DataBean.LevelListBean> list) {
        levelList = list;
        notifyDataSetChanged();
    }

    public void bindItemClicker(LevelItemClicker clicker) {
        itemClicker = clicker;
    }
}
