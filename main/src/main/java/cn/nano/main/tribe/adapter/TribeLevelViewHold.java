package cn.nano.main.tribe.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import cn.nano.common.glide.ImgLoader;
import cn.nano.main.R;
import cn.nano.main.server.result.LevelListResult;

public class TribeLevelViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LevelListResult.DataBean.LevelListBean levelBean;


    private ImageView thumbV;
    private TextView titleV;
    private TextView costV;
    private TextView rewardV;

    private View rootView;

    public TribeLevelViewHold(@NonNull View itemView) {
        super(itemView);
        rootView = itemView;
        initView(itemView);
    }

    private void initView(View itemView) {
        titleV = itemView.findViewById(R.id.level_title);
        costV = itemView.findViewById(R.id.level_cost);
        rewardV = itemView.findViewById(R.id.level_reward);
        thumbV = itemView.findViewById(R.id.level_thumb);

        itemView.findViewById(R.id.level_redeem).setOnClickListener(this);
        itemView.setOnClickListener(this);
        itemView.findViewById(R.id.level_detail).setOnClickListener(this);
    }


    private LevelItemClicker levelItemClicker;

    public void setItemClickListener(LevelItemClicker clickListener) {
        levelItemClicker = clickListener;
    }


    public void bindLevelData(LevelListResult.DataBean.LevelListBean inflateBean) {
        levelBean = inflateBean;

        String title = inflateBean.getLevel_title();
        int cost = inflateBean.getLevel_coin();
        int profit = inflateBean.getLevel_profit();

        titleV.setText(title);
        costV.setText(String.format(rootView.getContext().getString(R.string.level_cost), cost));
        rewardV.setText(String.format(rootView.getContext().getString(R.string.level_reward), cost, profit));
        ImgLoader.displayWithError(itemView.getContext(), inflateBean.getLevel_icon(), thumbV, R.mipmap.icon_avatar_placeholder);
    }

    @Override
    public void onClick(View v) {
        if (levelItemClicker != null) {
            levelItemClicker.onItemClck(levelBean, v);
        }
    }
}
