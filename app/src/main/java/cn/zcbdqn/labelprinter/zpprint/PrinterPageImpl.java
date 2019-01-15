package cn.zcbdqn.labelprinter.zpprint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by gumuyun on 2019/1/8.
 */

public class PrinterPageImpl {
    private byte[] totalData = new byte[204800];
    private int totalDataLen = 0;
    private byte[] listData = new byte[204800];
    private int listDataLen = 0;
    private String begin;
    private int _pw;
    private int _ph;
    private String end;
    private Context _context;
    private String _fontSrc = "Printer";
    private String _fontNameLast = "56";
    private int _fontSizeLast = 0;
    private String strINVERSE = "";
    private boolean INVERSE = false;
    public String _str;
    private Paint _paint = null;
    List<DrawTextItem> listDrawText = new ArrayList();
    List<PrinterPageImpl.DrawLineItem> listDrawLine = new ArrayList();
    List<PrinterPageImpl.DrawBoxItem> listDrawBox = new ArrayList();
    List<PrinterPageImpl.DrawBitmapItem> listDrawBitmap = new ArrayList();
    List<PrinterPageImpl.DrawBarcode1DItem> listDrawBarcode1D = new ArrayList();
    List<PrinterPageImpl.DrawBarcodeQRcodeItem> listDrawBarcodeQRcode = new ArrayList();
    private static List<PrinterPageImpl.FontInfo> _listFontInfo = new ArrayList();

    public PrinterPageImpl() {
    }

    public void Create(int width, int height) {
        this._fontSrc = "Printer";
        this._fontNameLast = "56";
        this._fontSizeLast = 0;
        this.listDataLen = 0;
        this.listDrawText.clear();
        this.listDrawLine.clear();
        this.listDrawBox.clear();
        this.listDrawBarcode1D.clear();
        this.listDrawBitmap.clear();
        this.listDrawBarcodeQRcode.clear();
        this._pw = width;
        this._ph = height;
    }

    public void Clear() {
        this.listDataLen = 0;
    }

    public void add(byte[] d) {
        System.arraycopy(d, 0, this.listData, this.listDataLen, d.length);
        this.listDataLen += d.length;
    }

    public void feed() {
    }

    public int getDataLen() {
        return this.totalDataLen;
    }

    public byte[] GetData(int r, int gap) {
        if(r == 1) {
            if(gap == 0) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("ZPROTATE\r\nPRINT\r\n", new Object[0]);
            }

            if(gap == 1) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("ZPROTATE\r\nFORM\r\nPRINT\r\n", new Object[0]);
            }

