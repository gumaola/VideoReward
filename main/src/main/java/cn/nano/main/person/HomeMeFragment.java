package cn.nano.main.person;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.nano.common.app.CommonFragment;
import cn.nano.common.refresh.CustomRefreshViewHolder;
import cn.nano.common.utils.GsonUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.constant.IntentConstant;
import cn.nano.main.person.adapter.MeVideoListAdapter;
import cn.nano.main.person.view.HomeMeHeadView;
import cn.nano.main.person.view.IPresenter;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.SingleVideoListResult;
import cn.nano.main.server.result.VideoInfo;
import okhttp3.Call;

public class HomeMeFragment extends CommonFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, IPresenter, View.OnClickListener, MeVideoListAdapter.VideoPreviewItemClick {

    private boolean isSelf = true;
    private int otherMemberId = -1;

    private BGARefreshLayout refreshLayout;

    private HomeMeHeadView headView;

    //切换按钮
    private TextView myVideoTab;
    private TextView favorVideoTab;

    private RecyclerView videoRecycler;
    private MeVideoListAdapter videoAdpter;
    private boolean isMyVideoType;


    //保存video两种类型
    private List<VideoInfo> myVideoInfos = new ArrayList<>();
    private List<VideoInfo> elseVideoInfos = new ArrayList<>();

    private static final int pageSize = 10;//每次取10条

    private boolean isCreated;

    /**
     * 外部调用传入参数
     *
     * @param isSelf
     * @return
     */
    public static HomeMeFragment createInstance(int memberId, boolean isSelf) {
        HomeMeFragment fragment = new HomeMeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstant.MEMBER_ID, memberId);
        bundle.putBoolean("isSelf", isSelf);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        Bundle args = getArguments();
        if (args != null) {
            isSelf = args.getBoolean("isSelf");
            otherMemberId = args.getInt(IntentConstant.MEMBER_ID);
        }

        initView(view, container);
        isCreated = true;
        return view;
    }

    private void initView(View view, @Nullable ViewGroup container) {
        //创建head view
        headView = new HomeMeHeadView(getActivity(), this, container, isSelf, otherMemberId);

        //video tab
        myVideoTab = headView.getInfateView().findViewById(R.id.home_me_tab_my_video);
        favorVideoTab = headView.getInfateView().findViewById(R.id.home_me_tab_favor_video);
        myVideoTab.setOnClickListener(this);
        favorVideoTab.setOnClickListener(this);

        //初始化视频列表
        videoRecycler = view.findViewById(R.id.home_me_video_list);
        initRecycle();

        //定位到我的video
        if (!isSelf) {
            favorVideoTab.setVisibility(View.GONE);
        }
        switchVieoTab(true);

        //refresh
        refreshLayout = view.findViewById(R.id.person_info_refresh);
        initRefresh();
    }

    private void initRecycle() {
        videoAdpter = new MeVideoListAdapter(this);
        videoAdpter.addHead(headView.getInfateView());

        videoRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));
        videoRecycler.setAdapter(videoAdpter);
    }


    private void initRefresh() {
        CustomRefreshViewHolder stickinessRefreshViewHolder =
                new CustomRefreshViewHolder(getContext(), true);

        refreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
        refreshLayout.setDelegate(this);
        refreshLayout.setIsShowLoadingMoreView(true);
        refreshLayout.beginRefreshing();
    }

    @Override
    public void onResume() {
        if (!isCreated) {
            return;
        }
        super.onResume();
        headView.refreshUserInfo();
        if (isSelf) {
            refreshMyVideo(true);
            refreshElseVideo(true);
        } else {
            refreshMyVideo(true);
        }
    }


    private void switchVieoTab(boolean isMyVideo) {
        isMyVideoType = isMyVideo;
        if (isMyVideo) {
            myVideoTab.setSelected(true);
            favorVideoTab.setSelected(false);
            myVideoTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            favorVideoTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            videoAdpter.refreshVideos(myVideoInfos);
        } else {
            myVideoTab.setSelected(false);
            favorVideoTab.setSelected(true);
            myVideoTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            favorVideoTab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            videoAdpter.refreshVideos(elseVideoInfos);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_me_tab_my_video) {
            switchVieoTab(true);
            return;
        }

        if (id == R.id.home_me_tab_favor_video) {
            switchVieoTab(false);
            return;
        }
    }

    @Override
    public void onClick(VideoInfo info) {
        String videoInfo = null;

        try {
            videoInfo = GsonUtil.createGson().toJson(info);
        } catch (Exception e) {
            videoInfo = null;
        }

        if (TextUtils.isEmpty(videoInfo)) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(VideoListActivity.VIDEO_INFO, videoInfo);

        VideoListActivity.forward(getContext(), bundle);
    }


    private void refreshMyVideo(final boolean isRefresh) {
        final int pageNum;

        //load more
        if (!isRefresh) {
            int currentSize = myVideoInfos != null ? myVideoInfos.size() : 0;
            pageNum = currentSize / pageSize;
        } else {
            pageNum = 0;
        }

        ServerApi.getMyVideoList(AccountManager.INSTANCE.getUserToken(), pageNum, pageSize, otherMemberId
                , new AutoLoginOutCallback<SingleVideoListResult>() {
                    @Override
                    public void onResponse(SingleVideoListResult response, int id) {
                        super.onResponse(response, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            List<VideoInfo> list = response.getData();
                            if (list != null && list.size() > 0) {
                                int currentSize = myVideoInfos.size();
                                int min = pageNum * pageSize;
                                for (int i = currentSize - 1; i >= min; i--) {
                                    myVideoInfos.remove(i);
                                }

                                myVideoInfos.addAll(list);
                                if (isMyVideoType) {
                                    videoAdpter.refreshVideos(myVideoInfos);
                                }

                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }


                    }
                });
    }


    private void refreshElseVideo(final boolean isRefresh) {
        final int pageNum;

        //load more
        if (!isRefresh) {
            int currentSize = elseVideoInfos != null ? elseVideoInfos.size() : 0;
            pageNum = currentSize / pageSize;
        } else {
            pageNum = 0;
        }

        ServerApi.getFavorVideoList(AccountManager.INSTANCE.getUserToken(), pageNum, pageSize
                , new AutoLoginOutCallback<SingleVideoListResult>() {
                    @Override
                    public void onResponse(SingleVideoListResult response, int id) {
                        super.onResponse(response, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            List<VideoInfo> list = response.getData();
                            if (list != null && list.size() > 0) {
                                int currentSize = elseVideoInfos.size();
                                int min = pageNum * pageSize;
                                for (int i = currentSize - 1; i >= min; i--) {
                                    elseVideoInfos.remove(i);
                                }

                                elseVideoInfos.addAll(list);
                                if (!isMyVideoType) {
                                    videoAdpter.refreshVideos(elseVideoInfos);
                                }

                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }


                    }
                });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (isSelf) {
            refreshMyVideo(true);
            refreshElseVideo(true);
        } else {
            refreshMyVideo(true);
        }

        headView.refreshUserInfo();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (isSelf) {
            if (isMyVideoType) {
                refreshMyVideo(false);
            } else {
                refreshElseVideo(false);
            }
        } else {
            refreshMyVideo(false);
        }
        return true;
    }


    //供head使用
    @Override
    public void onRefresh() {
        refreshLayout.beginRefreshing();
    }

    @Override
    public void onEndRefresh() {
        refreshLayout.endRefreshing();
    }

}
