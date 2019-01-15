package cn.zcbdqn.labelprinter.zpprint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import zpSDK.zpSDK.PrinterInterface;

/**
 * 自己修改芝科打印机的API
 * Created by gumuyun on 2019/1/8.
 */

public class MyZpBluetoothPrinter implements PrinterInterface {
    private Context context;
    static int w;
    static int h;
    private static OutputStream myOutStream = null;
    private static InputStream myInStream = null;
    private static BluetoothSocket mySocket = null;
    private static BluetoothAdapter myBluetoothAdapter;
    private static BluetoothDevice myDevice;
    private static int myBitmapHeight = 0;
    private static int myBitmapWidth = 0;
    private static int PrinterDotWidth = 576;
    private static int PrinterDotPerMM = 8;
    private static PrinterPageImpl impl = new PrinterPageImpl();
    private UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public MyZpBluetoothPrinter(Context context) {
        this.context = context;
    }

    private static void Create(int width, int height) {
        impl.Create(width, height);
    }

    private static byte[] GetPageData(int r, int gap) {
        return impl.GetData(r, gap);
    }

    private static int GetPageDataLen() {
        return impl.getDataLen();
    }

    private static void _feed() {
        impl.feed();
    }

    private static void Drawbox(int x0, int y0, int x1, int y1, int lineWidth) {
        impl.Drawbox(x0, y0, x1, y1, lineWidth);
    }

    private static void DrawInverse(int x0, int y0, int x1, int y1, int lineWidth) {
        impl.INVERSE(x0, y0, x1, y1, lineWidth);
    }

    private static void DrawLine(int x0, int y0, int x1, int y1, int lineWidth) {
        impl.DrawLine(x0, y0, x1, y1, lineWidth);
    }

    private static void DrawBitmap(int x, int y, Bitmap bitmap, boolean rotate) {
        impl.DrawBitmap(bitmap, x, y, rotate);
    }

    private static void DrawBarcode1D(int x, int y, String text, String type, int width, int height, boolean rotate) {
        impl.DrawBarcode1D(type, x, y, text, width, height, rotate);
    }

    private static void DrawText(int text_x, int text_y, String text, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        impl.DrawText(text_x, text_y, text, fontSize, rotate, bold, reverse, underline);
    }

    private static void DrawBarcodeQRcode(int x, int y, String text, int size) {
        impl.DrawBarcodeQRcode(x, y, text, size, "M", false);
    }

    private static void DrawBarcodeQRcode(int x, int y, String text, int size, String errLeval, boolean rotate) {
        impl.DrawBarcodeQRcode(x, y, text, size, errLeval, rotate);
    }

    private static String r_bmp_data() {
        return impl.ru();
    }

    private static void zp_printer_status_detect() {
        byte[] data = new byte[]{29, -103, 0, 0};
        SPPWrite(data, 4);
    }

    public static int zp_printer_status_get(int timeout) {
        byte[] readata = new byte[4];
        int a = 0;
        if(!SPPReadTimeout(readata, 4, timeout)) {
            return -1;
        } else if(readata[0] != 29) {
            return -1;
        } else if(readata[1] != -103) {
            return -1;
        } else if(readata[3] != -1) {
            return -1;
        } else {
            byte status = readata[2];
            if((status & 1) != 0) {
                a = 1;
            }

            if((status & 2) != 0) {
                a = 2;
            }

            return a;
        }
    }

    private static boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
        for(int i = 0; i < Timeout / 5; ++i) {
            try {
                if(myInStream.available() >= DataLen) {
                    try {
                        myInStream.read(Data, 0, DataLen);
                        return true;
                    } catch (IOException var5) {
                        return false;
                    }
                }
            } catch (IOException var7) {
                return false;
            }

            try {
                Thread.sleep(5L);
            } catch (InterruptedException var6) {
                return false;
            }
        }

