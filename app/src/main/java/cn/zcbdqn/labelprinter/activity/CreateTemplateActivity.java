package cn.zcbdqn.labelprinter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.adapter.SelectPrinterAdapter;
import cn.zcbdqn.labelprinter.context.MyApplication;
import cn.zcbdqn.labelprinter.pojo.Printer;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.pojo.User;
import cn.zcbdqn.labelprinter.util.DateUtil;

public class CreateTemplateActivity extends AppCompatActivity {

    @BindView(R.id.tvCreate)
    Button saveBtn;
    @BindView(R.id.tvCancel)
    Button cancelBtn;

    @BindView(R.id.etDevicesName)
    Spinner printerSpn;
    @BindView(R.id.etLabelName)
    EditText templateNameEt;
    @BindView(R.id.etLabelWidth)
    EditText templateWidthEt;
    @BindView(R.id.etLabelHeight)
    EditText templateHeightEt;
    @BindView(R.id.rgPaperType)
    RadioGroup paperTypeRg;

    private SelectPrinterAdapter selectPrinterAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_create);
        ButterKnife.bind(this);
        //printerSpn.setAdapter();
        List<Printer> printerList = DataSupport.findAll(Printer.class);//查出所有的打印机
        selectPrinterAdapter=new SelectPrinterAdapter(this);
        selectPrinterAdapter.setList(printerList);
        printerSpn.setAdapter(selectPrinterAdapter);

    }

    @OnClick({R.id.tvCreate,R.id.tvCancel})
    public void onClick(View view){

        Intent intent=new Intent();
        switch (view.getId()){
            case R.id.tvCreate:

                User user = (User) MyApplication.applicationMap.get("user");
                int userId=1;
                if (user!=null){
                    userId=user.getId();
                }
                //打印机类型
                Printer printer = (Printer) printerSpn.getSelectedItem();
                String templateName = templateNameEt.getText().toString();
                String templateWidth = templateWidthEt.getText().toString();
                String templateHeight = templateHeightEt.getText().toString();
                int pageTypeId = paperTypeRg.getCheckedRadioButtonId();
                int paperType=pageTypeId==R.id.rbPaperType1?1:3;//1:间隙纸,3:连续纸
                Template template=new Template(R.layout.item_customer_template,templateName,"android.support.constraint.ConstraintLayout",printer.getId(),paperType,Integer.valueOf(templateWidth),Integer.valueOf(templateHeight),userId, DateUtil.format(null,new Date()),null);

                template.save();//保存

                Log.e("gumy","保存的新模版id:"+template.getId()+",layoutId:"+template.getLayoutId());
                intent.putExtra("result","success");
                intent.putExtra("id","1");
                setResult(SUCCESS,intent);
                finish();
                break;
            case R.id.tvCancel:
                intent.putExtra("result","cancel");
                setResult(CANCEL,intent);
                finish();
                break;

        }
    }
    public static final int SUCCESS=1;
    public static final int CANCEL=0;


    @Override
    public void onBackPressed() {
        setResult(CANCEL);
        finish();
    }
}
