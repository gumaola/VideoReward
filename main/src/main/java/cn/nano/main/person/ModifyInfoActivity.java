package cn.nano.main.person;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.main.R;

public class ModifyInfoActivity extends CommonActivity implements View.OnClickListener {
    private static final String BUNDLE_KEY = "extra";

    public static final String MODIFY_TYPE = "modify_type";

    public static final String RESULT_DATA = "result";

    private String modifyType;


    private TextView modifyNameV;
    private TextView modifyDescV;

    //统一跳转
    public static void forward(Activity activity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, ModifyInfoActivity.class);
        intent.putExtra(BUNDLE_KEY, bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        Bundle bundle = getIntent().getBundleExtra(BUNDLE_KEY);
        if (bundle != null) {
            modifyType = bundle.getString(MODIFY_TYPE);
        }

        initView();
    }

    private void initView() {
        findViewById(R.id.modify_back).setOnClickListener(this);
        findViewById(R.id.modify_save).setOnClickListener(this);

        View modifyNameRoot = findViewById(R.id.modify_name_root);
        View modifyDescRoot = findViewById(R.id.modify_desc_root);

        if ("name".equalsIgnoreCase(modifyType)) {
            modifyNameRoot.setVisibility(View.VISIBLE);
            modifyDescRoot.setVisibility(View.GONE);
        } else if ("desc".equalsIgnoreCase(modifyType)) {
            modifyDescRoot.setVisibility(View.VISIBLE);
            modifyNameRoot.setVisibility(View.GONE);
        }

        modifyNameV = findViewById(R.id.modify_name_editor);
        modifyDescV = findViewById(R.id.modify_desc_editor);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.modify_back) {
            finish();
            return;
        }

        if (id == R.id.modify_save) {
            String result = "";
            if ("name".equalsIgnoreCase(modifyType)) {
                result = modifyNameV.getText().toString();
            } else if ("desc".equalsIgnoreCase(modifyType)) {
                result = modifyDescV.getText().toString();
            }


            Intent i = new Intent();
            i.putExtra(MODIFY_TYPE, modifyType);
            i.putExtra(RESULT_DATA, result);

            setResult(RESULT_OK, i);
            finish();
        }

    }
}
