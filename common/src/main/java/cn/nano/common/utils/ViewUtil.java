package cn.nano.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Created by cl2567 on 2016/3/3.
 * Warning: 本工具类的所有view必须有父布局包裹，否则getLayoutParams会产生空指针
 */
public class ViewUtil {

    public final static int DRAWABLELEFT = 1;
    public final static int DRAWABLERIGHT = 2;

    /**
     * 按照宽高值设定view比例参数的基本函数
     *
     * @param view   传入的layout，必须要有父布局包裹
     * @param width  int型的宽度参数
     * @param height int型的高度参数
     */
    public static void setViewParams(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams objectParams = view.getLayoutParams();
            if (objectParams != null) {
                objectParams.width = width;
                objectParams.height = height;
                view.setLayoutParams(objectParams);
            }
        }
    }

    /**
     * 按照宽高占屏幕比例设定view比例参数
     *
     * @param view          传入的layout，必须要有父布局包裹
     * @param widthPercent  float型的占屏幕宽的百分比
     * @param heightPercent float型的占屏幕高的百分比
     */
    public static void setViewParams(View view, float widthPercent, float heightPercent) {
        if (DeviceUtil.getScreenWidth() != -1 && DeviceUtil.getScreenHeight() != -1) {
            int width = (int) widthPercent * DeviceUtil.getScreenWidth();
            int height = (int) heightPercent * DeviceUtil.getScreenHeight();
            setViewParams(view, width, height);
        }
    }

    /**
     * 按照固定值仅设定view的高度参数，宽度默认填满父布局
     *
     * @param view   传入的layout，必须要有父布局包裹
     * @param height int型的高度参数
     */
    public static void setViewHeight(View view, int height) {
        setViewParams(view, WindowManager.LayoutParams.MATCH_PARENT, height);
    }

    /**
     * 按照固定值仅设定view的宽度参数，高度默认填满父布局
     *
     * @param view  传入的layout，必须要有父布局包裹
     * @param width int型的宽度参数
     */
    public static void setViewWidth(View view, int width) {
        setViewParams(view, width, WindowManager.LayoutParams.MATCH_PARENT);
    }

    /**
     * 按照百分比仅设定view的高度参数，宽度默认填满父布局
     *
     * @param view          传入的layout，必须要有父布局包裹
     * @param heightPercent float型的占屏幕高的百分比
     */
    public static void setViewHeight(View view, float heightPercent) {
        if (DeviceUtil.getScreenHeight() != -1) {
            int height = (int) heightPercent * DeviceUtil.getScreenHeight();
            setViewParams(view, WindowManager.LayoutParams.MATCH_PARENT, height);
        }
    }

    /**
     * 按照百分比仅设定view的宽度参数，高度默认填满父布局
     *
     * @param view         传入的layout，必须要有父布局包裹
     * @param widthPercent float型的占屏幕宽的百分比
     */
    public static void setViewWidth(View view, float widthPercent) {
        if (DeviceUtil.getScreenWidth() != -1) {
            int width = (int) widthPercent * DeviceUtil.getScreenWidth();
            setViewParams(view, width, WindowManager.LayoutParams.MATCH_PARENT);
        }
    }

    public static boolean setVisibity(View view, int viewState) {
        if (view == null) {
            return false;
        }

        if (viewState == View.VISIBLE && view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
            return true;
        } else if (viewState == View.GONE && view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
            return true;
        } else if (viewState == View.INVISIBLE && view.getVisibility() != View.INVISIBLE) {
            view.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    public static boolean setSelected(View view, boolean isSelect) {

        if (view == null) {
            return false;
        }

        if (view.isSelected() && !isSelect) {
            view.setSelected(isSelect);
            return true;
        } else if (!view.isSelected() && isSelect) {
            view.setSelected(isSelect);
            return true;
        }
        return false;
    }

    public static boolean setEnable(View view, boolean isEnable) {

        if (view == null) {
            return false;
        }

        if (view.isEnabled() && !isEnable) {
            view.setEnabled(isEnable);
            return true;
        } else if (!view.isEnabled() && isEnable) {
            view.setEnabled(isEnable);
            return true;
        }
        return false;
    }

    public static void setTextViewDrawable(final Context context, TextView textView,
                                           int resourceId, int drawableDirection) {
        Drawable drawable = context.getResources().getDrawable(resourceId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        Drawable[] drawables = textView.getCompoundDrawables();
        if (drawableDirection == DRAWABLELEFT) {
            if (drawables != null) {
                textView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
            } else {
                textView.setCompoundDrawables(drawable, null, null, null);
            }
        } else if (drawableDirection == DRAWABLERIGHT) {
            if (drawables != null) {
                textView.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
            } else {
                textView.setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    public static void setTextViewLeftPic(final Context context, TextView textView,
                                          String text, int resourceId) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                int id = Integer.parseInt(source);
                Drawable drawable = context.getResources().getDrawable(id);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                return drawable;
            }
        };
        CharSequence charSequence = Html.fromHtml("<img src='" + resourceId + "'/>", imageGetter, null);
        textView.setText(charSequence);
        textView.append(" " + text);
    }

}