        return false;
    }

    private static void _text(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawText(pageWidth, pageHeight, pageRotate);
    }

    private static void _line(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawLine(pageWidth, pageHeight, pageRotate);
    }

    private static void _Box(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawBox(pageWidth, pageHeight, pageRotate);
    }

    private static void _Bitmap(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawBitmap(pageWidth, pageHeight, pageRotate);
    }

    private static void _Barcode1D(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawBarcode1D(pageWidth, pageHeight, pageRotate);
    }

    private static void _BarcodeQRcode(int pageWidth, int pageHeight, int pageRotate) {
        impl.makeDrawBarcodeQRcode(pageWidth, pageHeight, pageRotate);
    }

    private static void Clear() {
        impl.Clear();
    }

    private static boolean zp_page_print(boolean IsPortrait) {
        return true;
    }

    private static boolean zp_goto_mark_left(int MaxFeedMM) {
        return true;
    }

    private static boolean zp_goto_mark_right(int MaxFeedMM) {
        return true;
    }

    private static boolean zp_goto_mark_label(int MaxFeedMM) {
        return true;
    }

    public boolean connect(String address) {
        if(address == "") {
            return false;
        } else {
            myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(myBluetoothAdapter == null) {
                return false;
            } else {
                myDevice = myBluetoothAdapter.getRemoteDevice(address);
                return myDevice == null?false:this.SPPOpen(myBluetoothAdapter, myDevice);
            }
        }
    }

    private boolean SPPOpen(BluetoothAdapter BluetoothAdapter, BluetoothDevice btDevice) {
        Log.e("a", "SPPOpen");
        myBluetoothAdapter = BluetoothAdapter;
        myDevice = btDevice;
        if(!myBluetoothAdapter.isEnabled()) {
            return false;
        } else {
            myBluetoothAdapter.cancelDiscovery();

            try {
                mySocket = myDevice.createRfcommSocketToServiceRecord(this.SPP_UUID);
            } catch (IOException var11) {
                var11.printStackTrace();
                return false;
            }

            try {
                mySocket.connect();
            } catch (IOException var10) {
                return false;
            }

            try {
                myOutStream = mySocket.getOutputStream();
            } catch (IOException var9) {
                try {
                    mySocket.close();
                } catch (IOException var5) {
                    var5.printStackTrace();
                }

                return false;
            }

            try {
                myInStream = mySocket.getInputStream();
            } catch (IOException var8) {
                try {
                    mySocket.close();
                } catch (IOException var6) {
                    var6.printStackTrace();
                }

                return false;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException var7) {
                ;
            }

            Log.e("a", "SPPOpen OK");
            return true;
        }
    }

    private void SPPClose() {
        try {
            mySocket.close();
        } catch (IOException var2) {
            ;
        }

    }

    public void disconnect() {
        this.SPPClose();
    }

    public boolean print(int horizontal, int skip) {
        _Bitmap(w, h, 180);
        _text(w, h, horizontal);
        _line(w, h, horizontal);
        _Box(w, h, 180);
        _Barcode1D(w, h, horizontal);
        _BarcodeQRcode(w, h, 180);
        SPPWrite(GetPageData(horizontal, skip), GetPageDataLen());
        return true;
    }

    public void pageSetup(int pageWidth, int pageHeight) {
        w = pageWidth;
        h = pageHeight;
        Create(pageWidth, pageHeight);
    }

    public void drawBox(int lineWidth, int top_left_x, int top_left_y, int bottom_right_x, int bottom_right_y) {
        Drawbox(top_left_x, top_left_y, bottom_right_x, bottom_right_y, lineWidth);
    }

    public void drawLine(int lineWidth, int start_x, int start_y, int end_x, int end_y, boolean fullline) {
        DrawLine(start_x, start_y, end_x, end_y, lineWidth);
    }

    public void drawText(int text_x, int text_y, String text, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        DrawText(text_x, text_y, text, fontSize, rotate, bold, reverse, underline);
    }

    public void drawText(int text_x, int text_y, int width, int height, String str, int fontsize, int rotate, int bold, boolean underline, boolean reverse) {
        int f_height = 0;
        if(fontsize == 1) {
            f_height = 16;
        }

        if(fontsize == 20) {
            f_height = 20;
        }

        if(fontsize == 2) {
            f_height = 24;
        }

        if(fontsize == 3) {
            f_height = 32;
        }

        if(fontsize == 4) {
            f_height = 48;
        }

        if(fontsize == 5) {
            f_height = 48;
        }

        if(fontsize == 6) {
            f_height = 72;
        }

        if(fontsize == 7) {
            f_height = 64;
        }

        int _x = width / f_height;
        int _y = height / f_height;
        int _xx = _x;
        int ii = 0;
        boolean ver = true;
        int a = str.length();
        if(_y == 1) {
            DrawText(text_x, text_y, str, fontsize, rotate, bold, reverse, underline);
        } else {
            int b = str.length();
            int t;
            if(_x == 1) {
                for(t = 0; t < b; ++t) {
                    String s = str.substring(ii, _x);
                    DrawText(text_x, text_y + f_height * (t + 1) - 24, s, fontsize, rotate, bold, reverse, underline);
                    ++ii;
                    ++_x;
                }
            } else {
                int tt = 0;
                if(_x - a > 0 && ver) {
                    DrawText(text_x, text_y, str, fontsize, rotate, bold, reverse, underline);
                    ver = false;
                    return;
                }

                if(_x - a < 0) {
                    for(t = 0; t < _y; ++t) {
                        ver = false;
                        String s = str.substring(ii, _x);
                        DrawText(text_x, text_y + f_height * tt++, s, fontsize, rotate, bold, reverse, underline);
                        ii += _xx;
                        a -= _xx;
                        _x += _xx;
                        if(_xx - a >= 0) {
                            s = str.substring(ii, str.length());
                            DrawText(text_x, text_y + f_height * tt++, s, fontsize, rotate, bold, reverse, underline);
                            return;
                        }
                    }
                }
            }
        }

    }

    public void drawBarCode(int start_x, int start_y, String text, int type, boolean rotate, int linewidth, int height) {
        String type_ = "128";
        if(type == 0) {
            type_ = "39";
        }

        if(type == 1) {
            type_ = "128";
        }

        if(type == 2) {
            type_ = "93";
        }

        if(type == 3) {
            type_ = "CODABAR";
        }

        if(type == 4) {
            type_ = "EAN8";
        }

        if(type == 5) {
            type_ = "EAN13";
        }

        if(type == 6) {
            type_ = "UPCA";
        }

        if(type == 7) {
            type_ = "UPCE";
        }

        if(type == 8) {
            type_ = "I2OF5";
        }

        DrawBarcode1D(start_x, start_y, text, type_, linewidth, height, rotate);
    }

    public void drawQrCode(int start_x, int start_y, String text, int rotate, int ver, int lel) {
        DrawBarcodeQRcode(start_x, start_y, text, ver);
    }

    public void drawGraphic(int start_x, int start_y, int bmp_size_x, int bmp_size_y, Bitmap bmp) {
        DrawBitmap(start_x, start_y, bmp, false);
    }

    public void feed() {
        _feed();
    }

    private static boolean SPPWrite(byte[] Data, int DataLen) {
        try {
            myOutStream.write(Data, 0, DataLen);
            return true;
        } catch (IOException var3) {
            return false;
        }
    }

    public String printerStatus() {
        zp_printer_status_detect();
        return null;
    }

    public int GetStatus() {
        int a = zp_printer_status_get(8000);
        return a;
    }

    public void Write(byte[] Data) {
        SPPWrite(Data);
    }

    public static boolean SPPWrite(byte[] Data) {
        try {
            myOutStream.write(Data);
            return true;
        } catch (IOException var2) {
            return false;
        }
    }

    public String r_data() {
        return r_bmp_data();
    }

    public void drawINVERSE(int x0, int y0, int x1, int y1, int width) {
        DrawInverse(x0, y0, x1, y1, width);
    }

    public void Read(byte[] Data, int len, int timeout) {
        SPPReadTimeout(Data, len, timeout);
    }
}
