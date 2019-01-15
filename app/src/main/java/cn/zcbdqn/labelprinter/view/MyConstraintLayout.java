package cn.zcbdqn.labelprinter.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import java.io.Serializable;

/**
 * Created by gumuyun on 2018/12/2.
 */

public class MyConstraintLayout extends ConstraintLayout implements Serializable {
    public MyConstraintLayout(Context context) {
        super(context);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



}
