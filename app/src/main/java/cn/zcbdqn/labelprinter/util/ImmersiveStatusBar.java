package cn.zcbdqn.labelprinter.util;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

import cn.zcbdqn.labelprinter.context.MyApplication;

/**
 * Created by gumuyun on 2018/10/22.
 */

public class ImmersiveStatusBar {

    private static ImmersiveStatusBar immersiveStatusBar;

    // 构造函数私有化
    private ImmersiveStatusBar() {

    }

    public static ImmersiveStatusBar getInstance() {
        if (immersiveStatusBar == null) {
            // 加锁提高使用效率
            synchronized (ImmersiveStatusBar.class) {
                if (immersiveStatusBar == null) {
                    immersiveStatusBar = new ImmersiveStatusBar();
                }
            }
        } return immersiveStatusBar;
    }

    /***
     * 状态栏透明化
     * @param window    Window对象
     * @param actionBar ActionBar对象
     */ public void Immersive(Window window, ActionBar actionBar) {
         if (Build.VERSION.SDK_INT >= 21) {
             View view = window.getDecorView();
             //  两个FLAG一起使用表示会让应用的主体内容占用系统状态栏的时空间
             int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
             view.setSystemUiVisibility(option);
             // 将状态栏设置成    透明色
             String mainTonality = (String) MyApplication.applicationMap.get("mainTonality");
             window.setStatusBarColor(Color.parseColor(mainTonality));

         }
         // 将ActionBar隐藏
        if (actionBar!=null){
            actionBar.hide();
        }
     }

}