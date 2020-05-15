package cn.nano.getui;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

public class GetuiSDK {

    /**
     * 最好在app创建和main activity创建各调用一次，重复调用无影响
     *
     * @param context
     */
    public static void initSDK(Context context) {
        PushManager.getInstance().initialize(context);

        if (context instanceof Application) {
            PushManager.getInstance().setDebugLogger(context, new IUserLoggerInterface() {
                @Override
                public void log(String s) {
                    Log.e("GetuiSDK", s);
                }
            });
        }
    }
}
