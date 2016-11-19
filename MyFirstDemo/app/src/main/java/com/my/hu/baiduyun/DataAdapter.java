package com.my.hu.baiduyun;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.hu.myfirstdemo.R;
import com.my.hu.myinterface.JsonData;

import java.util.List;

/**
 * Created by hu on 9/18/16.
 */
public class DataAdapter extends BaseAdapter {
    private List<JsonData> jsonDataList;
    private int resource;   //数据绑定到哪个资源界面上
    private LayoutInflater inflater; //布局填充器，可以使用xmL文件来生成它的view对象
    private Context context;
    private final String BAIDU_PACKAGE_NAME="com.baidu.netdisk.download.engine";

    public DataAdapter(Context context, List<JsonData> lists, int resource){
        this.jsonDataList=lists;
        this.resource=resource;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       // Log.v("hook_getView",""+i);
        final int position=i;
        ViewCache cache = null;
        if(view==null){
            cache = new ViewCache();
            //Log.v("hook_view=null_resource",""+resource);
            view=inflater.inflate(resource, null);
            cache.id=(TextView)view.findViewById(R.id.id);
            cache.md5=(TextView)view.findViewById(R.id.md5);
          //  Log.v("hook_view=md5","md5");
            cache.platform_name=(TextView)view.findViewById(R.id.platform_name);
            cache.downLoad_url=(TextView)view.findViewById(R.id.old_url);
            //存入在标志里当作缓存使用
            view.setTag(cache);
        }else{
            cache=(ViewCache)view.getTag();
        }
        JsonData js=jsonDataList.get(i);
        cache.id.setText(js.id+"");
        cache.md5.setText(js.md5);
       // Log.i("hook_md5",js.md5);
        cache.platform_name.setText(js.platform_name);
        cache.downLoad_url.setText(js.old_url);
        Log.i("hook_js.old_url",js.old_url);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailInfo(position);
            }
        });
        return view;
    }



    private  static String[] url_array=new String[]{
            "baiduyun://127.0.0.1/action.DOWNLOAD?shareid=36115247&uk=3897033868&username=ggzhushou&gameId=a1",
            "baiduyun://127.0.0.1/action.DOWNLOAD?shareid=3105698195&uk=3897033868&username=ggzhushou&gameId=b2",
            "baiduyun://127.0.0.1/action.DOWNLOAD?shareid=1973885935&uk=3897033868&username=ggzhushou&gameId=c3"
    };


    private void showDetailInfo(int clickId){
        JsonData jsonData=(JsonData) getItem(clickId);
        Log.i("hook_id",jsonData.id+"");
     //   for(int i=0; i<url_array.length; i++) {
            Log.i("hook_showDetailInfo",url_array[0]);
            OpenBaiduYunApp app = new OpenBaiduYunApp(context);
            app.openStartApp(BAIDU_PACKAGE_NAME, jsonData.old_url, jsonData.id);
  /*          try {
               Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
      //  }
        /*new AlertDialog.Builder(context)
                .setTitle("编号"+jsonData.id)
                .setMessage("url:"+jsonData.old_url)
                .setPositiveButton("确定",null)
                .show();*/

    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return jsonDataList.get(i);
    }

    @Override
    public int getCount() {
        return jsonDataList.size();
    }


    private final class ViewCache{
        public TextView id;
        public TextView md5;
        public TextView platform_name;
        public TextView downLoad_url;
    }

}
