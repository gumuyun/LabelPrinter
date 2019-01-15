package cn.zcbdqn.labelprinter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.adapter.TradeAdapter;

public class MainMenuActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏*/
        setContentView(R.layout.activity_main_menu);
        //设置标题栏的背景色
        //ImmersiveStatusBar.getInstance().Immersive(getWindow(), getActionBar());
        //初始化行业ListView
        listView= (ListView) findViewById(R.id.main_menu_lv);
        //创建行业ListView适配器,内部初始化数据
        TradeAdapter tradeAdapter=new TradeAdapter(this);
        //关联
        listView.setAdapter(tradeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainMenuActivity.this,SelectTemplateActivity.class);
                startActivity(intent);
            }
        });
    }

    private long currentTime;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-currentTime<2000) {
            finish();
        }else {
            currentTime=System.currentTimeMillis();
            Toast.makeText(this, "再次点击返回退出程序", Toast.LENGTH_SHORT).show();
        }
    }
}
