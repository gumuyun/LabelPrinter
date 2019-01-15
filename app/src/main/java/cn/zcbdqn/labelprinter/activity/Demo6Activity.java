package cn.zcbdqn.labelprinter.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.zxing.WriterException;

import org.litepal.crud.DataSupport;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.util.BitmapUtil;

public class Demo6Activity extends AppCompatActivity {

    private ConstraintLayout template;
    private int layoutId;
    private String templateName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo6);
        //存放模板布局的view
        template= (ConstraintLayout) findViewById(R.id.template);
        //获得intent
        Intent intent = getIntent();
        //数据
        HashMap<String, Object> templateData = (HashMap<String, Object>) intent.getSerializableExtra("templateData");
        //模板父级
        //模板ID
        layoutId = (Integer) templateData.get("layoutId");
        templateName = (String) templateData.get("templateName");

        //从数据库中查询模板
        Template template = DataSupport.where("layoutId=?", layoutId+"").findFirst(Template.class);

        //布局
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(layoutId, null);
        //布局中的父局,要打印的块
        ConstraintLayout templateParent = (ConstraintLayout) constraintLayout.findViewById(R.id.template_parent);
        //从数据库中查询出所有的子模板
        List<TemplateEditView> templateEditViews = DataSupport.where("parentViewId=?", layoutId + "").find(TemplateEditView.class);

        Log.e("gumy",template.toString());
        //创建模板参数对象,layout
        ConstraintLayout.LayoutParams layoutParams=new ConstraintLayout.LayoutParams(template.getWidth(),template.getHeight());
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
                    //设置文字
                    editText.setText(templateEditView.getText());
                    //颜色
                    editText.setTextColor(Color.BLACK);
                    //文字大小,像素
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,templateEditView.getTextSize());
                    /*editText.setTop(templateEditView.getTopMargin());
                    editText.setRight(templateEditView.getRightMargin());
                    editText.setBottom(templateEditView.getBottomMargin());
                    editText.setLeft(templateEditView.getLeftMargin());*/
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
                }else if (aClass.equals(ImageView.class)){
                    //是图片使用,文字生成图片
                    EditText editText = (EditText) templateParent.findViewById(templateEditViews.get(i + 1).getViewId());
                    Bitmap barcodeBitMap1 = BitmapUtil.createOneDCode(editText.getText().toString());
                    ImageView imageView= (ImageView) templateParent.findViewById(templateEditViews.get(i).getViewId());
                    imageView.setImageBitmap(barcodeBitMap1);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        this.template.addView(constraintLayout);

    }


}
