package cn.nano.common.regionselector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.common.R;
import cn.nano.common.app.CommonActivity;
import cn.nano.common.regionselector.adapter.RegionAdapter;
import cn.nano.common.regionselector.adapter.RegionItemClickListener;
import cn.nano.common.regionselector.bean.AreaListBean;
import cn.nano.common.utils.LocationUtils;
import cn.nano.common.utils.ToastUtil;

public class AddressSelectorActivity extends CommonActivity implements RegionItemClickListener {

    private RecyclerView addressRecycler;
    private RegionAdapter addressAdapter;


    //请求code和返回结果key
    public static final int REQUEST_ADDRESS_CODE = 0x1000;
    public static final String KEY_ADDRESS_RESULT = "address_result";

    private String currentItemName;

    //统一跳转
    public static void forward(Activity activity) {
        Intent intent = new Intent(activity, AddressSelectorActivity.class);
        activity.startActivityForResult(intent, REQUEST_ADDRESS_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selector);
        addressRecycler = findViewById(R.id.address_recycler);

        initView();
        initData();

        //请求地理位置权限
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
    }

    @Override
    protected void onPermissionGranted() {
        addressAdapter.requestLocation();
    }

    private void initView() {
        addressAdapter = new RegionAdapter(true, this);
        addressRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        addressRecycler.setAdapter(addressAdapter);
    }

    private void initData() {
        showloading();
        RegionDataModel.getModel().installData(this, new RegionDataModel.RegionDataLoadListener() {
            @Override
            public void onLoadSuccess() {
                addressRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        dissLoading();
                        notifyDataSetChange();
                    }
                });
            }

            @Override
            public void onLoadFail() {
                addressRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        dissLoading();
                        ToastUtil.show("数据加载失败！");
                    }
                });

                addressRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        });
    }

    private void notifyDataSetChange() {
        addressAdapter.addData(RegionDataModel.getModel().getRegions());
    }


    public void goback(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RegionDataModel.getModel().release();
        LocationUtils.getInstance().release();
    }

    //成功告诉上一页面结果
    private void returnResult(String result) {
        Intent i = new Intent();
        i.putExtra(KEY_ADDRESS_RESULT, result);

        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegionActivity.REQUEST_REGION_CODE && resultCode == RESULT_OK) {
            String childArea = data.getStringExtra(RegionActivity.KEY_REGION_RESULT);
            returnResult(currentItemName + "," + childArea);
        }
    }

    //点击获取
    @Override
    public void onHeadClick(String address) {
        returnResult(address);
    }

    @Override
    public void onItemClick(AreaListBean area) {
        currentItemName = area.getName();

        //有子区域则跳转下一页面
        if (area.isHasChildArea()) {
            RegionActivity.forward(this, area.getCode());
            return;
        }

        //没有则将选中结果返回
        returnResult(currentItemName);
    }
}