            if(gap == 2) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE-LEFT\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("ZPROTATE\r\nFORM\r\nPRINT\r\n", new Object[0]);
            }

            if(gap == 3) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("ZPROTATE\r\nFORM\r\nPRINT\r\n", new Object[0]);
            }
        } else {
            if(gap == 0) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("PRINT\r\n", new Object[0]);
            }

            if(gap == 1) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nGAP-SENSE\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("FORM\r\nPRINT\r\n", new Object[0]);
            }

            if(gap == 2) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE-LEFT\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("FORM\r\nPRINT\r\n", new Object[0]);
            }

            if(gap == 3) {
                this.begin = String.format("! 0 200 200 %d 1\r\nPAGE-WIDTH %d \r\nBAR-SENSE\r\n", new Object[]{Integer.valueOf(this._ph), Integer.valueOf(this._pw)});
                this.end = String.format("FORM\r\nPRINT\r\n", new Object[0]);
            }
        }

        int total_len = this.begin.length() + this.listDataLen + this.end.length();
        this.totalDataLen = total_len;
        int pos = 0;
        System.arraycopy(this.begin.getBytes(), 0, this.totalData, pos, this.begin.length());
        pos = pos + this.begin.length();
        System.arraycopy(this.listData, 0, this.totalData, pos, this.listDataLen);
        pos += this.listDataLen;
        System.arraycopy(this.end.getBytes(), 0, this.totalData, pos, this.end.length());
        this.listDataLen = 0;
        return this.totalData;
    }

    private boolean IsFontPath(String fontName) {
        if(!fontName.substring(0, 1).equals("/")) {
            return false;
        } else {
            String str = ".ttf";
            return fontName.contains(str);
        }
    }

    private boolean IsFontRes(String fontName) {
        String str = ".ttf";
        return fontName.contains(str);
    }

    public void DrawText(int text_x, int text_y, String text, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        PrinterPageImpl.DrawTextItem item = new PrinterPageImpl.DrawTextItem();
        item.text_x = text_x;
        item.text_y = text_y;
        item.text = text;
        item.fontSize = fontSize;
        item.rotate = rotate;
        item.bold = bold;
        item.reverse = reverse;
        item.underline = underline;
        this.listDrawText.add(item);
    }

    private void realDrawText(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawTextItem item) {
        int text_x = item.text_x;
        int text_y = item.text_y;
        String text = item.text;
        int fontSize = item.fontSize;
        int rotate = item.rotate;
        int bold = item.bold;
        boolean reverse = item.reverse;
        boolean underline = item.underline;
        int f_size = 24;
        int f_height = 24;
        if(underline) {
            this.add("UNDERLINE ON\r\n".getBytes());
        } else {
            this.add("UNDERLINE OFF\r\n".getBytes());
        }

        String textScale = "";
        if(fontSize == 1) {
            f_size = 55;
            f_height = 16;
        }

        if(fontSize == 2) {
            f_size = 24;
            f_height = 24;
        }

        if(fontSize == 3) {
            f_size = 56;
            f_height = 32;
        }

        if(fontSize == 4) {
            f_size = 24;
            textScale = String.format("SETMAG %d %d\r\n", new Object[]{Integer.valueOf(2), Integer.valueOf(2)});
            this.add(textScale.getBytes());
            f_height = 48;
        }

        if(fontSize == 5) {
            f_size = 56;
            textScale = String.format("SETMAG %d %d\r\n", new Object[]{Integer.valueOf(2), Integer.valueOf(2)});
            this.add(textScale.getBytes());
            f_height = 64;
        }

        if(fontSize == 6) {
            f_size = 24;
            textScale = String.format("SETMAG %d %d\r\n", new Object[]{Integer.valueOf(3), Integer.valueOf(3)});
            this.add(textScale.getBytes());
            f_height = 72;
        }

        if(fontSize == 7) {
            f_size = 32;
            textScale = String.format("SETMAG %d %d\r\n", new Object[]{Integer.valueOf(4), Integer.valueOf(4)});
            this.add(textScale.getBytes());
            f_height = 96;
        }
        //自已增加的
        if (fontSize>7){
            f_size = (fontSize-7)*12+32;
            textScale = String.format("SETMAG %d %d\r\n", new Object[]{Integer.valueOf(fontSize-3), Integer.valueOf(fontSize-3)});
            this.add(textScale.getBytes());
            f_height = (fontSize-3)*24;

        }

        if(bold == 1) {
            this.add("SETBOLD 1\r\n".getBytes());
        } else {
            this.add("SETBOLD 0\r\n".getBytes());
        }

        String cmd = "T";
        if(rotate == 1) {
            cmd = "VT";
        }

        if(rotate == 2) {
            cmd = "T180";
        }

        if(rotate == 3) {
            cmd = "T270";
        }

        String str = String.format("%s %s %s %d %d %s\r\n", new Object[]{cmd, Integer.valueOf(f_size), Integer.valueOf(0), Integer.valueOf(text_x), Integer.valueOf(text_y), text});
        byte[] byteStr = (byte[])null;

        try {
            byteStr = str.getBytes("gbk");
        } catch (UnsupportedEncodingException var23) {
            ;
        }

        if(byteStr != null) {
            this.add(byteStr);
        }

        this.add("SETMAG 0 0 \r\n".getBytes());
        if(reverse) {
            byte[] bytetext = (byte[])null;

            try {
                bytetext = text.getBytes("gbk");
            } catch (UnsupportedEncodingException var22) {
                return;
            }

            if(bytetext == null) {
                return;
            }

            int block_w = f_height / 2 * bytetext.length;
            this.INVERSE(text_x, text_y, text_x + block_w, text_y, f_height);
        }

    }

    public void makeDrawText(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawText.size(); ++i) {
            this.realDrawText(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawTextItem)this.listDrawText.get(i));
        }

    }

    public void DrawText(int x, int y, String text, String font, int textSize, boolean bold, boolean rotate) {
        Typeface tf = this.GetTypeFace(font);
        if(textSize > 0) {
            this._paint = new Paint();
            this._paint.setTextSize((float)textSize);
            this._paint.setColor(-16777216);
            this._paint.setFakeBoldText(bold);
            this._paint.setTypeface(tf);
            int textWidth = (int)this._paint.measureText(text);
            Bitmap b = Bitmap.createBitmap(textWidth, textSize * 3, Config.RGB_565);
            Canvas canvas = new Canvas(b);
            canvas.drawColor(-1);
            canvas.drawText(text, 0.0F, (float)(textSize * 2 - 1), this._paint);
            //int offset_x = false;
            int offset_y = -textSize;
            int top = this.GetTop(b);
            int bottom = this.GetBottom(b);
            offset_y += top;
            Bitmap newBmp = Bitmap.createBitmap(b.getWidth(), bottom - top, b.getConfig());
            Canvas c = new Canvas(newBmp);
            c.drawBitmap(b, 0.0F, (float)(-top), new Paint());
            this.DrawBitmap1(newBmp, x, y + offset_y, rotate);
        }
    }

    public void DrawLine(int x0, int y0, int x1, int y1, int width) {
        PrinterPageImpl.DrawLineItem item = new PrinterPageImpl.DrawLineItem();
        item.x0 = x0;
        item.y0 = y0;
        item.x1 = x1;
        item.y1 = y1;
        item.width = width;
        this.listDrawLine.add(item);
    }

    private void realDrawLine(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawLineItem item) {
        int x0 = item.x0;
        int y0 = item.y0;
        int x1 = item.x1;
        int y1 = item.y1;
        int width = item.width;
        if(pageRotate != 0 && pageRotate != 1 && pageRotate == 2) {
            x0 = pageWidth - x0;
            y0 = pageHeight - y0;
            x1 = pageWidth - x1;
            y1 = pageHeight - y1;
        }

        String str = String.format("LINE %d %d %d %d %d\r\n", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width)});
        this.add(str.getBytes());
    }

    public void makeDrawLine(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawLine.size(); ++i) {
            this.realDrawLine(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawLineItem)this.listDrawLine.get(i));
        }

    }

    public void Drawbox(int x0, int y0, int x1, int y1, int width) {
        PrinterPageImpl.DrawBoxItem item = new PrinterPageImpl.DrawBoxItem();
        item.x0 = x0;
        item.y0 = y0;
        item.x1 = x1;
        item.y1 = y1;
        item.width = width;
        this.listDrawBox.add(item);
    }

    private void realDrawBox(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawBoxItem item) {
        int x0 = item.x0;
        int y0 = item.y0;
        int x1 = item.x1;
        int y1 = item.y1;
        int width = item.width;
        String str = String.format("BOX %d %d %d %d %d\r\n", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width)});
        this.add(str.getBytes());
    }

    public void makeDrawBox(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawBox.size(); ++i) {
            this.realDrawBox(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawBoxItem)this.listDrawBox.get(i));
        }

    }

    public void INVERSE(int x0, int y0, int x1, int y1, int width) {
        this.strINVERSE = String.format("INVERSE-LINE %d %d %d %d %d\r\n", new Object[]{Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width)});
        this.INVERSE = true;
    }

    public void DrawBitmap(Bitmap bmp, int x, int y, boolean rotate) {
        PrinterPageImpl.DrawBitmapItem item = new PrinterPageImpl.DrawBitmapItem();
        item.bmp = bmp;
        item.x = x;
        item.y = y;
        item.rotate = rotate;
        this.listDrawBitmap.add(item);
    }

    private void realDrawBitmap(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawBitmapItem item) {
        Bitmap bitmap = item.bmp;
        int x = item.x;
        int y = item.y;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int byteCountW = (w + 7) / 8;
        int[] bmpData = new int[w * h];
        bitmap.copyPixelsToBuffer(IntBuffer.wrap(bmpData));

        int hh;
        for(int y0 = 0; y0 < h; y0 = hh + y0 + y) {
            hh = h - y0;
            if(hh > 128) {
                hh = 128;
            }

            byte[] outData = new byte[byteCountW * hh];

            for(int yy = y0; yy < y0 + hh; ++yy) {
                for(int xx = 0; xx < w; ++xx) {
                    int c = bmpData[yy * w + xx];
                    int r = c >> 16 & 255;
                    int g = c >> 8 & 255;
                    int b = c >> 0 & 255;
                    int gray = (r * 30 + g * 59 + b * 11 + 50) / 100;
                    if(gray < 128) {
                        outData[byteCountW * (yy - y0) + xx / 8] = (byte)(outData[byteCountW * (yy - y0) + xx / 8] | 128 >> xx % 8);
                    }
                }
            }

            String cmd = "CG";
            String strCmdHeader = String.format("%s %d %d %d %d \n", new Object[]{cmd, Integer.valueOf(byteCountW), Integer.valueOf(hh), Integer.valueOf(x), Integer.valueOf(y0 + y)});
            this.add(strCmdHeader.getBytes());
            this.add(outData);
        }

    }

    public String ru() {
        return this._str;
    }

    public void makeDrawBitmap(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawBitmap.size(); ++i) {
            this.realDrawBitmap(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawBitmapItem)this.listDrawBitmap.get(i));
        }

    }

    public void DrawBarcode1D(String type, int x, int y, String text, int width, int height, boolean rotate) {
        PrinterPageImpl.DrawBarcode1DItem item = new PrinterPageImpl.DrawBarcode1DItem();
        item.type = type;
        item.x = x;
        item.y = y;
        item.text = text;
        item.width = width;
        item.height = height;
        item.rotate = rotate;
        this.listDrawBarcode1D.add(item);
    }

    private void realDraBarcode1D(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawBarcode1DItem item) {
        String type = item.type;
        int x = item.x;
        int y = item.y;
        String text = item.text;
        int width = item.width;
        int height = item.height;
        boolean rotate = item.rotate;
        String cmd = "BARCODE";
        if(rotate) {
            cmd = "VBARCODE";
        }

        String str = String.format("%s %s %d 1 %d %d %d %s\r\n", new Object[]{cmd, type, Integer.valueOf(width - 1), Integer.valueOf(height), Integer.valueOf(x), Integer.valueOf(y), text});
        this.add(str.getBytes());
    }

    public void makeDrawBarcode1D(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawBarcode1D.size(); ++i) {
            this.realDraBarcode1D(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawBarcode1DItem)this.listDrawBarcode1D.get(i));
        }

    }

    public void DrawBarcodeQRcode(int x, int y, String text, int size, String errLevel, boolean rotate) {
        PrinterPageImpl.DrawBarcodeQRcodeItem item = new PrinterPageImpl.DrawBarcodeQRcodeItem();
        item.x = x;
        item.y = y;
        item.text = text;
        item.size = size + 3;
        item.errLevel = errLevel;
        item.rotate = rotate;
        this.listDrawBarcodeQRcode.add(item);
    }

    private void realBarcodeQRcode(int pageWidth, int pageHeight, int pageRotate, PrinterPageImpl.DrawBarcodeQRcodeItem item) {
        int x = item.x;
        int y = item.y;
        String text = item.text;
        int size = item.size;
        String errLevel = item.errLevel;
        boolean rotate = item.rotate;
        String cmd = "BARCODE";
        if(rotate) {
            cmd = "VBARCODE";
        }

        String str = String.format("%s QR %d %d M %d U %d\r\n", new Object[]{cmd, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(2), Integer.valueOf(size)});
        this.add(str.getBytes());
        this.add(String.format("%sA,", new Object[]{errLevel}).getBytes());
        byte[] buf = (byte[])null;

        try {
            buf = text.getBytes("gbk");
        } catch (UnsupportedEncodingException var15) {
            ;
        }

        if(buf != null) {
            this.add(buf);
        }

        this.add("\r\nENDQR\r\n".getBytes());
    }

    public void makeDrawBarcodeQRcode(int pageWidth, int pageHeight, int pageRotate) {
        for(int i = 0; i < this.listDrawBarcodeQRcode.size(); ++i) {
            this.realBarcodeQRcode(pageWidth, pageHeight, pageRotate, (PrinterPageImpl.DrawBarcodeQRcodeItem)this.listDrawBarcodeQRcode.get(i));
        }

    }

    public void DrawBitmap1(Bitmap bmp, int x, int y, boolean rotate) {
        if(bmp != null) {
            Bitmap tagetBmp = bmp;
            int byteWidth = (bmp.getWidth() + 7) / 8;
            byte[] bmpBuf = new byte[byteWidth * bmp.getHeight()];
            short[] buf = new short[bmp.getWidth() * bmp.getHeight()];
            ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
            bmp.copyPixelsToBuffer(shortBuffer);

            for(int xx = 0; xx < tagetBmp.getWidth(); ++xx) {
                for(int yy = 0; yy < tagetBmp.getHeight(); ++yy) {
                    if(buf[yy * tagetBmp.getWidth() + xx] == 0) {
                        bmpBuf[byteWidth * yy + xx / 8] = (byte)(bmpBuf[byteWidth * yy + xx / 8] | 128 >> xx % 8);
                    }
                }
            }

            String cmd = "CG";
            if(rotate) {
                cmd = "VCG";
            }

            String strCmdHeader = String.format("%s %d %d %d %d \n", new Object[]{cmd, Integer.valueOf(byteWidth), Integer.valueOf(tagetBmp.getHeight()), Integer.valueOf(x), Integer.valueOf(y)});
            this.add(strCmdHeader.getBytes());
            this.add(bmpBuf);
            this.add("\r\n".getBytes());
        }
    }

    public void PageFree() {
        this.Clear();
    }

    private String IntToHex(byte data) {
        String r = "";
        switch(data) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                char ch = (char)(data + 48);
                r = Character.toString(ch);
                break;
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                Log.d("long", "ch is error ");
        }

        return r;
    }

    private String ByteToString(byte data) {
        String str = "";
        byte d1 = (byte)(data >> 4 & 15);
        byte d2 = (byte)(data & 15);
        str = this.IntToHex(d1) + this.IntToHex(d2);
        return str;
    }

    private Typeface GetTypeFace(String fontName) {
        Typeface tf = null;
        Iterator var4 = _listFontInfo.iterator();

        PrinterPageImpl.FontInfo fontInfo;
        while(var4.hasNext()) {
            fontInfo = (PrinterPageImpl.FontInfo)var4.next();
            if(fontInfo.FontName.equals(fontName)) {
                return fontInfo.FontTypeface;
            }
        }

        if(!fontName.equals("sans") && !fontName.equals("serif") && !fontName.equals("monospace")) {
            if(this.IsFontPath(fontName)) {
                tf = Typeface.createFromFile(fontName);
            } else {
                if(!this.IsFontRes(fontName)) {
                    return null;
                }

                tf = Typeface.createFromAsset(this._context.getAssets(), String.format("fonts/%s", new Object[]{fontName}));
            }
        } else {
            tf = Typeface.create(fontName, 0);
        }
        //fontInfo = new _PrinterPageImpl.FontInfo((_PrinterPageImpl.FontInfo)null);
        fontInfo = new PrinterPageImpl.FontInfo();
        fontInfo.FontName = fontName;
        fontInfo.FontTypeface = tf;
        _listFontInfo.add(fontInfo);
        return tf;
    }

    private int GetTop(Bitmap b) {
        int top = 0;
        short[] buf = new short[b.getWidth() * b.getHeight()];
        ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
        b.copyPixelsToBuffer(shortBuffer);

        for(int yy = 0; yy < b.getHeight(); ++yy) {
            for(int xx = 0; xx < b.getWidth(); ++xx) {
                if(buf[yy * b.getWidth() + xx] == 0) {
                    return yy;
                }
            }
        }

        return top;
    }

    private int GetBottom(Bitmap b) {
        int bottom = 0;
        short[] buf = new short[b.getWidth() * b.getHeight()];
        ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
        b.copyPixelsToBuffer(shortBuffer);

        for(int yy = b.getHeight() - 1; yy >= 0; --yy) {
            for(int xx = 0; xx < b.getWidth(); ++xx) {
                if(buf[yy * b.getWidth() + xx] == 0) {
                    return yy + 1;
                }
            }
        }

        return bottom;
    }

    private int GetLeft(Bitmap b) {
        short[] buf = new short[b.getWidth() * b.getHeight()];
        ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
        b.copyPixelsToBuffer(shortBuffer);

        for(int xx = 0; xx < b.getWidth(); ++xx) {
            for(int yy = 0; yy < b.getHeight(); ++yy) {
                if(buf[yy * b.getWidth() + xx] == 0) {
                    return xx;
                }
            }
        }

        return 0;
    }

    private int GetRight(Bitmap b) {
        short[] buf = new short[b.getWidth() * b.getHeight()];
        ShortBuffer shortBuffer = ShortBuffer.wrap(buf);
        b.copyPixelsToBuffer(shortBuffer);

        for(int xx = b.getWidth() - 1; xx >= 0; --xx) {
            for(int yy = 0; yy < b.getHeight(); ++yy) {
                if(buf[yy * b.getWidth() + xx] == 0) {
                    return xx + 1;
                }
            }
        }

        return 0;
    }

    class DrawBarcode1DItem {
        String type;
        int x;
        int y;
        String text;
        int width;
        int height;
        boolean rotate;

        DrawBarcode1DItem() {
        }
    }

    class DrawBarcodeQRcodeItem {
        int x;
        int y;
        String text;
        int size;
        String errLevel;
        boolean rotate;

        DrawBarcodeQRcodeItem() {
        }
    }

    class DrawBitmapItem {
        Bitmap bmp;
        int x;
        int y;
        boolean rotate;

        DrawBitmapItem() {
        }
    }

    class DrawBoxItem {
        int x0;
        int y0;
        int x1;
        int y1;
        int width;

        DrawBoxItem() {
        }
    }

    class DrawLineItem {
        int x0;
        int y0;
        int x1;
        int y1;
        int width;

        DrawLineItem() {
        }
    }

    class DrawTextItem {
        int text_x;
        int text_y;
        String text;
        int fontSize;
        int rotate;
        int bold;
        boolean reverse;
        boolean underline;

        DrawTextItem() {
        }
    }

    private class FontInfo {
        public Typeface FontTypeface;
        public String FontName;

        private FontInfo() {
        }
    }
}
