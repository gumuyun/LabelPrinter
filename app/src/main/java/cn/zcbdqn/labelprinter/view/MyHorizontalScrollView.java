package cn.zcbdqn.labelprinter.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by gumuyun on 2019/1/9.
 */

public class MyHorizontalScrollView extends HorizontalScrollView {
    public static boolean flag= true;
    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
