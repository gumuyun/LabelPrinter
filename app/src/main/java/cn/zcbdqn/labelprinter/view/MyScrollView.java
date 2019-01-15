package cn.zcbdqn.labelprinter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by gumuyun on 2019/1/9.
 */

public class MyScrollView extends ScrollView {
    public static boolean flag=true;
    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(flag){
            return super.onTouchEvent(ev);
        }else{
            return false;
        }
    }
}
