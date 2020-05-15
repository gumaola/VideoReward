package cn.nano.common.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import cn.nano.common.R;
import cn.nano.common.app.GlobalConfig;

public class ToastUtil {

    private static Toast sToast;
    private static long sLastTime;
    private static String sLastString;

    static {
        sToast = makeToast();
    }

    private static Toast makeToast() {
        Toast toast = new Toast(GlobalConfig.getInstance().currentApp());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(GlobalConfig.getInstance().currentApp()).inflate(R.layout.view_toast, null);
        toast.setView(view);
        return toast;
    }


    public static void show(@StringRes int res) {
        show(GlobalConfig.getInstance().currentApp().getString(res));
    }

    public static void show(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        long curTime = System.currentTimeMillis();
        if (curTime - sLastTime > 2000) {
            sLastTime = curTime;
            sLastString = s;
            sToast.setText(s);
            sToast.show();
        } else {
            if (!s.equals(sLastString)) {
                sLastTime = curTime;
                sLastString = s;
                sToast = makeToast();
                sToast.setText(s);
                sToast.show();
            }
        }

    }

}
