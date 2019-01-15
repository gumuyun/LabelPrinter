package cn.zcbdqn.labelprinter.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by gumuyun on 2019/1/5.
 */

public class GraphicUtil {

    public static int getGraphicWidth(Context context){
        //1、通过WindowManager获取
        // DisplayMetrics dm = new DisplayMetrics();
        // heigth = dm.heightPixels;
        // width = dm.widthPixels;
        // 2、通过Resources获取
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        // heigth = dm.heightPixels;
        int width = dm.widthPixels;
        // 3、获取屏幕的默认分辨率
        // Display display = getWindowManager().getDefaultDisplay();
        // heigth = display.getWidth();
        // width = display.getHeight();
        // 4、通过类直接取
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
       return width;
    }
    public static int getGraphicHeight(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static double getDrawingScale(Context context,double px){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;//6----5
        return width/px;
    }

    /**
     * 相素转换
     * @param unit
     * @param value
     * @param metrics
     * @return
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics){
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f/72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                //mm*metrics.xdpi * (1.0f/25.4f);
                return value * metrics.xdpi * (1.0f/25.4f);

        }
        return 0;
    }

    public static float px2mm(float value, DisplayMetrics metrics){
        Log.e("gumy","metrics.xdpi:"+metrics.xdpi);
        return value / (metrics.xdpi * (1.0f/25.4f));
    }
}
