package cn.nano.common.regionselector.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import cn.nano.common.R;
import cn.nano.common.utils.LocationUtils;
import cn.nano.common.utils.ToastUtil;

public class RegionHeadViewHold extends RecyclerView.ViewHolder {

    private RegionItemClickListener clickListener;

    private TextView locationName;

    private Context context;

    //状态机
    private int location_state = STATE_NONE;
    private static final int STATE_NONE = 0;//定位中
    private static final int STATE_SUCCESS = 1;//成功
    private static final int STATE_FAIL = 2;//失败

    public RegionHeadViewHold(@NonNull View itemView, RegionItemClickListener listener) {
        super(itemView);
        context = itemView.getContext();
        clickListener = listener;
        initView(itemView);

        //开始获取location
        getLocation();
    }

    private void initView(View itemView) {
        locationName = itemView.findViewById(R.id.head_location);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //还在定位
                if (location_state == STATE_NONE) {
                    return;
                }

                //失败
                if (location_state == STATE_FAIL) {
                    getLocation();
                    return;
                }

                //成功
                if (clickListener != null) {
                    clickListener.onHeadClick(locationName.getText().toString());
                }
            }
        });
    }


    //地理位置权限
    private boolean hasPermission() {
        int perm = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }


    private void getLocation() {
        switchStateNone();

        //没权限
        if (!hasPermission()) {
            ToastUtil.show("定位权限关闭，无法获取地理位置");
            switchStateFail();
            return;
        }

        //开始获取
        LocationUtils.getInstance().getLocations(context, new LocationUtils.LocationListener() {
            @Override
            public void onSuccess(final String location) {
                locationName.post(new Runnable() {
                    @Override
                    public void run() {
                        switchStateSuccess(location);
                    }
                });
            }

            @Override
            public void onfail() {
                locationName.post(new Runnable() {
                    @Override
                    public void run() {
                        switchStateFail();
                    }
                });
            }
        });
    }

    public void rquestLocation() {
        getLocation();
    }


    private void switchStateNone() {
        location_state = STATE_NONE;
        locationName.setText(R.string.get_location_process);
    }

    private void switchStateFail() {
        location_state = STATE_FAIL;
        locationName.setText(R.string.get_location_unknown);
    }


    private void switchStateSuccess(String location) {
        location_state = STATE_SUCCESS;
        locationName.setText(location);
    }


}
