package cn.zcbdqn.labelprinter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.util.ImmersiveStatusBar;

/**
 * ConstraintLayout 控件的demo
 * Created by gumuyun on 2018/11/18.
 */

public class DemoActivity extends AppCompatActivity implements View.OnTouchListener, View.OnFocusChangeListener, View.OnClickListener {

    private Button btn3;
    private Button btn4;
    private int lastX;
    private int lastY;
    private EditText editText;
    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏*/
        setContentView(R.layout.activity_demo);
        //设置标题栏的背景色
        ImmersiveStatusBar.getInstance().Immersive(getWindow(), getActionBar());

        editText = (EditText) findViewById(R.id.edit_text);
        text1 = (TextView) findViewById(R.id.text_1);


        btn3 = (Button) findViewById(R.id.button3);
        btn4 = (Button) findViewById(R.id.button4);
        text1.setOnTouchListener(this);
        editText.setOnTouchListener(this);

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
            }
        });

        /*editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("输入完点击确认执行该方法", "输入结束");
                return false;
            }
        });*/

        /*btn4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 记录触摸点坐标
                        lastX = x;
                        lastY = y;
                        Log.e("gumy","btn4 current position x:"+lastX+"-y:"+lastY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算偏移量
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;
                        // 在当前left、top、right、bottom的基础上加上偏移量
                        view.layout(view.getLeft() + offsetX,
                                view.getTop() + offsetY,
                                view.getRight() + offsetX,
                                view.getBottom() + offsetY);
                        Log.e("gumy","move btn4 偏移 position x:"+offsetX+"-y:"+offsetY);
                        break;
                }
                return true;
            }
        });


        btn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 记录触摸点坐标
                        lastX = x;
                        lastY = y;
                        Log.e("gumy","btn3 current position x:"+lastX+"-y:"+lastY);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 计算偏移量
                        int offsetX = x - lastX;
                        int offsetY = y - lastY;
                        // 在当前left、top、right、bottom的基础上加上偏移量
                        view.layout(view.getLeft() + offsetX,
                                view.getTop() + offsetY,
                                view.getRight() + offsetX,
                                view.getBottom() + offsetY);
                        Log.e("gumy","move btn3 偏移 position x:"+offsetX+"-y:"+offsetY);
                        break;
                }
                return true;
            }
        });*/
    }

    private long currentTime;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - currentTime < 2000) {
            finish();
        } else {
            currentTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击返回退出程序", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int offsetX = 0;
        int offsetY = 0;
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
                view.layout(view.getLeft() + offsetX,
                        view.getTop() + offsetY,
                        view.getRight() + offsetX,
                        view.getBottom() + offsetY);
                Log.e("gumy", "ACTION_MOVE 偏移 position x:" + offsetX + "-y:" + offsetY);
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("gumy", "ACTION_UP current position x:" + lastX + "-y:" + lastY + "-触摸时长:" + (System.currentTimeMillis() - currentTime));
                if (System.currentTimeMillis() - currentTime < 200) {

                    showSoftInputFromWindow(this,editText);
                    return false;
                }
                break;
        }
       /* if (System.currentTimeMillis()-currentTime<200){
            return false;
        }*/
        return true;
    }

    public static void showSoftInputFromWindow(Activity activity, TextView editText) {
        Log.e("gumy","showSoftInputFromWindow");
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            Log.e("gumy", "onFocusChange:" + b);
        } else {
            Log.e("gumy", "onFocusChange:" + b);
        }
    }

    @Override
    public void onClick(View view) {
        Log.e("gumy", "onClick:" + view.getId());
    }


   /* private int edtextX, edtextY;// 文字随手指移动时的坐标
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                edtextX = (int) event.getRawX();
                edtextY = (int) event.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                int x2 = (int) event.getRawX();
                int y2 = (int) event.getRawY();
                //让包含edittext的linearlayout随手指的移动而改变位置
                editText1.scrollBy(edtextX - x2, edtextY - y2);
                edtextX = x2;
                edtextY = y2;
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                break;
            }
        }
        return true;
    }*/
}
