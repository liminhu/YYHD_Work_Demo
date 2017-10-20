package com.gameassist.plugin.bean;

import com.gameassist.plugin.hongbao.WalletLuckyMoneyDetail;
import com.gameassist.plugin.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hulimin on 2017/9/8.
 */

public class InstructionsBean {
    private static final String[] qSet = {"q40", "q60", "q80", "q100", "q120", "q140", "q160", "q180", "q200", "q300", "q400"};


    //{"instruct_type": 0, "code": "stb", "group_id": "", "user_info, time , money_info}
    public static  final  String INSTRUCT_TYPE="instruct_type";
    private static final String CODE="code";
    private static final String GROUP_ID="group_id";
    private static final String TIME="time";
    private static final String MONEY_INFO="money_info";

    private static final String PERIOD="period";     //0|1|2|3|4|5|6|7
    private static final String MSG_TYPE="msg_type"; //0|1|2 注： 0表示各个期间指令 1文字指令 2图片指令,
    private static final String MSG="msg";   //当 msg_type 为0 msg为空字符串; msg_type 为1 msg为非空字符串; msg_type 为2 msg为图片二进制,
    private static final String RC="rc";   // 0 注：成功返回0,
    private static final String TIP="tip";     // 注： 错误信息


    private static final String USER_INFO="user_info";
    private static final String USER_NAME ="user_name";
    private static final String NICK_NAME="nick_name";

    private static final String CURRENT_TIME="time";
    private static final String MONEY_AMOUNT="amount";



    public static boolean isContainsQset(String content){
        for(int i=0; i<qSet.length; i++){
            if(content.contains(qSet[i])){
                return true;
            }
        }
        return false;
    }



