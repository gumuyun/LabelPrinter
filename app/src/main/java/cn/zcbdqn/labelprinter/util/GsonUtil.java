package cn.zcbdqn.labelprinter.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by gumuyun on 2018/9/15.
 */

public class GsonUtil {
    public static Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public static String object2Json(Object obj){
        return gson.toJson(obj);
    }
    public static <T> T json2Object(String json,Class<T> clazz){
       return gson.fromJson(json,clazz);
    }
    public static <T> T json2Object(String json,Type type){
       return gson.fromJson(json,type);
    }
}
