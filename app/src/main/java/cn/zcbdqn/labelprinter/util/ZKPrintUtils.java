package cn.zcbdqn.labelprinter.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.zcbdqn.labelprinter.pojo.TemplateEditView;
import cn.zcbdqn.labelprinter.zpprint.MyZpBluetoothPrinter;
import zpSDK.zpSDK.PrinterInterface;
import zpSDK.zpSDK.zpBluetoothPrinter;

public class ZKPrintUtils {

    /**
     * 打印标签
     *
     * @param bluetoothDeviceAddress 蓝牙打印机地址
     * @param barCodeText            一维码
     * @param printBarCodeText       是否打印一维码文字
     * @param qrCode                 二维码
     * @param printQrCodeText        是否打印二维码文字
     * @param drawableId             图片资源ID
     * @param context                上下文
     */
    public static void print(String bluetoothDeviceAddress,
                             String barCodeText,
                             boolean printBarCodeText,
                             String qrCode,
                             boolean printQrCodeText,
                             int drawableId, Context context) {
        //创建对象
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(context);
        //判断是否连接打印机
        if (!zpSDK.connect(bluetoothDeviceAddress)) {
            Toast.makeText(context, "连接打印机失败", Toast.LENGTH_LONG).show();
            return;
        }

        int text_x = 0;//距左边界的位置,用于计算居中
        int height = 20;//开始高度
        int barCodeHeight = 50;//一维码高度
        int qrCodeVer = 6;//二维码级别,,,,预计高度=级别*40
        int pageHeight = height + barCodeHeight;//设置面页高度,
        int pageWidth = 785;//设置面页宽度,
        //如果打印一维码文字页面高度增加36
        if (printBarCodeText) {
            pageHeight += 36;
        }
        //如果打印二维码页面高度增加    级别*40
        if (qrCode != null && !"".equals(qrCode)) {
            pageHeight += qrCodeVer * 40;
        }
        //如果打印二维码文字页面高度增加36
        if (printQrCodeText) {
            pageHeight += 36;
        }
        pageHeight += 50;
        // 设置页面宽高
        //zpSDK.pageSetup(668, pageHeight);
        zpSDK.pageSetup(pageWidth, pageHeight);

        // 打印一维码--参数-水平起始位置,垂直位置,内容,类型,是否旋转,线条粗细,高度
        zpSDK.drawBarCode(100, 20, barCodeText, 100, false, 3, barCodeHeight);
        if (printBarCodeText) {
            // 打印文字--参数-水平起始位置,垂直位置,内容,字体大小,旋转,加粗,反转,下划线
            height += barCodeHeight;
            text_x = (int) ((pageWidth - barCodeText.length() * 20) / 2 * 0.71);
            Log.e("gumy", "print barCodeText left:" + text_x);
            zpSDK.drawText(text_x, height, barCodeText, 3, 0, 0, false, false);
        }
        if (qrCode != null && !"".equals(qrCode)) {
            height += 36;
            // 打印二维码---参数-水平起始位置,垂直位置,二维码文字,旋转,宽度,?
            text_x = (pageWidth - qrCodeVer * 40) / 2;
            zpSDK.drawQrCode(190, height, qrCode, 0, 6, 6);
        }
        if (printQrCodeText) {
            height += qrCodeVer * 40;
            text_x = (int) ((pageWidth - qrCode.length() * 20) / 2 * 0.71);
            // 打印二维码文字--参数-水平起始位置,垂直位置,内容,字体大小,旋转,加粗,反转,下划线
            zpSDK.drawText(text_x, height, qrCode, 3, 0, 0, false, false);
        }
        if (drawableId != 0) {
            //需要调整位置
            // 打印图片-- 参数-水平起始位置,垂直位置,bmp_size_x,bmp_size_y,图片
            //资源对象,可以获得图片等
            Resources res = context.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, drawableId);
            zpSDK.drawGraphic(90, 48, 0, 0, bmp);
        }


        zpSDK.print(0, 0);

        zpSDK.printerStatus();
        int a = zpSDK.GetStatus();

        if (a == -1) { // "获取状态异常------";
            Toast.makeText(context, "获取状态异常", Toast.LENGTH_LONG).show();

        }
        if (a == 1) {// "缺纸----------";
            Toast.makeText(context, "缺纸", Toast.LENGTH_LONG).show();
        }
        if (a == 2) {
            // "开盖----------";
            Toast.makeText(context, "纸仓未合上", Toast.LENGTH_LONG).show();
        }
        if (a == 0) {

            // "打印机正常-------";
            Toast.makeText(context, "打印完成", Toast.LENGTH_LONG).show();
        }