    public static  boolean isNumeric(String str){
        if(str.contains(":\n")){
            String[] temp_name=str.split(":\n");
            str=temp_name[1];
        }

        if(str.startsWith("sh")){
            Pattern pattern = Pattern.compile("^sh[0-9]*");
            Matcher isNum = pattern.matcher(str);
            if(isNum.matches() ){
                return true;
            }
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(isNum.matches() ){
            return true;
        }
        return false;
    }





    private  int instruct_type;
    private  String code;
    private  String group_id;
    private  String user_name;
    private  String nick_name;
    private long current_time;

    public InstructionsBean(int instruct_type, String code, String group_id, String user_name, String nick_name, long current_time) {
        this.instruct_type = instruct_type;
        this.code = code;
        this.group_id = group_id;
        this.user_name = user_name;
        this.nick_name = nick_name;
        this.current_time = current_time;
    }

    public InstructionsBean() {
    }




    //{"instruct_type": 0, "code": "stb", "group_id": ""} ,"user_info": {"uid": "", "nick_name": ""
    public static  String genFirstBeginInstruct(ChatroomTb chatroomTb, int instruct_type, String code, String group_id) {
        JSONObject js = new JSONObject();
        try {
            js.put(INSTRUCT_TYPE, instruct_type);
            js.put(CODE, code);
            js.put(GROUP_ID, group_id);
            JSONArray array=new JSONArray();

            Map<String, String> map=chatroomTb.getMap();
            for (String key : map.keySet()) {
                String value =map.get(key);
                JSONObject js_name=new JSONObject();
                js_name.put(USER_NAME, key);
                js_name.put(NICK_NAME, value);
                array.put(js_name);
            }
            js.put(USER_INFO, array);
             MyLog.e(js.toString());
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return js.toString();
    }





    public String genHongBaoInsJsonStr(List<WalletLuckyMoneyDetail> list) {
        JSONObject js = new JSONObject();
        try {
           // MyLog.e("genHongBaoInsJsonStr --- "+list.toArray().toString());
            js.put(INSTRUCT_TYPE, instruct_type);
            js.put(CODE, code);
            js.put(GROUP_ID, group_id);
            JSONArray array=new JSONArray();
            for(int i=0; i<list.size(); i++){
                JSONObject money=new JSONObject();
                money.put(USER_NAME, list.get(i).getReceive_userName());
                money.put(NICK_NAME, list.get(i).getNick_name());  //更正nick_name
                money.put(MONEY_AMOUNT, list.get(i).getReceive_money_amount());
                money.put(TIME, list.get(i).getReceive_time());
                array.put(money);
            }
           // MyLog.e("genHongBaoInsJsonStr array --- "+array.toString());
            js.put(MONEY_INFO, array);
          //  MyLog.e(js.toString());
        }catch (Exception e){
            MyLog.e("genHongBaoInsJsonStr --- "+e.getMessage());
        }
        return js.toString();
    }


    public static  final  String   KEY_SENDID="key_sendid";
    private String   key_sendid;

    public String getKey_sendid() {
        return key_sendid;
    }

    public void setKey_sendid(String key_sendid) {
        this.key_sendid = key_sendid;
    }

    public String genSendInstructionJsonString(ChatroomTb chatroomTb){
        JSONObject js=new JSONObject();
        try {
            js.put(INSTRUCT_TYPE, instruct_type);
            js.put(CODE, code);
            js.put(GROUP_ID, group_id);

            if(instruct_type==6 || instruct_type==7){
                JSONArray array=new JSONArray();
                Map<String, String> map=chatroomTb.getMap();
                for (String key : map.keySet()) {
                    String value =map.get(key);
                    JSONObject js_name=new JSONObject();
                    js_name.put(USER_NAME, key);
                    js_name.put(NICK_NAME, value);
                    array.put(js_name);
                }
                js.put(USER_INFO, array);

                return js.toString();  //续庄与下庄不带时间
            }

            js.put(CURRENT_TIME, current_time);

            if(instruct_type==4){
                return js.toString();  //不带用户信息
            }

            if(getInstruct_type() == 3){
                js.put(KEY_SENDID, getKey_sendid());
            }

            JSONObject user=new JSONObject();
            user.put(USER_NAME, user_name);
            user.put(NICK_NAME, nick_name);
            js.put(USER_INFO, user);

        }catch (Exception e){

        }
        return js.toString();
    }





    public String genSendInstructionJsonString(){
        JSONObject js=new JSONObject();
        try {
            js.put(INSTRUCT_TYPE, instruct_type);
            js.put(CODE, code);
            js.put(GROUP_ID, group_id);

            if(instruct_type==6 || instruct_type==7){
                return js.toString();  //续庄与下庄不带时间
            }

            js.put(CURRENT_TIME, current_time);

            if(instruct_type==4){
                return js.toString();  //不带用户信息
            }

            if(getInstruct_type() == 3){
                js.put(KEY_SENDID, getKey_sendid());
            }

            JSONObject user=new JSONObject();
            user.put(USER_NAME, user_name);
            user.put(NICK_NAME, nick_name);
            js.put(USER_INFO, user);

        }catch (Exception e){

        }
        return js.toString();
    }



    public InstructionsBean(long current_time) {
        this.current_time = current_time;
    }

    public int getInstruct_type() {
        return instruct_type;
    }

    public void setInstruct_type(int instruct_type) {
        this.instruct_type = instruct_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }




    public static InstructionsBean getInsBeanFromJSON(String data){
        InstructionsBean bean=new InstructionsBean();
        try{
            JSONObject js=new JSONObject(data);
            bean.setMsg(js.optString(MSG, ""));
            bean.setTip(js.optString(TIP, ""));
            bean.setPeriod(js.optInt(PERIOD, 0));
            bean.setMsg_type(js.optInt(MSG_TYPE, 1));
            bean.setRc(js.optInt(RC, 0));
        }catch (Exception e){

        }
        MyLog.e(bean.toString());
        return bean;
    }




    private String msg;
    private String tip;
    private int period;
    private int msg_type;
    private int rc;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }
}
