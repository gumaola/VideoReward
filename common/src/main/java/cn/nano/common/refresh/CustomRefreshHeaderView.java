package cn.nano.common.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wang.avi.AVLoadingIndicatorView;

import cn.nano.common.R;

public class CustomRefreshHeaderView extends FrameLayout {
    private AVLoadingIndicatorView mLoading;

    /**
     * 是否正在刷新
     */
    private boolean mIsRefreshing = false;

    public CustomRefreshHeaderView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CustomRefreshHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_refresh_header, this);

        mLoading = findViewById(R.id.refresh_loading_view);
    }


    public void handleScale(float scale, int moveYDistance) {
        scale = 0.3f + 0.7f * scale;
        mLoading.setScaleX(scale);
        mLoading.setScaleY(scale);
    }

    public void startRefresh() {
        mIsRefreshing = true;
//        mLoading.show();
    }


    public void endRefresh() {
        mIsRefreshing = false;
//        mLoading.hide();
    }
}
