package cn.nano.common.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by wjj3771 on 2017/9/13.
 */

public class StateListUtil {

    public static ColorStateList getColorStateList(int normalColor,
                                                   int selectColor,
                                                   int pressColor) {
        int[] colors = new int[]{selectColor, pressColor, normalColor};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static ColorStateList getColorStateList(Context context,
                                                   int normalColor,
                                                   int selectColor,
                                                   int pressColor) {
        int[] colors = new int[]{context.getResources().getColor(selectColor),
                context.getResources().getColor(pressColor),
                context.getResources().getColor(normalColor)};
        int[][] states = new int[3][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static StateListDrawable getStateListDrawable(Context context,
                                                         int normalRes,
                                                         int selectRes,
                                                         int pressRes) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normalDrawable = normalRes == -1 ? null : context.getResources().getDrawable(normalRes);
        Drawable selectDrawable = selectRes == -1 ? null : context.getResources().getDrawable(selectRes);
        Drawable pressedDrawable = pressRes == -1 ? null : context.getResources().getDrawable(pressRes);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, selectDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
}
