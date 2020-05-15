package cn.nano.main.person.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.common.glide.ImgLoader;
import cn.nano.main.R;
import cn.nano.main.server.result.VideoInfo;

public class MeVideoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<VideoInfo> videoList;
    private View headView;

    private VideoPreviewItemClick click;

    public MeVideoListAdapter(VideoPreviewItemClick clicker) {
        click = clicker;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new VideoHeadViewHold(headView);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_grid_list, parent, false);
        return new VideoGridViewHold(view, click);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (headView != null && position == 0) {
            return;
        }

        VideoInfo info = videoList.get(translateToRealPos(position));
        ((VideoGridViewHold) holder).binddata(info);
    }

    public int translateToRealPos(int position) {
        int headSize = headView != null ? 1 : 0;
        return position - headSize;
    }

    @Override
    public int getItemCount() {
        int listSize = videoList == null ? 0 : videoList.size();
        int headCount = headView == null ? 0 : 1;

        return listSize + headCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (headView != null && position == 0) {
            return 0;//head
        }
        return 1;//item
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {   // 布局是GridLayoutManager所管理
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果是Header、Footer的对象则占据spanCount的位置，否则就只占用1个位置
                    return (position == 0 && headView != null) ? gridLayoutManager
                            .getSpanCount() : 1;
                }
            });
        }
    }


    public void refreshVideos(List<VideoInfo> list) {
        videoList = list;
        notifyDataSetChanged();
    }

    public void addHead(View view) {
        headView = view;
    }


    public class VideoHeadViewHold extends RecyclerView.ViewHolder {

        public VideoHeadViewHold(@NonNull View itemView) {
            super(itemView);
        }
    }


    public class VideoGridViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView preview;
        private VideoInfo videoInfo;
        private VideoPreviewItemClick clickL;

        public VideoGridViewHold(@NonNull View itemView, VideoPreviewItemClick click) {
            super(itemView);
            preview = itemView.findViewById(R.id.video_preview);
            itemView.setOnClickListener(this);
            clickL = click;
        }

        public void binddata(VideoInfo info) {
            videoInfo = info;
            ImgLoader.displayWithError(preview.getContext(), info.getVideo_img(), preview, R.drawable.item_color);
        }

        @Override
        public void onClick(View v) {
            if (clickL != null) {
                clickL.onClick(videoInfo);
            }
        }
    }


    public interface VideoPreviewItemClick {
        void onClick(VideoInfo info);
    }
}
