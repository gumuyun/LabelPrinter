package cn.zcbdqn.labelprinter.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by gumuyun on 2018/12/25.
 */

public class MyEditText extends android.support.v7.widget.AppCompatEditText {

    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSelection(getText().length());
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public MyEditText(Context context) {
        this(context, null);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //光标首次获取焦点是在最后面，之后操作就是按照点击的位置移动光标
        if (isEnabled() && hasFocus() && hasFocusable()) {
            setSelection(selEnd);
        } else {
            setSelection(getText().length());
        }
    }
}
