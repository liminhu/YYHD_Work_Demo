package com.my.hu.webviewjsinjectv2.util;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.my.hu.webviewjsinjectv2.R;
import com.my.hu.webviewjsinjectv2.domain.LinkJsonData;
import com.my.hu.webviewjsinjectv2.domain.StraightChainData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 9/18/16.
 */
public class DataAdapter extends BaseAdapter {
    private List<LinkJsonData> jsonDataList;
    private int resource;   //数据绑定到哪个资源界面上
    private LayoutInflater inflater; //布局填充器，可以使用xmL文件来生成它的view对象
    private Context context;

    public DataAdapter(Context context, List<LinkJsonData> lists, int resource){
        this.jsonDataList=lists;
        this.resource=resource;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater=LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position=i;
        ViewCache cache = null;
        if(view==null){
            cache = new ViewCache();
            view=inflater.inflate(resource, null);
            cache.id=(TextView)view.findViewById(R.id.id);
            cache.md5=(TextView)view.findViewById(R.id.md5);
            cache.platform_name=(TextView)view.findViewById(R.id.platform_name);
            cache.state=(TextView)view.findViewById(R.id.state);
            cache.downLoad_url=(TextView)view.findViewById(R.id.link_url);
            //存入在标志里当作缓存使用
            view.setTag(cache);
        }else{
            cache=(ViewCache)view.getTag();
        }
        LinkJsonData js=jsonDataList.get(i);
        cache.id.setText(js.id+"");
        cache.md5.setText(js.md5);
       // Log.i("hook_md5",js.md5);
        cache.platform_name.setText(js.platform_name);
        cache.downLoad_url.setText(js.link_url);
        cache.state.setText(js.state+"");
        Log.i("hook_cache_state","id:\t"+js.id+"\t"+js.state+"");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailInfo(position);
            }
        });
        return view;
    }

    private void showDetailInfo(int clickId){
        //Log.v("hook_showDetailInfo",""+clickId);
        LinkJsonData jsonData=(LinkJsonData)getItem(clickId);
        Log.i("hook_showdata",jsonData.link_url);
        new AlertDialog.Builder(context)
                .setTitle("编号"+jsonData.id)
                .setMessage("\tstate:"+jsonData.state+"\turl:"+jsonData.link_url)
                .setPositiveButton("确定",null)
                .show();
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
        public TextView state;
        public TextView downLoad_url;
    }




    public static  List<LinkJsonData> straightChainToLinkJsonData(List<StraightChainData> jsonDatas){
        List<LinkJsonData> list=new ArrayList<LinkJsonData>();
        for(int i=0; i<jsonDatas.size(); i++){
            LinkJsonData jsonData=new LinkJsonData();
            jsonData.id=jsonDatas.get(i).id;
            jsonData.link_url=jsonDatas.get(i).link_url;
            jsonData.md5=jsonDatas.get(i).md5;
            jsonData.platform_name=jsonDatas.get(i).platform_name;
            jsonData.update_time=jsonDatas.get(i).update_time;
            jsonData.state=jsonDatas.get(i).state;
            list.add(jsonData);
        }
        return list;
    }
}
