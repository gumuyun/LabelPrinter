package cn.zcbdqn.labelprinter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zcbdqn.labelprinter.R;

public class Demo2Activity extends AppCompatActivity {


    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        ButterKnife.bind( this ) ;

    }

    @OnClick({R.id.button1,R.id.button2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button1:

                break;

            case R.id.button2:


                break;
        }
    }

}
