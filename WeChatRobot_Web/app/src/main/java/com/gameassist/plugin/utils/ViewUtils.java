package com.gameassist.plugin.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.gameassist.plugin.utils.FileUtils.copyByDrawingCache;
import static com.gameassist.plugin.utils.FileUtils.saveBitmap;

/**
 * Created by hulimin on 2017/9/6.
 */


// TODO: 2017/9/7 :待优化，得通过id找到对象

/**
 * 2131756235  --- 相册的按钮id
  */
public class ViewUtils {
    public static View getLinearLayoutIma(View edit){
        String name="android.widget.LinearLayout";
        ViewGroup editPP=(ViewGroup) edit.getParent().getParent();
        View result= getChildViewByNameFromViewGroup(editPP,name);
        return result;       // getViewFromViewGroup(edit, 2131756235, 2131756236);  //正在查找隐藏相册框。。。
    }


    public static View getEditText(ViewGroup vg){
        String viewName="EditText";
        return getViewByName(vg, viewName);
    }

    public static View getSendButton(ViewGroup vg){
        String viewName="android.widget.Button";
        String realTextName="发送";
        return getViewByRealTextName(vg, viewName,realTextName,2);
    }



    public static View getImgViewByViewId(ViewGroup vg){
        String viewName="android.widget.TextView";
        String realTextName="相册";
        View view=getViewByRealTextName(vg, viewName,realTextName,1);
        return view;
    }




    public static View getImgViewByViewId(ViewGroup vg, String realTextName){
        String viewName="android.widget.TextView";
        View view=getViewByRealTextName(vg, viewName,realTextName,1);
        return view;
    }


    public static View getMMFlipperDCIM(ViewGroup vg){
        String viewName="com.tencent.mm.ui.base.MMFlipper";
        View view=getViewByName(vg, viewName);
        return view;
    }

    public static View getDefaultGridView(ViewGroup vg){
        String viewName="android.widget.GridView";
        View view=getViewByName(vg, viewName);
        return view;
    }






    public static View getViewByName(final ViewGroup vg, final String viewName){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().contains(viewName)) {
                        return vg.getChildAt(i);
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewByName((ViewGroup)vg.getChildAt(i), viewName);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }

                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //1:表示textview, 2:表示button
    private static View getViewByRealTextName(ViewGroup vg, String viewName, String realTextName, int type){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                  //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().equals(viewName)) {
                        if(type==2){
                            Button button=(Button) vg.getChildAt(i);
                           // MyLog.e("button :\t"+button.getText().toString());
                            if(button.getText().equals(realTextName)) {
                                return button;
                            }
                        }else if(type==1){
                            TextView textView=(TextView) vg.getChildAt(i);
                            //MyLog.e("textView :\t"+textView.getText().toString());
                            if(textView.getText().equals(realTextName)) {
                                return textView;
                            }
                        }
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewByRealTextName((ViewGroup) vg.getChildAt(i), viewName, realTextName,type);
                        if (result == null) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }
                return result;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }


    //单独处理打开隐藏相册框。。。
    private static View getChildViewByNameFromViewGroup(ViewGroup vg, String viewClassName){
        for(int i=0; i<vg.getChildCount(); i++) {
            final View childView = vg.getChildAt(i);
            if (childView.getClass().getName().contains(viewClassName)) {
               // MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());
                View result=vg.getChildAt(i);
                if(!isIncludeMMEditText((ViewGroup)result)) {
                    return vg.getChildAt(i);
                }
            }
        }
        return null;
    }


    private   static boolean isIncludeMMEditText(ViewGroup vg){
        String name="MMEditText";
        for(int i=0; i<vg.getChildCount(); i++) {
            final View childView = vg.getChildAt(i);
            if (childView.getClass().getName().contains(name)) {
                MyLog.e("MMEditText is include ... ");
                return true;
            }
        }
        return  false;
    }


}
