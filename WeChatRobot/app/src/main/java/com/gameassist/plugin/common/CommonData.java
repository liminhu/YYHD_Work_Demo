package com.gameassist.plugin.common;

import com.gameassist.plugin.hongbao.WalletLuckyMoneyDetail;
import com.gameassist.plugin.mm.robot.BuildConfig;
import com.gameassist.plugin.utils.MyLog;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hulimin on 2017/9/4.
 */

public class CommonData {
    public static  final  int[]  SUPPORTED_VERSION_NUMBER={1100, 1120};  //1120
    public static  final  String[]  SUPPORTED_VERSION_NAME={"6.5.13","6.15.16"}; //6.15.16


    public static String default_clz_sqlite_database = "com.tencent.wcdb.database.SQLiteDatabase";
    public static String default_method_activdb = "getActiveDatabases";
    public static String default_second_main_activity = "com.tencent.mm.ui.chatting.En_5b8fbb1e";  //兼容另一个聊天界面
    public static String default_first_main_activity = "com.tencent.mm.ui.LauncherUI";
    public static String default_current_need_activity_name = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";


    public static String default_receive_username = "userName";
    public static String default_key_sendid = "key_sendid";

    // TODO: 2017/10/11 不同版本，需要根据实际情况更新下面的数据
    public static String default_homeUI_upx = "uPX";  //LauncherUI -- >HomeUI -->  Chat_User
    public static String default_homeUI_upb = "uPb";


    public static String default_list_getview_baseadapter = "npP";   //listView
    public static String default_nick_name = "nlw";
    public static String default_receive_time = "nlk";
    public static String default_receive_money_amount = "nlj";
    public static String default_is_finished_desc = "nlz";



    public static void initDifferVersionCommonData(int versionCode){
        if(BuildConfig.DEBUG){
           // UrlManager.CURRENT_IP="redbull.ggdawanjia.com";   //线上的ip
        }
        MyLog.e("code:"+versionCode);
        if(versionCode==1120){
            //方法2：
            default_homeUI_upx="vnd";
            default_homeUI_upb="vjX";
            default_list_getview_baseadapter="nAK"; // com.tencent.mm.plugin.luckymoney.ui.i
            default_receive_time="nwf";
            default_nick_name = "nwr";
            default_is_finished_desc = "nwu";
            default_receive_money_amount = "nwe";
        }
    }





    public static Map initLuckyMoneyDectilMap() {
        Map field_method_map = new HashMap();
        field_method_map.put(WalletLuckyMoneyDetail.CURRENT_NEED_ACTIVITY_NAME, default_current_need_activity_name);
        field_method_map.put(WalletLuckyMoneyDetail.LIST_GETVIEW_BASEADAPTER, default_list_getview_baseadapter);
        field_method_map.put(WalletLuckyMoneyDetail.NICK_NAME, default_nick_name);
        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_TIME, default_receive_time);

        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_MONEY_AMOUNT, default_receive_money_amount);
        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_USERNAME, default_receive_username);
        field_method_map.put(WalletLuckyMoneyDetail.IS_FINISHED_DESC, default_is_finished_desc);
        field_method_map.put(WalletLuckyMoneyDetail.KEY_SENDID, default_key_sendid);
        return field_method_map;
    }


    public static boolean isSupportCurrentVersion(int verstion){
        MyLog.e("test supported_version_number ..."+verstion);
        for(int i=0; i<SUPPORTED_VERSION_NUMBER.length; i++){
            if(SUPPORTED_VERSION_NUMBER[i]==verstion){
                return true;
            }
        }
        return false;
    }


}
