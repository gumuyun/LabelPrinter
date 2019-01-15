package cn.zcbdqn.labelprinter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import org.litepal.crud.DataSupport;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.adapter.PrintTemplateAdapter;
import cn.zcbdqn.labelprinter.context.MyApplication;
import cn.zcbdqn.labelprinter.db.MyDataOpenHelper;
import cn.zcbdqn.labelprinter.db.TableNames;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.pojo.User;
import cn.zcbdqn.labelprinter.service.TemplateEditViewService;
import cn.zcbdqn.labelprinter.service.TemplateService;
import cn.zcbdqn.labelprinter.service.impl.TemplateEditViewServiceImpl;
import cn.zcbdqn.labelprinter.service.impl.TemplateServiceImpl;
import cn.zcbdqn.labelprinter.util.BitmapUtil;
import cn.zcbdqn.labelprinter.util.Constant;
import cn.zcbdqn.labelprinter.util.DateUtil;
import cn.zcbdqn.labelprinter.util.Object2Values;
import cn.zcbdqn.labelprinter.util.SharedPreferencesUtil;
import cn.zcbdqn.labelprinter.util.ZKPrintUtils;

public class PrintTemplateActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private TemplateEditViewService templateEditViewService;
    private TemplateService templateService;
    /**
     * 获得焦点的控件
     */
    private EditText currentEditText;

    /**
     * 震动
     */
    private Vibrator vibrator;
    /**
     * 要移运的view的位置
     */
    private int lastX;
    private int lastY;

    /**
     * 当前时间
     */
    private long currentTime;

    /**
     * 放置模板的块
     */
    private ConstraintLayout templateParentCL;
    /**
     * 使用layoutId反射的模板
     */
    private ConstraintLayout templateChildCL;

    /**
     * 打印按扭
     */
    @BindView(R.id.print_btn)
    ImageView printBtn;
    /**
     * 返回按扭
     */
    @BindView(R.id.back)
    Button back;

    /**
     * 保存
     */
    @BindView(R.id.save_btn)
    ImageView saveBtn;
    /**
     * 预览
     */
    @BindView(R.id.preview_btn)
    ImageView reviewBtn;

    /**
     * 打印机设置
     */
    @BindView(R.id.setting_print)
    Button settingPrintBtn;

    /**
     * 模版id
     */
    int layoutId;
    /**
     * 模版id
     */
    private String templateName;

    @BindView(R.id.text_btn)
    ImageView addTextBtn;
    @BindView(R.id.delete_btn)
    ImageView delTextBtn;
    @BindView(R.id.barcode_btn)
    ImageView addBarcodeBtn;
    @BindView(R.id.scale_b_btn)
    ImageView textSizeAddBtn;
    @BindView(R.id.scale_s_btn)
    ImageView textSizeSubBtn;
    @BindView(R.id.default_btn)
    ImageView defaultLayoutBtn;

    HashMap<String, Object> templateData;

    //模板,选择模板时传递过来
    private Template template;

    private ConstraintLayout templateParent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_template);
        ButterKnife.bind( this ) ;
        //震动
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //业务
        templateEditViewService=new TemplateEditViewServiceImpl();
        templateService=new TemplateServiceImpl();
        //获得intent
        Intent intent = getIntent();
        //数据
        templateData = (HashMap<String, Object>) intent.getSerializableExtra("templateData");
        //模板父级
        templateParentCL = (ConstraintLayout) findViewById(R.id.template);
        //模板ID
        layoutId = (Integer) templateData.get("layoutId");
        templateName = (String) templateData.get("templateName");
        template= (Template) templateData.get("template");
        //templateChildCL= (ConstraintLayout) LayoutInflater.from(this).inflate(layoutId,null);

        //从数据库反射
        //inflateFromDataBase();
        if (layoutId==R.layout.item_customer_template){
            inflateFromDataBase();
            Log.e("gumy","自定义模板从数据库中反射");
            TextView bottomSizeTextView = (TextView) templateParentCL.findViewById(R.id.bottom_size_display);
            TextView rightSizeTextView = (TextView) templateParentCL.findViewById(R.id.right_size_display);
            bottomSizeTextView.setText(template.getWidth()+"mm");
            rightSizeTextView.setText(template.getHeight()+"mm");
        }else {
            //inflateFromLayout(templateData);
            //查看数据库中是否有保存该模板
            int count = DataSupport.where("layoutId=?", layoutId+"").count(Template.class);
            if (count>0){
                //从数据库中加载数据
                inflateFromDataBase();
                Log.e("gumy","公共模板从数据库中反射");
            }else {
                //适配器,加载模板数据
                inflateFromLayout(templateData);
                Log.e("gumy","公共模板使用布局反射");
            }
        }
        //查看数据库中是否有保存该模板
       /* int count = DataSupport.where("layoutId=?", layoutId+"").count(Template.class);
        if (count>0){
            //从数据库中加载数据
            inflateFromDataBase();

        }else {
            //适配器,加载模板数据
            inflateFromLayout(templateData);
        }*/

       /* printBtn = (ImageView) findViewById(R.id.print_btn);
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        reviewBtn = (ImageView) findViewById(R.id.preview_btn);
        back = (Button) findViewById(R.id.back);
        settingPrintBtn = (Button) findViewById(R.id.setting_print);*/

       /* saveBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        reviewBtn.setOnClickListener(this);
        settingPrintBtn.setOnClickListener(this);*/

        //反射已有的控件,注册触摸事件
        traversalView((ViewGroup) templateChildCL.findViewById(R.id.template_parent));
        //如果有一维码的模板,设置一维码图片
        makerBarcodeImg();
        //初始化id
        initIds();
    }

    private void inflateFromLayout(HashMap<String, Object> templateData) {
        PrintTemplateAdapter printTemplateAdapter = new PrintTemplateAdapter(this);
        //反射模板
        templateChildCL = (ConstraintLayout) printTemplateAdapter.inflaterTemplate(layoutId);
        //设置数据
        printTemplateAdapter.putTemplateData(templateData, layoutId);
        //关联
        templateParentCL.addView(templateChildCL);
        //布局中的父局,要打印的块
        templateParent= (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
    }

    private void makerBarcodeImg() {
        if (layoutId == R.layout.item_commom2_template_40_30) {
            final ImageView barcodeImageView = (ImageView) templateChildCL.findViewById(R.id.barcode);
            EditText barcodeEditText = (EditText) templateChildCL.findViewById(R.id.barcode_text);
            barcodeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }
                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable!=null && !"".equals(editable.toString())){
                        createBitmapByText(barcodeImageView,editable.toString());
                    }
                }
            });
            String barcodeText = barcodeEditText.getText().toString().trim();
           /* //生成一维码
            //第一种方式
            Bitmap barcodeBitMap1 = BitmapUtil.createOneDCode(barcodeText);
            //第二种方式 毫米转像素
            //int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 100, getResources().getDisplayMetrics());
            //int height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 60, getResources().getDisplayMetrics());
            //Bitmap barcodeBitMap2 = BitmapUtil.creatBarcode(this, barcodeText, 100, 100, true);
            barcodeImageView.setImageBitmap(barcodeBitMap1);*/
            createBitmapByText(barcodeImageView,barcodeText);
        }
    }

    /**
     * 从数据库中加载数据
     */
    private void inflateFromDataBase() {
        //从数据库中查询模板
        //Template template = DataSupport.where("layoutId=?", layoutId+"").findFirst(Template.class);
        //Template template = (Template) templateData.get("template");

        //布局
        templateChildCL = (ConstraintLayout) getLayoutInflater().inflate(layoutId, null);
        //布局中的父局,要打印的块
        templateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
        //从数据库中查询出所有的子模板
        List<TemplateEditView> templateEditViews = DataSupport.where("parentViewId=?", template.getId() + "").find(TemplateEditView.class);
        //如果已经删除的则要在原始模板中删除
        for(int i=templateParent.getChildCount()-1;i>=0;i--){
            View view = templateParent.getChildAt(i);
            if (view instanceof FrameLayout){
                FrameLayout frameLayout= (FrameLayout) view;
                boolean flag=false;
                //循环判断
                for (TemplateEditView editView:templateEditViews){
                    if (frameLayout.getChildAt(0).getId()==editView.getViewId()){
                        flag=true;
                        break;
                    }
                }
                if (!flag){
                    //删除
                    templateParent.removeView(frameLayout);
                }
            }

        }
       //Log.e("gumy",template.toString());
        //创建模板参数对象,layout,设置宽高
        //毫米转像素
        int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, template.getWidth(), getResources().getDisplayMetrics());
        int height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, template.getHeight(), getResources().getDisplayMetrics());

        Log.e("gumy","创建模板参数对象,layout,设置宽:"+width+"高:"+height);
        ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(width,height);
        templateParent.setLayoutParams(layoutParams);
        //遍历所有的子控件 数据
        for (int i=0;i<templateEditViews.size();i++){
            Log.e("gumy",templateEditViews.get(i).toString());
            try {
                //获得子控件的类型
                Class<?> aClass = Class.forName(templateEditViews.get(i).getViewType());
                //子控件是输入框时
                if (aClass.equals(EditText.class)){
                    //子控件数据
                    TemplateEditView templateEditView= templateEditViews.get(i);
                    //反射子控件
                    EditText editText= (EditText) templateParent.findViewById(templateEditView.getViewId());
                    if (editText==null){
                        //View view = getLayoutInflater().inflate(R.layout.item_add_edittext, null);
                        //editText= (EditText) view.findViewById(templateEditView.getViewId());
                        FrameLayout newFrameLayout=new FrameLayout(this);
                        editText=new EditText(this);
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,50);
                        editText.setHint("请输入");
                        editText.setBackgroundColor(Color.WHITE);
                        editText.setOnTouchListener(this);
                        editText.setId(templateEditView.getViewId());
                        newFrameLayout.addView(editText);

                        templateParent.addView(newFrameLayout);
                    }
                    //设置文字
                    editText.setText(templateEditView.getText());
                    editText.setVisibility(templateEditView.getVisibility());
                    //颜色
                    editText.setTextColor(Color.BLACK);
                    //文字大小,像素
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,templateEditView.getTextSize());
                    //布局参数
                    FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(editText.getLayoutParams());
                    //使用反射给属性赋值
                    Field[] fields = lp.getClass().getFields();
                    AccessibleObject.setAccessible(fields,true);
                    for (Field field:fields){
                        try {
                            //Log.e("gumy",field.getName()+":"+field.get(lp));
                            //上-右-下-左
                            if (field.getName().equals("topMargin")){
                                field.set(lp,templateEditView.getTopMargin());
                            }else if (field.getName().equals("rightMargin")){
                                field.set(lp,templateEditView.getRightMargin());
                            }else if (field.getName().equals("bottomMargin")){
                                field.set(lp,templateEditView.getBottomMargin());
                            }else if (field.getName().equals("leftMargin")){
                                field.set(lp,templateEditView.getLeftMargin());
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    //设置布局参数
                    editText.setLayoutParams(lp);
                    editText.setGravity(templateEditView.getGravity());
                    //editText.setOnTouchListener(this);
                }else if (aClass.equals(ImageView.class)){
                    //是图片使用,文字生成图片
                    EditText editText = (EditText) templateParent.findViewById(templateEditViews.get(i + 1).getViewId());//获得该图片的下一个输入框
                    ImageView imageView= (ImageView) templateParent.findViewById(templateEditViews.get(i).getViewId());//反射图片
                    editText.setVisibility(imageView.getVisibility());
                    createBitmapByText(imageView,editText.getText().toString());//文字生成图片
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        templateParentCL.addView(templateChildCL);
    }

    /**
     * 使用文本生成图片
     * @param imageView
     * @param barcodeText
     */
    private void createBitmapByText(ImageView imageView, String barcodeText) {
        if (barcodeText!=null && barcodeText!=""){
            try {
                Bitmap barcodeBitMap = BitmapUtil.createOneDCode(barcodeText.toString());
                imageView.setImageBitmap(barcodeBitMap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this,"生成一维码失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            imageView.setImageResource(R.mipmap.barcode);
        }
    }

    /**
     * 遍历所有view
     *
     * @param viewGroup
     */
    public void traversalView(ViewGroup viewGroup) {
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                traversalView((ViewGroup) view);
            } else {
                doView(view);
            }
        }
    }

    /**
     * 处理view
     *
     * @param view
     */
    private void doView(View view) {
        if (view instanceof EditText){
            EditText editText= (EditText) view;
            //editText.setSelection(editText.getText().length());
            //@Override protected void onSelectionChanged(int selStart, int selEnd) { super.onSelectionChanged(selStart, selEnd); //光标首次获取焦点是在最后面，之后操作就是按照点击的位置移动光标 if (isEnabled() && hasFocus() && hasFocusable()) { setSelection(selEnd); } else { setSelection(getText().length()); } }
            if (currentEditText==null){
                currentEditText=editText;

            }
        }
        view.setOnTouchListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printBitMap(layoutId);
            }
        });
        //setImageBitmapOne();
    }


    /*方法一
   * */
    private void setImageBitmap() {
        final View template = templateChildCL.findViewById(R.id.template_parent);
        template.setDrawingCacheEnabled(true);
        template.buildDrawingCache(true);
        Bitmap bitmap = template.getDrawingCache();
        //saveBtn.setImageBitmap(bitmap);
        //printBitMap(bitmap);
    }

    Bitmap cacheBitmap;

    /**
     * 打印图片
     * @param layoutId
     */
    private void printBitMap(final int layoutId) {
        vibrator.vibrate(30);
        //ConstraintLayout templateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
        if (template.getWidth()<=useZpSDKWidth){
            templateParent.setBackgroundColor(Color.WHITE);
        }
        if (currentEditText!=null){
            currentEditText.setCursorVisible(false);
        }
        final String defaultBluePrintAddress = SharedPreferencesUtil.getString("defaultBluePrintAddress", null);
        Log.e("gumy", "蓝牙打印机地址:" + defaultBluePrintAddress + ",layoutId:" + layoutId);

        if (defaultBluePrintAddress == null || defaultBluePrintAddress.equals("")) {
            Intent intent = new Intent(PrintTemplateActivity.this, SettingPrintActivity.class);
            startActivity(intent);
            return;
        }
        showAlertDialog("正在打印,请稍后!");

       if (template.getWidth()<80){//小于60用图片打印,大于使用文字
            //把view转换为bitmap
            //Bitmap bitmap = BitmapUtil.getBitmapByLayout(templateChildCL, R.id.template_parent);

            templateParent.setDrawingCacheEnabled(true);//缓存图片
            templateParent.buildDrawingCache(true);//硬件加速
            cacheBitmap=templateParent.getDrawingCache();
            //Bitmap bitmap = BitmapUtil.convertViewToBitmap(findViewById(R.id.template_parent));

            //bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.select);
            //int width = bitmap.getWidth();
            //int height = bitmap.getHeight()+50;
            //单位转换  像素转毫米,毫米转像素
            //float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 30, getResources().getDisplayMetrics());
            //Log.e("gumy",width+"-"+height+"-----"+f);
            //开启线程打印,完成之后,清除缓存
            new Thread(){
                @Override
                public void run() {
                    Bitmap bitmap=null;
                    int status=-1;
                    switch (layoutId) {
                        case R.layout.item_commom1_template_40_20:
                            bitmap = BitmapUtil.zoomImg(cacheBitmap, Constant.COMMON1_TEMPLATE_WIDTH, Constant.COMMON1_TEMPLATE_HEIGHT);
                            status=ZKPrintUtils.drawGraphic(PrintTemplateActivity.this, defaultBluePrintAddress, bitmap, Constant.COMMON1_TEMPLATE_WIDTH, Constant.COMMON1_TEMPLATE_HEIGHT);
                            //ZKPrintUtils.drawGraphic(this,defaultBluePrintAddress,bitmap,width,height);
                            break;
                        case R.layout.item_commom2_template_40_30:
                            //打印的时候生成一次图片
                            //makerBarcodeImg();
                            bitmap = BitmapUtil.zoomImg(cacheBitmap, Constant.COMMON2_TEMPLATE_WIDTH, Constant.COMMON2_TEMPLATE_HEIGHT);
                            status= ZKPrintUtils.drawGraphic(PrintTemplateActivity.this, defaultBluePrintAddress, bitmap, Constant.COMMON2_TEMPLATE_WIDTH, Constant.COMMON2_TEMPLATE_HEIGHT);
                            //ZKPrintUtils.drawGraphic(this,defaultBluePrintAddress,bitmap,width,height);
                            break;
                        case R.layout.item_commom3_template_30_40:
                            //缩放,控件的宽高单位是毫米(mm),在android显示时会以像素显示(android自己转换),
                            // 把控件生成图片时宽高是像素,所以要进行缩放
                            bitmap = BitmapUtil.zoomImg(cacheBitmap, Constant.COMMON3_TEMPLATE_WIDTH, Constant.COMMON3_TEMPLATE_HEIGHT);
                            status=ZKPrintUtils.drawGraphic(PrintTemplateActivity.this, defaultBluePrintAddress, bitmap, Constant.COMMON3_TEMPLATE_WIDTH, Constant.COMMON3_TEMPLATE_HEIGHT);
                            //ZKPrintUtils.drawGraphic(this,defaultBluePrintAddress,bitmap, width,height);
                            break;
                        case R.layout.item_commom4_template_58_40:
                            bitmap = BitmapUtil.zoomImg(cacheBitmap, Constant.COMMON4_TEMPLATE_WIDTH, Constant.COMMON4_TEMPLATE_HEIGHT);
                            status=ZKPrintUtils.drawGraphic(PrintTemplateActivity.this, defaultBluePrintAddress, bitmap, Constant.COMMON4_TEMPLATE_WIDTH, Constant.COMMON4_TEMPLATE_HEIGHT);
                            //ZKPrintUtils.drawGraphic(this,defaultBluePrintAddress,bitmap, width,height);
                            break;
                        case R.layout.item_customer_template:

                            //计算间隔
                            int h=0;
                            switch (template.getPaperType()){
                                case 1:
                                    h=18;
                                    break;
                                case 3:
                                    h=50;
                                    break;
                            }
                            int pageWidth=template.getWidth()*8;
                            int pageHeight=template.getHeight()*8+h ;
                            bitmap = BitmapUtil.zoomImg(cacheBitmap,pageWidth , pageHeight);
                            status=ZKPrintUtils.drawGraphic(PrintTemplateActivity.this, defaultBluePrintAddress, bitmap, pageWidth, pageHeight);
                            //ZKPrintUtils.drawGraphic(this,defaultBluePrintAddress,bitmap, width,height);
                            break;
                    }
                    bitmap.recycle();
                    Message message=new Message();
                    message.what=status;
                    printHandler.sendMessage(message);
                }
            }.start();
        }else if(template.getWidth()<90){
            new Thread(){
                @Override
                public void run() {
                    List<TemplateEditView> templateEditView = getTemplateEditView();
                    boolean flag=false;
                    if (templateEditView!=null && templateEditView.size()>0){
                        for (TemplateEditView templateEditView1:templateEditView){
                            //如果有文字大于7(缩放值),则使用重写的芝科sdk
                            if (templateEditView1.getScaleTextSize()>7){
                                flag=true;
                                break;
                            }
                        }
                    }
                    int status = ZKPrintUtils.printByEditText(PrintTemplateActivity.this, defaultBluePrintAddress, templateEditView, template.getWidth() * 8, template.getHeight()*8 + 18,flag);
                    Message message = new Message();
                    message.what=status;
                    printHandler.sendMessage(message);
                }
            }.start();
        }else {
            new Thread(){
                @Override
                public void run() {
                    List<TemplateEditView> templateEditView = getTemplateEditView();
                    boolean flag=false;
                    if (templateEditView!=null && templateEditView.size()>0){
                        for (TemplateEditView templateEditView1:templateEditView){
                            //如果有文字大于7(缩放值),则使用重写的芝科sdk
                            if (templateEditView1.getScaleTextSize()>7){
                                flag=true;
                                break;
                            }
                        }
                    }
                    Template template = DataSupport.where("id=?", PrintTemplateActivity.this.template.getId().toString()).findFirst(Template.class);
                    int status = ZKPrintUtils.printByEditText(PrintTemplateActivity.this, defaultBluePrintAddress, templateEditView, 110 * 8, template.getHeight()*8 + 18,flag);
                    Message message = new Message();
                    message.what=status;
                    printHandler.sendMessage(message);
                }
            }.start();
        }


    }

    Handler printHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what){
                case -2:
                    Toast.makeText(PrintTemplateActivity.this, "连接打印机失败", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(PrintTemplateActivity.this, "获取状态异常", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(PrintTemplateActivity.this, "打印完成", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(PrintTemplateActivity.this, "缺纸", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(PrintTemplateActivity.this, "纸仓未合上", Toast.LENGTH_LONG).show();
                    break;
            }
            if (cacheBitmap!=null){
                cacheBitmap.recycle();
                cacheBitmap=null;
            }
            templateParent.destroyDrawingCache();//清除缓存
            if (currentEditText!=null){
                currentEditText.setCursorVisible(true);//显示光标
            }
        }
    };

    AlertDialog dialog;
    public void showAlertDialog(String message){
        dialog=new AlertDialog.Builder(this)
                .setTitle("提示...")
                .setMessage(message)
                .setPositiveButton("确定", null)
                .create();
        dialog.show();
    }

    /*
    * 方法二
    * */
    private void setImageBitmapOne() {

        final View template = templateChildCL.findViewById(R.id.template_parent);
        template.setDrawingCacheEnabled(true);
        template.buildDrawingCache(true);
        ViewTreeObserver viewTreeObserver = template.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                template.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Bitmap bitmap = Bitmap.createBitmap(template.getMeasuredWidth(), template.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                template.draw(canvas);
                saveBtn.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.getParent().requestDisallowInterceptTouchEvent(true);//屏蔽父控件拦截onTouch事件
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
                //Log.e("gumy",offsetX+"---"+offsetY);
                //Log.e("gumy","before:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());
                int left = view.getLeft() + offsetX;
                int top = view.getTop() + offsetY;
                int bottom = view.getBottom() - offsetY;
                int right = view.getRight() - offsetX;
                if (view.getLayoutParams() instanceof FrameLayout.LayoutParams){
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)view.getLayoutParams();
                    params.setMargins(left, top, 3, 3);//改变位置
                    view.setLayoutParams(params);
                }
                //Log.e("gumy", "ACTION_MOVE 偏移 position x:" + offsetX + "-y:" + offsetY);
                //Log.e("gumy","after:左:"+view.getLeft()+"-上:"+view.getTop()+"-右:"+view.getRight()+"-下:"+view.getBottom());
                return true;
            case MotionEvent.ACTION_UP:
                //Log.e("gumy", "ACTION_UP current position x:" + lastX + "-y:" + lastY + "-触摸时长:" + (System.currentTimeMillis() - currentTime));
                if (System.currentTimeMillis() - currentTime < 200) {
                    //editText.setText(textView.getText());
                    if (view instanceof EditText){
                        currentEditText= (EditText) view;
                        return showSoftInputFromWindow(this,currentEditText);
                    }
                }
                break;
        }
        return true;
    }

    public boolean showSoftInputFromWindow(Activity activity, EditText editText) {
        Log.e("gumy","showSoftInputFromWindow");
        editText.setSelection(editText.getText().length());
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText,InputMethodManager.HIDE_NOT_ALWAYS);
        //((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    @OnClick({R.id.setting_print,R.id.save_btn,R.id.preview_btn,R.id.back,R.id.scale_b_btn,R.id.scale_s_btn,R.id.default_btn,R.id.delete_btn,R.id.text_btn})
    public void onClick(View view) {
        Intent intent =null;
        vibrator.vibrate(30);
        ConstraintLayout templateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
        switch (view.getId()) {
            case R.id.setting_print:
                intent = new Intent(PrintTemplateActivity.this, SettingPrintActivity.class);
                startActivity(intent);
                break;
            case R.id.save_btn:
                //保存模板

                //Log.e("gumy","templateChildCL.getChildCount():"+templateChildCL.getChildCount()+",templateChildCL.getLayoutParams():"+templateChildCL.getLayoutParams().toString());

                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                templateChildCL.measure(w, h);
                int height = templateChildCL.getMeasuredHeight();
                int width = templateChildCL.getMeasuredWidth();
                Log.e("gumy","class: "+templateChildCL.getClass().getName()+"measure width=" + width + " height=" + height);

             /*   Template template=new Template();
                template.setLayoutId(layoutId);
                template.setTemplateName(templateName);*/
                template.setWidth(width);
                template.setHeight(height);
                Integer userId= 1;
                User user=(User) MyApplication.applicationMap.get("user");
                if (user!=null){
                    userId=user.getId();
                }
                template.setPaperType(Constant.PAGE_TYPE1);//纸张类型
                template.setPrintType(Constant.PRINTER_TYPE1);//打印机类型
                template.setCreateBy(userId);
                template.setCreateDate(DateUtil.format(null,new Date()));
                template.setViewType(templateChildCL.getClass().getName());
                //保存模板
                //saveOrUpdateTemplate(template);
                //template.save();
                //templateService.saveOrUpdateTemplate(template);

                for (int i=0;i<templateParent.getChildCount();i++){
                    if (templateParent.getChildAt(i) instanceof FrameLayout){
                        FrameLayout frameLayout = (FrameLayout) templateParent.getChildAt(i);

                        ViewGroup.LayoutParams frameLayoutLp = frameLayout.getLayoutParams();

                        //如果是EditText
                        if (frameLayout.getChildAt(0) instanceof EditText){
                            EditText editText = (EditText) frameLayout.getChildAt(0);
                            editText.measure(w, h);
                            int editTextH = editText.getMeasuredHeight();
                            int editTextW = editText.getMeasuredWidth();
                            ViewGroup.LayoutParams editTextLp = editText.getLayoutParams();
                            TemplateEditView tev=new TemplateEditView();
                            tev.setWidth(editTextW);
                            tev.setHeight(editTextH);
                            tev.setText(editText.getText().toString());
                            tev.setViewType(EditText.class.getName());
                            Field[] lpFields = editTextLp.getClass().getFields();
                            AccessibleObject.setAccessible(lpFields,true);
                            for (Field field:lpFields){
                                try {
                                    //Log.e("gumy","lpFields-"+field.getName()+":"+field.get(editTextLp));
                                    if (field.getName().equals("topMargin")){
                                        tev.setTopMargin(field.getInt(editTextLp));
                                    }else if (field.getName().equals("rightMargin")){
                                        tev.setRightMargin(field.getInt(editTextLp));
                                    }else if (field.getName().equals("bottomMargin")){
                                        tev.setBottomMargin(field.getInt(editTextLp));
                                    }else if (field.getName().equals("leftMargin")){
                                        tev.setLeftMargin(field.getInt(editTextLp));
                                    }else if (field.getName().equals("FILL_PARENT")){
                                        tev.setFillParent(field.getInt(editTextLp));
                                    }else if (field.getName().equals("MATCH_PARENT")){
                                        tev.setMatchParent(field.getInt(editTextLp));
                                    }else if (field.getName().equals("WRAP_CONTENT")){
                                        tev.setWrapContent(field.getInt(editTextLp));
                                    }else if (field.getName().equals("gravity")){
                                        tev.setGravity(field.getInt(editTextLp));
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            tev.setCreateBy(userId);
                            tev.setCreateDate(DateUtil.format(null,new Date()));
                            tev.setTextSize(editText.getTextSize());
                            tev.setTextColor("#000");
                            tev.setId(editText.getId());
                            tev.setVisibility(editText.getVisibility());
                            tev.setViewId(editText.getId());
                            tev.setParentViewId(template.getId());
                            Log.e("gumy",tev.toString());
                            //保存控件
                            //tev.save();
                            //saveOrUpdateTemplateEditView(tev);
                            templateEditViewService.saveOrUpdateTemplateEditView(tev);
                        }else if (frameLayout.getChildAt(0) instanceof ImageView){
                            ImageView imageView= (ImageView) frameLayout.getChildAt(0);
                            imageView.measure(w, h);
                            int imageViewH = imageView.getMeasuredHeight();
                            int imageViewW = imageView.getMeasuredWidth();
                            ViewGroup.LayoutParams imageViewLp = imageView.getLayoutParams();
                            TemplateEditView tev=new TemplateEditView();
                            tev.setWidth(imageViewH);
                            tev.setHeight(imageViewW);
                            tev.setVisibility(imageView.getVisibility());
                            //tev.setText(imageView.getText().toString());
                            Field[] lpFields = imageViewLp.getClass().getFields();
                            AccessibleObject.setAccessible(lpFields,true);
                            for (Field field:lpFields){
                                try {
                                    //Log.e("gumy","lpFields-"+field.getName()+":"+field.get(imageViewLp));
                                    if (field.getName().equals("topMargin")){
                                        tev.setTopMargin(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("rightMargin")){
                                        tev.setRightMargin(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("bottomMargin")){
                                        tev.setBottomMargin(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("leftMargin")){
                                        tev.setLeftMargin(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("FILL_PARENT")){
                                        tev.setFillParent(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("MATCH_PARENT")){
                                        tev.setMatchParent(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("WRAP_CONTENT")){
                                        tev.setWrapContent(field.getInt(imageViewLp));
                                    }else if (field.getName().equals("gravity")){
                                        tev.setGravity(field.getInt(imageViewLp));
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            tev.setCreateBy(userId);
                            tev.setCreateDate(DateUtil.format(null,new Date()));
                            //tev.setTextSize(50);
                            //tev.setTextColor("#000");
                            tev.setId(imageView.getId());
                            tev.setViewId(imageView.getId());
                            tev.setParentViewId(template.getId());
                            tev.setViewType(ImageView.class.getName());
                            //saveOrUpdateTemplateEditView(tev);
                            //tev.save();
                            templateEditViewService.saveOrUpdateTemplateEditView(tev);
                            Log.e("gumy",tev.toString());
                        }
                        //templateEditViewService.saveOrUpdateTemplateEditView(tev);
                    }

                }
                Toast.makeText(this,"保存模板成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.preview_btn:

                break;
            case R.id.scale_b_btn:
                if (currentEditText!=null){
                    //float scaled = getResources().getDisplayMetrics().scaledDensity;
                    //如果使用自定义模板宽度大于useZpSDKWidth  80,文字大小使用缩放
                    if (template.getWidth()>useZpSDKWidth){
                        Integer scaleFontSize = temporaryTemplateEditViewTextSize.get(currentEditText.getId());
                        if (scaleFontSize==null){
                            scaleFontSize=4;
                        }else{
                           /* if (scaleFontSize>50){
                               Toast.makeText(this,"已经是最大了",Toast.LENGTH_SHORT).show();
                               break;
                            }*/
                            scaleFontSize++;
                        }
                        int textSize=0;
                        switch (scaleFontSize){
                            case 2:
                                textSize=48;
                                break;
                            case 3:
                                textSize=64;
                                break;
                            default:
                                textSize=64+(scaleFontSize-3)*32;
                        }
                        temporaryTemplateEditViewTextSize.put(currentEditText.getId(),scaleFontSize);
                        //更新到数据库
                        ContentValues values=new ContentValues();
                        values.put("scaleTextSize",scaleFontSize);
                        values.put("textSize",textSize);
                        DataSupport.updateAll(TemplateEditView.class,values,"parentViewId=? and viewId=?",template.getId().toString(),currentEditText.getId()+"");

                        currentEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                        Toast.makeText(this,currentEditText.getTextSize()+"",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    currentEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentEditText.getTextSize()+1);
                }
                break;
            case R.id.scale_s_btn:
                //震动
                //vibrator.vibrate(30);
                if (currentEditText!=null){
                    //使用缩放
                    //float scaled = getResources().getDisplayMetrics().scaledDensity;
                    //currentEditText.setTextSize((currentEditText.getTextSize()-1)/scaled);
                    if (template.getWidth()>useZpSDKWidth){
                        Integer scaleFontSize = temporaryTemplateEditViewTextSize.get(currentEditText.getId());
                        if (scaleFontSize==null){
                            scaleFontSize=2;
                        }else{
                           /* if (scaleFontSize==2){
                                Toast.makeText(this,"已经是最小了",Toast.LENGTH_SHORT).show();
                                break;
                            }*/
                            scaleFontSize--;
                        }

                        int textSize=0;
                        switch (scaleFontSize){
                            case 2:
                                textSize=48;
                                break;
                            case 3:
                                textSize=64;
                                break;
                            default:
                                textSize=64+(scaleFontSize-3)*32;
                        }
                        temporaryTemplateEditViewTextSize.put(currentEditText.getId(),scaleFontSize);
                        //更新到数据库
                        ContentValues values=new ContentValues();
                        values.put("scaleTextSize",scaleFontSize);
                        values.put("textSize",textSize);
                        DataSupport.updateAll(TemplateEditView.class,values,"parentViewId=? and viewId=?",template.getId().toString(),currentEditText.getId()+"");

                        //TemplateEditView templateEditView = DataSupport.select("scaleTextSize").where("parentViewId=? and viewId=?", template.getId().toString(), editText.getId() + "").findFirst(TemplateEditView.class);

                        currentEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
                        Toast.makeText(this,currentEditText.getTextSize()+"",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //使用像素
                    currentEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentEditText.getTextSize()-1);
                }
                break;
            case R.id.default_btn:

                if (layoutId!=R.layout.item_customer_template){
                    //删除所有的子控件
                    //templateParentCL.removeAllViews();
                    templateParent.removeAllViews();
                    //使用原布局
                    inflateFromLayout(templateData);
                }else {
                    //删除自定义布局中的控件
                    for (int i=0;i<templateParent.getChildCount();i++){
                        View childAt = templateParent.getChildAt(i);
                        if (childAt instanceof FrameLayout){
                            templateParent.removeView(childAt);
                        }
                    }
                }
                //反射已有的控件,注册触摸事件
                traversalView((ViewGroup) templateChildCL.findViewById(R.id.template_parent));
                //如果有一维码的模板,设置一维码图片
                makerBarcodeImg();
                temporaryTemplateEditViewTextSize.clear();
                //删除数据库数据
                int count = DataSupport.deleteAll(TemplateEditView.class, "parentViewId=?", template.getId() + "");
                //count+=DataSupport.deleteAll(Template.class,"layoutId=?",layoutId+"");
                Log.e("gumy","delete layoutId:"+layoutId+",数量:"+count);
                initIds();//初始化id
                break;

            case R.id.delete_btn:
                if (currentEditText!=null){
                    temporaryTemplateEditViewTextSize.put(currentEditText.getId(),3);
                    currentEditText.setVisibility(View.GONE);
                    //int visibility = currentEditText.getVisibility();
                    ConstraintLayout templateParent1 = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
                    templateParent1.removeView((View) currentEditText.getParent());
                    //删除数据库中数据
                    int count1=DataSupport.deleteAll(TemplateEditView.class, "parentViewId=? and viewId=?", template.getId() + "",currentEditText.getId()+"");
                    Log.e("gumy","delete viewId:"+currentEditText.getId()+",数量:"+count1);
                    //回收id
                    ids.addLast(currentEditText.getId());
                }
                break;
            case R.id.text_btn:
                if (ids.size()==0){
                    Toast.makeText(this,"已经放不下文本了",Toast.LENGTH_SHORT).show();
                    break;
                }
                //代码创建
                View newFrameLayout = makeNewEditText();
                //反射布局
                //View newFrameLayout = getLayoutInflater().inflate(R.layout.item_add_edittext, null);

                ConstraintLayout currTemplateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
                currTemplateParent.addView(newFrameLayout);
                break;
            case R.id.back:
                intent = new Intent(PrintTemplateActivity.this, SelectTemplateActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    int useZpSDKWidth=80;

    private Map<Integer,Integer> temporaryTemplateEditViewTextSize=new HashMap<Integer, Integer>();
    /**
     * 创建一个新的输入块,父级是FrameLayout,子控件是EditText
     * @return 父级FrameLayout
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View makeNewEditText(){

        FrameLayout newFrameLayout=new FrameLayout(this);
        EditText newEditText=new EditText(this);
        newEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX,66);
        newEditText.setHint("请输入");
        newEditText.setBackgroundColor(Color.WHITE);
        newEditText.setOnTouchListener(this);
        //newEditText.setMaxLines(1);//一行
        //newEditText.
        setEditTextId(newEditText);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        newEditText.setLayoutParams(params);
        //newEditText.setId(View.generateViewId());//系统自带的id生成器,每次启动程序从1开始,出现重复
        newFrameLayout.addView(newEditText);
        Log.e("gumy",newEditText.getId()+"");
        return newFrameLayout;
    }

    /**
     * 反射 新增用的空白布局
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View inflateFrameLayout(){
        View newFrameLayout = getLayoutInflater().inflate(R.layout.item_add_edittext, null);
        EditText editText = (EditText) newFrameLayout.findViewById(R.id.new_edit_text);
        editText.setOnTouchListener(this);
        setEditTextId(editText);
        //editText.setId(View.generateViewId());//系统自带的id生成器,每次启动程序从1开始,出现重复
        return newFrameLayout;
    }
    //用于增加子控件的id
    LinkedList<Integer> ids=new LinkedList<>();


    public void initIds(){
        ids.add(R.id.edit_text_1);
        ids.add(R.id.edit_text_2);
        ids.add(R.id.edit_text_3);
        ids.add(R.id.edit_text_4);
        ids.add(R.id.edit_text_5);
        ids.add(R.id.edit_text_6);
        ids.add(R.id.edit_text_7);
        ids.add(R.id.edit_text_8);
        ids.add(R.id.edit_text_9);
        ids.add(R.id.edit_text_10);
        ids.add(R.id.edit_text_11);
        ids.add(R.id.edit_text_12);
        ids.add(R.id.edit_text_13);
        ids.add(R.id.edit_text_14);
        ids.add(R.id.edit_text_15);
        ids.add(R.id.edit_text_16);
        ids.add(R.id.edit_text_17);
        ids.add(R.id.edit_text_18);
        ids.add(R.id.edit_text_19);
        ids.add(R.id.edit_text_20);
        ids.add(R.id.edit_text_21);
        ids.add(R.id.edit_text_22);
        ids.add(R.id.edit_text_23);
        ids.add(R.id.edit_text_24);
        ids.add(R.id.edit_text_25);
        ids.add(R.id.edit_text_26);
        ids.add(R.id.edit_text_27);
        ids.add(R.id.edit_text_28);
        ids.add(R.id.edit_text_29);
        ids.add(R.id.edit_text_30);
        ids.add(R.id.edit_text_31);
        ids.add(R.id.edit_text_32);
        ids.add(R.id.edit_text_33);
        ids.add(R.id.edit_text_34);
        ids.add(R.id.edit_text_35);
        ids.add(R.id.edit_text_36);
        ids.add(R.id.edit_text_37);
        ids.add(R.id.edit_text_38);
        ids.add(R.id.edit_text_39);
        ids.add(R.id.edit_text_40);
        //当前已有的去掉
        List<TemplateEditView> editViews = DataSupport.select("viewId").where("parentViewId=?", template.getId() + "").find(TemplateEditView.class);
        for(TemplateEditView editView:editViews){
            Log.e("gumy",editView.getViewId()+"----init-----"+editView.getId());
            ids.remove(editView.getViewId());
        }
        Log.e("gumy","----init----ids.size():"+ids.size());

    }
    public void setEditTextId(EditText editText){
        if (ids.size()>0){
            editText.setId(ids.getFirst());
            ids.removeFirst();
        }
    }


    public List<TemplateEditView> getTemplateEditView(){
        ConstraintLayout templateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        templateChildCL.measure(w, h);
        List<TemplateEditView> templateEditViews=new ArrayList<>();
        for (int i=0;i<templateParent.getChildCount();i++){
            if (templateParent.getChildAt(i) instanceof FrameLayout){
                FrameLayout frameLayout = (FrameLayout) templateParent.getChildAt(i);
                //如果是EditText
                TemplateEditView tev=new TemplateEditView();
                if (frameLayout.getChildAt(0) instanceof EditText){
                    EditText editText = (EditText) frameLayout.getChildAt(0);
                    editText.measure(w, h);
                    int editTextH = editText.getMeasuredHeight();
                    int editTextW = editText.getMeasuredWidth();
                    ViewGroup.LayoutParams editTextLp = editText.getLayoutParams();

                    tev.setWidth(editTextW);
                    tev.setHeight(editTextH);
                    tev.setText(editText.getText().toString());
                    tev.setViewType(EditText.class.getName());
                    Field[] lpFields = editTextLp.getClass().getFields();
                    AccessibleObject.setAccessible(lpFields,true);
                    for (Field field:lpFields){
                        try {
                            //Log.e("gumy","lpFields-"+field.getName()+":"+field.get(editTextLp));
                            if (field.getName().equals("topMargin")){
                                tev.setTopMargin(field.getInt(editTextLp));
                            }else if (field.getName().equals("rightMargin")){
                                tev.setRightMargin(field.getInt(editTextLp));
                            }else if (field.getName().equals("bottomMargin")){
                                tev.setBottomMargin(field.getInt(editTextLp));
                            }else if (field.getName().equals("leftMargin")){
                                tev.setLeftMargin(field.getInt(editTextLp));
                            }else if (field.getName().equals("FILL_PARENT")){
                                tev.setFillParent(field.getInt(editTextLp));
                            }else if (field.getName().equals("MATCH_PARENT")){
                                tev.setMatchParent(field.getInt(editTextLp));
                            }else if (field.getName().equals("WRAP_CONTENT")){
                                tev.setWrapContent(field.getInt(editTextLp));
                            }else if (field.getName().equals("gravity")){
                                tev.setGravity(field.getInt(editTextLp));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    tev.setTextSize(editText.getTextSize());
                    tev.setTextColor("#000");
                    tev.setId(editText.getId());
                    tev.setVisibility(editText.getVisibility());
                    tev.setViewId(editText.getId());
                    tev.setParentViewId(template.getId());
                    //获取文字缩放大小
                    TemplateEditView templateEditView = DataSupport.select("scaleTextSize").where("parentViewId=? and viewId=?", template.getId().toString(), editText.getId() + "").findFirst(TemplateEditView.class);
                    if (templateEditView!=null){
                        tev.setScaleTextSize(templateEditView.getScaleTextSize());
                    }else if (temporaryTemplateEditViewTextSize.get(editText.getId())!=null){
                        tev.setScaleTextSize(temporaryTemplateEditViewTextSize.get(editText.getId()));
                    }


                    Log.e("gumy",tev.toString());
                }else if (frameLayout.getChildAt(0) instanceof ImageView){
                    //获得文本控件
                    FrameLayout nextFrameLayout = (FrameLayout) templateParent.getChildAt(i+1);
                    EditText barcodeEditText = (EditText) nextFrameLayout.getChildAt(0);

                    ImageView imageView= (ImageView) frameLayout.getChildAt(0);
                    imageView.measure(w, h);
                    int imageViewH = imageView.getMeasuredHeight();
                    int imageViewW = imageView.getMeasuredWidth();
                    ViewGroup.LayoutParams imageViewLp = imageView.getLayoutParams();
                    tev.setWidth(imageViewH);
                    tev.setHeight(imageViewW);
                    tev.setVisibility(imageView.getVisibility());
                    tev.setText(barcodeEditText.getText().toString());//文本
                    Field[] lpFields = imageViewLp.getClass().getFields();
                    AccessibleObject.setAccessible(lpFields,true);
                    for (Field field:lpFields){
                        try {
                            //Log.e("gumy","lpFields-"+field.getName()+":"+field.get(imageViewLp));
                            if (field.getName().equals("topMargin")){
                                tev.setTopMargin(field.getInt(imageViewLp));
                            }else if (field.getName().equals("rightMargin")){
                                tev.setRightMargin(field.getInt(imageViewLp));
                            }else if (field.getName().equals("bottomMargin")){
                                tev.setBottomMargin(field.getInt(imageViewLp));
                            }else if (field.getName().equals("leftMargin")){
                                tev.setLeftMargin(field.getInt(imageViewLp));
                            }else if (field.getName().equals("FILL_PARENT")){
                                tev.setFillParent(field.getInt(imageViewLp));
                            }else if (field.getName().equals("MATCH_PARENT")){
                                tev.setMatchParent(field.getInt(imageViewLp));
                            }else if (field.getName().equals("WRAP_CONTENT")){
                                tev.setWrapContent(field.getInt(imageViewLp));
                            }else if (field.getName().equals("gravity")){
                                tev.setGravity(field.getInt(imageViewLp));
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    tev.setId(imageView.getId());
                    tev.setViewId(imageView.getId());
                    tev.setParentViewId(template.getId());
                    tev.setViewType(ImageView.class.getName());
                    Log.e("gumy",tev.toString());
                }
                templateEditViews.add(tev);
            }
        }
        return templateEditViews;
    }




    /**
     * 使用MyDataOpenHelper保存
     * 181217弃用,改用litepal实现
     * @param template
     * @return
     */
    @Deprecated
    public long saveOrUpdateTemplate(Template template) {
        MyDataOpenHelper<Template> dataOpenHelper=new MyDataOpenHelper<Template>(this,Template.class);
        int count = dataOpenHelper.queryCount("select count(1) from " + TableNames.TEMPLATE_PARENT_TABLE + " where layoutId=?", template.getLayoutId().toString());
        if (count>0){
            //修改
            String sql="update sys_template set width=? ,height=? where layoutId=?";
            //dataOpenHelper.execSQL(sql,new Object[]{template.getWidth(),template.getHeight(),template.getLayoutId()});
            Log.e("gumy","execSQL:"+sql);
        }else {
            //增加
            String sql="insert into sys_template (layoutId,templateName,viewType,printType,paperType,width,height,createBy,createDate) values(?,?,?,?,?,?,?,?,?)";
            dataOpenHelper.execSQL(sql,new Object[]{template.getLayoutId(),template.getTemplateName(),template.getViewType(),template.getPrintType(),template.getPaperType(),template.getWidth(),template.getHeight(),template.getCreateBy(),template.getCreateDate()});
            Log.e("gumy","execSQL:"+sql);
        }
        return 1;
    }
    /**
     * 使用MyDataOpenHelper保存
     * 181217弃用,改用litepal实现
     * @param templateEditView
     * @return
     */
    @Deprecated
    public long saveOrUpdateTemplateEditView(TemplateEditView templateEditView) {
        MyDataOpenHelper<TemplateEditView> dataOpenHelper=new MyDataOpenHelper<TemplateEditView>(this,TemplateEditView.class);

        int count = dataOpenHelper.queryCount("select count(1) from sys_template_edit_view where viewId=? and parentViewId=?", templateEditView.getViewId().toString(), templateEditView.getParentViewId().toString());
        if (count>0){
            int update = dataOpenHelper.update(TableNames.EDIT_VIEW_TABLE, Object2Values.object2Values(templateEditView), " viewId=? and parentViewId=?", templateEditView.getViewId().toString(), templateEditView.getParentViewId().toString());
            Log.e("gumy","update sys_template_edit_view success");
            return update;
        }else {
            long insert = dataOpenHelper.insert(TableNames.EDIT_VIEW_TABLE, null, Object2Values.object2Values(templateEditView));
            Log.e("gumy","insert sys_template_edit_view success");
            return insert;
        }


    }

    public void moveInParent(){
        //判断是否超出父级
               /* int marginLeft = 0;
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
                        ViewGroup parent = (ViewGroup) view.getParent().getParent();
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
                       *//* mLastLeft = left;
                        mLastRight = right;
                        mLastTop = top;
                        mLastBottom = bottom;*//*
                    }
                }*/
    }
}
