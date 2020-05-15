package cn.nano.main.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.yczbj.ycvideoplayerlib.manager.VideoPlayerManager;
import org.yczbj.ycvideoplayerlib.player.VideoPlayer;

import java.util.List;

import cn.nano.common.app.CommonFragment;
import cn.nano.common.app.GlobalConfig;
import cn.nano.main.comment.CommentActivity;
import cn.nano.common.okhttp.utils.L;
import cn.nano.common.snappager.OnPagerListener;
import cn.nano.common.snappager.PagerLayoutManager;
import cn.nano.common.utils.DialogUtil;
import cn.nano.common.utils.PreferenceUtil;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.AppPrefs;
import cn.nano.main.constant.IntentConstant;
import cn.nano.main.home.adapter.OnVideoProgressChange;
import cn.nano.main.home.adapter.VideoHoldClicker;
import cn.nano.main.home.adapter.VideoPlayerAdapter;
import cn.nano.main.home.adapter.VideoPlayerViewHold;
import cn.nano.main.person.PersonInfoActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.CoinConfigBean;
import cn.nano.main.server.result.ConfigResult;
import cn.nano.main.server.result.GetCoinResult;
import cn.nano.main.server.result.VideoListResult;
import okhttp3.Call;
import okhttp3.Response;

public class HomeMainFragment extends CommonFragment implements VideoHoldClicker, TimeCallBack, OnVideoProgressChange {

    private RecyclerView videoRecycler;
    private VideoPlayerAdapter videoPlayerAdapter;

    private View alarmRoot;
    private TextView alarmTime;

    private Dialog dialog;

    //利用handle刷新time
    private static final int REFRESH_TIME = 0x1000;
    private TimeHandle timeHandle;

    //video info
    private int currentSelectVideo = 0;
    //记录各个视频播放到何处
    private SparseArray<Integer> videoProgressList;

    //倒计时
    private static class TimeHandle extends Handler {

        private TextView timeV;
        private int currentTime;

        private int needPostCoin;
        private String coinId;

        private TimeCallBack callBack;

        private TimeHandle(TextView textView, TimeCallBack c) {
            timeV = textView;
            callBack = c;
        }

        public void bindTime(int time, String id, int coin) {
            currentTime = time;
            needPostCoin = coin;
            coinId = id;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == REFRESH_TIME) {
                currentTime = currentTime - 1;

                int min = currentTime / 60;
                int second = currentTime % 60;
                String minStr;
                String secondStr;

                if (min >= 10) {
                    minStr = String.valueOf(min);
                } else {
                    minStr = "0" + min;
                }

                if (second > 10) {
                    secondStr = String.valueOf(second);
                } else {
                    secondStr = "0" + second;
                }

                timeV.setText(minStr + ":" + secondStr);

                if (currentTime == 0) {
                    if (callBack != null) {
                        callBack.onTimerComplete(coinId, needPostCoin);
                        coinId = null;
                        needPostCoin = -1;
                    }
                } else {
                    sendEmptyMessageDelayed(REFRESH_TIME, 1000);
                }

            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        initData();
        videoProgressList = new SparseArray<>();
        timeHandle = new TimeHandle(alarmTime, this);
        return view;
    }

    private void initView(View root) {
        videoRecycler = root.findViewById(R.id.main_video_list);
        alarmRoot = root.findViewById(R.id.main_video_alarm);
        alarmTime = root.findViewById(R.id.main_alarm_time);

        alarmRoot.setVisibility(View.GONE);

        dialog = DialogUtil.loadingDialog(getContext(), null);

        initRecycler();
    }

