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
    public static View getLinearLayoutIma(ViewGroup vg){
        //2131756235
        View edit= ViewUtils.getEditText(vg);
        return getViewFromViewGroup(edit, 2131756235, 2131756236);  //正在查找隐藏相册框。。。
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





    public static View getLinearLayoutIma(ViewGroup vg, int id){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    View childView=vg.getChildAt(i);
                   // MyLog.e("childview--getLinearLayoutIma---"+childView.getClass().getName()+"---id:"+childView.getId());
                    if (vg.getChildAt(i).getId() == id) {
                        return childView;
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getLinearLayoutIma((ViewGroup) vg.getChildAt(i),id);
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



    public static View getViewFromViewGroup(View edit, int parent_id, int child_id){
        View result=null;
        ViewGroup viewGroup=(ViewGroup) edit.getParent().getParent();
        for(int i=0; i<viewGroup.getChildCount(); i++) {
            final View childView = viewGroup.getChildAt(i);
            if(childView.getId()==parent_id){
             //   MyLog.e("childview-----"+childView.getClass().getName()+"---id:"+childView.getId());

        /*        ViewGroup vg=(ViewGroup)childView;
                MyLog.e("childview----1111-"+childView.getClass().getName()+"---id:"+childView.getId());
                for(int j=0; j<vg.getChildCount(); j++) {
                    final View ch = vg.getChildAt(j);
                    MyLog.e("111--childview-----"+ch.getClass().getName()+"---id:"+ch.getId());

                    if(ch.getId() == child_id){
                        return ch;
                    }
                }*/
               // MyLog.e("0000--childview----- size:"+vg.getChildCount());
                return childView;
            }
        }
        return result;
    }



    private static View getViewByName(ViewGroup vg, String viewName){
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
                        result = getEditText((ViewGroup) vg.getChildAt(i));
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





}
