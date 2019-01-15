package cn.zcbdqn.labelprinter.service.impl;

import android.util.Log;

import org.litepal.crud.DataSupport;

import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.service.TemplateEditViewService;

/**
 * Created by gumuyun on 2018/12/17.
 */

public class TemplateEditViewServiceImpl implements TemplateEditViewService {
    @Override
    public long saveOrUpdateTemplateEditView(TemplateEditView templateEditView) {
        //按viewId和parentViewId查询,如果有,则更新,否则保存
        int count = DataSupport.where("viewId=? and parentViewId=?", templateEditView.getViewId().toString(), templateEditView.getParentViewId().toString()).count(TemplateEditView.class);
        Log.e("gumy","TemplateEditViewServiceImpl.saveOrUpdateTemplateEditView:count="+count);
        if (count>0){
            count=templateEditView.updateAll("viewId=? and parentViewId=?", templateEditView.getViewId().toString(), templateEditView.getParentViewId().toString());
            Log.e("gumy","update:"+templateEditView.toString());
        }else {
            boolean isSave = templateEditView.save();
            if (isSave){
                Log.e("gumy","insert:"+templateEditView.toString());
                return 1;
            }
        }
        return count;
    }
}
