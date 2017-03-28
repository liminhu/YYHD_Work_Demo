package com.my.backups.demo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.my.backups.demo.test.BackUpDataOpt2;
import com.my.backups.demo.test.BackUpOptTest1;
import com.my.backups.demo.test.BackUpOptTest2;
import com.my.backups.demo.test.BackUpTest;
import com.my.backups.demo.utils.BackupUtils;
import com.my.backups.demo.utils.StringUtil;

import org.json.JSONArray;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getBaseContext();
    }


    public void testClick1(View v) {

        String sign=getSign(context);
        Toast toast=Toast.makeText(context, sign, Toast.LENGTH_LONG);
        StringBuilder sb=new StringBuilder(sign);
        int i=0;
        Log.d("hook_signatures_0_len:", ""+sign.length());
        int add=500,flag=1;
        while(i<sign.length() && flag==1){
            if(i+add<sign.length()){
                String data=sb.substring(i,i+add);
                i+=add;
                Log.d("hook_signatures_0_"+i, data);
            }else{
                String data=sb.substring(i,sign.length());
                Log.d("hook_signatures_0_"+sign.length(), data);
                flag=0;
            }
        }
        Log.d("hook_signatures_0_end",""+(sign.length()-i));
        showMyToast(toast, 10*1000);
    }



    private String getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageName.equals(context.getPackageName())) {
                String data=packageinfo.signatures[0].toCharsString();
                 Log.d("hook_sign_old_len:", ""+data.length()+"\n"+data);
                return StringUtil.byteArrayToHexString(data.getBytes());
            }
        }
        return null;
    }









    public void testClick(View v) {






  /*      String data= BackUpOptTest1.initbackupJsonStr();
        Log.i("hook_data",data);
        Log.i("hook_world3", BackUpOptTest1.initbackupJsonStr_World3());


        String[] packageName={"nz.co.codepoint.minimetro","com.ea.game.simcitymobile_row","com.FDGEntertainment.Oceanhorn.gp","com.pixeltoys.freeblade"};
            for(int i=0; i<packageName.length; i++){
            String test=BackUpOptTest1.initbackupJsonStr_Common(packageName[i], "*");
            Log.i("hook_data_"+i, test);
        }

        String test_1=BackUpOptTest1.initbackupJsonStr_Common(packageName[0], "*");
        Log.i("hook_+"+packageName[0], test_1);


        String test1=BackUpOptTest1.initbackupJsonStr_HexageReaper("net.hexage.reaper","210413");
        Log.i("hook_HexageReaper", test1);
*/

        String def= BackUpOptTest2.initBackupDefaultJsonStr();
        Log.i("hook_def",def);

        String terrariaPaid=BackUpOptTest2.initbackupTerrariaPaidJsonStr();
        Log.i("hook_terrariaPaid",terrariaPaid);

        String terraria=testParsonJson(terrariaPaid);
       // Log.e("hook_terraria",terraria);


        String World3= BackUpOptTest2.initbackupJsonStr_World3();
        Log.i("hook_World3",World3);


        String hexageReaper= BackUpOptTest2.initbackupJsonStr_HexageReaper();
        Log.i("hook_hexageReaper",hexageReaper);

        String[] packageName={"nz.co.codepoint.minimetro","com.ea.game.simcitymobile_row","com.FDGEntertainment.Oceanhorn.gp","com.pixeltoys.freeblade"};
        for(int i=0; i<packageName.length; i++){
            String test=BackUpOptTest2.initbackupJsonStr_Common(packageName[i], "-1");
            Log.i("hook_data_"+i, test);
            if(i==0){
                String test0=testParsonJson(test);
              //  Log.e("hook_test0",test0);
            }
        }
        String parson=testParsonJson(hexageReaper);
        Log.e("hook_hexageReaper",hexageReaper);
        Log.e("hook_parson",parson);
        Toast toast=Toast.makeText(this, parson, Toast.LENGTH_LONG);
        showMyToast(toast, 10*1000);
    }



    private String testParsonJson(String data){
        String json=data;
                //"{\"target\":{\"verCode\":\"12785\",\"pkgName\":\"com.and.games505.TerrariaPaid\"},\"files\":{\"data\":{\"exclude\":[\"app_gameassist\\\\\\/.\\\\*.?\",\"lib\\\\\\/.\\\\*.?\",\"cache\\\\\\/.\\\\*.?\",\"files\\\\\\/.\\*.png\",\"shared_prefs\\/com.codeglue.terraria.Terraria.xml\"]}}}";
        List<String> list= BackupUtils.parseJsonGetDataExcludeList(json);
        if(list==null){
            return "";
        }
        StringBuilder str=new StringBuilder();
        for(int i=0; i<list.size(); i++){
            str.append("\t"+list.get(i)+"\t");
        }
        return str.toString();
    }

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}
