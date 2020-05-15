package cn.nano.main.comment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.nano.common.app.CommonActivity;
import cn.nano.common.refresh.CustomRefreshViewHolder;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.comment.adapter.CommentAdapter;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.CommentListResult;
import okhttp3.Call;

public class CommentActivity extends CommonActivity implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    public static final int COMMENT_REQUEST_CODE = 0x3000;
    private static final String COMMENT_VIDEO_ID = "video_id";

    private BGARefreshLayout refreshLayout;
    private RecyclerView commentRecycler;
    private CommentAdapter commentAdapter;

    private TextView commentEmptyTip;

    private TextView commentEditorText;

    //所有comment
    private List<CommentListResult.DataBean> comments = new ArrayList<>();
    private static final int pageSize = 10;

    //video id
    private int commentVideoId;

    //是否在此页面评论
    private boolean isCommentSuccess;

    //统一跳转
    public static void forward(Activity context, int videoId) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(COMMENT_VIDEO_ID, videoId);
        context.startActivityForResult(intent, COMMENT_REQUEST_CODE);
        context.overridePendingTransition(R.anim.anime_pull_in, 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_comment);
        intData();
        initView();
    }

    private void intData() {
        Intent i = getIntent();
        commentVideoId = i.getIntExtra(COMMENT_VIDEO_ID, 0);
    }

    private void initView() {
        findViewById(R.id.comment_exit).setOnClickListener(this);
        refreshLayout = findViewById(R.id.comment_refresh);
        commentRecycler = findViewById(R.id.comment_recycler);
        commentEmptyTip = findViewById(R.id.comment_empty);
        commentEditorText = findViewById(R.id.comment_editor_text);
        commentEditorText.setOnClickListener(this);

        initRecycler();
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

    private void initRecycler() {
        commentAdapter = new CommentAdapter();

        commentRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        commentRecycler.setAdapter(commentAdapter);
    }

    private void refresh(final boolean isRefresh) {
        final int pageNum;

        //load more
        if (!isRefresh) {
            int currentSize = comments != null ? comments.size() : 0;
            pageNum = currentSize / pageSize;
        } else {
            pageNum = 0;
        }

        ServerApi.getCommentList(AccountManager.INSTANCE.getUserToken(), commentVideoId, pageNum, pageSize
                , new AutoLoginOutCallback<CommentListResult>() {
                    @Override
                    public void onResponse(CommentListResult response, int id) {
                        super.onResponse(response, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }

                        if (response.getCode() == BaseResult.SUCCESS_CODE) {
                            List<CommentListResult.DataBean> list = response.getData();
                            if (list != null && list.size() > 0) {
                                int currentSize = comments.size();
                                int min = pageNum * pageSize;
                                for (int i = currentSize - 1; i >= min; i--) {
                                    comments.remove(i);
                                }

                                comments.addAll(list);
                            }
                        }

                        showComments();

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        super.onError(call, e, id);
                        if (isRefresh) {
                            refreshLayout.endRefreshing();
                        } else {
                            refreshLayout.endLoadingMore();
                        }
                        showComments();

                    }
                });
    }

    private void showComments() {
        if (comments != null && comments.size() > 0) {
            commentEmptyTip.setVisibility(View.GONE);
            commentAdapter.refreshList(comments);
        } else {
            commentEmptyTip.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (isCommentSuccess) {
            setResult(RESULT_OK);
        }

        finish();
        overridePendingTransition(0, R.anim.anime_drop_left);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        refresh(true);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        refresh(false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommentEditorActivity.COMMENT_EDITOR_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String text = data.getStringExtra(CommentEditorActivity.COMMENT_EDITOR_RESULT);
            postComment(text);
        }
    }


    private void postComment(String content) {
        showloading();
        ServerApi.postComment(AccountManager.INSTANCE.getUserToken(), commentVideoId, content, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                dissLoading();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    isCommentSuccess = true;
                    ToastUtil.show(getString(R.string.comment_success));
                    refreshLayout.beginRefreshing();
                } else {
                    ToastUtil.show(response.getMsg());
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                dissLoading();
                ToastUtil.show(getString(R.string.common_error));
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.comment_exit) {
            goBack();
            return;
        }

        if (id == R.id.comment_editor_text) {
            CommentEditorActivity.forward(this);
            return;
        }
    }
}
