package cn.nano.main.person.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.common.glide.ImgLoader;
import cn.nano.main.R;
import cn.nano.main.person.MemberListActivity;
import cn.nano.main.server.result.MemberInfo;
import cn.nano.main.server.result.MemberSimpleResult;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MemberViewHolder> {


    private List<MemberSimpleResult> memberInfos;
    private String uiType = MemberListActivity.followers;

    public MemberListAdapter(String type) {
        uiType = type;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_member_list_bean, parent, false);
        return new MemberViewHolder(view, uiType);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.bindData(memberInfos.get(position));
    }

    @Override
    public int getItemCount() {
        return memberInfos == null ? 0 : memberInfos.size();
    }


    public void bindDatas(List<MemberSimpleResult> infos) {
        memberInfos = infos;
        notifyDataSetChanged();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        private ImageView avatarV;
        private TextView nameV;
        private TextView followV;

        private String type;

        public MemberViewHolder(@NonNull View itemView, String uiType) {
            super(itemView);
            type = uiType;
            avatarV = itemView.findViewById(R.id.member_avater);
            nameV = itemView.findViewById(R.id.member_name);
            followV = itemView.findViewById(R.id.member_follow);

            followV.setVisibility(View.GONE);
        }

        public void bindData(MemberSimpleResult info) {
            String name = info.getNick_name();
            String phone = info.getLogin_phone();
            String avatar = info.getMember_avatar();

            ImgLoader.displayWithError(itemView.getContext(), avatar, avatarV, R.mipmap.icon_avatar_placeholder);

            if (MemberListActivity.inviters.equalsIgnoreCase(type)) {
                nameV.setText(phone);
            } else {
                nameV.setText(name);
            }
        }
    }
}
