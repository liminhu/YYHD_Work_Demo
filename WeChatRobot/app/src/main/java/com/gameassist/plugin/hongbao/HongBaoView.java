package com.gameassist.plugin.hongbao;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gameassist.plugin.common.SendManager;
import com.gameassist.plugin.utils.ClickEventUtils;
import com.gameassist.plugin.utils.MyLog;


/**
 * Created by hulimin on 2017/9/26.
 */

public class HongBaoView {
    private static String HONG_BAO_IS_HAVE="红包派完了";
    private static String SEE_ALL_LUCKY="大家的手气";
    private static String LUCKY_MONEY="微信红包";


     public static View getHongBaoView(ViewGroup vg){
         String viewName="android.widget.TextView";
         View view= getViewByRealTextName(vg, viewName,LUCKY_MONEY);
         if(view!=null){
             ViewGroup parent=(ViewGroup) view.getParent().getParent();
             ClickEventUtils.touchClick(parent);
         }
         return view;
     }




    public static void getHongBaoDetailView(final Activity activity, final Handler handler, final int time){
        View topView1 = activity.getWindow().getDecorView();
        final ViewGroup vg = (ViewGroup) topView1;
        String viewName="android.widget.TextView";
        View view= getViewByRealTextName(vg, viewName,HONG_BAO_IS_HAVE);
        if(view!=null){
            View lucky_view= getViewByRealTextName(vg, viewName,SEE_ALL_LUCKY);
            if(lucky_view!=null){
                MyLog.e("not null 1111:\t"+SEE_ALL_LUCKY);
                ClickEventUtils.touchClick((ViewGroup)lucky_view.getParent());
            }else {
                MyLog.e("is null SEE_ALL_LUCKY:\t"+SEE_ALL_LUCKY);
                SendManager.onBack();
            }
        }else{
            MyLog.e(time+ "  ---  is null HONG_BAO_IS_HAVE:\t"+HONG_BAO_IS_HAVE);
            if (time < 3) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getHongBaoDetailView(activity, handler, time + 1);
                    }
                }, 1500*time);
            }else {
                SendManager.onBack();
            }
        }
    }


    private static View getViewByRealTextName(ViewGroup vg, String viewName, String realTextName){
        View result=null;
        try{
            if(vg.getChildCount() == 0){
                return null;
            }else{
                for(int i=0; i<vg.getChildCount(); i++){
                    //  MyLog.e(vg.getChildAt(i).getClass().getName());
                    if (vg.getChildAt(i).getClass().getName().equals(viewName)) {
                            TextView textView=(TextView) vg.getChildAt(i);
                            //MyLog.e("textView :\t"+textView.getText().toString());
                            //int tv_id=textView.getId();
                            if(textView.getText().toString().contains(realTextName) ) {  //&& !hasSearchTextview.contains(textView.getId()
                                return textView;
                            }
                    }
                    if (vg.getChildAt(i) instanceof ViewGroup) {
                        result = getViewByRealTextName((ViewGroup) vg.getChildAt(i), viewName, realTextName);
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
