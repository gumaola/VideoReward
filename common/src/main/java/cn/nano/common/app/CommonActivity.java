package cn.nano.common.app;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import cn.nano.common.R;
import cn.nano.common.utils.DialogUtil;
import cn.nano.common.utils.ToastUtil;

public class CommonActivity extends AppCompatActivity {

    private Dialog dialog;

    //申请权限相关
    private String checkPermission;
    private String[] needPermission;
    private int requestPermissonCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = DialogUtil.loadingDialog(this, null);
    }

    protected void showloading() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    protected void dissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissLoading();
    }

    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);

            Rect r = new Rect();
            v.getLocationOnScreen(l);

            v.getGlobalVisibleRect(r);

            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        try {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                // 如果开启
                imm.showSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘
     */
    protected void hideInputKeyBroad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        try {
            if (imm.isActive() && getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                // 如果开启
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //设置页面需要申请的权限
    protected void requestPermission(String checkPermission, String[] needPermission, int requestCode) {
        this.checkPermission = checkPermission;
        this.needPermission = needPermission;
        this.requestPermissonCode = requestCode;

        if (ContextCompat.checkSelfPermission(this, checkPermission) == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, needPermission, requestCode);
        }
    }

    protected void onPermissionGranted() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissonCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted();
            } else {
                ToastUtil.show(R.string.request_permission_error);
            }
        }
    }
}
