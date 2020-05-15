package cn.nano.common.refresh;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.nano.common.R;

public class CustomRefreshViewHolder extends BGARefreshViewHolder {

    private CustomRefreshHeaderView mHeaderView;

    public CustomRefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
    }

    @Override
    public View getRefreshHeaderView() {
        if (mRefreshHeaderView == null) {
            mHeaderView = new CustomRefreshHeaderView(mContext);
            mHeaderView.setBackgroundColor(Color.TRANSPARENT);
            if (mRefreshViewBackgroundColorRes != -1) {
                mHeaderView.setBackgroundResource(mRefreshViewBackgroundColorRes);
            }
            if (mRefreshViewBackgroundDrawableRes != -1) {
                mHeaderView.setBackgroundResource(mRefreshViewBackgroundDrawableRes);
            }

            mRefreshHeaderView = mHeaderView;
        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale, int moveYDistance) {
        mHeaderView.handleScale(scale, moveYDistance);
    }

    @Override
    public void changeToIdle() {

    }

    @Override
    public void changeToPullDown() {

    }

    @Override
    public void changeToReleaseRefresh() {

    }

    @Override
    public void changeToRefreshing() {
        mHeaderView.startRefresh();
    }

    @Override
    public void onEndRefreshing() {
        mHeaderView.endRefresh();
    }

    @Override
    public View getLoadMoreFooterView() {
        if (mLoadMoreFooterView == null) {
            mLoadMoreFooterView = View.inflate(mContext, R.layout.layout_recycler_foot, null);
            mLoadMoreFooterView.setBackgroundColor(Color.TRANSPARENT);
        }

        return mLoadMoreFooterView;
    }
}
