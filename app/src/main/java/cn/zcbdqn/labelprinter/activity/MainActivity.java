package cn.zcbdqn.labelprinter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.context.MyApplication;
import cn.zcbdqn.labelprinter.pojo.Printer;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.util.DateUtil;
import cn.zcbdqn.labelprinter.util.SharedPreferencesUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_main);
        //获得主色调
        String mainTonality = SharedPreferencesUtil.getString("mainTonality", "#16beea");
        MyApplication.applicationMap.put("mainTonality", mainTonality);
       /* List<Template> templates = DataSupport.findAll(Template.class);
        for (Template template:templates){
            Log.e("template",template.toString());
        }*/

       //增加打印机
        Printer printer1=new Printer(1,"CS3",80,1, DateUtil.format(null,new Date()));
        Printer printer2=new Printer(2,"XT423",80,1, DateUtil.format(null,new Date()));
        Printer printer3=new Printer(3,"XT4131A",80,1, DateUtil.format(null,new Date()));
        Printer printer4=new Printer(4,"HDT334",110,1, DateUtil.format(null,new Date()));
        Printer printer5=new Printer(5,"CS4",110,1, DateUtil.format(null,new Date()));
        Printer printer6=new Printer(6,"CS2",58,1, DateUtil.format(null,new Date()));
        Printer printer7=new Printer(7,"AK912",58,1, DateUtil.format(null,new Date()));

        List<Printer> printers=new ArrayList<>();
        printers.add(printer1);
        printers.add(printer2);
        printers.add(printer3);
        printers.add(printer4);
        printers.add(printer5);
        printers.add(printer6);
        printers.add(printer7);
        for (Printer printer:printers){
            int count = DataSupport.where("printerName=?", printer.getPrinterName()).count(Printer.class);
            if (count==0){
                printer.save();
            }
        }
        //int count = DataSupport.count(Printer.class);
        //Log.e("gumy","printer_count:"+count);
        //增加公共的模板
        Template template1=new Template(R.layout.item_commom1_template_40_20, "item_commom1_template_40_20", "android.support.constraint.ConstraintLayout", 1, 1, 40, 20, 1, DateUtil.format(null,new Date()));
        Template template2=new Template(R.layout.item_commom2_template_40_30, "item_commom2_template_40_30", "android.support.constraint.ConstraintLayout", 1, 1, 40, 30, 1, DateUtil.format(null,new Date()));
        Template template3=new Template(R.layout.item_commom3_template_30_40, "item_commom3_template_30_40", "android.support.constraint.ConstraintLayout", 1, 1, 30, 40, 1, DateUtil.format(null,new Date()));
        Template template4=new Template(R.layout.item_commom4_template_58_40, "item_commom4_template_58_40", "android.support.constraint.ConstraintLayout", 1, 1, 58, 40, 1, DateUtil.format(null,new Date()));
        /*List<Template> templateList=new ArrayList<>();
        templateList.add(template1);
        templateList.add(template2);
        templateList.add(template3);
        templateList.add(template4);*/
       /* for (Template template:templateList){

        }*/
        //增加公共的模板1
        int count=DataSupport.where("layoutId=?",template1.getLayoutId().toString()).count(Template.class);
        if (count==0){
            template1.save();
            Log.e("gumy",template1.toString());
            TemplateEditView templateEditView1=new TemplateEditView(R.id.goods_name, "android.widget.EditText", template1.getId(), 312, 67, -1, 10, 0, 0, 20, -1, -1, -2, "品名:弹力船袜", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView1.save();
            TemplateEditView templateEditView2=new TemplateEditView(R.id.goods_size, "android.widget.EditText", template1.getId(), 312, 67, -1, 10, 0, 0, 20, -1, -1, -2, "尺码:28A", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView2.save();
            TemplateEditView templateEditView3=new TemplateEditView(R.id.goods_ingredient, "android.widget.EditText", template1.getId(), 372, 67, -1, 10, 0, 0, 20, -1, -1, -2, "成份:绵", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView3.save();
        }

        //增加公共的模板2
        count=DataSupport.where("layoutId=?",template2.getLayoutId().toString()).count(Template.class);
        if (count==0){
            template2.save();
            Log.e("gumy",template2.toString());
            TemplateEditView templateEditView1=new TemplateEditView(R.id.goods_name, "android.widget.EditText", template2.getId(), 300, 67, -1, 10, 0, 0, 20, -1, -1, -2, "隐形蕾丝内衣", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView1.save();
            TemplateEditView templateEditView2=new TemplateEditView(R.id.goods_type, "android.widget.EditText", template2.getId(), 201, 67, -1, 10, 0, 0, 20, -1, -1, -2, "型号:80A", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView2.save();
            TemplateEditView templateEditView3=new TemplateEditView(R.id.goods_price, "android.widget.EditText", template2.getId(), 296, 67, -1, 10, 0, 0, 20, -1, -1, -2, "零售价:￥299", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView3.save();
            TemplateEditView templateEditView4=new TemplateEditView(R.id.barcode, "android.widget.ImageView", template2.getId(), 200, 500, -1, 5, 0, 0, 20, -1, -1, -2, null, 0.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView4.save();
            TemplateEditView templateEditView5=new TemplateEditView(R.id.barcode_text, "android.widget.EditText", template2.getId(), 216, 67, -1, 0, 0, 0, 0, -1, -1, -2, "123456789", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView5.save();
        }

        //增加公共的模板3
        count=DataSupport.where("layoutId=?",template3.getLayoutId().toString()).count(Template.class);
        if (count==0){
            template3.save();
            Log.e("gumy",template3.toString());
            TemplateEditView templateEditView1=new TemplateEditView(R.id.goods_name, "android.widget.EditText", template3.getId(), 392, 67, -1, 10, 0, 0, 20, -1, -1, -2, "品名:圆领短袖T恤", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView1.save();
            TemplateEditView templateEditView2=new TemplateEditView(R.id.goods_type, "android.widget.EditText", template3.getId(), 306, 67, -1, 10, 0, 0, 20, -1, -1, -2, "型号:165/88A", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView2.save();
            TemplateEditView templateEditView3=new TemplateEditView(R.id.goods_color, "android.widget.EditText", template3.getId(), 317, 67, -1, 10, 0, 0, 20, -1, -1, -2, "颜色:209/浅黄", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView3.save();
            TemplateEditView templateEditView4=new TemplateEditView(R.id.goods_style, "android.widget.EditText", template3.getId(), 280, 67, -1, 10, 0, 0, 20, -1, -1, -2, "款号:612345", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView4.save();
            TemplateEditView templateEditView5=new TemplateEditView(R.id.goods_price, "android.widget.EditText", template3.getId(), 296, 67, -1, 10, 0, 0, 20, -1, -1, -2, "零售价:￥299", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView5.save();
        }
        //增加公共的模板4
        count=DataSupport.where("layoutId=?",template4.getLayoutId().toString()).count(Template.class);
        if (count==0){
            template4.save();
            Log.e("gumy",template4.toString());
            TemplateEditView templateEditView1=new TemplateEditView(R.id.goods_name, "android.widget.EditText", template4.getId(), 392, 67, -1, 10, 0, 0, 20, -1, -1, -2, "品名:圆领短袖T恤", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView1.save();
            TemplateEditView templateEditView2=new TemplateEditView(R.id.goods_type, "android.widget.EditText", template4.getId(), 306, 67, -1, 10, 0, 0, 20, -1, -1, -2, "型号:165/88A", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView2.save();
            TemplateEditView templateEditView3=new TemplateEditView(R.id.goods_color, "android.widget.EditText", template4.getId(), 317, 67, -1, 10, 0, 0, 20, -1, -1, -2, "颜色:209/浅黄", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView3.save();
            TemplateEditView templateEditView4=new TemplateEditView(R.id.goods_style, "android.widget.EditText", template4.getId(), 280, 67, -1, 10, 0, 0, 20, -1, -1, -2, "款号:612345", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView4.save();
            TemplateEditView templateEditView5=new TemplateEditView(R.id.goods_price, "android.widget.EditText", template4.getId(), 296, 67, -1, 10, 0, 0, 20, -1, -1, -2, "零售价:￥299", 50.0f, "#000", 1, DateUtil.format(null,new Date()), 0);
            templateEditView5.save();


        }

        List<TemplateEditView> templateEditViews = DataSupport.findAll(TemplateEditView.class);
        for(TemplateEditView editView:templateEditViews){
            Log.e("gumy",editView.toString());
        }

        //int count = DataSupport.count(Template.class);
        //Log.e("gumy","Template_count:"+count);
        openDateOrNextActivity();
    }

    //1.0秒后进入主菜单界面
    private void openDateOrNextActivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainMenuActivity.class);
                //intent.setClass(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
