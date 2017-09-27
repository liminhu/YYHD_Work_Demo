package com.gameassist.plugin.common;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.mm.robot.PluginEntry;
import com.gameassist.plugin.utils.ClickEventUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.FileUtils;
import com.gameassist.plugin.utils.ViewUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hulimin on 2017/9/19.
 */

public class WebSocketMessage {
    public static void onBack() {
        // TODO: 2017/9/26 不能放主线程中
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    MyLog.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }


    public static boolean sendTextMessage(final String msg, final Handler handler, int time) {
        MyLog.e(msg + "\tcase 1: --- 发送文字  ---- time: 次数 -- " + time);
        View topView1 = PluginEntry.currentActivity.getWindow().getDecorView();
        final ViewGroup topViewGp1 = (ViewGroup) topView1;
        final EditText editText = (EditText) ViewUtils.getEditText(topViewGp1);
        final Button sendButton = (Button) ViewUtils.getSendButton(topViewGp1);
        try {
            if (editText != null && sendButton != null) {
                MyLog.e("editText -- id:" + editText.getId());
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(msg);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // MyLog.e("editText -- id:" + editText.getId());
                                sendButton.performClick();
                            }
                        }, 100);
                    }
                });
            } else {
                if (time < 3) {
                    Thread.sleep(500 * time);
                    sendTextMessage(msg, handler, time + 1);
                }
            }
        } catch (Exception e) {

        }
        return false;
    }


    public static boolean sendEmojiMessageByFileMd5(final Context context, final Handler handler, final String md5, final int time) {
        String bean210 = md5;
        final String testFileMd5 = bean210 + ".gif";
        Activity currentActivity = PluginEntry.currentActivity;
        MyLog.e("发送表情 " + testFileMd5 + "---- 次数： "+time+"开始  - \t" + currentActivity.toString());

        File data = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), testFileMd5);
        try {
            InputStream is = context.getAssets().open(testFileMd5);
            FileUtils.copyFile(is, data.getAbsolutePath());
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(data)));  //更新系统图库的方法
            MyLog.e("发送表情 --" + MyLog.getFormatrerTime(System.currentTimeMillis()));
            Thread.sleep(100);

            View topView = currentActivity.getWindow().getDecorView();
            final ViewGroup topViewGp = (ViewGroup) topView;
            final EditText edit = (EditText) ViewUtils.getEditText(topViewGp);
            if (edit == null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "没有发现聊天界面的文字输入框。。。", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
            edit.post(new Runnable() {
                @Override
                public void run() {
                    edit.setText("");
                    View testdcimButton = ViewUtils.getImgViewByViewId(topViewGp);
                    if (testdcimButton == null) {
                        LinearLayout linearLayoutImg = (LinearLayout) ViewUtils.getLinearLayoutIma(topViewGp);
                        ClickEventUtils.touchClick(linearLayoutImg);
                        MyLog.e("正在查找隐藏相册框。。。");  //这个地方失败得重试

                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            View dcimButton = ViewUtils.getImgViewByViewId(topViewGp);
                            if (dcimButton == null) {
                                //Toast.makeText(context, "查找隐藏相册框 失败 !!!", Toast.LENGTH_SHORT).show();
                                if(time<3){
                                    sendEmojiMessageByFileMd5(context, handler, md5, time+1);
                                }
                            } else {
                                sendEmojiOpenDCIMDir(handler,0);
                            }
                        }
                    }, 100);
                }
            });
        } catch (IOException e) {
            MyLog.e(e.getMessage());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, testFileMd5 + " no find !!!", Toast.LENGTH_SHORT).show();
                }
            });
            return false;
        } catch (InterruptedException e) {
        }
        return true;
    }


    private static boolean sendEmojiOpenDCIMDir(final Handler handler, final int time) {
        Activity currentActivity = PluginEntry.currentActivity;
        MyLog.e("正在打开相册 --case 212:" + currentActivity);
        View topView = currentActivity.getWindow().getDecorView();
        final ViewGroup topViewGp = (ViewGroup) topView;
        View testView = ViewUtils.getLinearLayoutIma(topViewGp, 2131755517);
        if (testView != null) {
            // ViewUtils.printfView((ViewGroup) testView);
            float x = testView.getWidth() / 8;
            float y = testView.getHeight() / 4;
            MyLog.e("正在打开相册--touchClick---x -- new :" + x + ";\ty:" + y);
            ClickEventUtils.touchClick(testView, x, y);
        } else {
            MyLog.e("正在打开相册 is null:");
            try {
                if (time < 5) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendEmojiOpenDCIMDir(handler, time + 1);
                        }
                    }, 1000*time);

                }
            } catch (Exception e) {

            }
        }
        return true;
    }


    public static boolean chooseDefaultGIF(final Handler handler,  final int time) {
        MyLog.e("默认选择相册第一张图 --case 213:" + PluginEntry.currentActivity);
        View topView1 = PluginEntry.currentActivity.getWindow().getDecorView();
        final ViewGroup topViewGp1 = (ViewGroup) topView1;
        View testView1 = ViewUtils.getLinearLayoutIma(topViewGp1, 2131759452); //
        if (testView1 != null) {
            MyLog.e("hook_iv_111:" + testView1.getClass().getName() + "\t" + testView1.toString() + "id:_p:" + testView1.getId());
            GridView gv = (GridView) testView1;
            int num = gv.getNumColumns();
            if (num > 0) {
                float x = gv.getWidth() / num;
                float y = gv.getHeight() / num;
                MyLog.e("默认选择相册第一张图 -- new :" + x + ";\ty:" + y + "\t:" + num);
                ClickEventUtils.touchClick(gv, x / 2, y / 2);
            }
        } else {
            try {
                if (time < 3) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            chooseDefaultGIF(handler, time + 1);
                        }
                    }, 200*time);
                }
            } catch (Exception e) {

            }
        }
        return true;
    }


    public static boolean sendLastGIF(final Handler handler, final int time) {
        MyLog.e("正发送相册第一张图。。。" + PluginEntry.currentActivity.toString());
        View topView1 = PluginEntry.currentActivity.getWindow().getDecorView();
        final ViewGroup topViewGp1 = (ViewGroup) topView1;
        View newSend = ViewUtils.getImgViewByViewId(topViewGp1, "发送");
        if (newSend != null) {
            MyLog.e("childview --case 12:  TextView is no null");
            final TextView tv = (TextView) newSend;
            MyLog.e("hook_childview_111:" + tv.getClass().getName() + "\t" + tv.getText().toString() + "id:_:" + tv.getId());  //2131755288
           // tv.performClick();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv.performClick();
                }
            }, 500);
            //SharedPreferenceUtils.updateDataToSharedPre(PluginEntry.getInstance().getContext(), SharedPreferenceUtils.IS_IMAGEPREVIEWUI, false);
        } else {
            if (time < 3) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendLastGIF(handler, time + 1);
                    }
                }, 500*time);
            }
        }
        return true;
    }


}
