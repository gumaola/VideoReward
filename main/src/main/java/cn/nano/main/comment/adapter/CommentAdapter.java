package cn.nano.main.comment.adapter;

import android.media.Image;
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
import cn.nano.main.server.result.CommentListResult;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {


    private List<CommentListResult.DataBean> comments;


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
    }


    public void refreshList(List<CommentListResult.DataBean> list) {
        comments = list;
        notifyDataSetChanged();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private ImageView avatar;
        private TextView content;
        private TextView time;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.comment_item_avatar);
            content = itemView.findViewById(R.id.comment_item_content);
            time = itemView.findViewById(R.id.comment_item_time);
        }


        public void bindData(CommentListResult.DataBean comment) {
            ImgLoader.displayWithError(avatar.getContext(), comment.getMember_avatar(), avatar, R.mipmap.icon_avatar_placeholder);
            content.setText(comment.getComment_detail());
            time.setText(comment.getComment_time());
        }
    }
}
