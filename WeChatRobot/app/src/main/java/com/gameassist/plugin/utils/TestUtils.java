package com.gameassist.plugin.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

import static com.gameassist.plugin.utils.FileUtils.copyByDrawingCache;
import static com.gameassist.plugin.utils.FileUtils.saveBitmap;

/**
 * Created by hulimin on 2017/9/27.
 */

public class TestUtils {
    public static  void printfLuckyMoneyLog(Activity currentActivity){
        try{
            Object npP=ReflectionUtils.getValue(currentActivity,"npP");
            if(npP!=null){
                Integer size= (Integer) ReflectionUtils.callMethod(npP,"getCount");
                if(size!=null && size>0){
                    MyLog.e("size --- "+size);
                    for(int i=0; i<size; i++){
                        Object m=ReflectionUtils.callMethod(npP, "getItem", new Class[]{int.class}, new Object[]{i});
                        if(m!=null){
                            MyLog.e(m.toString());
                            ReflectionUtils.getValue(m, "nkO");
                            ReflectionUtils.getValue(m, "nlj");
                            ReflectionUtils.getValue(m, "nlk");
                            ReflectionUtils.getValue(m, "nlw");
                            ReflectionUtils.getValue(m, "nlx");
                            ReflectionUtils.getValue(m, "nly");
                            ReflectionUtils.getValue(m, "nlz");
                            ReflectionUtils.getValue(m, "userName");
                        }else {
                            MyLog.e("m is null ... ");
                        }
                    }
                }
            }
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
    }







    public static void testSaveImage(ViewGroup viewGroup, Handler mainHandler){
        for(int i=0; i<viewGroup.getChildCount(); i++){
            final View childView = viewGroup.getChildAt(i);
            MyLog.e("childview-----"+childView.toString());
            if(childView.getClass().getName().contains("Button")  || childView.getClass().getName().contains("Image")  ||  childView.getClass().getName().contains("TextView")  ||  childView.getClass().getName().contains("Layout") ){
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());
                        Bitmap bitmap = copyByDrawingCache(childView);
                        if(null != bitmap ){
                            saveBitmap(bitmap,childView.getClass().getName()+"-"+childView.getId());
                        }
                        else{
                            MyLog.e("childview-----"+"获取图片失败");
                        }

                    }
                });
            }

            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                testSaveImage((ViewGroup) viewGroup.getChildAt(i), mainHandler);
            }
        }
    }




    public static void testSaveImage(View view, Handler mainHandler){
        ViewGroup viewGroup=(ViewGroup) view.getParent().getParent();
        for(int i=0; i<viewGroup.getChildCount(); i++){
            final View childView = viewGroup.getChildAt(i);
            MyLog.e("childview-----"+childView.toString());
            //  if(childView.getClass().getName().contains("Button")){
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());
                    Bitmap bitmap = FileUtils.copyByDrawingCache(childView);
                    if(null != bitmap ){
                        FileUtils.saveBitmap(bitmap,childView.getClass().getName()+"-"+childView.getId());
                    }
                    else{
                        MyLog.e("childview-----"+"获取图片失败");
                    }

                }
            });
            //    }

        }
    }



    static int num=0;
    public static  void printfView(ViewGroup vg){
        try{
            if(vg.getChildCount() == 0){
                return;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    String name=vg.getChildAt(i).getClass().getName();
                    MyLog.e("hook_"+num+"\tname:"+name);
                    if(name.contains("android.widget.Button")) {
                        Button button=(Button) vg.getChildAt(i);
                        // MyLog.e("hook_Button:"+num+":"+"\tid:"+button.getId()+"\t"+button.getText().toString());
                        num++;
                    }else if(name.contains("TextView")){
                        TextView button=(TextView) vg.getChildAt(i);
                        MyLog.e("hook_TextView"+num+":"+button.getText().toString());
                        num++;
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        num++;
                        printfView((ViewGroup) vg.getChildAt(i));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }




    public static  void printActivityLog(Activity activity) {
        MyLog.e("printActivityLog  ----   "+activity.getClass().getName());
        Intent intent = activity.getIntent();
        Bundle bundle = intent.getExtras();
        Set<String> keySet = bundle.keySet();  //获取所有的Key,
        for (String key : keySet) {  //bundle.get(key);来获取对应的value
            MyLog.e("key : "+key);
            MyLog.e("key : "+key +"\t value --- "+ intent.getStringExtra(key));
        }
    }


    public static  void printActivityLog(Intent intent) {
        MyLog.e("printintentLog  ----   "+intent.getClass().getName());
        Bundle bundle = intent.getExtras();
        Set<String> keySet = bundle.keySet();  //获取所有的Key,
        for (String key : keySet) {  //bundle.get(key);来获取对应的value
            MyLog.e("key : "+key);
            MyLog.e("key : "+key +"\t value --- "+ intent.getStringExtra(key));
        }
    }




    public static  void printBundleLog(Bundle bundle) {
        MyLog.e("printBundle  Log  ----   "+bundle.getClass().getName());
        if(bundle==null){
            MyLog.e("printBundleLog   --- bundle is null ...");
            return;
        }
        Set<String> keySet = bundle.keySet();  //获取所有的Key,
        for (String key : keySet) {  //bundle.get(key);来获取对应的value
            MyLog.e("key : "+key);
            MyLog.e("key : "+key +"\t value --- "+ bundle.getString(key));
        }
    }


}
