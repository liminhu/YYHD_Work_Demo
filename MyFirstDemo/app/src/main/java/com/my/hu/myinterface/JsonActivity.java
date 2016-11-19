package com.my.hu.myinterface;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.my.hu.myfirstdemo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class JsonActivity extends AppCompatActivity {
    private  static final int TIMEOUT_IN_MILLIONS = 5000;
    private  static final String url_Interface="http://ggapi-2014904031.cn-north-1.elb.amazonaws.com.cn:8010/api/get/dead_pan_links";
    private  String  result=null;
    private  static final String BAIDU_PLATFORM="baidu";
    private  static final String TIANYI_PLATFORM="tianyi";
    private  static final String QH_360_PLATFORM="360";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
    }


    public void showJsonData(View view){
        Intent intent=new Intent(JsonActivity.this, ShowDataActivity.class);
        startActivity(intent);
    }




    //Ctrl+Alt+T可以把代码包在一块内，例如try/catch
    public void GetJson(View view){
        new Thread(getThread).start();
    }


    public void saveJsonData(View view){
        Log.v("hook_saveJsonData","saveJsonData");
        new Thread(myUrlThread).start();
    }






    private Handler postHandler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what==0){
                try {
                    JSONObject json = new JSONObject(result);
                    String json_msg=json.getString("msg");
                    if(json_msg.equals("ok")){
                         JSONObject json1=json.getJSONObject("data");
                         String baiduStr=json1.getString(BAIDU_PLATFORM);

                        new AlertDialog.Builder(JsonActivity.this)
                                .setTitle("title").setMessage("baidu:"+baiduStr)
                                .setPositiveButton("close",null).show();

                         JsonBean baiduBean=parseJsonString(baiduStr,BAIDU_PLATFORM);
                         String qh360Str=json1.getString(QH_360_PLATFORM);
                         JsonBean qh360Bean=parseJsonString(baiduStr,QH_360_PLATFORM);
                         String tanyiStr=json1.getString(TIANYI_PLATFORM);
                         JsonBean tanyiBean=parseJsonString(tanyiStr,TIANYI_PLATFORM);
                    }
                }catch (Exception e){
                    Log.e("hook_handleMessage","handleMessage exception");
                }
            }else if(msg.what==1){
                Bundle b=msg.getData();
                String data=b.getString("data");
                Log.v("hook_JSON_db","data");
                SaveDataToDB db=new SaveDataToDB(getBaseContext());
                try {
                    JSONObject json = new JSONObject(data);
                    String json_msg=json.getString("msg");
                    if(json_msg.equals("ok")){
                        Log.v("hook_JSON_data","ok");
                        JSONObject json1=json.getJSONObject("data");
                        String baiduStr=json1.getString(BAIDU_PLATFORM);
                      //  Log.v("hook_JSON_baiduStr",baiduStr);
                        JsonBean baiduBean=parseJsonString(baiduStr,BAIDU_PLATFORM);
                        db.saveDataJsonBean(baiduBean);
                        String qh360Str=json1.getString(QH_360_PLATFORM);
                      //  Log.v("hook_JSON_qh360Str",qh360Str);
                        JsonBean qh360Bean=parseJsonString(qh360Str,QH_360_PLATFORM);
                        db.saveDataJsonBean(qh360Bean);


                        String tanyiStr=json1.getString(TIANYI_PLATFORM);
                        JsonBean tanyiBean=parseJsonString(tanyiStr,TIANYI_PLATFORM);
                        db.saveDataJsonBean(tanyiBean);
                    }
                }catch (Exception e){
                    Log.e("hook_handleMessage","handleMessage exception");
                }
                db.closeSqliteDBHelp();
                new AlertDialog.Builder(JsonActivity.this)
                        .setTitle("save data to database").setMessage("数据保存成功！！！")
                        .setPositiveButton("close",null).show();
               // Intent intent=new Intent(JsonActivity.this, ShowDataActivity.class);
               // startActivity(intent);
            }
        };
    };


    private JsonBean parseJsonString(String dataStr, String platform_name){
        try {
            JsonBean jb=new JsonBean();
            jb.setPlatform_name(platform_name);
            HashMap<String, String> map=new HashMap<String,String>();
            String data_temp=dataStr.substring(dataStr.indexOf("{")+1,dataStr.lastIndexOf("}"));
            String temp1=data_temp.replace('\"',' ');
            String temp2=temp1.replace('/','\\');
            String temp3=temp2.replace("\\\\","\\");
            String temp=temp3.replace('\\','/');
            String[] dataArray=temp.split(",");
            for(int i=0; i<dataArray.length; i++){
                String data=dataArray[i];
                String[] str=data.split(" : ");
                map.put(str[0].trim(), str[1].trim());
                //Log.v("hook_parseJson_str0", str[0].trim());
                //Log.v("hook_parseJson_str1", str[1].trim());
            }
            jb.setData(map);
            return jb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("hook_parseJson", "exception");
        return null;
    }



   private MyUrlThread myUrlThread=new MyUrlThread(postHandler,url_Interface);



    private Thread getThread = new Thread() {
        public void run() {
            URL url = null;
            HttpURLConnection conn = null;
            InputStream is = null;
            ByteArrayOutputStream baos = null;
            try {
                url = new URL(url_Interface);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
                conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    int len = -1;
                    byte[] buf = new byte[128];

                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    baos.flush();
                    result=baos.toString();
                    Message msg=Message.obtain();
                    msg.what=0;
                    postHandler.sendMessage(msg);
                } else {
                    throw new RuntimeException(" responseCode is not 200 ... ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null)
                        baos.close();
                    if (is != null)
                        is.close();
                    if(conn!=null) {
                        conn.disconnect();
                    }
                } catch (IOException e) {
                    Log.e("hook_Interface","url request exception");
                }
            }
        };
    };


}
