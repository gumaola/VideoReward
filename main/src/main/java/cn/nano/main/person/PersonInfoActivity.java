package cn.nano.main.person;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import cn.nano.common.app.CommonActivity;
import cn.nano.main.R;
import cn.nano.main.constant.IntentConstant;

public class PersonInfoActivity extends CommonActivity {
    private static final String PERSON_INFO_RXTRA = "person_extra";


    //统一跳转
    public static void forward(Context context, Bundle bundle) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(PERSON_INFO_RXTRA, bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        Bundle bundle = getIntent().getBundleExtra(PERSON_INFO_RXTRA);
        int memberId = 0;
        if (bundle != null) {
            memberId = bundle.getInt(IntentConstant.MEMBER_ID);
        }

        HomeMeFragment fragment = HomeMeFragment.createInstance(memberId, false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.person_info_root, fragment)
                .commit();
    }
}
