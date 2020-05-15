package cn.nano.common.utils;
/**
 * Created by cl2567 on 2016/10/31.
 */

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

public class ScanUtil {

    private static final String TAG = ScanUtil.class.getSimpleName();

    private MediaScannerConnection mConn = null;
    private SannerClient mClient = null;
    private File mFile = null;
    private String mMimeType = null;
    private MediaScannerConnection.MediaScannerConnectionClient mScanListener;

    public ScanUtil(Context context) {
        if (mClient == null) {
            mClient = new SannerClient();
        }
        if (mConn == null) {
            mConn = new MediaScannerConnection(context, mClient);
        }
    }

    class SannerClient implements
            MediaScannerConnection.MediaScannerConnectionClient {

        public void onMediaScannerConnected() {

            if (mFile == null) {
                return;
            }
            scan(mFile, mMimeType);
        }

        public void onScanCompleted(String path, Uri uri) {
            mConn.disconnect();
            if (mScanListener != null) {
                mScanListener.onScanCompleted(path, uri);
            }
        }

        private void scan(File file, String type) {
            LogUtil.logE(TAG, "scan " + file.getAbsolutePath());
            if (file.isFile()) {
                mConn.scanFile(file.getAbsolutePath(), null);
                return;
            }
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : file.listFiles()) {
                scan(f, type);
            }
        }
    }

    public void scanFile(File file, String mimeType, MediaScannerConnection
            .MediaScannerConnectionClient l) {
        mFile = file;
        mMimeType = mimeType;
        mScanListener = l;
        mConn.connect();
    }

    public static void scanFile(String[] paths, Context context, MediaScannerConnection
            .OnScanCompletedListener l) {
        MediaScannerConnection.scanFile(context, paths, null, l);
    }

}
