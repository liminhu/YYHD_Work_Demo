package com.gameassist.plugin.mm.robot;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gameassist.plugin.common.LoginManager;
import com.gameassist.plugin.utils.DialogUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.SharedPreferenceUtils;
import com.gameassist.plugin.websocket.WebSocketService;

/**
 * Created by hulimin on 2017/9/7.
 */

public class PluginVIew extends FrameLayout implements View.OnClickListener {
    private Context context;
    private Handler handler;
    private TextView tv_insturct_0, tv_insturct_6, tv_insturct_7, tv_insturct_login, tv_login_out;


    public PluginVIew(Context context, Handler handler) {
        super(context);
        this.context = context;
        this.handler = handler;
        initView();
    }

    void initView() {
        inflate(context, R.layout.prompt, this);

        tv_insturct_0 = (TextView) findViewById(R.id.insturct_0);
        tv_insturct_0.setOnClickListener(this);


        tv_insturct_6 = (TextView) findViewById(R.id.insturct_6);
        tv_insturct_6.setOnClickListener(this);


        tv_insturct_7 = (TextView) findViewById(R.id.insturct_7);
        tv_insturct_7.setOnClickListener(this);

        tv_insturct_login = (TextView) findViewById(R.id.insturct_login);
        tv_insturct_login.setOnClickListener(this);

        //登出
        tv_login_out=(TextView)findViewById(R.id.insturct_login_out);
        tv_login_out.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insturct_0:
                String token= SharedPreferenceUtils.getTokenStrFromSharedPre(context);
                if (TextUtils.isEmpty(token)) {
                    Toast.makeText(context, "请先登录 .... ", Toast.LENGTH_SHORT).show();
                }else if (WebSocketService.isClosed) {
                    handler.sendEmptyMessage(0);   //初始化数据
                }else{
                    handler.sendEmptyMessage(1);  //标庄开始
                }
                break;

            case R.id.insturct_6:
                handler.sendEmptyMessage(202);  //续庄
                break;

            case R.id.insturct_7:
                handler.sendEmptyMessage(203);  //下庄
                break;

            case R.id.insturct_login_out:  //登出
                MyLog.e(" insturct_login_out  ---- ");
                String loginout= SharedPreferenceUtils.getTokenStrFromSharedPre(context);
                if(!TextUtils.isEmpty(loginout)){
                    LoginManager loginManager =new LoginManager(getContext());
                    loginManager.loginOut();
                }
                Toast.makeText(getContext(), "现已经退出登录。。。", Toast.LENGTH_SHORT).show();
                break;

            case R.id.insturct_login:  //登录
               // handler.sendEmptyMessage(2006);
                DialogUtils.showLoginDialog(getContext(), PluginEntry.currentActivity);
                break;
        }
    }

}
