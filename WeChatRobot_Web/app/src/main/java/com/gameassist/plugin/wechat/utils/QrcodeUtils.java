package com.gameassist.plugin.wechat.utils;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.gameassist.plugin.utils.MyLog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

public class QrcodeUtils {
    private   static  File saveBitmap(Bitmap bm,File f) {
        MyLog.e("view--  saveBitmap:"+bm.toString());
        MyLog.e("view--  name:"+f.getAbsolutePath());
        if(f.exists()){
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            MyLog.e("view--  saveBitmap: succ"+f.getAbsolutePath());
        } catch (Exception e) {
            MyLog.e("view--  1111: succ"+       e.getMessage());

        }
        return f;
    }


    public static boolean saveQRcodeFileToDCIM(Context context, String url){
        Bitmap bitmap=createQRCodeBitmap(url);
        if(bitmap!=null){
            try {
                String name="MyQRcode";
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), name+".png");
                saveBitmap(bitmap,f);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));  //更新系统图库的方法
                MyLog.e("二维码生成功-  ---"+f.getAbsolutePath());
                return true;
            }catch (Exception e){

            }
        }
        return false;
    }


    private BitMatrix updateBit(BitMatrix matrix, int margin){
        int tempM = margin*2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for(int i= margin; i < resWidth- margin; i++){   //循环，将二维码图案绘制到新的bitMatrix中
            for(int j=margin; j < resHeight-margin; j++){
                if(matrix.get(i-margin + rec[0], j-margin + rec[1])){
                    resMatrix.set(i,j);
                }
            }
        }
        return resMatrix;
    }





    //500*500
    private static Bitmap createQRCodeBitmap(String url) {
        // 用于设置QR二维码参数
        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
        // 设置QR二维码的纠错级别——这里选择最高H级别
        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        // 设置编码方式
        qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 设定二维码里面的内容，这里我采用我博客的地址
        //   String cont = "http://blog.csdn.net/fengltxx";
        qrParam.put(EncodeHintType.MARGIN, 1);  //边距
        // 生成QR二维码数据——这里只是得到一个由true和false组成的数组
        // 参数顺序分别为：编码内容url地址，编码类型，生成图片宽度，生成图片高度，设置参数
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url,
                    BarcodeFormat.QR_CODE, 300, 300, qrParam);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
            int w = bitMatrix.getWidth();
            int h = bitMatrix.getHeight();
            int[] data = new int[w * h];

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (bitMatrix.get(x, y))
                        data[y * w + x] = 0xff000000;// 黑色
                    else
                        data[y * w + x] = -1;// -1 相当于0xffffffff 白色
                }
            }

            // 创建一张bitmap图片，采用最高的效果显示
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            // 将上面的二维码颜色数组传入，生成图片颜色
            bitmap.setPixels(data, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
