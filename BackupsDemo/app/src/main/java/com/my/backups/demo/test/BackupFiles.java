package com.my.backups.demo.test;

import android.widget.ListView;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by hu on 17-2-7.
 */

public class BackupFiles {
    public  Data data;
    public  SdcardData sdcardData;
    public  SdcardRoot sdcardRoot;

    public BackupFiles() {
        data=new Data();
        sdcardData=new SdcardData();
        sdcardRoot=new SdcardRoot();
    }


    /*   public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public SdcardData getSdcardData() {
        return sdcardData;
    }

    public void setSdcardData(SdcardData sdcardData) {
        this.sdcardData = sdcardData;
    }


    public SdcardRoot getSdcardRoot() {
        return sdcardRoot;
    }

    public void setSdcardRoot(SdcardRoot sdcardRoot) {
        this.sdcardRoot = sdcardRoot;
    }*/

    class Data{
        public List<String> exclude;
        public List<String> include;
    }


    class SdcardData{
        public List<String> exclude;
        public List<String> include;
    }

    class SdcardRoot{
        public List<String> exclude;
        public List<String> include;
    }



    private JSONArray getJsonArrayFromList(List<String> list){
        JSONArray jsonArray=new JSONArray(list);
        return jsonArray;
    }



    public JSONArray getSdcardDataIncludeJsonArray(){
        return getJsonArrayFromList(sdcardData.include);
    }


    public JSONArray getDataExcludeJsonArray(){
        return getJsonArrayFromList(data.exclude);
    }

    public JSONArray getSdcardRootIncludeJsonArray(){
        return getJsonArrayFromList(sdcardRoot.include);
    }
}
