package cn.nano.main.person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.nano.common.app.CommonActivity;
import cn.nano.common.refresh.CustomRefreshViewHolder;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.comment.adapter.CommentAdapter;
import cn.nano.main.person.adapter.MemberListAdapter;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.FollowListResult;
import cn.nano.main.server.result.MemberSimpleResult;
import cn.nano.main.server.result.MyInvitersResult;
import cn.nano.main.server.result.VideoInfo;
import okhttp3.Call;

public class MemberListActivity extends CommonActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {


    private static final String member_type = "type";
    public static final String fans = "fans";
    public static final String followers = "followers";
    public static final String inviters = "inviters";

    private String currentType;


    private BGARefreshLayout refreshLayout;
    private RecyclerView memberRecycler;
    private MemberListAdapter memberListAdapter;


    private List<MemberSimpleResult> members;
    private static final int pageSize = 10;//每次取10条


    //统一跳转
    public static void forward(Context context, String type) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(member_type, type);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        Intent i = getIntent();
        currentType = i.getStringExtra(member_type);

        initView();
    }


    private void initView() {
        refreshLayout = findViewById(R.id.member_list_refresh);
        memberRecycler = findViewById(R.id.member_list_recycler);


        memberListAdapter = new MemberListAdapter(currentType);

        memberRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        memberRecycler.setAdapter(memberListAdapter);

        TextView view = findViewById(R.id.member_list_title);
        if (inviters.equalsIgnoreCase(currentType)) {
            view.setText(R.string.my_invite);
        } else if (followers.equalsIgnoreCase(currentType)) {
            view.setText(R.string.my_follow);
        } else {
            view.setText(R.string.my_fans);
        }

        initRefresh();

    }


    private void initRefresh() {
        CustomRefreshViewHolder stickinessRefreshViewHolder =
                new CustomRefreshViewHolder(this, true);

        refreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
        refreshLayout.setDelegate(this);
        refreshLayout.setIsShowLoadingMoreView(true);
        refreshLayout.beginRefreshing();
    }

    private void requestData(boolean isRefresh) {

        if (inviters.equalsIgnoreCase(currentType)) {
            requestInviteData(isRefresh);
        } else if (followers.equalsIgnoreCase(currentType)) {
            requestFansOrFollowData(isRefresh, false);
        } else {
            requestFansOrFollowData(isRefresh, true);
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        requestData(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        requestData(false);
        return true;
    }

    private void requestFansOrFollowData(final boolean isRefresh, boolean isFans) {
        final int pageNum;

        //load more
        if (!isRefresh) {
            int currentSize = members != null ? members.size() : 0;
            pageNum = currentSize / pageSize;
        } else {
            pageNum = 0;
        }

        ServerApi.getMyFollowsOrFans(AccountManager.INSTANCE.getUserToken(), isFans ? 2 : 1, pageNum, pageSize
                , new AutoLoginOutCallback<FollowListResult>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }
                    }


                    @Override
                    public void onResponse(FollowListResult response, int id) {
                        super.onResponse(response, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            List<MemberSimpleResult> list = response.getData().getFollowList();
                            notifyListChanged(list, pageNum);
                        }
                    }
                });

    }

    private void notifyListChanged(List<MemberSimpleResult> list, int pageNum) {
        if (members == null) {
            members = new ArrayList<>();
        }
        if (list != null && list.size() > 0) {
            int currentSize = members.size();
            int min = pageNum * pageSize;
            for (int i = currentSize - 1; i >= min; i--) {
                members.remove(i);
            }

            members.addAll(list);
            memberListAdapter.bindDatas(members);
        }
    }


    private void requestInviteData(final boolean isRefresh) {
        final int pageNum;

        //load more
        if (!isRefresh) {
            int currentSize = members != null ? members.size() : 0;
            pageNum = currentSize / pageSize;
        } else {
            pageNum = 0;
        }

        ServerApi.getMyInvites(AccountManager.INSTANCE.getUserToken(), pageNum, pageSize
                , new AutoLoginOutCallback<MyInvitersResult>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }
                    }


                    @Override
                    public void onResponse(MyInvitersResult response, int id) {
                        super.onResponse(response, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            List<MemberSimpleResult> list = response.getData();
                            notifyListChanged(list, pageNum);
                        }
                    }
                });

    }


    public void goback(View v) {
        finish();
    }
}
