package cn.zcbdqn.labelprinter.service;

import cn.zcbdqn.labelprinter.pojo.Template;

/**
 * Created by gumuyun on 2018/12/15.
 */

public interface TemplateService {
    long saveOrUpdateTemplate(Template template);
}
