package com.gameassist.plugin.common;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.gameassist.plugin.bean.TokenBean;
import com.gameassist.plugin.utils.HttpUtils;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.SharedPreferenceUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hulimin on 2017/9/25.
 */

public class LoginManager {
    private String login_acccount;
    private String login_password;
    private Context context;


    public LoginManager() {
    }

    public LoginManager(String login_acccount, String login_password,Context context) {
        super();
        this.login_acccount = login_acccount;
        this.login_password = login_password;
        this.context=context;
    }


    public LoginManager(Context context) {
        this.context = context;
    }

    public  void loginOut(){
        new InitLoutOutAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }



    public void loginByAsyncHttpPost() {
        new InitAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }




    public class InitLoutOutAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... params) {
              MyLog.e("hook_url_doInBackground - %s", "1111");
            String token= SharedPreferenceUtils.getTokenStrFromSharedPre(context);
            MyLog.e(token+" ---- ");
            if (!TextUtils.isEmpty(token)) {
                Map map=new HashMap();
                map.put("token", token);
                String loginUrl= UrlManager.getLoginOutTokenUrl(UrlManager.HTTP_PROTOCOL);
                String result= HttpUtils.submitPostData(loginUrl, map, "utf-8");
                MyLog.e("result  -- "+result);
                return result;
            }
            return  "";
        }


        @Override
        protected void onPostExecute(String result) {
            MyLog.e(result);
            TokenBean tokenBean=getTokenData(result);
            MyLog.e(tokenBean.msg);
            SharedPreferenceUtils.clearAllData(context);
            super.onPostExecute(result);
        }
    }









    public class InitAsyncTask extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... params) {
         //   MyLog.e("hook_url_doInBackground - %s", url);
            Map map=new HashMap();
            map.put("username", login_acccount);
            map.put("password", login_password);
            String loginUrl= UrlManager.getTokenUrl(UrlManager.HTTP_PROTOCOL);
            String token= HttpUtils.submitPostData(loginUrl, map, "utf-8");
            return  token;
        }


        @Override
        protected void onPostExecute(String result) {
            MyLog.e(result);
            TokenBean tokenBean=getTokenData(result);
            if(tokenBean!=null){
                MyLog.e("token --- 1111 %s", tokenBean.token);
                if(tokenBean.rc==0){
                    SharedPreferenceUtils.saveOrUpdateToken(context, tokenBean);
                    Toast.makeText(context,"登录成功。。。",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context,tokenBean.msg,Toast.LENGTH_LONG).show();
                }
            }else {
                MyLog.e("token --- is null");
            }
            super.onPostExecute(result);
        }
    }


    private TokenBean getTokenData(String result){
        TokenBean tokenBean=new TokenBean();
        try{
            JSONObject js=new JSONObject(result);
            tokenBean.msg=js.optString("msg", "");
            tokenBean.data=js.optString("data", "");
            tokenBean.rc=js.optInt("rc", -1);
            if(tokenBean.rc==0){
                JSONObject token_data=new JSONObject(tokenBean.data);
                String token=token_data.optString("token", "");
                MyLog.e(token);
                //int index=token.indexOf('|');
               // String last=token.substring(index+1, token.length());
                tokenBean.token=token; //token;
                tokenBean.username=login_acccount;
            }
            return tokenBean;
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return null;
    }




}
