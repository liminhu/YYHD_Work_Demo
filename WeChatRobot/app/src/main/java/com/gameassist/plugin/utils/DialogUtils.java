package com.gameassist.plugin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gameassist.plugin.common.LoginManager;
import com.gameassist.plugin.mm.robot.R;
import com.gameassist.plugin.websocket.WebSocketService;

import static android.view.View.inflate;

/**
 * Created by hulimin on 2017/9/26.
 */

public class DialogUtils {

    public static void showWebSocketDialog(Activity currentActivity, final int index) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle("连接提示");
        builder.setMessage("当前连接服务器失败， 请是否重试？ ");
        final AlertDialog alertDialog = builder.create();
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSocketService.restartConnectionWebSocket(index);
                alertDialog.dismiss();
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).show();
    }






    private static String login_password="12345678", login_acccount="hulimin";

    public static void showLoginDialog(final Context context, Activity currentActivity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        final View v = inflate(context, R.layout.dialog_login, null);
        final EditText et_login_password = ((EditText) v.findViewById(R.id.login_password));
        final EditText et_login_account = ((EditText) v.findViewById(R.id.login_account_new));


        if (!TextUtils.isEmpty(login_acccount)) {
            et_login_account.setText(login_acccount);
        }

        if (!TextUtils.isEmpty(login_password)) {
            et_login_password.setText(login_password);
        }


        builder.setView(v);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        v.findViewById(R.id.save_btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_password=et_login_password.getText().toString();
                login_acccount=et_login_account.getText().toString();
                //   MyLog.e("login_password : %s, login_acccount:%s", login_password, login_acccount);
                if(!TextUtils.isEmpty(login_acccount) && !TextUtils.isEmpty(login_password)){
                    LoginManager login=new LoginManager(login_acccount, login_password,context);
                    login.loginByAsyncHttpPost();
                    Toast.makeText(context, "login", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(context, "账号或密码为空！！！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        v.findViewById(R.id.save_btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
