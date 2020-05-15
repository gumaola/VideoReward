package cn.nano.common.regionselector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cn.nano.common.R;
import cn.nano.common.app.CommonActivity;
import cn.nano.common.regionselector.adapter.RegionAdapter;
import cn.nano.common.regionselector.adapter.RegionItemClickListener;
import cn.nano.common.regionselector.bean.AreaListBean;
import cn.nano.common.regionselector.bean.CityListBean;
import cn.nano.common.regionselector.bean.ProvinceListBean;

public class RegionActivity extends CommonActivity implements RegionItemClickListener {

    //请求code和返回结果key
    public static final int REQUEST_REGION_CODE = 0x2000;
    public static final String KEY_REGION_RESULT = "area_result";

    public static final String KEY_REGION_CODE = "area_code";

    private RecyclerView regionRecycler;
    private RegionAdapter adapter;
    private TextView titleV;

    //记录当前选中结果
    private String currentItemName;

    //统一跳转
    public static void forward(Activity activity, String code) {
        Intent intent = new Intent(activity, RegionActivity.class);
        intent.putExtra(KEY_REGION_CODE, code);
        activity.startActivityForResult(intent, REQUEST_REGION_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selector);
        initView();
        initData();
    }

    private void initData() {
        Intent i = getIntent();
        String code = i.getStringExtra(KEY_REGION_CODE);


        ProvinceListBean province = RegionDataModel.getModel().getProvice(code);
        if (province != null) {
            titleV.setText(province.getName());
            //直辖市跳过city筛选
            if (province.isDirectCity()) {
                List<AreaListBean> list = RegionDataModel.getModel().getCity(province.getCode()).getAreaList();
                adapter.addData(list);
            } else {
                List<CityListBean> list = province.getCityList();
                adapter.addData(list);
            }
        } else {
            CityListBean city = RegionDataModel.getModel().getCity(code);
            List<AreaListBean> list = city.getAreaList();
            titleV.setText(city.getName());
            adapter.addData(list);
        }

    }

    private void initView() {
        titleV = findViewById(R.id.address_selector_title);
        regionRecycler = findViewById(R.id.address_recycler);

        adapter = new RegionAdapter(false, this);
        regionRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false));
        regionRecycler.setAdapter(adapter);
    }

    public void goback(View view) {
        finish();
    }

    //成功告诉上一页面结果
    private void returnResult(String result) {
        Intent i = new Intent();
        i.putExtra(KEY_REGION_RESULT, result);

        setResult(RESULT_OK, i);
        finish();
    }

    //接收下一页面返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGION_CODE && resultCode == RESULT_OK) {
            String childArea = data.getStringExtra(KEY_REGION_RESULT);
            returnResult(currentItemName + "," + childArea);
        }
    }

    @Override
    public void onHeadClick(String address) {
        //todo nothing
    }

    @Override
    public void onItemClick(AreaListBean bean) {
        currentItemName = bean.getName();

        //有子区域则跳转下一页面
        if (bean.isHasChildArea()) {
            RegionActivity.forward(this, bean.getCode());
            return;
        }

        //没有则将选中结果返回
        returnResult(currentItemName);
    }
}
