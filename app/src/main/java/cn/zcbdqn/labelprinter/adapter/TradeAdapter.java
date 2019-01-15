package cn.zcbdqn.labelprinter.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zcbdqn.labelprinter.R;

/**
 *
 * Created by gumuyun on 2018/11/18.
 */

public class TradeAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private static List<Map<String,Object>> list;

    /**
     * 初始化,并加载values里的资源
     * 图片,中文行业名,英文行业名
     * @param context
     */
    public TradeAdapter(Context context) {
        //资源集合只加载一次,本该在static块中加载,但需要用到context,在构造器中加载
        if (list==null){
            list=new ArrayList<Map<String,Object>>();
            //图片ID
            TypedArray images = context.getResources().obtainTypedArray(R.array.trade_images);
            //中文行业名
            String[] titlesCN = context.getResources().getStringArray(R.array.trade_titles_cn);
            //英文行业名
            String[] titlesEN = context.getResources().getStringArray(R.array.trade_titles_en);

            if (images!=null) {
                //增加到集合中,准备使用,
                for (int i = 0; i < images.length(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("tradeIcon", images.getResourceId(i, R.mipmap.default_home_64));
                    map.put("tradeTitleCn", titlesCN[i]);
                    map.put("tradeTitleEn", titlesEN[i]);
                    list.add(map);
                }
            }
            images.recycle();//回收
        }
        this.inflater = LayoutInflater.from(context);
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
        TradeAdapterHelper helper=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.item_trade_layout,null);
            helper=new TradeAdapterHelper();

            helper.tradeIcon= (ImageView) convertView.findViewById(R.id.trade_icon);
            helper.tradeTitleCn= (TextView) convertView.findViewById(R.id.trade_title_cn);
            helper.tradeTitleEn= (TextView) convertView.findViewById(R.id.trade_title_en);
            convertView.setTag(helper);
        }else {
            helper= (TradeAdapterHelper) convertView.getTag();
        }
        Map<String,Object> map=list.get(position);
        //Log.e("gumy","TradeAdapter position:"+position+" ,map:"+map.toString());
        helper.tradeIcon.setImageResource((Integer)map.get("tradeIcon"));
        String tradeTitleCn = (String) map.get("tradeTitleCn");
        String tradeTitleEn = (String) map.get("tradeTitleEn");
        helper.tradeTitleCn.setText(tradeTitleCn);
        helper.tradeTitleEn.setText(tradeTitleEn);

        return convertView;
    }


    //助手类
    private static class TradeAdapterHelper{
        ImageView tradeIcon;
        TextView tradeTitleCn;
        TextView tradeTitleEn;
    }
}
