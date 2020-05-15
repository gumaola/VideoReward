package cn.nano.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.InputStream;

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    private BitmapUtils() {
    }

    // added by polling
    public static Bitmap getBitmap(InputStream in) {
        Bitmap bm = null;
        if (in != null)
            bm = BitmapFactory.decodeStream(in);
        return bm;
    }

    public static Bitmap getBitmap(byte[] bytes, int width, int heigth) {
        Bitmap bm = null;
        if (bytes != null) {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            int realW = opts.outWidth;
            int realH = opts.outHeight;
            int xScale = realW / width;
            int yScale = realH / heigth;
            int scale = xScale > yScale ? xScale : yScale;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = scale;
            bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        }
        return bm;
    }


    public static Bitmap createScaleBitmap(Context context, int resId, float destW, float destH) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), resId);

        Matrix matrix = new Matrix();
        float scaleW = destW / bmp.getWidth();
        float scaleH = destH / bmp.getHeight();
        matrix.postScale(scaleW, scaleH);

        Bitmap newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        if (!bmp.isRecycled()) {
            bmp.recycle();
        }

        return newbmp;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bm = null;
        if (path != null) {
            bm = BitmapFactory.decodeFile(path);
        }
        return bm;
    }

    public static Bitmap getBitmap(String path, int width, int heigth) {
        Bitmap bm = null;
        if (path != null) {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int realW = opts.outWidth;
            int realH = opts.outHeight;
            int xScale = realW / width;
            int yScale = realH / heigth;
            int scale = xScale < yScale ? xScale : yScale;
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = scale;
            bm = BitmapFactory.decodeFile(path, opts);
        }
        return bm;
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        return rotateAndMirror(b, degrees, false, false);
    }

    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees, boolean needRGBA) {
        return rotateAndMirror(b, degrees, false, needRGBA);
    }

    // Rotates and/or mirrors the bitmap. If a new bitmap is created, the
    // original bitmap is recycled.
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror,
                                         boolean needRgba) {
        if ((degrees != 0 || mirror) && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate((float) b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate((float) b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees="
                            + degrees);
                }
            }

            m.rectStaysRect();
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
                if (android.os.Build.VERSION.SDK_INT <= 10 && needRgba) {
                    if (b.getConfig() == Bitmap.Config.RGB_565) {
                        Bitmap b3 = b.copy(Bitmap.Config.ARGB_8888, true);
                        if (b != b3) {
                            b.recycle();
                            b = b3;
                        }
                    }
                }

            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    private static boolean checkFsWritable() {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
        String directoryName = Environment.getExternalStorageDirectory()
                .toString() + "/DCIM";
        File directory = new File(directoryName);
        if (!directory.isDirectory()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }

    private static boolean hasStorage() {
        return hasStorage(true);
    }

    private static boolean hasStorage(boolean requireWriteAccess) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (requireWriteAccess) {
                boolean writable = checkFsWritable();
                return writable;
            } else {
                return true;
            }
        } else if (!requireWriteAccess
                && Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static final int NO_STORAGE_ERROR = -1;
    public static final int CANNOT_STAT_ERROR = -2;

    public static int getAvailableSpace() {
        try {
            if (!hasStorage()) {
                return NO_STORAGE_ERROR;
            } else {
                String storageDirectory = Environment
                        .getExternalStorageDirectory().toString();
                StatFs stat = new StatFs(storageDirectory);
                final int PICTURE_BYTES = 1500000;
                float remaining = ((float) stat.getAvailableBlocks() * (float) stat
                        .getBlockSize()) / PICTURE_BYTES;
                return (int) remaining;
            }
        } catch (Exception ex) {
            // if we can't stat the filesystem then we don't know how many
            // pictures are remaining. it might be zero but just leave it
            // blank since we really don't know.
            Log.d(TAG, "Fail to access sdcard" + ex.getMessage());
            return CANNOT_STAT_ERROR;
        }
    }
}
