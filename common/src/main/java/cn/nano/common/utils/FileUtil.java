package cn.nano.common.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class FileUtil {
    private static Map<String, List<String>> mAssetsList = new HashMap<>();

    public static byte[] loadStyle(Context context, String filename) {
        if (TextUtils.isEmpty(filename))
            return null;
        BufferedSource buffer = null;
        File file = new File(filename);
        try {
            if (file.exists()) {
                buffer = Okio.buffer(Okio.source(new FileInputStream(filename)));
                return buffer.readByteArray();
            } else {
                buffer = Okio.buffer(Okio.source(context.getAssets().open(filename)));
                return buffer.readByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 读文件
     */
    public static String readFile2String(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) return null;
        BufferedSource bufferedSource = null;
        try {
            InputStream steam = new FileInputStream(file);
            bufferedSource = Okio.buffer(Okio.source(steam));
            return bufferedSource.readByteString().utf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSource != null) {
                try {
                    bufferedSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


    /**
     * 读文件
     */
    public static InputStream readFile2Stream(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        File file = new File(filePath);
        FileInputStream inStream = null;
        if (file.exists()) {
            try {
                inStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return inStream;
        } else {
            return null;
        }
    }

    /**
     * 删除文件
     *
     * @param dirPath  文件夹
     * @param fileName 文件名称
     */
    public static void deleteFile(String dirPath, String fileName) {
        if (dirPath == null || fileName == null) {
            return;
        }
        File file = new File(dirPath, fileName);
        deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param filePath 需要删除文件的全路径
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        deleteFile(file);
    }

    /**
     * 删除文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                if (file.isDirectory()) {
                    File[] childFile = file.listFiles();
                    if ((childFile != null) && (childFile.length != 0)) {
                        for (File f : childFile) {
                            deleteFile(f);
                        }
                    }
                    file.delete();
                }
            }
        }
    }

    /**
     * 文件重命名
     */
    public static boolean renameFile(String oldFileName, String newFile)
            throws IOException {

        File fromFile = new File(oldFileName);
        if (!fromFile.exists())
            return false;
        File toFile = new File(newFile);
        fromFile.renameTo(toFile);
        return true;
    }

    /**
     * 判断文件是否存在SD卡上
     */
    public static boolean isExistFile(String filePath) {
        return !TextUtils.isEmpty(filePath) && isExistFile(new File(filePath));
    }

    /**
     * 判断文件是否存在SD卡上
     */
    public static boolean isExistFile(File srcFile) {
        return srcFile.exists();
    }

    public static boolean isExistAssetsFile(Context context, String fileName, String assetsPath) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }

        if (assetsPath == null) {
            assetsPath = "";
        }

        try {
            List<String> fileLists = mAssetsList.get(assetsPath);
            LogUtil.logD("DIYwei", "Time mAssetsList:fileName-" + fileName);
            if (fileLists == null || fileLists.size() <= 0) {
                LogUtil.logD("DIYwei", "Time mAssetsList:assetsPath-" + assetsPath);
                fileLists = Arrays.asList(context.getResources().getAssets().list(assetsPath));
                if (fileLists != null && fileLists.size() > 0) {
                    mAssetsList.put(assetsPath, fileLists);
                }
            }

            if (fileLists != null && fileLists.contains(fileName)) {
                return true;
            }
        } catch (IOException e) {

        }

        return false;
    }

    public static boolean isExistFile(Context context, String filePath, String fileName, String assetsPath) {
        if (isExistAssetsFile(context, fileName, assetsPath)) {
            return true;
        } else
            return !TextUtils.isEmpty(filePath) && isExistFile(new File(filePath));
    }

    /**
     * 删除目录下的所有文件和目录
     */
    public static void deleteDir(String dirPath) {
        File f = new File(dirPath);// 定义文件路径
        if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
            File[] files = f.listFiles();
            if (files == null) {
                return;
            }
            if (files.length == 0) {// 若目录下没有文件则直接删除
                f.delete();
            } else {// 若有则把文件放进数组，并判断是否有下级目录
                File delFile[] = f.listFiles();
                int i = f.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (delFile[j].isDirectory()) {
                        deleteDir(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
                    } else
                        delFile[j].delete();
                }
                f.delete();
            }
        }
    }

    /**
     * 加载
     */
    public static String[] getAllFiles(String dirPath) {
        File srcFile = new File(dirPath);
        if (!srcFile.exists()) {
            return null;
        }
        if (!srcFile.isDirectory()) {
            return null;
        }
        return srcFile.list();

    }


    /**
     * 读取 /data/data/files/ 目录下文件
     *
     * @param context 上下文对象
     * @param name    文件名
     * @return File 对象
     */
    public static File getLocalFile(Context context, String name) {
        File resultFile = null;
        try {
            resultFile = context.getFileStreamPath(name);
        } catch (Exception e) {
            return null;
        }

        return resultFile;

    }

    public static boolean copyFileTo(String source, String dest) {
        if (TextUtils.isEmpty(source) || TextUtils.isEmpty(dest)) return false;
        File sourceFile = new File(source);
        if (!sourceFile.exists()) return false;
        File destFile = new File(dest);
        destFile.getParentFile().mkdirs();
        try {
            FileChannel inChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel outChannel = new FileOutputStream(destFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 保存字符串到指定的文件名
     *
     * @param filePath  全路径
     * @param resString 需要保存的文件名称
     */
    public static void saveStr2File(String filePath, String resString) {
        if (TextUtils.isEmpty(resString))
            return;

        File resfile = new File(filePath);
        if (resfile.exists()) {
            resfile.delete();
        }
        // 判断文件的目录是否存在
        String parent = resfile.getParent();
        if (!isExistFile(parent)) {
            if (!mkDirs(parent)) {
                return;
            }
        }
        BufferedSink buffer = null;
        try {
            buffer = Okio.buffer(Okio.sink(resfile));
            buffer.write(resString.getBytes());
            buffer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class SaveStringRunnable implements Runnable {
        private String filePath, content;

        SaveStringRunnable(String filePath, String content) {
            this.filePath = filePath;
            this.content = content;
        }

        @Override
        public void run() {
            try {
                saveStr2File(filePath, content);
            } catch (Exception e) {
                LogUtil.logD("writeFileException", "write " + content + " into " + filePath);
                e.printStackTrace();
            }
        }
    }


    public static String getFileNameWithoutSuffix(String fileName) {

        if (!TextUtils.isEmpty(fileName)) {
            int pos = fileName.lastIndexOf(".");
            if (pos > 0) {
                return fileName.substring(0, pos);
            }
        }

        return fileName;
    }


    /**
     * 去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 递归删除文件夹下面所有的文件
     */
    public static void recursionDeleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f.getAbsolutePath());
            }
            file.delete();
        }
    }

    /**
     * 创建一级目录 （"/sdcard/audio/")：
     */
    public static void mkDir(String filePath) {
        File srcDir = new File(filePath);
        if (srcDir.exists()) {
            return;
        }
        srcDir.mkdir();
    }

    /**
     * 创建多级目录（"/sdcard/media/audio/")：
     */
    public static boolean mkDirs(String strFolder) {
        if (TextUtils.isEmpty(strFolder)) {
            return false;
        }
        File file = new File(strFolder);
        return file.exists() || file.mkdirs();
    }

    /**
     * 新建文件
     */
    public static boolean createFile(String path, String fileName) {
        boolean hasFile = false;
        try {
            File dir = new File(path);
            boolean hasDir = dir.exists() || dir.mkdirs();
            if (hasDir) {
                File file = new File(dir, fileName);
                hasFile = file.exists() || file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hasFile;
    }


    /**
     * 保存bitmap对象到指定文件
     */
    public static File saveBitmap2File(String fileName, Bitmap srcBitmap)
            throws IOException {
        if (srcBitmap == null) {
            return null;
        }
        File resfile = new File(fileName);
        if (resfile.exists()) {
            resfile.delete();
        }
        resfile.createNewFile();
        FileOutputStream outSteam = new FileOutputStream(resfile);
        srcBitmap.compress(CompressFormat.JPEG, 80, outSteam);
        outSteam.close();
        outSteam = null;
        return resfile;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

        // DocumentProvider
        if (isKitKat && FileUtil.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = FileUtil.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = FileUtil.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = FileUtil.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            } else if (isDriveDocument(uri)) {
                // Log.e("King", "isDriveDocument");
                // final String docId = FileUtil.getDocumentId(uri);
                // Cursor cursor = context.getContentResolver().query(uri, null,
                // null, null, null);
                // cursor.moveToFirst();
                // // Link to the image
                // final String imageFilePath = cursor.getString(1);
                //
                // Log.e("King", docId);
                // return imageFilePath;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DriveProvider.
     */
    private static boolean isDriveDocument(Uri uri) {
        return "com.google.android.apps.docs.storage"
                .equals(uri.getAuthority());
    }

    private static final String PATH_DOCUMENT = "document";

    /**
     * Intent action used to identify {@link DocumentsProvider} instances. This
     * is used in the {@code <intent-filter>} of a {@code <provider>}.
     */
    public static final String PROVIDER_INTERFACE = "android.content.action.DOCUMENTS_PROVIDER";

    /**
     * Test if the given URI represents a {@link Document} backed by a
     * {@link DocumentsProvider}.
     */
    private static boolean isDocumentUri(Context context, Uri uri) {
        final List<String> paths = uri.getPathSegments();
        if (paths.size() < 2) {
            return false;
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            return false;
        }

        // final Intent intent = new Intent(PROVIDER_INTERFACE);
        // final List<ResolveInfo> infos = context.getPackageManager()
        // .queryIntentContentProviders(intent, 0);
        // for (ResolveInfo info : infos) {
        // if (uri.getAuthority().equals(info.providerInfo.authority)) {
        // return true;
        // }
        // }
        return true;
    }

    /**
     * Extract the  from the given URI.
     */
    private static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        return paths.get(1);
    }


    /**
     * 从assets 目录下读取文件，从原来的eclipse方法，到现在studio ，目录结构不一样导致；但是老的读取方法也是正确的；
     */
    public static String getAssetsString(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }

        BufferedSource bufferedSource = null;
        try {
            InputStream steam = context.getAssets().open(name);
            bufferedSource = Okio.buffer(Okio.source(steam));
            return bufferedSource.readByteString().utf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSource != null) {
                try {
                    bufferedSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
/*        if (TextUtils.isEmpty(name)) {
            return "";
        }
        String content = null;
        InputStream steam = null;
        try {
            steam = getAssetsStream(context, name);
            if (steam == null) {
                return "";
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(steam));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line).append("\r\n");
            }
            content = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (steam != null) {
                try {
                    steam.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;*/
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(File destfile) {
        long length = 0;
        if (destfile.isDirectory()) {
            try {
                for (File file : destfile.listFiles()) {
                    length += getFileSize(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            length = destfile.length();
        }

        return length;
    }


    public static InputStream getAssetsStream(Context context, String name) {
        InputStream steam = null;
        try {
//            steam = context.getClass().getClassLoader().getResourceAsStream("assets/" + name);
            steam = context.getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return steam;
    }


    public static String readAsset2String(@NonNull Context context, String name) {

        if (TextUtils.isEmpty(name)) {
            return null;
        }

        BufferedSource bufferedSource = null;
        try {
            InputStream is = context.getAssets().open(name);
            bufferedSource = Okio.buffer(Okio.source(is));
            return bufferedSource.readByteString().utf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSource != null) {
                try {
                    bufferedSource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 得到图片字节流 数组大小
     */
    public static byte[] readStream2Bytes(InputStream inStream)
            throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 保存字符串到指定的文件名
     */
    public static void saveLog2File(String fileName, String resString)
            throws IOException {

        File resfile = new File(fileName);
        // 判断文件的目录是否存在
        String parent = resfile.getParent();
        if (!isExistFile(parent)) {
            if (!mkDirs(parent)) {
                return;
            }
        }
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true)));
            out.write(resString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建多级目录（"/sdcard/media/audio/")：
     */
    public static void logCat2File(String tag, String resString, String fileName) {
        String timeStr = TimeUtil.getTimeStr("MM/dd HH:mm:ss:SSS");
        String logRes = "time : " + timeStr + " & tag : " + tag
                + " & logValue :" + resString + "\r\n";
        try {
            FileUtil.saveLog2File(fileName, logRes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存KeyPoint到指定的文件名
     */
    public static void saveKeyPoint2File(String filePath, float[] keypoint)
            throws IOException {
        if (null == keypoint) {
            return;
        }
        int nLen = keypoint.length;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < nLen - 1; i++) {
            stringBuffer.append((int) keypoint[i]).append(',');
        }
        stringBuffer.append((int) keypoint[nLen - 1]);
        String resString = stringBuffer.toString();

        if (TextUtils.isEmpty(resString))
            return;

        File resfile = new File(filePath);
        if (resfile.exists()) {
            resfile.delete();
        }
        // 判断文件的目录是否存在
        String parent = resfile.getParent();
        if (!isExistFile(parent)) {
            if (!mkDirs(parent)) {
                return;
            }
        }
        saveStr2File(filePath, resString);
/*        resfile.createNewFile();
        try {
            FileOutputStream fis = new FileOutputStream(resfile);
            byte[] buf = resString.getBytes();
            fis.write(buf, 0, buf.length);
            fis.flush();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 获取文件内的key point点位
     */
    public static int[] getKeyPointFromFile(String filePath) {
        String text = readFile2String(filePath);
        int[] keyPoints = null;
        if (!TextUtils.isEmpty(text)) {
            text = text.replaceAll("\n", "");
            text = text.replaceAll(" ", "");
            String[] keypointStrList = text.split(",");
            if (keypointStrList.length > 0) {
                keyPoints = new int[keypointStrList.length];
                try {
                    for (int i = 0; i < keypointStrList.length; i++) {
                        keyPoints[i] = Integer.parseInt(keypointStrList[i]);
                    }
                } catch (Exception exp) {
                    keyPoints = null;
                }
            }
        }
        return keyPoints;
    }


    public static void saveFaceRect2File(String filePath, Rect faceRect) throws IOException {
        if (faceRect == null) {
            return;
        }
        String resString = faceRect.left + "," + faceRect.top + "," + faceRect.right + "," + faceRect.bottom;
        saveStr2File(filePath, resString);
    }

    /**
     * 获取文件内的face rect点位
     */
    public static Rect getFaceRectFromFile(String filePath) {
        String text = readFile2String(filePath);
        Rect faceRect = new Rect();
        if (!TextUtils.isEmpty(text)) {
            text = text.replaceAll("\n", "");
            text = text.replaceAll(" ", "");

            String[] list = text.split(",");
            if (list.length == 4) {
                faceRect.left = Integer.parseInt(list[0], 10);
                faceRect.top = Integer.parseInt(list[1], 10);
                faceRect.right = Integer.parseInt(list[2], 10);
                faceRect.bottom = Integer.parseInt(list[3], 10);
            }
        }
        return faceRect;
    }

    /**
     * 获取文件内的key point点位
     *
     * @param filePath 全路径
     */
    public static int[] getKeyPointToolFromFile(String filePath) {
        String text = readFile2String(filePath);
        int[] keyPoints = null;
        if (!TextUtils.isEmpty(text)) {
            text = text.replaceAll("\n", "");
            text = text.replaceAll(" ", "");
            text = text.replace("KeyPoint={", "");
            text = text.replace(",}", "");
            String[] keypointStrList = text.split(",");
            if (keypointStrList.length > 0) {
                keyPoints = new int[keypointStrList.length];
                for (int i = 0; i < keypointStrList.length; i++) {
                    keyPoints[i] = Integer.parseInt(keypointStrList[i]);
                }
            }
        }
        return keyPoints;
    }

    public static Rect getFaceRectToolFromFile(String filePath) {
        String text = readFile2String(filePath);
        Rect faceRect = new Rect();
        if (!TextUtils.isEmpty(text)) {
            text = text.replaceAll("\n", "");
            text = text.replaceAll(" ", "");
            text = text.replace("FaceRect={", "");
            text = text.replace("}", "");


            String[] list = text.split(",");
            if (list.length == 4) {
                faceRect.left = Integer.parseInt(list[0], 10);
                faceRect.top = Integer.parseInt(list[1], 10);
                faceRect.right = Integer.parseInt(list[2], 10);
                faceRect.bottom = Integer.parseInt(list[3], 10);
            }
        }
        return faceRect;
    }

    /**
     * @param fileDir
     * @param fileName
     * @param tag
     * @param msg
     */
    public static void logFileWithTimeStamp(String fileDir, String fileName, String tag, String msg) {

        if (!LogUtil.isDebug()) {
            return;
        }

        LogUtil.logD(tag, msg);

        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());// 用于格式化日期,作为日志文件名的一部分
        String time = format.format(new Date());

        String finalName = fileName + "-" + time + ".log";

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {

                File dir = new File(fileDir);
                mkDir(fileDir);
                if (!dir.exists())
                    dir.mkdir();
                FileOutputStream fos = new FileOutputStream(new File(fileDir,
                        finalName), true);
                format = new SimpleDateFormat(
                        "yyyy-MM-dd-HH-mm-ss", Locale.getDefault());// 用于格式化日期,作为日志文件名的一部分
                time = format.format(new Date());
                msg = time + ":" + msg + "\n";
                fos.write(msg.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载文件 包括jpg图片，以及模板相关的ini.cng,还有png图片
     *
     * @param url      服务器地址
     * @param filePath 文件夹路径
     * @param fileName 下载文件名
     * @return 返回是否下载成功，
     */
    public static boolean downLoadFile(final String url, final String filePath,
                                       final String fileName) {
        boolean isDownloadOk = true;
        // Log.e("sxl", "downLoadImage<----------------");
        // Log.e("sxl", "url = " + url + ",filePath=" + filePath + ",fileName="
        // + fileName);
        if (TextUtils.isEmpty(url)) {
            return false;
        }
//        if (!Environment.MEDIA_MOUNTED.equalsIgnoreCase(MakeupApp.sdCardState)
//                || MakeupApp.sdCardRootDir == null
//                || MakeupApp.sdCardRootDir == "") {
//            return false;
//        }
        String imageName = url.substring(url.lastIndexOf("/") + 1);
        if (!TextUtils.isEmpty(fileName)) {
            imageName = fileName;
        }
        String imagePath = filePath + "/" + imageName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl
                    .openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(imagePath);
            final int buffer_size = 1024;
            try {
                byte[] bytes = new byte[buffer_size];
                for (; ; ) {
                    int count = is.read(bytes, 0, buffer_size);
                    if (count == -1)
                        break;
                    os.write(bytes, 0, count);
                }
            } catch (Exception ex) {
                // Log.e(TAG, "downLoadFile error 1-------> " +
                // ex.getMessage());
                isDownloadOk = false;
                ex.printStackTrace();
            }
            os.close();
            is.close();
            is = null;
            os = null;
            imageUrl = null;
            conn.disconnect();
            conn = null;
            file = null;
        } catch (Exception ex) {
            // Log.e(TAG, "downLoadFile error 2-------> " + ex.getMessage());
            ex.printStackTrace();
            isDownloadOk = false;
        }
        return isDownloadOk;
        // Log.e("sxl", "downLoadImage---------------->");
    }

    /**
     * 移动文件
     *
     * @param srcFileName 源文件完整路径
     * @param destDirName 目的目录完整路径
     * @param newName     新文件名
     * @return 文件移动成功返回true，否则返回false
     */
    public static boolean moveFile(String srcFileName, String destDirName, String newName) {

        String dstFileName = "";
        File srcFile = new File(srcFileName);
        if (!srcFile.exists() || !srcFile.isFile())
            return false;

        File destDir = new File(destDirName);
        if (!destDir.exists())
            destDir.mkdirs();

        if (newName == null) {
            dstFileName = destDirName + File.separator + srcFile.getName();
        } else {
            dstFileName = destDirName + File.separator + newName;
        }

        return srcFile.renameTo(new File(dstFileName));
    }
}
