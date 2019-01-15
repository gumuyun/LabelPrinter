package cn.zcbdqn.labelprinter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import cn.zcbdqn.labelprinter.R;

/**
 * Created by gumuyun on 2018/11/18.
 */

public class TemplateSelectAdapter extends BaseAdapter {

    /**
     * 布局文件ID集合,要与数据顺序一致
     */
    private List<Integer> layoutIds;
    protected LayoutInflater inflater;
    private List<Map<String,Object>> list;

    public TemplateSelectAdapter() {
    }

    public TemplateSelectAdapter(Context context) {
        this.inflater=LayoutInflater.from(context);
    }

    public TemplateSelectAdapter(Context context, List<Integer> layoutIds){
        this.layoutIds=layoutIds;
        this.inflater=LayoutInflater.from(context);
    }
    public TemplateSelectAdapter(Context context,List<Integer> layoutIds,List<Map<String,Object>> list){
        this.layoutIds=layoutIds;
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }

    public void setLayoutIds(List<Integer> layoutIds) {
        this.layoutIds = layoutIds;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    /**
     * 获得item数量
     * @return
     */
    @Override
    public int getViewTypeCount() {
        Log.e("gumy","getViewTypeCount-count:"+layoutIds.size());
        return layoutIds.size();
    }

    /**
     * 获得item类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //Log.e("gumy","getItemViewType-position:"+position);
       // return layoutIds.get(position); 报数组越界异常,getItemViewType的值将做为下标去数组中取数据,
       return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TemplateHolder1 holderTemplate1=null;
        TemplateHolder2 holderTemplate2=null;
        TemplateHolder3 holderTemplate3=null;
        TemplateHolder4 holderTemplate4=null;

        int layoutId=layoutIds.get(position);

        //不同的布局选择
        //inflaterTemplate(layoutId,convertView);
        if (convertView==null){

            switch (layoutId){
                case R.layout.item_commom1_template_40_20:
                    //反射公共模版1
                    convertView=inflater.inflate(layoutId,null);
                    holderTemplate1=new TemplateHolder1();
                    //子控件
                    holderTemplate1.goodsName= (EditText) convertView.findViewById(R.id.goods_name);
                    //holderTemplate1.goodsNameText= (TextView) convertView.findViewById(R.id.goods_name_text);
                    holderTemplate1.goodsSize= (EditText) convertView.findViewById(R.id.goods_size);
                    //holderTemplate1.goodsSizeText= (TextView) convertView.findViewById(R.id.goods_size_text);
                    holderTemplate1.goodsIngredient= (EditText) convertView.findViewById(R.id.goods_ingredient);
                    //holderTemplate1.goodsIngredientText= (TextView) convertView.findViewById(R.id.goods_ingredient_text);
                    convertView.setTag(holderTemplate1);
                    break;
                case R.layout.item_commom2_template_40_30:
                    //反射公共模版2
                    convertView=inflater.inflate(layoutId,null);
                    holderTemplate2=new TemplateHolder2();
                    //子控件
                    holderTemplate2.goodsName= (EditText) convertView.findViewById(R.id.goods_name);
                    holderTemplate2.goodsType= (EditText) convertView.findViewById(R.id.goods_type);
                    // holderTemplate2.goodsTypeText= (TextView) convertView.findViewById(R.id.goods_type_text);
                    holderTemplate2.goodsPrice= (EditText) convertView.findViewById(R.id.goods_price);
                    //holderTemplate2.goodsPriceText= (TextView) convertView.findViewById(R.id.goods_price_text);
                    holderTemplate2.barcodeImage= (ImageView) convertView.findViewById(R.id.barcode);
                    holderTemplate2.barcodeText= (EditText) convertView.findViewById(R.id.barcode_text);
                    convertView.setTag(holderTemplate2);
                    break;
                case R.layout.item_commom3_template_30_40:
                    convertView=inflater.inflate(layoutId,null);
                    holderTemplate3=new TemplateHolder3();

                    holderTemplate3.goodsName= (EditText) convertView.findViewById(R.id.goods_name);
                    // holderTemplate3.goodsNameText= (EditText) convertView.findViewById(R.id.goods_name_text);
                    holderTemplate3.goodsType= (EditText) convertView.findViewById(R.id.goods_type);
                    //holderTemplate3.goodsTypeText= (EditText) convertView.findViewById(R.id.goods_name_text);
                    holderTemplate3.goodsColor= (EditText) convertView.findViewById(R.id.goods_color);
                    //holderTemplate3.goodsColorText= (EditText) convertView.findViewById(R.id.goods_color_text);
                    holderTemplate3.goodsPrice= (EditText) convertView.findViewById(R.id.goods_price);
                    //holderTemplate3.goodsPriceText= (EditText) convertView.findViewById(R.id.goods_price_text);
                    holderTemplate3.goodsStyle= (EditText) convertView.findViewById(R.id.goods_style);
                    //holderTemplate3.goodsStyleText= (EditText) convertView.findViewById(R.id.goods_style_text);
                    convertView.setTag(holderTemplate3);
                    break;
                case R.layout.item_commom4_template_58_40:
                    convertView=inflater.inflate(layoutId,null);
                    holderTemplate4=new TemplateHolder4();
                    //子控件
                    holderTemplate4.goodsName= (EditText) convertView.findViewById(R.id.goods_name);
                    break;
            }
        }else {
            switch (layoutId) {
                case R.layout.item_commom1_template_40_20:
                    holderTemplate1= (TemplateHolder1) convertView.getTag();
                    break;
                case R.layout.item_commom2_template_40_30:
                    holderTemplate2= (TemplateHolder2) convertView.getTag();
                    break;
                case R.layout.item_commom3_template_30_40:
                    holderTemplate3= (TemplateHolder3) convertView.getTag();
                    break;
                case R.layout.item_commom4_template_58_40:
                    holderTemplate4= (TemplateHolder4) convertView.getTag();
                    break;
            }
        }
        //获得行数据
        Map<String,Object> map=list.get(position);
        //设置数据
        switch (layoutId) {
            case R.layout.item_commom1_template_40_20:
                holderTemplate1.goodsName.setText((String)map.get("goodsName"));
                //holderTemplate1.goodsNameText.setText((String)map.get("goodsNameText"));
                holderTemplate1.goodsSize.setText((String)map.get("goodsSize"));
                //holderTemplate1.goodsSizeText.setText((String)map.get("goodsSizeText"));
                holderTemplate1.goodsIngredient.setText((String)map.get("goodsIngredient"));
                //holderTemplate1.goodsIngredientText.setText((String)map.get("goodsIngredientText"));
                break;
            case R.layout.item_commom2_template_40_30:
                holderTemplate2.goodsName.setText((String)map.get("goodsName"));
                holderTemplate2.goodsType.setText((String)map.get("goodsType"));
                //holderTemplate2.goodsTypeText.setText((String)map.get("goodsTypeText"));
                holderTemplate2.goodsPrice.setText((String)map.get("goodsPrice"));
                //holderTemplate2.goodsPriceText.setText((String)map.get("goodsPriceText"));
                holderTemplate2.barcodeImage.setImageResource((Integer)map.get("barcodeImage"));
                holderTemplate2.barcodeText.setText((String)map.get("barcodeText"));
                break;
            case R.layout.item_commom3_template_30_40:
                holderTemplate3.goodsName.setText((String)map.get("goodsName"));
                //holderTemplate3.goodsNameText.setText((String)map.get("goodsNameText"));
                holderTemplate3.goodsType.setText((String)map.get("goodsType"));
                //holderTemplate3.goodsTypeText.setText((String)map.get("goodsTypeText"));
                holderTemplate3.goodsColor.setText((String)map.get("goodsColor"));
                //holderTemplate3.goodsColorText.setText((String)map.get("goodsColorText"));
                holderTemplate3.goodsPrice.setText((String)map.get("goodsPrice"));
                //holderTemplate3.goodsPriceText.setText((String)map.get("goodsPriceText"));
                holderTemplate3.goodsStyle.setText((String)map.get("goodsStyle"));
                //holderTemplate3.goodsStyleText.setText((String)map.get("goodsStyleText"));
                break;
            case R.layout.item_commom4_template_58_40:
                holderTemplate4.goodsName.setText((String)map.get("goodsName"));
                break;
        }
        return convertView;
    }

    protected static class Holder<T>{
        protected Class<T> clazz;
    }

    protected static class TemplateHolder1 extends Holder<TemplateHolder1>{
        EditText goodsName;
        //TextView goodsNameText;
        EditText goodsSize;
        //TextView goodsSizeText;
        EditText goodsIngredient;
        //TextView goodsIngredientText;

    }
    protected static class TemplateHolder2 extends Holder<TemplateHolder2>{
        EditText goodsName;
        EditText goodsType;
        //TextView goodsTypeText;
        EditText goodsPrice;
        //TextView goodsPriceText;
        ImageView barcodeImage;
        EditText barcodeText;
    }
    protected static class TemplateHolder3 extends Holder<TemplateHolder3>{
        EditText goodsName;
        //EditText goodsNameText;
        EditText goodsType;
        //EditText goodsTypeText;
        EditText goodsPrice;
        //EditText goodsPriceText;
        EditText goodsStyle;
        //EditText goodsStyleText;
        EditText goodsColor;
        //EditText goodsColorText;
    }
    protected static class TemplateHolder4 extends Holder<TemplateHolder4>{
        EditText goodsName;
    }
}
