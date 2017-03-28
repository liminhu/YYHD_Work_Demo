package com.my.backups.demo.update.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.my.backups.demo.R;
import com.my.backups.demo.TaskBean;
import com.my.backups.demo.test.BackUpDataOpt2;
import com.my.backups.demo.test.BackUpOptTest2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestMain2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main2);
    }




    public void testClick(View v) {
        List<TaskBean> list=new ArrayList<TaskBean>();
        TaskBean taskBean1=new TaskBean(1, "fun1", "1","0", "换皮肤","develop");
        TaskBean taskBean2=new TaskBean(2, "fun1", "1","0", "初始化蛇长度", "develop");
        list.add(taskBean1);
        list.add(taskBean2);
        String js = new Gson().toJson(list).toString();
        Log.e("hook_array", js.toString());

        try{
            JSONArray  array=new JSONArray(js);
            List<TaskBean> list_array=new ArrayList<TaskBean>();
            for(int i=0; i<array.length(); i++){
                 JSONObject js1=(JSONObject) array.get(i);
                 TaskBean taskBean=new TaskBean();
                 taskBean.setTaskId(js1.getInt(TaskBean.TASKID));
                 taskBean.setTaskName(js1.getString(TaskBean.TASKNAME));
                 taskBean.setTaskType(js1.getString(TaskBean.TASKTYPE));
                 taskBean.setTaskProtocol(js1.getString(TaskBean.TASKPROTOCOL));
                 taskBean.setTaskDetail(js1.getString(TaskBean.TASKDETAIL));
                taskBean.setTaskDeveloper(js1.getString(TaskBean.TASKDEVELOPER));
                  Log.e("hook_taskbena_"+i, taskBean.toString());
                 list_array.add(taskBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


//        JSONArray array=new JSONArray(list);
//        JSONObject js=new JSONObject();
//        try{
//            js.put("array",array);
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        //GenerateJsonStr gen=new GenerateJsonStr("com.tinybuild.PartyHardGO","7");  //疯狂派对谋杀案GO
       // GenerateJsonStr gen=new GenerateJsonStr("com.dotemu.titanquest","10102");    //泰坦之旅
        GenerateJsonStr gen=new GenerateJsonStr("com.squareenixmontreal.deusexgo","87803");    //杀出重围GO
        String def= gen.genBackupJsonStr();
        Log.i("hook_def_1",def);



        gen=new GenerateJsonStr("com.gameloft.android.ANMP.GloftG5EG.kaopu","26320");    //点杀泰坦2
        def= gen.genBackupJsonStr();
        Log.i("hook_def_4444",def);





  /*      gen=new GenerateJsonStr("com.frogmind.badland2.cmcm","1001044");    //破碎大陆2
       // gen=new GenerateJsonStr("com.fgol.HungrySharkEvolution","175");    //饥饿的鲨鱼
        def= gen.genBackupJsonStr();
        Log.i("hook_def_2",def);*/

   /*     gen=new GenerateJsonStr("com.gamehivecorp.taptitans2","145");    //点杀泰坦2
        def= gen.genBackupJsonStr();
        Log.i("hook_def_2",def);*/




        gen=new GenerateJsonStr("com.rayark.implosion","102009080");    //聚爆
        def= gen.genBackupJsonStr();
        Log.i("hook_def_2",def);


        gen=new GenerateJsonStr("com.ea.game.pvz2_na","232");    //植物大战僵尸2
        def= gen.genBackupJsonStr();
        Log.i("hook_def_3",def);





/*
        gen=new GenerateJsonStr("com.rockstargames.bully","14");    //恶霸鲁尼
        def= gen.genBackupJsonStr();
        Log.i("hook_def_2",def);


        gen=new GenerateJsonStr("com.turner.castledoombad","51");    //末日城堡  -- 还原后可能anr
        def= gen.genBackupJsonStr();
        Log.i("hook_def_3",def);


        gen=new GenerateJsonStr("com.square_enix.android_googleplay.dq8","10103");    //勇者斗恶龙8
        def= gen.genBackupJsonStr_example_3();
        Log.i("hook_def_4",def);
*/


       /* gen=new GenerateJsonStr("com.kurechii.postknight","72");    //快递骑士
        def= gen.genBackupJsonStr();
        Log.i("hook_def_2",def);


        gen=new GenerateJsonStr("com.squareenixmontreal.lcgo","71492");    //劳拉go
        def= gen.genBackupJsonStr();
        Log.i("hook_def_3",def);*/


/*        gen=new GenerateJsonStr("com.DoonomeGames.MadnessteerLive","1001");    //疯狂赛车直播间
        def= gen.genBackupJsonStr_example_1();
        Log.i("hook_def_3",def);


        gen=new GenerateJsonStr("com.gamehivecorp.taptitans","86");    //点杀泰坦  -- 未处理
        def= gen.genBackupJsonStr_example_2();
        Log.i("hook_def_4",def);*/


        Toast toast=Toast.makeText(this, def, Toast.LENGTH_LONG);
        showMyToast(toast, 10*1000);
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
