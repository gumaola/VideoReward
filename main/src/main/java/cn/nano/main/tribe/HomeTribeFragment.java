package cn.nano.main.tribe;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.nano.common.app.CommonFragment;
import cn.nano.common.glide.ImgLoader;
import cn.nano.common.okhttp.utils.L;
import cn.nano.common.refresh.CustomRefreshViewHolder;
import cn.nano.common.utils.DialogUtil;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.BillingListResult;
import cn.nano.main.server.result.LevelListResult;
import cn.nano.main.tribe.adapter.LevelItemClicker;
import cn.nano.main.tribe.adapter.TribeLevelListAdapter;
import okhttp3.Call;

public class HomeTribeFragment extends CommonFragment implements BGARefreshLayout.BGARefreshLayoutDelegate
        , View.OnClickListener
        , LevelItemClicker {

    private BGARefreshLayout mRefreshRoot;


    private TextView mUncompletedTitle;
    private TextView mCompletedTitle;
    private TextView mUncompletedDafault;
    private TextView mCompletedDafault;
    private LinearLayout mUncompletelist;
    private LinearLayout mCompleteList;

    private Dialog loading;

    //刷新哪个订单
    private boolean isCompleteType;


    //刷新订单页数
    private int currentCompletePageNum;
    private int currentUnCompletePageNum;
    private int currentCompletedPageListSize;//如果page num = 0，有可能第一页还未刷新完
    private int currentUnCompletedPageListSize;//如果page num = 0，有可能第一页还未刷新完
    private static final int pageSize = 10;//每页10条

    //等级相关页面
    private FrameLayout levelPage;
    private FrameLayout leveDetailView;
    private RecyclerView levelList;
    private TribeLevelListAdapter leveAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tribe, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loading = DialogUtil.loadingDialog(getContext(), null);
        mRefreshRoot = view.findViewById(R.id.home_tribe_refresh);
        mUncompletedTitle = view.findViewById(R.id.home_tribe_uncompleted_billing_title);
        mUncompletedDafault = view.findViewById(R.id.home_tribe_uncompleted_billing_default);
        mUncompletelist = view.findViewById(R.id.home_tribe_uncompleted_billing_list);

        mCompletedTitle = view.findViewById(R.id.home_tribe_completed_billing_title);
        mCompletedDafault = view.findViewById(R.id.home_tribe_completed_billing_default);
        mCompleteList = view.findViewById(R.id.home_tribe_completed_billing_list);
        mCompletedTitle.setOnClickListener(this);
        mUncompletedTitle.setOnClickListener(this);
        view.findViewById(R.id.home_tribe_level_root).setOnClickListener(this);

        //等级页面
        levelPage = view.findViewById(R.id.home_tribe_level_page);
        levelList = view.findViewById(R.id.tribe_level_list);
        leveDetailView = view.findViewById(R.id.tribe_level_detail_root);
        view.findViewById(R.id.tribe_level_back).setOnClickListener(this);
        levelPage.setOnClickListener(this);


        initLevelList();

        //默认定位进行中
        switchBillingType(false);

        initRefresh();
    }

    private void initLevelList() {
        leveAdapter = new TribeLevelListAdapter();
        leveAdapter.bindItemClicker(this);
        levelList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,
                false));
        levelList.setAdapter(leveAdapter);
    }

    private void initRefresh() {
        CustomRefreshViewHolder stickinessRefreshViewHolder =
                new CustomRefreshViewHolder(getContext(), true);

        mRefreshRoot.setRefreshViewHolder(stickinessRefreshViewHolder);
        mRefreshRoot.setDelegate(this);
        mRefreshRoot.setIsShowLoadingMoreView(true);
        mRefreshRoot.beginRefreshing();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        loadbillingData(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        loadbillingData(false);
        return true;
    }

    //如果是刷新则是下拉，如果不是则是上拉
    public void loadbillingData(final boolean refresh) {
        int oldPageNum = isCompleteType ? currentCompletePageNum : currentUnCompletePageNum;
        int oldListSize = isCompleteType ? currentCompletedPageListSize : currentUnCompletedPageListSize;
        final boolean isNeedForceRefresh = oldListSize < pageSize;
        final int needLoadPagenum;
        if (!refresh && !isNeedForceRefresh) {
            needLoadPagenum = oldPageNum + 1;
        } else {
            needLoadPagenum = 0;
        }
        ServerApi.getBillingList(AccountManager.INSTANCE.getUserToken(), isCompleteType ? 1 : 0,
                needLoadPagenum, pageSize, new AutoLoginOutCallback<BillingListResult>() {
                    @Override
                    public void onResponse(BillingListResult response, int id) {
                        super.onResponse(response, id);
                        if (refresh) {
                            mRefreshRoot.endRefreshing();
                        } else {
                            mRefreshRoot.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            if (!refresh && !isNeedForceRefresh) {
                                if (isCompleteType) {
                                    currentCompletePageNum++;
                                } else {
                                    currentUnCompletePageNum++;
                                }
                                onMoreData(response.getData());
                            } else {
                                onRefreshData(response.getData());
                            }
                        } else {
                            ToastUtil.show(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        if (refresh) {
                            mRefreshRoot.endRefreshing();
                        } else {
                            mRefreshRoot.endLoadingMore();
                        }

                        ToastUtil.show(R.string.common_error);
                    }
                });
    }

    private void onRefreshData(List<BillingListResult.DataBean> data) {
        LinearLayout root = isCompleteType ? mCompleteList : mUncompletelist;
        TextView empty = isCompleteType ? mCompletedDafault : mUncompletedDafault;

        if (data == null || data.size() <= 0) {
            root.removeAllViews();
            root.addView(empty);
            bindCurrentSize(0);
            return;
        }


        for (BillingListResult.DataBean dataBean : data) {
            root.removeAllViews();
            View view = createBillingView(dataBean);
            root.addView(view);
        }

        bindCurrentSize(root.getChildCount());
    }


    private void onMoreData(List<BillingListResult.DataBean> data) {
        LinearLayout root = isCompleteType ? mCompleteList : mUncompletelist;

        if (data == null || data.size() <= 0) {
            return;
        }


        for (BillingListResult.DataBean dataBean : data) {
            View view = createBillingView(dataBean);
            root.addView(view);
        }

        bindCurrentSize(root.getChildCount());

    }

    private void bindCurrentSize(int currentSize) {
        if (isCompleteType) {
            currentCompletedPageListSize = currentSize;
        } else {
            currentUnCompletedPageListSize = currentSize;
        }
    }

    private View createBillingView(BillingListResult.DataBean dataBean) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_billing_view, isCompleteType ? mCompleteList : mUncompletelist, false);

        TextView title = view.findViewById(R.id.billing_id);
        TextView status = view.findViewById(R.id.billing_status);

        title.setText(String.format(getString(R.string.billing_id), dataBean.getLevo_no()));
        status.setText(isCompleteType ? getString(R.string.billing_status_complete) : getString(R.string.billing_status_uncomplete));

        return view;
    }


    //以下是订单相关切换和请求
    private void switchBillingType(boolean complete) {
        isCompleteType = complete;
        if (complete) {
            mCompletedTitle.setSelected(true);
            mCompleteList.setVisibility(View.VISIBLE);

            mUncompletedTitle.setSelected(false);
            mUncompletelist.setVisibility(View.GONE);
        } else {
            mCompletedTitle.setSelected(false);
            mCompleteList.setVisibility(View.GONE);

            mUncompletedTitle.setSelected(true);
            mUncompletelist.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.home_tribe_completed_billing_title) {
            boolean isNeedRefresh = !isCompleteType;
            switchBillingType(true);
            if (isNeedRefresh) {
                loadbillingData(true);
            }
            return;
        }


        if (id == R.id.home_tribe_uncompleted_billing_title) {
            boolean isNeedRefresh = isCompleteType;
            switchBillingType(false);
            if (isNeedRefresh) {
                loadbillingData(true);
            }
            return;
        }

        if (id == R.id.home_tribe_level_root) {
            showLevelList();
            return;
        }

        if (id == R.id.tribe_level_back) {
            hideLeveList();
            return;
        }

        if (id == R.id.home_tribe_level_page) {
            if (leveDetailView.getVisibility() == View.VISIBLE) {
                leveDetailView.setVisibility(View.GONE);
            }

            if (levelList.getVisibility() == View.GONE) {
                levelList.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showLevelList() {
        loading.show();
        ServerApi.getLevelList(AccountManager.INSTANCE.getUserToken(), new AutoLoginOutCallback<LevelListResult>() {
            @Override
            public void onResponse(LevelListResult response, int id) {
                super.onResponse(response, id);
                loading.dismiss();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    levelPage.setVisibility(View.VISIBLE);
                    leveAdapter.bindList(response.getData().getLevelList());
                } else {
                    ToastUtil.show(response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                loading.dismiss();
                ToastUtil.show(R.string.common_error);
            }
        });

    }

    private void hideLeveList() {
        levelPage.setVisibility(View.GONE);
        levelList.setVisibility(View.VISIBLE);
        leveDetailView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClck(LevelListResult.DataBean.LevelListBean bean, View content) {
        int id = content.getId();

        if (id == R.id.level_redeem) {
            upLevel(bean.getLevel_id());
            return;
        }

        if (id == R.id.level_item_root) {
            levelList.setVisibility(View.GONE);
            leveDetailView.setVisibility(View.VISIBLE);
            inflateDetail(bean);
        }
    }

    private void upLevel(int level_id) {
        loading.show();
        ServerApi.upLevel(AccountManager.INSTANCE.getUserToken(), level_id, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                loading.dismiss();
                hideLeveList();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(R.string.level_up_success);
                    switchBillingType(false);
                    mRefreshRoot.beginRefreshing();
                } else {
                    ToastUtil.show(response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                loading.dismiss();
                hideLeveList();
                ToastUtil.show(R.string.common_error);
            }
        });
    }

    private void inflateDetail(LevelListResult.DataBean.LevelListBean bean) {
        leveDetailView.removeAllViews();
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_level_detail, leveDetailView, false);

        ImageView thumb = root.findViewById(R.id.level_detail_thumb);
        TextView title = root.findViewById(R.id.level_detail_title);
        TextView percent = root.findViewById(R.id.level_detail_percent);
        TextView cost = root.findViewById(R.id.level_detail_cost);
        TextView day = root.findViewById(R.id.level_detail_day);

        title.setText(bean.getLevel_title());
        ImgLoader.displayWithError(getContext(), bean.getLevel_icon(), thumb, R.mipmap.icon_avatar_placeholder);
        percent.setText(String.format(getString(R.string.level_reward_percent), bean.getLevel_profit()));
        cost.setText(String.format(getString(R.string.level_cost), bean.getLevel_coin()));
        day.setText(String.format(getString(R.string.level_reward_day), bean.getProfit_day()));

        leveDetailView.addView(root);
    }
}
