package cn.nano.common.thread;

import android.os.Handler;
import android.os.HandlerThread;

public class WorkThread extends HandlerThread {
    private Handler workHandler;

    public WorkThread(String name) {
        super(name);
    }

    @Override
    public synchronized void start() {
        super.start();
        workHandler = new Handler(getLooper());
    }

    public void post(Runnable runnable) {
        if (workHandler != null) {
            workHandler.post(runnable);
        }
    }

    public void release() {
        workHandler = null;
        quit();
    }
}
