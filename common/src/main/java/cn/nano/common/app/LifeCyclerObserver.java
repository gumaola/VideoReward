package cn.nano.common.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LifeCyclerObserver implements Application.ActivityLifecycleCallbacks {

    private List<Activity> activityStack = new ArrayList<>();

    private static LifeCyclerObserver observer;

    private LifeCyclerObserver() {
    }

    public static LifeCyclerObserver getObserver() {
        if (observer == null) {
            synchronized (LifeCyclerObserver.class) {
                if (observer == null) {
                    observer = new LifeCyclerObserver();
                }
            }
        }
        return observer;
    }


    public Activity getCurrentActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.get(activityStack.size() - 1);
        }

        return null;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activityStack.add(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activityStack.remove(activity);
    }
}
