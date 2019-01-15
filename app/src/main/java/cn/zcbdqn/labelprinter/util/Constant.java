package cn.zcbdqn.labelprinter.util;

/**
 * Created by gumuyun on 2018/11/20.
 */

/**
 * 把图片转换为实际要打印的大小
 * 宽 实际毫米*8
 * 高 实际毫米*8+18  18为每个标签之间的距离
 */
public interface Constant {

    int COMMON1_TEMPLATE_WIDTH=320;
    int COMMON1_TEMPLATE_HEIGHT=178;
    int COMMON2_TEMPLATE_WIDTH=320;
    int COMMON2_TEMPLATE_HEIGHT=258;
    int COMMON3_TEMPLATE_WIDTH=258;
    int COMMON3_TEMPLATE_HEIGHT=320;
    int COMMON4_TEMPLATE_WIDTH=464;
    int COMMON4_TEMPLATE_HEIGHT=338;


    int PAGE_TYPE1=1;
    int PAGE_TYPE2=2;
    int PAGE_TYPE3=3;
    int PRINTER_TYPE1=1;
    int PRINTER_TYPE2=2;
    int PRINTER_TYPE3=3;
    int PRINTER_TYPE4=4;

    int useZpSDKWidth=80;

}
