package cn.zcbdqn.labelprinter.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.zcbdqn.labelprinter.R;

public class Demo3Activity extends AppCompatActivity implements View.OnTouchListener {


    private FrameLayout textFl;
    private TextView textView;
    private EditText editText;

    private int lastX;
    private int lastY;

    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);
        textFl= (FrameLayout) findViewById(R.id.text_fl);
        textView= (TextView) findViewById(R.id.text_1);
        editText= (EditText) findViewById(R.id.edit_text);

        textFl.setOnTouchListener(this);
        // 输入的内容变化的监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // 输入的内容变化的监听
                Log.e("输入过程中执行该方法", "文字变化,charSequence:"+charSequence+",start:"+start+",count:"+count+",before:"+before);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                // 输入前的监听
                Log.e("输入前确认执行该方法", "开始输入,charSequence:"+charSequence+",start:"+start+",count:"+count+",after:"+after);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // 输入后的监听
                Log.e("输入结束执行该方法", "输入结束");
                textView.setText(editText.getText());
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handlerHiddenIMM.sendEmptyMessage(1);
                    }
                };
            }
        });

    }

    Handler handlerHiddenIMM=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            editText.setVisibility(View.GONE);
        }
    };

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
                view.layout(view.getLeft() + offsetX,
                        view.getTop() + offsetY,
                        view.getRight() + offsetX,
                        view.getBottom() + offsetY);

                Log.e("gumy", "ACTION_MOVE 偏移 position x:" + offsetX + "-y:" + offsetY);
                Log.e("gumy","after:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("gumy", "ACTION_UP current position x:" + lastX + "-y:" + lastY + "-触摸时长:" + (System.currentTimeMillis() - currentTime));
                if (System.currentTimeMillis() - currentTime < 200) {
                    editText.setText(textView.getText());
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