        zpSDK.disconnect();
    }

    /**
     * 打印图片
     *
     * @param context                上下文
     * @param bluetoothDeviceAddress 蓝牙打印机地址
     * @param bitmap                 图片
     * @param pageWidth              页面宽
     * @param pageHeight             页面高
     */
    public static int drawGraphic(Context context, String bluetoothDeviceAddress, Bitmap bitmap, int pageWidth, int pageHeight) {

        //创建对象
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(context);
        //判断是否连接打印机
        if (!zpSDK.connect(bluetoothDeviceAddress)) {
            //Toast.makeText(context, "连接打印机失败", Toast.LENGTH_LONG).show();
            return -2;
        }
        Log.e("gumy", "drawGraphic准备打印图片:页面宽高===>" + pageWidth + ":" + pageHeight);
        zpSDK.pageSetup(pageWidth, pageHeight);
        zpSDK.drawGraphic(0, 0, 0, 0, bitmap);
        zpSDK.print(0, 0);
        zpSDK.printerStatus();
        int status = zpSDK.GetStatus();
        bitmap.recycle();
        zpSDK.disconnect();
        return status;
    }

    public static int getStatus(Context context, String bluetoothDeviceAddress) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter(context);
        if (!zpSDK.connect(bluetoothDeviceAddress)) {
            //Toast.makeText(context, "连接打印机失败", Toast.LENGTH_LONG).show();
            return -1;
        }
        //zpSDK.pageSetup(1, 1);
        //zpSDK.drawText(1, 1, "", 1, 0, 0, false, false);
        //zpSDK.print(0, 0);
        zpSDK.printerStatus();
        int status = zpSDK.GetStatus();
        zpSDK.disconnect();
        return status;
    }


    public static int printByEditText(Context context, String blueToothPrintAddress, List<TemplateEditView> templateEditViews, int pageWidth, int pageHeight, boolean useCustomSdk) {
        int status = -2;
        //创建对象
        PrinterInterface zpSDK = null;
        //判断是否使用自定义SDK
        if (useCustomSdk) {
            zpSDK = new MyZpBluetoothPrinter(context);//自定义的类
        } else {
            zpSDK = new zpBluetoothPrinter(context);//原装sdk
        }

        //相素转毫米

        //判断是否连接打印机
        if (!zpSDK.connect(blueToothPrintAddress)) {
            //Toast.makeText(context, "连接打印机失败", Toast.LENGTH_LONG).show();
            return status;
        }
        Log.e("gumy", "printByEditText准备打印文字:页面宽高===>" + pageWidth + ":" + pageHeight);
        zpSDK.pageSetup(pageWidth, pageHeight);

        try {
            if (templateEditViews != null && templateEditViews.size() > 0) {
                int height = 20;
                for (TemplateEditView templateEditView : templateEditViews) {
                    float topMargin = GraphicUtil.px2mm(templateEditView.getTopMargin(), context.getResources().getDisplayMetrics()) * 8 + 0.5F;
                    float viewHeight = GraphicUtil.px2mm(templateEditView.getHeight(), context.getResources().getDisplayMetrics()) * 8 + 0.5F;
                    float leftMargin = GraphicUtil.px2mm(templateEditView.getLeftMargin(), context.getResources().getDisplayMetrics()) * 8 + 0.5F;
                    Float textSize = templateEditView.getTextSize();
                    int fontSize = (int) (GraphicUtil.px2mm(textSize, context.getResources().getDisplayMetrics()) * 8 + 0.5F);
                    //height = (int) (topMargin + height);

                    //2#----48px     递增0      50+0*16
                    //3#----64px     递增16     50+1*16
                    //4#----98px     递增32     64+(4-3)*32
                    //5#----128px    递增32     64+(5-3)*32
                    //6#----152px
                    Log.e("gumy", "topMargin:" + topMargin + "--viewHeight:" + viewHeight + "--leftMargin:" + leftMargin + "---fontSize:" + fontSize);
                    Class<?> aClass = Class.forName(templateEditView.getViewType());
                    if (aClass == EditText.class) {
                        // 打印二维码文字--参数-水平起始位置,垂直位置,内容,字体大小,旋转,加粗,反转,下划线
                        zpSDK.drawText((int) leftMargin, (int)topMargin, templateEditView.getText(), templateEditView.getScaleTextSize(), 0, 0, false, false);
                        Log.e("gumy","leftMargin:"+leftMargin+"---height:"+height+"---zpSDK.drawText:"+templateEditView.getText());
                    } else if (aClass == ImageView.class) {
                        //图片
                        // 打印二维码---参数-水平起始位置,垂直位置,二维码文字,旋转,宽度,?
                        zpSDK.drawQrCode((int) leftMargin, height, templateEditView.getText(), 0, 6, 6);
                    }
                    height = (int) (viewHeight + height);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        zpSDK.print(0, 0);
        zpSDK.printerStatus();
        status = zpSDK.GetStatus();
        zpSDK.disconnect();


        return status;
    }
}
