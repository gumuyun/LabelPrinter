package cn.zcbdqn.labelprinter.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.zcbdqn.labelprinter.context.MyApplication;

/**
 * Created by gumuyun on 2018/10/22.
 */

public class SharedPreferencesUtil {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor ed;
    private final static String name = "SharedPreferences";// 表名
    static {
        sp = MyApplication.getInstance().getApplicationContext()
                .getSharedPreferences(name, Context.MODE_PRIVATE);
        ed = sp.edit();
    }

    private SharedPreferencesUtil() {
        super();
    }

    /**
     * @Description: 添加boolean
     * @author gumy
     * @create 2018-10-20 下午4:49:07
     * @updateTime 2013-8-20 下午4:49:07
     * @param key
     * @param value
     * @return 添加成功返回true，否则false
     */
    public static boolean putBoolean(String key, boolean value) {
        try {
            ed.putBoolean(key, value);
            ed.apply();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @Description: TODO
     * @author gumy
     * @create 2018-10-20 下午4:49:07
     * @updateTime 2013-8-20 下午4:49:07
     * @param key
     * @param value
     * @return
     */
    public static boolean getBoolean(String key, boolean value) {
        return sp.getBoolean(key, value);
    }

    public static boolean putFloat(String key, float value) {
        try {
            ed.putFloat(key, value);
            ed.apply();

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    public static float getFloat(String key, float value) {
        return sp.getFloat(key, value);
    }

    public static boolean putInt(String key, int value) {
        try {

            ed.putInt(key, value);
            ed.apply();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    public static int getInt(String key, int value) {
        return sp.getInt(key, value);

    }

    public static boolean putLong(String key, Long value) {
        try {
            ed.putFloat(key, value);
            ed.apply();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    public static long getLong(String key, Long value) {
        return sp.getLong(key, value);

    }

    public static boolean putString(String key, String value) {
        try {
            ed.putString(key, value);
            ed.apply();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;

    }

    /**
     *
     * @param key
     * @param value 默认
     * @return
     */
    public static String getString(String key, String value) {
        return sp.getString(key, value);

    }

    public static void removeShare(String key) {
        ed.remove(key);
        ed.apply();
    }
}
