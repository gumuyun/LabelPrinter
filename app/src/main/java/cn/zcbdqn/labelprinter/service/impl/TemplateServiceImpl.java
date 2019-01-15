package cn.zcbdqn.labelprinter.service.impl;

import android.util.Log;

import org.litepal.crud.DataSupport;

import cn.zcbdqn.labelprinter.R;
import cn.zcbdqn.labelprinter.pojo.Template;
import cn.zcbdqn.labelprinter.service.TemplateService;

/**
 * Created by gumuyun on 2018/12/17.
 */

public class TemplateServiceImpl implements TemplateService {
    @Override
    public long saveOrUpdateTemplate(Template template) {

        //如果是自定义模板则修改
        if (template.getLayoutId()== R.layout.item_customer_template){
            return template.updateAll("id=?",template.getId().toString());
        }

        //公共模板
        int count = DataSupport.where("id=?", template.getId()+"").count(Template.class);
        Log.e("gumy","TemplateServiceImpl.saveOrUpdateTemplate:count="+count);
        if (count>0){
            count=template.updateAll("id=?",template.getId()+"");
            Log.e("gumy","update:template"+template.toString());
        }else {
            boolean isSave = template.save();
            if (isSave){
                Log.e("gumy","insert:template--"+template.toString());
                return 1;
            }
        }
        return count;
    }

    public boolean save(Template template){
        return template.save();
    }

    public int update(Template template){
       return template.updateAll("id=?",template.getId()+"");
    }
}