    private void initRecycler() {
        PagerLayoutManager viewPagerLayoutManager = new PagerLayoutManager(getContext(), OrientationHelper.VERTICAL);

        videoPlayerAdapter = new VideoPlayerAdapter();
        videoPlayerAdapter.setVideoClicker(this);
        videoRecycler.setAdapter(videoPlayerAdapter);
        videoRecycler.setLayoutManager(viewPagerLayoutManager);
        videoPlayerAdapter.setOnProgressChange(this);


        viewPagerLayoutManager.setOnViewPagerListener(new OnPagerListener() {
            @Override
            public void onInitComplete() {
                L.e("comm");
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                L.e("release");
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                L.e("pos");
                currentSelectVideo = position;
                videoPlayerAdapter.replayHolderVideo(position);
            }
        });

        videoRecycler.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
                VideoPlayer videoPlayer = ((VideoPlayerViewHold) holder).mVideoPlayer;
                if (videoPlayer == VideoPlayerManager.instance().getCurrentVideoPlayer()) {
                    VideoPlayerManager.instance().releaseVideoPlayer();
                }
            }
        });
    }


    @Override
    public void onProgressChange(VideoListResult.DataBean.VlistBean bean, int progress) {
        if (bean == null) {
            return;
        }

        if (isInPause) {
            VideoPlayerManager.instance().suspendVideoPlayer();
        }

        //已经在刷新了
        boolean hasMsg = timeHandle.hasMessages(REFRESH_TIME);

        if (bean.getVInfo().getIs_coin() == 1) {
            //当前视频是否已经播放
            int videoId = bean.getVInfo().getVideo_id();
            Integer oldProgress = videoProgressList.get(videoId);
            if (oldProgress == null) {
                oldProgress = 0;
            }
            if (progress > oldProgress) {
                if (!hasMsg) {
                    timeHandle.sendEmptyMessage(REFRESH_TIME);
                }

                videoProgressList.put(videoId, progress);

            } else {
                if (hasMsg) {
                    timeHandle.removeMessages(REFRESH_TIME);
                }
            }
        } else {
            if (hasMsg) {
                timeHandle.removeMessages(REFRESH_TIME);
            }
        }
    }


    private void initData() {
        videoRecycler.setVisibility(View.GONE);
        alarmRoot.setVisibility(View.GONE);

        getVideoList();

        getConfig();

    }

    private void getVideoList() {
        dialog.show();
        String token = AccountManager.INSTANCE.getUserToken();
        ServerApi.getVideoList(token, new AutoLoginOutCallback<VideoListResult>() {
            @Override
            public VideoListResult parseNetworkResponse(Response response, int id) throws Exception {

                VideoListResult result = super.parseNetworkResponse(response, id);
//                if (result.getCode() == BaseResult.SUCCESS_CODE) {
//                    List<VideoListResult.DataBean.VlistBean> list = result.getData().getVlist();
//                    for (VideoListResult.DataBean.VlistBean bean : list) {
//                        VideoPlayerViewHold.addThumbs(bean.getVInfo().getVideo_id(),
//                                bean.getVInfo().getVideo_url());
//                    }
//                }

                return result;
            }

            @Override
            public void onResponse(VideoListResult response, int id) {
                dialog.dismiss();
                if (response != null) {
                    if (response.getCode() == BaseResult.SUCCESS_CODE) {
                        List<VideoListResult.DataBean.VlistBean> list = response.getData().getVlist();
                        videoPlayerAdapter.bindVideoList(list);
                        videoPlayerAdapter.notifyDataSetChanged();
                        alarmRoot.setVisibility(View.VISIBLE);
                        videoRecycler.setVisibility(View.VISIBLE);
                    } else {
                        String msg = response.getMsg();
                        ToastUtil.show(msg);
                    }
                } else {
                    ToastUtil.show(R.string.common_error);
                }

            }

            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                ToastUtil.show(R.string.common_error);
            }
        });
    }

    private void getConfig() {
        ServerApi.getConfigInfo(AccountManager.INSTANCE.getUserToken(), new AutoLoginOutCallback<ConfigResult>() {
            @Override
            public void onResponse(ConfigResult response, int id) {
                super.onResponse(response, id);
                if (response != null && response.getCode() == BaseResult.SUCCESS_CODE) {
                    String notice = response.getData().getNotice().getNotice_detail();
                    boolean isUploadVideo = response.getData().getIsUpload() == 1;
                    boolean isComment = response.getData().getIsComment() == 1;
                    float ratio = response.getData().getCost();

                    SharedPreferences pref = PreferenceUtil.getPreferences(GlobalConfig.getInstance().currentApp(), AppPrefs.FILE_CONFIG);
                    pref.edit().putString(AppPrefs.KEY_NOTICE_DESC, notice)
                            .putBoolean(AppPrefs.KEY_CAN_UPLOAD_VIDEO, isUploadVideo)
                            .putBoolean(AppPrefs.KEY_CANCOMMENT, isComment)
                            .putFloat(AppPrefs.KEY_CHARGE_RATIO, ratio)
                            .apply();

                    //获取视频第一个倒计时
                    startTimer(response.getData().getCoin());
                }
            }
        });
    }


    private void startTimer(CoinConfigBean coinConfig) {
        if (coinConfig == null) {
            return;
        }

        timeHandle.bindTime(coinConfig.getTime(), coinConfig.getId(), coinConfig.getCoin());
        timeHandle.sendEmptyMessage(REFRESH_TIME);
        alarmRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimerComplete(String coinId, int coin) {
//        ServerApi.getCoin(AccountManager.INSTANCE.getUserToken(), );
        alarmRoot.setVisibility(View.GONE);
        VideoListResult.DataBean.VlistBean bean = videoPlayerAdapter.getVideBean(currentSelectVideo);
        if (bean == null) {
            return;
        }

        ServerApi.getCoin(AccountManager.INSTANCE.getUserToken(), bean.getVInfo().getVideo_id(), -1, 1, coinId,
                new AutoLoginOutCallback<GetCoinResult>() {
                    @Override
                    public void onResponse(GetCoinResult response, int id) {
                        super.onResponse(response, id);
                        if (response != null && response.getCode() == BaseResult.SUCCESS_CODE) {
                            startTimer(response.getData());
                        }
                    }

                });
    }


    private boolean isInPause = false;
    private boolean isHasMsg = true;

    public void resumeVideoPlayer() {
        if (isInPause) {
            if (isHasMsg) {
                timeHandle.sendEmptyMessage(REFRESH_TIME);
            }

            isInPause = false;
        }
        VideoPlayerManager.instance().resumeVideoPlayer();
    }

    public void pauseVideoPlayer() {
        isInPause = true;
        isHasMsg = timeHandle.hasMessages(REFRESH_TIME);
        if (isHasMsg) {
            timeHandle.removeMessages(REFRESH_TIME);
        }

        VideoPlayerManager.instance().suspendVideoPlayer();
    }


    private VideoPlayerViewHold currentViewHold;
    private VideoListResult.DataBean.VlistBean currentVidebean;

    //右侧按钮点击事件
    @Override
    public void onClick(View v, VideoListResult.DataBean.VlistBean
            videoBean, VideoPlayerViewHold viewHold) {
        if (videoBean == null) {
            return;
        }

        int id = v.getId();

        //跳转到个人信息页面
        if (id == R.id.video_user_thumb) {
            VideoListResult.DataBean.VlistBean.MInfoBean member = videoBean.getMInfo();
            Bundle bundle = new Bundle();
            bundle.putInt(IntentConstant.MEMBER_ID, member.getMember_id());
            PersonInfoActivity.forward(getContext(), bundle);
            return;
        }

        //开始关注用户
        if (id == R.id.video_follow_user) {
            followUserOrVideo(videoBean.getMInfo().getMember_id(), false, videoBean, viewHold);
            return;
        }

        //开始收藏video
        if (id == R.id.video_fav_icon) {
            followUserOrVideo(videoBean.getVInfo().getVideo_id(), true, videoBean, viewHold);
            return;
        }


        //评论video
        if (id == R.id.video_comment_icon) {
            boolean isCanComment = PreferenceUtil.getBoolean(getContext(), AppPrefs.FILE_CONFIG, AppPrefs.KEY_CANCOMMENT, false);
            if (isCanComment) {
                currentVidebean = videoBean;
                currentViewHold = viewHold;
                CommentActivity.forward(getActivity(), videoBean.getVInfo().getVideo_id());
            } else {
                ToastUtil.show(getString(R.string.no_permission));
            }

            return;
        }

        //分享
        if (id == R.id.video_share_icon) {
            //todo
            ToastUtil.show("分享功能尚在开发");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommentActivity.COMMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            int num = currentVidebean.getVInfo().getComment_num();
            currentVidebean.getVInfo().setComment_num(num + 1);
            currentViewHold.getCommentCount().setText(String.valueOf(num + 1));
        }
    }

    private void followUserOrVideo(int id, final boolean isVideo,
                                   final VideoListResult.DataBean.VlistBean bean, final VideoPlayerViewHold viewHold) {
        dialog.show();
        String token = AccountManager.INSTANCE.getUserToken();
        ServerApi.follow(token, isVideo ? 0 : 1, id, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                dialog.dismiss();
                ToastUtil.show(R.string.common_error);
            }

            @Override
            public void onResponse(BaseResult response, int id) {
                dialog.dismiss();
                if (response.getCode() != BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(response.getMsg());
                    return;
                }


                if (isVideo) {
                    boolean isFollow = bean.getVInfo().getIs_follow() == 1;

                    if (!isFollow) {
                        bean.getVInfo().setIs_follow(1);
                        int count = bean.getVInfo().getSupport_num();
                        bean.getVInfo().setSupport_num(count + 1);
                        viewHold.getFavorIcon().setImageResource(R.mipmap.ico_favor_enable);
                        viewHold.getFavorCount().setText(String.valueOf(count + 1));
                    } else {
                        bean.getVInfo().setIs_follow(2);
                        int count = bean.getVInfo().getSupport_num();
                        bean.getVInfo().setSupport_num(count - 1);
                        viewHold.getFavorCount().setText(String.valueOf(count - 1));
                        viewHold.getFavorIcon().setImageResource(R.mipmap.ico_favor_disable);
                    }


                } else {
                    boolean isFollow = bean.getMInfo().getIs_follow() == 1;
                    bean.getMInfo().setIs_follow(isFollow ? 2 : 1);
                    viewHold.getFollowIcon().setText(isFollow ? "+" : "√");
                }
            }
        });
    }
}
