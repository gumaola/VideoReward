package cn.nano.main.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.yczbj.ycvideoplayerlib.constant.ConstantKeys;
import org.yczbj.ycvideoplayerlib.controller.VideoPlayerController;
import org.yczbj.ycvideoplayerlib.inter.listener.OnCompletedListener;
import org.yczbj.ycvideoplayerlib.inter.listener.OnProgressChangeListener;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.HashMap;

import cn.nano.common.app.GlobalConfig;
import cn.nano.common.glide.ImgLoader;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.AppPrefs;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.VideoInfo;
import cn.nano.main.server.result.VideoListResult;

public class VideoPlayerViewHold extends RecyclerView.ViewHolder implements View.OnClickListener, OnCompletedListener, OnProgressChangeListener {

    public VideoPlayerController mController;
    public VideoPlayer mVideoPlayer;

    private Context viewContext;

    private VideoHoldClicker clicker;

    //视频信息
    private VideoListResult.DataBean.VlistBean videoBean;

    //view
    private ImageView userAvatar;
    private TextView followUser;

    private ImageView favorIcon;
    private ImageView commentIcon;
    private ImageView shareIcon;

    private TextView favorCount;
    private TextView commentCount;
    private TextView shareCount;

    private TextView videoName;
    private TextView userName;

    //notice
    private LinearLayout noticeRoot;
    private TextView notice;

    //progress
    private OnVideoProgressChange progressChangeL;

    public VideoPlayerViewHold(@NonNull View itemView) {
        super(itemView);
        viewContext = itemView.getContext();
        mVideoPlayer = itemView.findViewById(R.id.video_player);

        //count
        userAvatar = itemView.findViewById(R.id.video_user_thumb);
        followUser = itemView.findViewById(R.id.video_follow_user);
        favorIcon = itemView.findViewById(R.id.video_fav_icon);
        commentIcon = itemView.findViewById(R.id.video_comment_icon);
        shareIcon = itemView.findViewById(R.id.video_share_icon);
        favorCount = itemView.findViewById(R.id.video_fav_count);
        commentCount = itemView.findViewById(R.id.video_comment_count);
        shareCount = itemView.findViewById(R.id.video_share_count);

        videoName = itemView.findViewById(R.id.video_name);
        userName = itemView.findViewById(R.id.video_user_name);

        noticeRoot = itemView.findViewById(R.id.video_notice_root);
        notice = itemView.findViewById(R.id.video_notice);

        userAvatar.setOnClickListener(this);
        followUser.setOnClickListener(this);
        favorIcon.setOnClickListener(this);
        commentIcon.setOnClickListener(this);
        shareIcon.setOnClickListener(this);
    }

    /**
     * 设置视频控制器参数
     *
     * @param controller 控制器对象
     */
    void setController(VideoPlayerController controller) {
        mController = controller;
        mVideoPlayer.setPlayerType(ConstantKeys.IjkPlayerType.TYPE_IJK);
        mVideoPlayer.setController(mController);
        mController.setOnCompletedListener(this);
        mController.setOnProgressChangeListener(this);
    }


    public void replay() {
        if (mController != null) {
            mVideoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mVideoPlayer.start();
                }
            }, 20);
        }
    }


    void setOnProgrssChanged(OnVideoProgressChange l) {
        progressChangeL = l;
    }

    @Override
    public void onProgress(int progress) {
        if (progressChangeL != null) {
            progressChangeL.onProgressChange(videoBean, progress);
        }
    }

    @Override
    public void onCompleted() {
        if (mController != null) {
            mVideoPlayer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mController.replayVideo();
                }
            }, 50);
        }
        if (videoBean != null) {
            ServerApi.getCoin(AccountManager.INSTANCE.getUserToken(), videoBean.getVInfo().getVideo_id(), 1, -1, null, null);
            videoBean.setPlayComplete(true);

            if (progressChangeL != null) {
                progressChangeL.onProgressChange(videoBean, 100);
            }
        }
    }

    void bindData(VideoListResult.DataBean.VlistBean video) {
        videoBean = video;

        boolean isFrist = !videoBean.isRefresh();

        bindVideoInfo(video.getVInfo(), isFrist);
        bindUserInfo(video.getMInfo());

        //bind notice
        bindNotice();
    }

    private void bindNotice() {
        String noticestr = PreferenceUtil.getString(GlobalConfig.getInstance().currentApp(), AppPrefs.FILE_CONFIG, AppPrefs.KEY_NOTICE_DESC, null);
        if (TextUtils.isEmpty(noticestr)) {
            noticeRoot.setVisibility(View.GONE);
        } else {
            noticeRoot.setVisibility(View.VISIBLE);
            notice.setText(noticestr);
        }

    }


    void setVideoHoldClicker(VideoHoldClicker clicker) {
        this.clicker = clicker;
    }


    private void bindVideoInfo(VideoInfo videoInfo, boolean isFrist) {
        //初始化默认图片和播放信息
        mController.setTitle(videoInfo.getVideo_title());
        ImgLoader.display(viewContext, videoInfo.getVideo_img(), mController.imageView());
//        mController.imageView().setImageBitmap(getNetVideoBitmap(videoInfo.getVideo_url()));

        mVideoPlayer.setUp(videoInfo.getVideo_url(), null);
        //不从上一次位置播放，也就是每次从0播放
        mVideoPlayer.continueFromLastPosition(false);
        mVideoPlayer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mVideoPlayer.start();
            }
        }, 50);

        //初始化额外信息
        int favor = videoInfo.getSupport_num();
        int comment = videoInfo.getComment_num();
        int share = videoInfo.getSupport_num();

        favorCount.setText(String.valueOf(favor));
        commentCount.setText(String.valueOf(comment));
        shareCount.setText(String.valueOf(share));

        String title = videoInfo.getVideo_title();
        videoName.setText(title);

        if (videoInfo.getIs_follow() == 1) {
            favorIcon.setImageResource(R.mipmap.ico_favor_enable);
        } else {
            favorIcon.setImageResource(R.mipmap.ico_favor_disable);
        }
    }

    public static Bitmap getNetVideoBitmap(String videoUrl) {
        Bitmap bitmap = null;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据url获取缩略图
            retriever.setDataSource(videoUrl, new HashMap());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    private void bindUserInfo(VideoListResult.DataBean.VlistBean.MInfoBean memberInfo) {
        if (memberInfo == null) {
            userAvatar.setVisibility(View.GONE);
            followUser.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            favorIcon.setVisibility(View.GONE);
            favorCount.setVisibility(View.GONE);
            commentIcon.setVisibility(View.GONE);
            commentCount.setVisibility(View.GONE);
            shareIcon.setVisibility(View.GONE);
            shareCount.setVisibility(View.GONE);
            return;
        }
        String avatar = memberInfo.getMember_avatar();
        ImgLoader.displayWithError(viewContext, avatar, userAvatar, R.mipmap.icon_avatar_placeholder);

        boolean isFllow = memberInfo.getIs_follow() == 1;
        if (isFllow) {
            followUser.setText("√");
        } else {
            followUser.setText("+");
        }

        String name = memberInfo.getNick_name();
        userName.setText("@" + name);
    }

    public TextView getFavorCount() {
        return favorCount;
    }

    public ImageView getFavorIcon() {
        return favorIcon;
    }


    public TextView getCommentCount() {
        return commentCount;
    }

    public TextView getShareCount() {
        return shareCount;
    }

    public TextView getFollowIcon() {
        return followUser;
    }

    @Override
    public void onClick(View v) {
        if (clicker != null) {
            clicker.onClick(v, videoBean, this);
        }
    }
}
