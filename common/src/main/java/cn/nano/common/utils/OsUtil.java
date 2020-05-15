package cn.nano.common.utils;

import android.os.Build;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * OS 系统的版本判断工具类
 * Created by sxl4287 on 16/3/2.
 * Email : sxl4287@arcsoft.com
 */
public class OsUtil {
    /**
     * hasIceCreamSandwich
     *
     * @return true false
     */
    public static boolean hasIceCreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }


    /**
     * 5.0 及以上
     *
     * @return
     */
    public static boolean aboveLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 8.0 及以上
     *
     * @return
     */
    public static boolean aboveO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }


    private static Object[] mArmArchitecture = new Object[3];

    /**
     * [获取cpu类型和架构]
     *
     * @return 三个参数类型的数组，第一个参数标识是不是ARM架构，第二个参数标识是V6还是V7架构，第三个参数标识是不是neon指令集
     */
    public static Object[] getCpuArchitecture() {
//        if ((Integer) mArmArchitecture[1] != -1) {
//            return mArmArchitecture;
//        }
        try {
            InputStream is = new FileInputStream("/proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            try {
                String nameProcessor = "Processor";
                String nameFeatures = "Features";
                String nameModel = "model name";
                String nameCpuFamily = "cpu family";
                while (true) {
                    String line = br.readLine();
                    String[] pair = null;
                    if (line == null) {
                        break;
                    }
                    pair = line.split(":");
                    if (pair.length != 2)
                        continue;
                    String key = pair[0].trim();
                    String val = pair[1].trim();
                    if (key.compareTo(nameProcessor) == 0) {
                        String n = "";
                        for (int i = val.indexOf("ARMv") + 4; i < val.length(); i++) {
                            String temp = val.charAt(i) + "";
                            if (temp.matches("\\d")) {
                                n += temp;
                            } else {
                                break;
                            }
                        }
                        mArmArchitecture[0] = "ARM";
                        mArmArchitecture[1] = Integer.parseInt(n);
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameFeatures) == 0) {
                        if (val.contains("neon")) {
                            mArmArchitecture[2] = "neon";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameModel) == 0) {
                        if (val.contains("Intel")) {
                            mArmArchitecture[0] = "INTEL";
                            mArmArchitecture[2] = "atom";
                        }
                        continue;
                    }

                    if (key.compareToIgnoreCase(nameCpuFamily) == 0) {
                        mArmArchitecture[1] = Integer.parseInt(val);
                        continue;
                    }
                }
            } finally {
                br.close();
                ir.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mArmArchitecture;
    }
}
