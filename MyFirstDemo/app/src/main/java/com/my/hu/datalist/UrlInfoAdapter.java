package com.my.hu.datalist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.hu.demain.UrlInfo;
import com.my.hu.myfirstdemo.R;
import com.my.hu.myfirstdemo.Test1MainActivity;

import java.util.List;
import java.util.Objects;

/**
 * Created by hu on 9/7/16.
 */
public class UrlInfoAdapter extends BaseAdapter {
    private List<UrlInfo> urlInfos;
    private int resource; //数据绑定到哪个资源界面上
    private LayoutInflater inflater; //布局填充器，可以使用xmL文件来生成它的view对象


    public UrlInfoAdapter(Context context,List<UrlInfo> urlInfos,int resource){
        this.urlInfos=urlInfos;
        this.resource=resource;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return urlInfos.size();
    }

    public Object getItem(int position){
        return urlInfos.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView id_view;
        TextView download_package_name_view;
        TextView downLoad_url_view;
        TextView request_time_view;
        if(convertView == null){//为第一页显示
            convertView=inflater.inflate(resource,null);
            id_view=(TextView)convertView.findViewById(R.id.id);
            download_package_name_view=(TextView)convertView.findViewById(R.id.download_package_name);
            downLoad_url_view=(TextView)convertView.findViewById(R.id.downLoad_url);
          //  request_time_view=(TextView)convertView.findViewById(R.id.request_time);
            ViewCache cache=new ViewCache();
            cache.id=id_view;
            cache.download_package_name=download_package_name_view;
            cache.downLoad_url=downLoad_url_view;
           // cache.request_time=request_time_view;
            //存入在标志里当作缓存使用
            convertView.setTag(cache);
        }else{
            ViewCache cache=(ViewCache)convertView.getTag();
            id_view=cache.id;
            download_package_name_view=cache.download_package_name;
            downLoad_url_view=cache.downLoad_url;
            request_time_view=cache.request_time;
        }
        UrlInfo urlInfo=urlInfos.get(position);
        id_view.setText(urlInfo.getId());
        download_package_name_view.setText(urlInfo.getDownload_package_name());
        downLoad_url_view.setText(urlInfo.getDownLoad_url());
        //request_time_view.setText(urlInfo.getRequest_time());
        return convertView;
    }





    /**
     * 缓存view对象
     */
    private final class ViewCache{
        public  TextView id;
        public  TextView download_package_name;
        public  TextView downLoad_url;
        public  TextView request_time;

    }

}
