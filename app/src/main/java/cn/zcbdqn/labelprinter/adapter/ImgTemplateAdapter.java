package cn.zcbdqn.labelprinter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import org.litepal.crud.DataSupport;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.util.BitmapUtil;

/**
 * Created by gumuyun on 2018/12/10.
 */

public class ImgTemplateAdapter extends BaseAdapter {
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;
    private Context context;
    public ImgTemplateAdapter(Context context,List<Map<String, Object>> list) {
        this.list = list;
        this.context=context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list==null?null:list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_commom_template,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.template_img);
            viewHolder.templateName= (TextView) convertView.findViewById(R.id.template_name);
            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = list.get(position);
        Integer img = (Integer) map.get("img");
        viewHolder.templateName.setText("模板名称:"+(String)map.get("templateName"));

        Integer layoutId = (Integer) map.get("layoutId");

        if(layoutId==R.layout.item_customer_template){
            //Bitmap bitmap = inflateLayoutFromDataBase((Template) map.get("template"));
            //viewHolder.imageView.setImageBitmap(bitmap);
        }else {
            viewHolder.imageView.setImageResource(img);//固定模板使用资源图片,自定义模版生成模板???
        }
        return convertView;
    }

    private Bitmap inflateLayoutFromDataBase(Template template) {
        //从数据库中查询模板
        //Template template = DataSupport.where("layoutId=?", layoutId+"").findFirst(Template.class);
        //Template template = (Template) templateData.get("template");

        //布局
        ConstraintLayout templateChildCL = (ConstraintLayout) inflater.inflate(template.getLayoutId(), null);
        TextView bottomSizeDisplay = (TextView) templateChildCL.findViewById(R.id.bottom_size_display);
        TextView rightSizeDisplay = (TextView) templateChildCL.findViewById(R.id.right_size_display);
        bottomSizeDisplay.setText(template.getWidth().toString()+"mm");
        rightSizeDisplay.setText(template.getHeight().toString()+"mm");

        //布局中的父局,要打印的块
        ConstraintLayout templateParent = (ConstraintLayout) templateChildCL.findViewById(R.id.template_parent);
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
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, template.getWidth(), context.getResources().getDisplayMetrics());
        int height =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, template.getHeight(), context.getResources().getDisplayMetrics());

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
                        FrameLayout newFrameLayout=new FrameLayout(context);
                        editText=new EditText(context);
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX,50);
                        editText.setHint("请输入");
                        editText.setBackgroundColor(Color.WHITE);
                        //editText.setOnTouchListener(this);
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

        templateParent.setDrawingCacheEnabled(true);//缓存图片
        templateParent.buildDrawingCache(true);//硬件加速
        Bitmap cacheBitmap=templateParent.getDrawingCache();

        if (template.getWidth()>60){
            return BitmapUtil.zoomImg(cacheBitmap,(int)(template.getWidth()*0.5),(int)(template.getHeight()*0.5));
        }
        return cacheBitmap;
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
                Toast.makeText(context,"生成一维码失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            imageView.setImageResource(R.mipmap.barcode);
        }
    }
    private static class ViewHolder{
        ImageView imageView;
        TextView templateName;
    }
}
