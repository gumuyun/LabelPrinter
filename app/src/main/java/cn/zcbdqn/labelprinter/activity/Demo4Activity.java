package cn.zcbdqn.labelprinter.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import cn.zcbdqn.labelprinter.R;

public class Demo4Activity extends AppCompatActivity implements View.OnTouchListener {


    private EditText editText;

    private int lastX;
    private int lastY;

    private long currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        editText= (EditText) findViewById(R.id.edit);

        editText.setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int offsetX = 0;
        int offsetY = 0;
        editText.setVisibility(View.VISIBLE);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸点坐标
                lastX = x;
                lastY = y;
                currentTime = System.currentTimeMillis();
                Log.e("gumy", "ACTION_DOWN current position x:" + lastX + "-y:" + lastY + "-currentTime:" + currentTime);

                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                offsetX = x - lastX;
                offsetY = y - lastY;
                // 在当前left、top、right、bottom的基础上加上偏移量
                Log.e("gumy",offsetX+"---"+offsetY);
                Log.e("gumy","before:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());

                int left = view.getLeft() + offsetX;
                int top = view.getTop() + offsetY;

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
                params.setMargins(left, top, 3, 3);//改变位置
                view.setLayoutParams(params);

                Log.e("gumy", "ACTION_MOVE 偏移 position x:" + offsetX + "-y:" + offsetY);
                Log.e("gumy","after:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("gumy", "ACTION_UP current position x:" + lastX + "-y:" + lastY + "-触摸时长:" + (System.currentTimeMillis() - currentTime));
                if (System.currentTimeMillis() - currentTime < 200) {
                    //editText.setText(textView.getText());
                    return showSoftInputFromWindow(this,editText);
                }
                break;
        }

        return true;
    }

    public boolean showSoftInputFromWindow(Activity activity, EditText editText) {
        Log.e("gumy","showSoftInputFromWindow");
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return true;
    }
}
