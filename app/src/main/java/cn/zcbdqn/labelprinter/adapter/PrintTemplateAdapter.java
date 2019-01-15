package cn.zcbdqn.labelprinter.adapter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Map;

import cn.zcbdqn.labelprinter.R;

/**
 * Created by gumuyun on 2018/11/20.
 */

public class PrintTemplateAdapter extends TemplateSelectAdapter{

    private TemplateHolder1 holderTemplate1=null;
    private TemplateHolder2 holderTemplate2=null;
    private TemplateHolder3 holderTemplate3=null;
    private TemplateHolder4 holderTemplate4=null;

    public PrintTemplateAdapter() {
    }

    public PrintTemplateAdapter(Context context) {
        super(context);
    }

    public View inflaterTemplate(int layoutId ){
        View convertView=null;

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
                holderTemplate4.goodsName= (EditText) convertView.findViewById(R.id.goods_name);
                break;
        }

        return convertView;
    }


    public Holder putTemplateData(Map<String,Object> map, int layoutId){

        switch (layoutId) {
            case R.layout.item_commom1_template_40_20:
                holderTemplate1.goodsName.setText((String)map.get("goodsName"));
                //holderTemplate1.goodsNameText.setText((String)map.get("goodsNameText"));
                holderTemplate1.goodsSize.setText((String)map.get("goodsSize"));
                //holderTemplate1.goodsSizeText.setText((String)map.get("goodsSizeText"));
                holderTemplate1.goodsIngredient.setText((String)map.get("goodsIngredient"));
                //holderTemplate1.goodsIngredientText.setText((String)map.get("goodsIngredientText"));
                return holderTemplate1;
            case R.layout.item_commom2_template_40_30:
                holderTemplate2.goodsName.setText((String)map.get("goodsName"));
                holderTemplate2.goodsType.setText((String)map.get("goodsType"));
                //holderTemplate2.goodsTypeText.setText((String)map.get("goodsTypeText"));
                holderTemplate2.goodsPrice.setText((String)map.get("goodsPrice"));
                //holderTemplate2.goodsPriceText.setText((String)map.get("goodsPriceText"));
                holderTemplate2.barcodeImage.setImageResource((Integer)map.get("barcodeImage"));
                holderTemplate2.barcodeText.setText((String)map.get("barcodeText"));
                return holderTemplate2;
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
                return holderTemplate3;
            case R.layout.item_commom4_template_58_40:
                holderTemplate4.goodsName.setText((String)map.get("goodsName"));
                return holderTemplate4;
        }
        return null;
    }
}
