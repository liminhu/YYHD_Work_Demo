package com.gameassist.plugin.common;

import com.gameassist.plugin.hongbao.WalletLuckyMoneyDetail;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by hulimin on 2017/9/4.
 */

public class CommonData {


    public static final String CLZ_SQLITE_DATABASE = "com.tencent.wcdb.database.SQLiteDatabase";
    public static final String METHOD_ACTIVDB = "getActiveDatabases";

    public static final String SECOND_MAIN_ACTIVITY = "com.tencent.mm.ui.chatting.En_5b8fbb1e";  //兼容另一个聊天界面
    public static final String FIRST_MAIN_ACTIVITY = "com.tencent.mm.ui.LauncherUI";


    public static Map initLuckyMoneyDectilMap() {
        Map field_method_map = new HashMap();
        field_method_map.put(WalletLuckyMoneyDetail.CURRENT_NEED_ACTIVITY_NAME, "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI");
        field_method_map.put(WalletLuckyMoneyDetail.LIST_GETVIEW_BASEADAPTER, "npP");
        field_method_map.put(WalletLuckyMoneyDetail.NICK_NAME, "nlw");
        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_TIME, "nlk");
        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_MONEY_AMOUNT, "nlj");
        field_method_map.put(WalletLuckyMoneyDetail.RECEIVE_USERNAME, "userName");
        field_method_map.put(WalletLuckyMoneyDetail.IS_FINISHED_DESC, "nlz");
        field_method_map.put(WalletLuckyMoneyDetail.KEY_SENDID, "key_sendid");
        return field_method_map;
    }


}
