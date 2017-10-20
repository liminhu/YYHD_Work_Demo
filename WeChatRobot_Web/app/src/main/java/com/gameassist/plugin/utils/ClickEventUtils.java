package com.gameassist.plugin.utils;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hulimin on 2017/9/27.
 */

public class ClickEventUtils {
    public static void touchClick(View view, float x, float y){
       // MyLog.e("childview--touchClick---x:"+x+";\ty:"+y);
        MyLog.e("childview--touchClick---"+view.getClass().getName()+"---id:"+view.getId());
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        dispatchTouchEvent(view, event);
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        dispatchTouchEvent(view, event);
    }


    public static void touchClick(View view, int shift){
        float x=view.getWidth()/2;
        float y=view.getHeight()/2;
        MyLog.e("childview--testClick_shift---"+view.getClass().getName()+"---id:"+view.getId() +"y:"+y);
        touchClick(view,x,y-shift);
    }


    public static void touchClick(View view){
        MyLog.e("childview--testClick_1111---"+view.getClass().getName()+"---id:"+view.getId());
        float x=view.getWidth()/2;
        float y=view.getHeight()/2;
        touchClick(view,x,y);
    }

    private static void dispatchTouchEvent(final View view, final MotionEvent event) {
        try {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.dispatchTouchEvent(event);
                }
            }, 10);
        } catch (Exception e) {
            MyLog.e(e.getMessage());
        }
    }

}
