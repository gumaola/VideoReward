package cn.nano.main.home.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;

import java.util.List;

import cn.nano.main.R;
import cn.nano.main.server.result.VideoListResult;

public class VideoPlayerAdapter extends RecyclerView.Adapter<VideoPlayerViewHold> {

    private List<VideoListResult.DataBean.VlistBean> videoList;
    private VideoHoldClicker clicker;


    @NonNull
    @Override
    public VideoPlayerViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_player,
                parent, false);
        VideoPlayerViewHold holder = new VideoPlayerViewHold(itemView);
        //创建视频播放控制器，主要只要创建一次就可以呢
        final VideoPlayerController controller = new VideoPlayerController(parent.getContext());
        controller.setTopBottomVisible(false);
        controller.setCenterPlayer(false, 0);
        controller.interceptTopBottomVisible(true);
        controller.setHideTime(1000);
        holder.setController(controller);
        holder.setVideoHoldClicker(clicker);
        holder.setOnProgrssChanged(progressChange);
        return holder;
    }

    private SparseArray<VideoPlayerViewHold> nowHolds = new SparseArray<>();

    @Override
    public void onBindViewHolder(@NonNull VideoPlayerViewHold holder, int position) {
        VideoListResult.DataBean.VlistBean video = videoList.get(position);
        nowHolds.put(position, holder);
        holder.bindData(video);
    }


    public void replayHolderVideo(int pos) {
        if (nowHolds != null) {
            VideoPlayerViewHold holder = nowHolds.get(pos);
            holder.replay();
        }
    }

    @Override
    public int getItemCount() {
        return videoList != null ? videoList.size() : 0;
    }


    public void bindVideoList(List<VideoListResult.DataBean.VlistBean> list) {
        videoList = list;
    }

    public void setVideoClicker(VideoHoldClicker clicker) {
        this.clicker = clicker;
    }

    public void refreshData(VideoListResult.DataBean.VlistBean bean) {
        int index = videoList.indexOf(bean);
        notifyItemChanged(index);
    }

    public VideoListResult.DataBean.VlistBean getVideBean(int index) {
        if (index >= 0 && index < getItemCount()) {
            return videoList.get(index);
        }

        return null;
    }


    private OnVideoProgressChange progressChange;

    public void setOnProgressChange(OnVideoProgressChange l) {
        progressChange = l;
    }
}
