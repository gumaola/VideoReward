package cn.nano.main.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.common.glide.ImgLoader;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.constant.IntentConstant;

public class InviteActivity extends CommonActivity implements View.OnClickListener {
    private static final String SETTING_INFO_RXTRA = "extra";


    //统一跳转
    public static void forward(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InviteActivity.class);
        intent.putExtra(SETTING_INFO_RXTRA, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        findViewById(R.id.setting_back).setOnClickListener(this);
        findViewById(R.id.setting_share).setOnClickListener(this);

        initData();
    }

    private void initData() {
        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra(SETTING_INFO_RXTRA);
        if (bundle != null) {
            String url = bundle.getString(IntentConstant.URL);
            ImageView view = findViewById(R.id.invite_image);

            ImgLoader.display(this, url, view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.setting_back) {
            finish();
            return;
        }


        if (id == R.id.setting_share) {
            //todo
            ToastUtil.show("分享功能尚未开启");
        }
    }
}
