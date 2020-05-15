package cn.nano.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by sxl4287 on 16/3/2.
 * Email : sxl4287@arcsoft.com
 */
public class ByteUtil {
    /**
     * byte[] 转为 对象
     *
     * @param bytes
     * @return
     */
    public static Object byteToObject(byte[] bytes) throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } finally {
            if (ois != null) ois.close();
        }
    }

    /**
     * 对象 转为 byte[]
     *
     * @param obj
     * @return
     */
    public static byte[] objectToByte(Object obj) throws Exception {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } finally {
            if (oos != null) oos.close();
        }
    }
}
