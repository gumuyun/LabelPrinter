package cn.zcbdqn.labelprinter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.adapter.ImgTemplateAdapter;
import cn.zcbdqn.labelprinter.adapter.TemplateSelectAdapter;
import cn.zcbdqn.labelprinter.pojo.Template;

public class SelectTemplateActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 模板ListView
     */
    private ListView listView;
    /**
     * 模板数据集合
     */
    private List<Map<String,Object>> list;
    private List<Integer> layoutIds;
    private TemplateSelectAdapter templateSelectAdapter;


    private ImgTemplateAdapter imgTemplateAdapter;
    /**
     * 返回按扭
     */
    private Button backTv;
    /**
     * 增加新模板
     */
    private Button addTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_template);
        //反射View
        backTv= (Button) findViewById(R.id.back);
        addTv= (Button) findViewById(R.id.add_template);
        //注册点击事件
        backTv.setOnClickListener(this);
        addTv.setOnClickListener(this);

        //初始数据
        //加载数据-----固定模板
        initCommonData();
        //加载数据-----自定义模板
        initCustomerTemplate();


        //加载模板ListView
        listView= (ListView) findViewById(R.id.template_select_lv);

        //创建模版适配器
        imgTemplateAdapter=new ImgTemplateAdapter(this,list);
        //templateSelectAdapter=new TemplateSelectAdapter(this,layoutIds,list);
        listView.setAdapter(imgTemplateAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                HashMap<String,Object> map = (HashMap<String, Object>) adapterView.getItemAtPosition(position);
                Intent intent=new Intent(SelectTemplateActivity.this,PrintTemplateActivity.class);
                intent.putExtra("templateData",map);
                startActivity(intent);
            }
        });

    }

    public void initCustomerTemplate(){
        List<Template> templateList = DataSupport.where("layoutId=?",R.layout.item_customer_template+"").find(Template.class);
        Log.e("gumy",templateList.toString());
        if (templateList!=null && templateList.size()>0){
            for (Template template:templateList){
                layoutIds.add(template.getLayoutId());
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("layoutId",template.getLayoutId());
                map.put("templateName",template.getTemplateName());
                map.put("img",R.mipmap.template1);
                map.put("template",template);
                list.add(map);
            }
        }
    }

    public void initCommonData(){
        //行布局集合
        layoutIds=new ArrayList<Integer>();
        layoutIds.add(R.layout.item_commom1_template_40_20);
        layoutIds.add(R.layout.item_commom2_template_40_30);
        layoutIds.add(R.layout.item_commom3_template_30_40);
        layoutIds.add(R.layout.item_commom4_template_58_40);
        //数据
        list =new ArrayList<Map<String,Object>>();
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("goodsName","品名:");
        //map1.put("goodsNameText","弹力船袜");
        map1.put("goodsSize","尺码:");
        //map1.put("goodsSizeText","22-25CM");
        map1.put("goodsIngredient","成份:");
        //map1.put("goodsIngredientText","棉,弹力纤维");
        map1.put("layoutId",R.layout.item_commom1_template_40_20);
        Template template1 = DataSupport.where("layoutId=?", R.layout.item_commom1_template_40_20 + "").findFirst(Template.class);
        map1.put("template",template1);
        map1.put("templateName","item_commom1_template_40_20");
        map1.put("img",R.mipmap.template1);

        list.add(map1);

        Map<String,Object> map2=new HashMap<String,Object>();

        map2.put("goodsName","品名:");
        map2.put("goodsType","型号:");
        //map2.put("goodsTypeText","80A");
        map2.put("goodsPrice","零售价:￥");
        //map2.put("goodsPriceText","￥299");
        map2.put("barcodeImage",R.mipmap.barcode);
        map2.put("barcodeText","123456789");
        map2.put("layoutId",R.layout.item_commom2_template_40_30);
        map2.put("templateName","item_commom2_template_40_30");
        map2.put("img",R.mipmap.template2);
        Template template2 = DataSupport.where("layoutId=?", R.layout.item_commom2_template_40_30 + "").findFirst(Template.class);
        map2.put("template",template2);
        list.add(map2);
        Map<String,Object> map3=new HashMap<String,Object>();

        map3.put("goodsName","品名:");
        //map3.put("goodsNameText","圆领短袖T恤");
        map3.put("goodsType","型号:");
        //map3.put("goodsTypeText","165/88A");
        map3.put("goodsPrice","零售价:￥");
        //map3.put("goodsPriceText","￥299");
        map3.put("goodsColor","颜色:");
        //map3.put("goodsColorText","209/浅黄");
        map3.put("goodsStyle","款号:");
        //map3.put("goodsStyleText","612345");
        map3.put("layoutId",R.layout.item_commom3_template_30_40);
        map3.put("templateName","item_commom3_template_30_40");
        map3.put("img",R.mipmap.template3);
        Template template3 = DataSupport.where("layoutId=?", R.layout.item_commom3_template_30_40 + "").findFirst(Template.class);
        map3.put("template",template3);
        list.add(map3);
        Map<String,Object> map4=new HashMap<String,Object>();
        map4.put("goodsName","品名:");
        map4.put("layoutId",R.layout.item_commom4_template_58_40);
        map4.put("templateName","item_commom4_template_58_40");
        map4.put("img",R.mipmap.template4);
        Template template4 = DataSupport.where("layoutId=?", R.layout.item_commom4_template_58_40 + "").findFirst(Template.class);
        map4.put("template",template4);
        list.add(map4);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case  R.id.add_template:
                //跳转到增加模板的界面
                Intent intent=new Intent(this,CreateTemplateActivity.class);
                startActivityForResult(intent,ADD_TEMPLATE_REQUEST_CODE);
                break;
        }
    }

    public final static int ADD_TEMPLATE_REQUEST_CODE=1;

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     *
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case CreateTemplateActivity.CANCEL:
                Log.i("gumy", "CANCEL");
                break;
            case CreateTemplateActivity.SUCCESS:
                String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
                Log.i("gumy", result);
                break;
        }
    }
}
