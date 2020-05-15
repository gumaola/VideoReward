package cn.nano.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.TypedValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class ImageUtil {
    /**
     * @param filePath
     * @return
     */
    public static boolean isSmallImage(String filePath) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        if (opts.outHeight <= 200 || opts.outWidth <= 200) {
            return true;
        }
        return false;
    }

    /**
     * drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable, int width, int height) {
        if (drawable instanceof BitmapDrawable) {//转换成Bitmap
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {//.9图片转换成Bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    drawable.getOpacity() != PixelFormat.OPAQUE ?
                            Config.ARGB_8888 : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * 从指定目录下获取RGB_565的bitmap对象,以此减少内存占用
     *
     * @param path 文件名
     * @return
     */
    public static Bitmap createBitmap(String path) {

        Options opts = new Options();

        opts.inJustDecodeBounds = false;
        opts.inDither = false;
        opts.inPreferredConfig = Config.RGB_565;

        return BitmapFactory.decodeFile(path, opts);
    }

    /**
     * 从指定目录下获取RGB_565的bitmap对象,以此减少内存占用
     *
     * @param fileName
     * @return
     */
    public static Bitmap createAssetsBitmap(Context contxt, String fileName) {
        InputStream stream = null;
        try {
            Options opts = new Options();

            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Config.RGB_565;
            stream = FileUtil.getAssetsStream(contxt, fileName);
            return BitmapFactory.decodeStream(stream, null, opts);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 从指定目录下获取ARGB888的bitmap对象，用于算法处理
     *
     * @param path 文件名
     * @return
     */
    public static Bitmap createBitmapARGB(String path) {
        if (path == null || !FileUtil.isExistFile(path)) {
            return null;
        }
        Options opts = new Options();

        opts.inJustDecodeBounds = false;
        opts.inDither = false;
        opts.inPreferredConfig = Config.ARGB_8888;
        Bitmap temp = null;
        temp = BitmapFactory.decodeFile(path, opts);
        if (temp == null) {
            return null;
        }
        if (temp.isMutable() && temp.getConfig() == Config.ARGB_8888) {
            // Log.e(TAG, "createBitmapARGB ok~~~>");
            return temp;
        } else {
            // Log.e(TAG, "temp.isMutable() isFalse");
        }

        Bitmap temp2 = temp.copy(Config.ARGB_8888, true);
        temp2.getWidth(); // 保证C层的访问
        temp2.getHeight();
        if (temp != null) {
            if (!temp.isRecycled()) {
                temp.recycle();
                temp = null;
            }
        }
        temp = temp2;
        return temp;
    }

    /**
     * 判断文件是否为
     *
     * @param path
     * @return
     */
    public static boolean isBitmap(String path) {
        Options opts = getBitmapOptions(path);
        if (-1 == opts.outWidth || -1 == opts.outHeight || 0 == opts.outWidth
                || 0 == opts.outHeight)
            return false;
        else
            return true;
    }

    /**
     * 判断文件是否为图像文件
     *
     * @param path
     * @return
     */
    public static Options getBitmapOptions(String path) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        return opts;
    }

    /**
     * 判断文件是否为
     *
     * @param path
     * @return
     */
    public static boolean testBitmap(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        Options opts = getBitmapOptions(path);
        if (-1 == opts.outWidth || -1 == opts.outHeight || 0 == opts.outWidth
                || 0 == opts.outHeight)
            return false;
        else
            return true;

    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final Bitmap.CompressFormat format,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(format, 90, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap createFitinBitmap(String path, int dstWidth, int dstHeight) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        try {
            int MAX_IMAGE_LENGTH = Math.max(dstWidth, dstHeight);

            BitmapFactory.decodeFile(path, opts);

            int sampleSize1 = opts.outWidth / MAX_IMAGE_LENGTH;
            int sampleSize2 = opts.outHeight / MAX_IMAGE_LENGTH;
            opts.inSampleSize = sampleSize1 > sampleSize2 ? sampleSize1
                    : sampleSize2;
            opts.inSampleSize = (opts.inSampleSize + 1) / 2 * 2;
            // opts.inSampleSize = (opts.inSampleSize) / 2 * 2;

            opts.inJustDecodeBounds = false;
            opts.inDither = false;
            opts.inPreferredConfig = Config.ARGB_8888;

            // int oldwidth = opts.outWidth;
            // int oldheight = opts.outHeight;
            Bitmap temp0 = BitmapFactory.decodeFile(path, opts);
            if (temp0 == null)
                return null;

            Matrix m = new Matrix();

            int degree = getExifOrientation(path);
            if (degree != 0) {
                m.postRotate(degree);
            }

            float sample1 = dstWidth / ((float) opts.outWidth);
            float sample2 = dstHeight / ((float) opts.outHeight);
            float sample = sample1 < sample2 ? sample1 : sample2;

            Bitmap temp;
            if (sample < 1.0) {
                m.postScale(sample, sample);
                temp = Bitmap.createBitmap(temp0, 0, 0, temp0.getWidth(),
                        temp0.getHeight(), m, true);
                temp0.recycle();
            } else {
                temp = temp0;
            }
            if (temp.isMutable() && temp.getConfig() == Config.ARGB_8888) {
                return temp;
            } else {
                // Log.e(TAG, "temp.isMutable() isFalse");
            }

            Bitmap temp2 = temp.copy(Config.ARGB_8888, true);

            temp2.getWidth(); // 保证C层的访问
            temp2.getHeight();

            temp.recycle();
            temp = null;

            return temp2;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * @param resources
     * @param id
     * @return
     */
    public static Bitmap decodeResource(Resources resources, int id) {
        Bitmap resBitmap = null;
        try {
            TypedValue value = new TypedValue();
            resources.openRawResource(id, value);
            Options opts = new Options();
            opts.inTargetDensity = value.density;
            opts.inPreferredConfig = Config.RGB_565;
            resBitmap = BitmapFactory.decodeResource(resources, id, opts);
        } catch (Exception e) {
            return null;
        }

        return resBitmap;
    }

    /**
     * @param resources
     * @param id
     * @return
     */
    public static Bitmap decodeResource(Resources resources, int id, int scale) {
        Bitmap resBitmap = null;
        try {
            TypedValue value = new TypedValue();
            resources.openRawResource(id, value);
            Options opts = new Options();
            opts.inSampleSize = scale;
            opts.inTargetDensity = value.density;
            opts.inPreferredConfig = Config.RGB_565;
            resBitmap = BitmapFactory.decodeResource(resources, id, opts);
        } catch (Exception e) {
            return null;
        }

        return resBitmap;
    }

    /**
     * @param resources
     * @param id
     * @return
     */
    public static Bitmap decodeResourceARGB(Resources resources, int id) {
        Bitmap resBitmap = null;
        try {
            TypedValue value = new TypedValue();
            resources.openRawResource(id, value);
            Options opts = new Options();
            opts.inTargetDensity = value.density;
            opts.inPreferredConfig = Config.ARGB_8888;
            resBitmap = BitmapFactory.decodeResource(resources, id, opts);
        } catch (Exception e) {
            return null;
        }

        return resBitmap;
    }

    /**
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public static Bitmap resizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        if (bm == null || bm.isRecycled()) {
            return null;
        }
        final int w = bm.getWidth();
        final int h = bm.getHeight();

        final float sw = ((float) newWidth) / w;
        final float sh = ((float) newHeight) / h;
        float ratio = 0.0f;

        if (sw >= sh) {
            ratio = sh;
        } else {
            ratio = sw;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bm, 0, 0, w, h, matrix, true);
    }

    /**
     * @param bitmap
     * @param name
     * @return
     */
    public static boolean saveBmp(Bitmap bitmap, String name) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        try {
            File pf = new File(name);
            if (!pf.exists())
                pf.createNewFile();
            else
                pf.delete();
            FileOutputStream stream;
            stream = new FileOutputStream(pf);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();
            return true;
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // return false;

    }

    /**
     * @param context
     * @param bitmap
     * @param name
     * @return
     */
    public static boolean saveLocalBmp(Context context, Bitmap bitmap,
                                       String name) {
        boolean bRet = false;
        if (bitmap == null || context == null) {
            return bRet;
        }
        try {
            context.deleteFile(name);
            FileOutputStream stream = context.openFileOutput(name,
                    Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();
            bRet = true;
        } catch (Exception exp) {
            bRet = false;
        }
        return bRet;
    }

    /**
     * 获取图像文件的exif 信息；
     *
     * @param filepath
     * @return
     */
    public static int getExifOrientation(String filepath) {
        if (TextUtils.isEmpty(filepath))
            return 0;
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }
}
