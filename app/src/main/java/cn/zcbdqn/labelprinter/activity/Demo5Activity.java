package cn.zcbdqn.labelprinter.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zcbdqn.labelprinter.R;

public class Demo5Activity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private EditText editText;
    private TextView textView;


    private Button addViewBtn;
    private int lastX;
    private int lastY;

    private long currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo5);

        editText= (EditText) findViewById(R.id.edit);
        textView= (TextView) findViewById(R.id.text);
        addViewBtn= (Button) findViewById(R.id.add_view);

        editText.setOnTouchListener(this);
        textView.setOnTouchListener(this);

        addViewBtn.setOnClickListener(this);
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
                Log.e("gumy",offsetX+"---"+offsetY);
                Log.e("gumy","before:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());

                int left = view.getLeft() + offsetX;
                int top = view.getTop() + offsetY;

                int bottom = view.getBottom() - offsetY;
                int right = view.getRight() - offsetX;
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
                //判断是否超出父级
                int marginLeft = 0;
                int marginRight = 0;
                int marginTop = 0;
                int marginBottom = 0;

                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    // 获取margin值，做移动的边界判断
                    ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    marginLeft = lp.leftMargin;
                    marginRight = lp.rightMargin;
                    marginTop = lp.topMargin;
                    marginBottom = lp.bottomMargin;
                    int parentWidth = 0;
                    int parentHeight = 0;
                    if (getParent() != null && view.getParent() instanceof ViewGroup) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        // 拿到父布局宽高
                        parentWidth = parent.getWidth();
                        parentHeight = parent.getHeight();
                        if (left < marginLeft) {
                            // 移到到最左的时候，限制在marginLeft
                            left = marginLeft;
                            right = view.getWidth() + left;
                        }
                        if (right > parentWidth - marginRight) {
                            // 移动到最右
                            right = parentWidth - marginRight;
                            left = right - view.getWidth();
                        }
                        if (top < marginTop) {
                            //移动到顶部
                            top = marginTop;
                            bottom = view.getHeight() + top;
                        }
                        if (bottom > parentHeight - marginBottom) {
                            //移动到底部
                            bottom = parentHeight - marginBottom;
                            top = bottom - view.getHeight();
                        }
                        //layout(left, top, right, bottom);
                        // 记录移动后的坐标值
                       /* mLastLeft = left;
                        mLastRight = right;
                        mLastTop = top;
                        mLastBottom = bottom;*/
                    }
                }

                params.setMargins(left, top, 3, 3);//改变位置
                view.setLayoutParams(params);

                Log.e("gumy", "ACTION_MOVE 偏移 position x:" + offsetX + "-y:" + offsetY);
                Log.e("gumy","after:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("gumy", "ACTION_UP current position x:" + lastX + "-y:" + lastY + "-触摸时长:" + (System.currentTimeMillis() - currentTime));
                if (System.currentTimeMillis() - currentTime < 200) {
                    //editText.setText(textView.getText());
                    if (view instanceof EditText){
                        EditText currentView= (EditText) view;
                        return showSoftInputFromWindow(this,currentView);
                    }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_view:
                LinearLayout rl= (LinearLayout) findViewById(R.id.rl);
                FrameLayout fl=new FrameLayout(this);
                rl.addView(fl);
                EditText newET=new EditText(this);
                newET.setHint("请输入....");
                fl.addView(newET);
                newET.setOnTouchListener(this);

                break;
        }
    }
}

