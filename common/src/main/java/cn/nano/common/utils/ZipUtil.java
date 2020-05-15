package cn.nano.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件解压缩的工具类
 * Created by sxl4287 on 16/3/3.
 * Email : sxl4287@arcsoft.com
 */
public class ZipUtil {
    private static String TAG = ZipUtil.class.getSimpleName();
    private final static int BUFFER_SIZE = 1024;

    /**
     * zip fileFolder include all files，and save to dstZipFilePath
     *
     * @param srcDirPath     需要进行压缩的源文件夹
     * @param dstZipFilePath 目标zip文件名称，SD 绝对路径（全路径）
     * @throws Exception
     */
    public static void zipFolder(String srcDirPath, String dstZipFilePath)
            throws Exception {

        // 创建Zip包
        java.util.zip.ZipOutputStream outZip = new java.util.zip.ZipOutputStream(
                new FileOutputStream(dstZipFilePath));

        // 打开要输出的文件
        File file = new File(srcDirPath);

        // 压缩
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);

        // 完成,关闭
        outZip.finish();
        outZip.close();

    }

    /**
     * 压缩文件夹下面所有文件，文件夹；
     *
     * @param folderPath 文件夹路径
     * @param filePath   文件名称
     * @param zipOut     ZipOutputStream 输出流
     * @throws Exception
     */
    private static void zipFiles(String folderPath, String filePath,
                                 java.util.zip.ZipOutputStream zipOut)
            throws Exception {
        if (zipOut == null) {
            return;
        }
        File file = new File(folderPath + filePath);

        // 判断是不是文件

        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            int len;
            byte[] buffer = new byte[BUFFER_SIZE];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }
            inputStream.close();
            zipOut.closeEntry();

        } else {

            // 文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();
            // 如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(
                        filePath + File.separator);
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }

            // 如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderPath, filePath + File.separator + fileList[i], zipOut);
            }

        }

    }


    /**
     * 进行Zip解压
     *
     * @param inputStream 需要解压的流
     * @return 解压后的输出流
     * @throws IOException 异常
     */
    public static InputStream unZip(InputStream inputStream) throws IOException {
        return new GZIPInputStream(inputStream);
    }

    public static boolean unZip(String zipFilePath, String targetDir) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(zipFilePath);
            boolean b = unZip2Folder(is, targetDir, true);
            return b;
        } catch (Exception cwj) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            // Log.e("unZip", "open zip error");
            return false;
        }
    }

    /**
     * 解压缩流,到某一个指定的目录文件夹中
     *
     * @param inputZipStream 输入压缩数据流
     * @param outFolderPath  输出路径
     * @return 解压是否成功
     */
    private static boolean unZip2Folder(InputStream inputZipStream, String outFolderPath, boolean
            overwriter) {
        boolean isUnzipOk = true;
        ZipInputStream inZip = null;
        ZipEntry zipEntry;
        String name;

        try {
            inZip = new ZipInputStream(inputZipStream);

            while ((zipEntry = inZip.getNextEntry()) != null) {
                name = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    name = name.substring(0, name.length() - 1);
                    File folder = new File(outFolderPath + File.separator + name);

                    folder.mkdirs();
                } else {
                    File file = new File(outFolderPath + File.separator + name);

                    if (file.getParentFile() != null) {
                        if (!file.exists()) {
                            file.getParentFile().mkdirs();
                        } else if (!overwriter) {
                            isUnzipOk = true;
                            continue;
                        }
                    }

                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        isUnzipOk = false;
                        LogUtil.logE(TAG, "createNewFile failed : " + e.getMessage());
                        return isUnzipOk;
                    }

                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((len = inZip.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
        } catch (IOException e) {
            LogUtil.logE(TAG, e.getMessage());
            isUnzipOk = false;
        }

        try {
            if (null != inZip)
                inZip.close();
        } catch (IOException e) {
            isUnzipOk = false;
            LogUtil.logE(TAG, e.getMessage());
        }
        return isUnzipOk;
    }

    /**
     * 解压Zip格式的文件，存到指定文件夹下面
     *
     * @param srcZipPath   解压文件的全路径
     * @param dstFolerPath 需要保存的文件夹目录
     * @return 解压是否成功
     */
    public static boolean unZip2Folder(String srcZipPath, String dstFolerPath, boolean overwriter) {
        InputStream stream = FileUtil.readFile2Stream(srcZipPath);
        if (null == stream) {
            return false;
        }
        return unZip2Folder(stream, dstFolerPath, overwriter);
    }

    /**
     * 解压Package
     *
     * @param zipFile         zip的文件路径，全路径
     * @param targetDir       zip 解压之后，需要存放的dir目录;
     * @param jsonDirPath     如果zip中包含有.json 文件，则将此.json文件迁移到此目录下,文件夹名称已style BADGE_ID;
     * @param isSpecialPrefix 如果是Mirror.zip 则用于保存zip加压出来的文件夹名称必须与zip名称的前缀相容
     *                        如:s60401.zip 保存的文件夹名称为S60401.....
     * @return
     */
    public static boolean unzipPackage(String zipFile, String targetDir, String jsonDirPath, boolean isSpecialPrefix) {

        boolean isUnZipOk = true;
        int BUFFER_SIZE = 4096;

        //先创建解压zip内容之后的文件夹
        FileUtil.mkDirs(targetDir);

        FileInputStream fis = null;
        ZipInputStream zis = null;
        try {
            ZipEntry entry;
            fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            byte data[] = new byte[BUFFER_SIZE];

            while ((entry = zis.getNextEntry()) != null) {

                try {
                    if (!entry.isDirectory()) {
                        String entryName = entry.getName();
                        String fileName = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.length());
                        String styleIndex;
                        if (isSpecialPrefix) {
                            //   .../static/Mirroraphone/HotstyleData/s50002.zip styleIndex = s50002
                            styleIndex = zipFile.substring(zipFile.lastIndexOf("/") + 1, zipFile.lastIndexOf("."));
                        } else {
                            styleIndex = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.lastIndexOf("."));
                            if (fileName.endsWith("json")) {
                                if (!FileUtil.isExistFile(jsonDirPath)) {
                                    FileUtil.mkDirs(jsonDirPath);
                                }
                            }
                            if (fileName.contains("_l")) {
                                styleIndex = entryName.substring(entryName.lastIndexOf("/") + 1, entryName.lastIndexOf("_"));
                            }
                        }

                        //将解压出来的文件，重新保存到一个目录下，文件夹名称与文件名相关
                        String saveFileDir = targetDir + styleIndex + "/";
                        FileUtil.mkDirs(saveFileDir);

                        String fileDir;
                        if (isSpecialPrefix) {
                            fileDir = saveFileDir;
                            if (fileName.endsWith("mba")) {
                                fileName = styleIndex + ".mba";
                            } else if (fileName.endsWith("txt")) {
                                fileName = styleIndex + ".txt";
                            } else if (fileName.endsWith("jpg")) {
                                fileName = styleIndex + ".jpg";
                            }
                        } else {
                            if (fileName.endsWith("json")) {
                                fileDir = jsonDirPath;
                            } else {
                                fileDir = saveFileDir;
                            }
                        }


                        File entryFile = new File(fileDir + fileName);
                        FileOutputStream fos = new FileOutputStream(entryFile);

                        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);

                        int count;
                        while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                            dest.write(data, 0, count);
                        }

                        dest.flush();
                        dest.close();
                    }
                } catch (FileNotFoundException ex) {
                    // Log.e("unzip", "zip output error");
                    isUnZipOk = false;
                }
            }
        } catch (Exception ex3) {
            // Log.e("unzip", "open zip error");
            isUnZipOk = false;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    isUnZipOk = false;
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    isUnZipOk = false;
                }
            }
        }
        return isUnZipOk;
    }
}
