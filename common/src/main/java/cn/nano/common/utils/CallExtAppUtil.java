package cn.nano.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by glwang on 2017/12/8.
 */
public class CallExtAppUtil {
    /**
     * 打电话
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean callTo(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent);
                return true;
            }
        }
        return false;

    }

    /**
     * 发邮件
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean mailTo(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (intent.resolveActivity(pm) != null) {
                context.startActivity(intent);
                return true;
            }
        }
        return false;

    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean openBrowser(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if ("http".equalsIgnoreCase(scheme) ||
                "https".equalsIgnoreCase(scheme) ||
                "market".equalsIgnoreCase(scheme)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            if (context != null) {
                PackageManager pm = context.getPackageManager();
                if (intent.resolveActivity(pm) != null) {
                    context.startActivity(intent);
                    return true; // If we return true, onPageStarted, onPageFinished won't be called.
                }
            }
        } else if ("tel".equalsIgnoreCase(scheme)) {
            return CallExtAppUtil.callTo(context, uri);
        } else if ("mailto".equalsIgnoreCase(scheme)) {
            return CallExtAppUtil.mailTo(context, uri);
        }
        return false;
    }
}
