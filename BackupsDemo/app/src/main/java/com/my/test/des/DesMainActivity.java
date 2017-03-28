package com.my.test.des;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.my.backups.demo.R;
import com.my.utils.FilesUtils;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesMainActivity extends AppCompatActivity{
    private static final String fileName="idsky_plugins_config.xml";
    private static final String fileName1="idsky_plugins.xml";
    private static final String key="idreamsky2009110";

    private static final String isEnable_No ="isEnabled=\"false\"";
    private static final String isEnable_Yes="isEnabled=\"true\"";


    private static final String temp_old="temp.txt";
    private static final String temp_old_1="temp_1.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des_main);
        Button button=(Button) findViewById(R.id.des_button);
    }


    public void showResult1_1(View v){
        try{
            StringBuilder sb=new StringBuilder();
            InputStream assetManager=getAssets().open(fileName1);
            byte[] data= FilesUtils.readStream(assetManager);
            String temp= new String(data);
            Log.e("hook_file",temp);
            String v0=test_decode(temp);
            Log.e("hook_file_1",v0);

             StringBuilder aaa=new StringBuilder(v0);
            //   v0.replaceAll(isEnable_Yes, isEnable_No);
           // Log.e("hook_file_2",v0);
          //  FilesUtils.writeDataToFile("/sdcard/test/temp.txt", result.getBytes());

            String s1=test_Encode(v0);
            String v1=test_decode(s1);

            String s2 =replaceBlank(s1);
            Log.d("hook_js", v1);
            if(s1.equals(temp)){
                Log.e("hook_result", "true");
            }else {
                Log.e("hook_result", "false");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }




    public void showResult1(View v){
        try {
            StringBuilder sb = new StringBuilder();
            InputStream assetManager = getAssets().open(temp_old);
            byte[] data = FilesUtils.readStream(assetManager);
            String temp = new String(data);

             Log.e("hook_file_1",temp);

            String s1=test_Encode(temp);
            FilesUtils.writeDataToFile("/sdcard/test/temp.txt", s1.getBytes());
            String v1=test_decode(s1);

            Log.e("hook_file", v1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void showResult22221(View v){
        try {
            StringBuilder sb = new StringBuilder();
            InputStream assetManager = getAssets().open(temp_old_1);
            byte[] data = FilesUtils.readStream(assetManager);
            String temp = new String(data);

          //  Log.e("hook_file_1",temp);

            String s1=test_Encode_1(temp);
            FilesUtils.writeDataToFile("/sdcard/test/temp_2.txt", s1.getBytes());
            String v1=test_decode(s1);

            Log.e("hook_file", v1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void showResult11(View v){
        try{
            StringBuilder sb=new StringBuilder();
            InputStream assetManager=getAssets().open(fileName1);
            byte[] data= FilesUtils.readStream(assetManager);
            String temp= new String(data);
            Log.e("hook_file",temp);
            String v0=test_decode(temp);
            String result=URLDecoder.decode(v0.toString(),"utf-8");
            Log.e("hook_file_1",result);
           // StringBuilder aaa=new StringBuilder(v0);
         //   v0.replaceAll(isEnable_Yes, isEnable_No);
            Log.e("hook_file_2",v0);
        //    FilesUtils.writeDataToFile("/sdcard/test/temp.txt", v0.getBytes());

   /*         String s1=test_Encode(v0);
            String v1=test_decode(s1);

            //String s2 =replaceBlank(s1);
            Log.d("hook_js", v1);
            if(s1.equals(temp)){
                Log.e("hook_result", "true");
            }else {
                Log.e("hook_result", "false");
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String test_decode(String data){
        try{
            StringBuilder sb=new StringBuilder();
            JSONArray jsonArray=new JSONArray(data);
            for(int i=0; i<jsonArray.length(); i++){
                String aaa=(String)jsonArray.get(i);
                Log.i("hook_js_"+i, aaa);
                Log.i("hook_js_len",""+aaa.length());

                byte[] base64= Base64.decode(((String) jsonArray.get(i)).getBytes(), Base64.DEFAULT);
                byte[] result=b(key,base64);
                String temp=new String(result);
               // Log.e("hook_ret_"+i, URLDecoder.decode(temp,"utf-8"));
                sb.append(new String(result));
                if(i+1<jsonArray.length()){
                    //sb.append("aaaa");
                }
            }
            Log.e("hook_result", "1111");
            if(sb.toString()!=null){
                Log.e("hook_result", sb.toString());
              //  String result=URLDecoder.decode(sb.toString(),"utf-8");
              //  Log.e("hook_decode",sb.toString());
                return  URLDecoder.decode(sb.toString(),"utf-8");
            }
        }catch (Exception e){
            Log.e("hook_result_111", e.getMessage());
        }
        return null;
    }

    public String test_Encode(String data){
        try{
            JSONArray js=new JSONArray();
            StringBuilder st=new StringBuilder();
            String[] array=data.split("aaaa");
            for(int i=0; i<array.length; i++){
                byte[]  v1=encode(key, array[i].getBytes());
                Log.d("hook_last", new String(v1));
                String test_data= URLEncoder.encode( new String(v1));
                Log.d("hook_111", test_data);

                Log.d("hook_111", "11111");
                byte[] v2=Base64.encode(v1, Base64.DEFAULT);
                Log.d("hook_222", new String(v2));
                js.put(new String(v2));
            }
            String s1 =replaceBlank(js.toString());
            return s1;
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }




    public String test_Encode_1(String data){
        try{
            JSONArray js=new JSONArray();
            StringBuilder st=new StringBuilder();
            for(int i=0; i<data.length(); i=i+140){
                Log.d("hook_i", ""+i);
                byte[] array=null;
                if(i+140 < data.length()) {
                    array = new byte[140];
                    System.arraycopy(data.getBytes(), i, array, 0, 140);
                }else{
                    array = new byte[data.length() % 140];
                    System.arraycopy(data.getBytes(), i, array, 0, data.length() % 140);
                }
                Log.d("hook_i", ""+new String(array));
                byte[]  v1=encode(key, array);
                Log.d("hook_last", new String(v1));
                String test_data= URLEncoder.encode( new String(v1));
                Log.d("hook_111", test_data);

                Log.d("hook_111", "11111");
                byte[] v2=Base64.encode(v1, Base64.DEFAULT);
                Log.d("hook_222", new String(v2));
                js.put(new String(v2));
            }
            String s1 =replaceBlank(js.toString());
            return s1;
        }catch ( Exception e){
            e.printStackTrace();
        }
        return null;
    }




    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        Log.e("hook_replaceBlank", dest);
        return dest;
    }





    private static byte[] b(String arg4, byte[] arg5) {
        byte[] v0_2;
        try {
            SecretKey v0_1 = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(arg4.getBytes()));
            Cipher v1 = Cipher.getInstance("DES/CBC/PKCS5Padding");
            v1.init(2, ((Key)v0_1), new IvParameterSpec("alwaysbe".getBytes()));
            v0_2 = v1.doFinal(arg5);
        }
        catch(Exception v0) {
            v0.printStackTrace();
            v0_2 = null;
        }
        return v0_2;
    }



    private static byte[] encode(String arg4, byte[] arg5) {
        byte[] v0_2;
        try {
            SecretKey v0_1 = SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(arg4.getBytes()));
            Cipher v1 = Cipher.getInstance("DES/CBC/PKCS5Padding");
            v1.init(1, ((Key)v0_1), new IvParameterSpec("alwaysbe".getBytes()));
            v0_2 = v1.doFinal(arg5);
        }
        catch(Exception v0) {
            v0.printStackTrace();
            v0_2 = null;
        }
        return v0_2;
    }


}
