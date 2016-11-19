package com.my.hu.datalist;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by hu on 9/7/16.
 */
public class ItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListView lView=(ListView)adapterView;
        Log.i("hook_onItemClick",""+i);
    }
}
