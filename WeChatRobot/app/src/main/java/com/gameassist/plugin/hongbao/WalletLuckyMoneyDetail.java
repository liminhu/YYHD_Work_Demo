package com.gameassist.plugin.hongbao;

import android.app.Activity;
import android.text.TextUtils;

import com.gameassist.plugin.common.CommonData;
import com.gameassist.plugin.utils.MyLog;
import com.gameassist.plugin.utils.ReflectionUtils;
import com.gameassist.plugin.utils.TestUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hulimin on 2017/9/13.
 */

public class WalletLuckyMoneyDetail {
    public static final String CURRENT_NEED_ACTIVITY_NAME ="current_activity_name"; //"com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static final String LIST_GETVIEW_BASEADAPTER="list_getview_baseadapter";
    public static  final  String   KEY_SENDID="key_sendid";  //1000039501201709146014839907103
    public static  final  String   RECEIVE_USERNAME="receive_userName";
    public static  final  String    RECEIVE_TIME="receive_time";
    public static  final  String    RECEIVE_MONEY_AMOUNT="receive_money_amount";
    public static final  String    NICK_NAME="nick_name";
    public static  final String     IS_FINISHED_DESC ="is_finished_desc";

    public static Map field_method_map;
    public static void initFieldMethodMap(){
        //查找方法  log 定位-- BaseAdapter
        if(field_method_map==null) {
            field_method_map= CommonData.initLuckyMoneyDectilMap();
        }
    }



    private String key_sendid;
    private String receive_userName;
    private String nick_name;
    private String is_finished_desc;
    private long receive_money_amount;
    private String receive_time;


    //"content":"<msg>\n<appmsg appid=\"\" sdkver=\"\">\n<des><![CDATA[我给你发了一个红包，赶紧去拆!]]><\/des>\n<url><![CDATA[https:\/\/wxapp.tenpay.com\/mmpayhb\/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201709156010055624213]]><\/url>\n<type><![CDATA[2001]]><\/type>\n<title><![CDATA[微信红包]]><\/title>\n<thumburl><![CDATA[http:\/\/wx.gtimg.com\/hongbao\/1701\/hb.png]]><\/thumburl>\n\n<wcpayinfo>\n<templateid><![CDATA[7a2a165d31da7fce6dd77e05c300028a]]><\/templateid>\n<url><![CDATA[https:\/\/wxapp.tenpay.com\/mmpayhb\/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201709156010055624213]]><\/url>\n<iconurl><![CDATA[http:\/\/wx.gtimg.com\/hongbao\/1701\/hb.png]]><\/iconurl>\n<receivertitle><![CDATA[恭喜发财，大吉大利]]><\/receivertitle>\n<sendertitle><![CDATA[恭喜发财，大吉大利]]><\/sendertitle>\n<scenetext><![CDATA[微信红包]]><\/scenetext>\n<senderdes><![CDATA[查看红包]]><\/senderdes>\n<receiverdes><![CDATA[领取红包]]><\/receiverdes>\n<nativeurl><![CDATA[wxpay:\/\/c2cbizmessagehandler\/hongbao\/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201709156010055624213&sendusername=wxid_kw5j68dk4wy022]]><\/nativeurl>\n<sceneid><![CDATA[1002]]><\/sceneid>\n<innertype><![CDATA[0]]><\/innertype>\n\n<paymsgid><![CDATA[1000039401201709156010055624213]]><\/paymsgid>\n\n\n\n<scenetext>微信红包<\/scenetext>\n\n\n\n\n\n<locallogoicon><![CDATA[c2c_hongbao_icon_cn]]><\/locallogoicon>\n\n\n<invalidtime><![CDATA[1505534024]]><\/invalidtime>\n\n<\/wcpayinfo>\n\n<\/appmsg>\n<fromusername><![CDATA[wxid_kw5j68dk4wy022]]><\/fromusername>\n<\/msg>","reserved":"~SEMI_XML~\u0000\u0019.msg.appmsg.wcpayinfo.url\u0000https:\/\/wxapp.tenpay.com\/mmpayhb\/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201709156010055624213\u0000\u001d.msg.appmsg.wcpayinfo.sceneid\u0000\u00041002\u0000\u0013.msg.appmsg.$sdkver\u0000\u0000\u0000 .msg.appmsg.wcpayinfo.templateid\u0000 7a2a165d31da7fce6dd77e05c300028a\u0000\u0011.msg.fromusername\u0000\u0013wxid_kw5j68dk4wy022\u0000\u000f.msg.appmsg.des\u0000\u000f我给你发了一个红包，赶紧去拆!\u0000\u001f.msg.appmsg.wcpayinfo.nativeurl\u0000wxpay:\/\/c2cbizmessagehandler\/hongbao\/receivehongbao?msgtype=1&channelid=1&sendid=1000039401201709156010055624213&sendusername=wxid_kw5j68dk4wy022\u0000\u001d.msg.appmsg.wcpayinfo.iconurl\u0000'http:\/\/wx.gtimg.com\/hongbao\/1701\/hb.png\u0000\u000f.msg.appmsg.url\u0000https:\/\/wxapp.tenpay.com\/mmpayhb\/wxhb_personalreceive?showwxpaytitle=1&msgtype=1&channelid=1&sendid=1000039401201709156010055624213\u0000!.msg.appmsg.wcpayinfo.sendertitle\u0000\t恭喜发财，大吉大利\u0000\u001f.msg.appmsg.wcpayinfo.senderdes\u0000\u0004查看红包\u0000#.msg.appmsg.wcpayinfo.receivertitle\u0000\t恭喜发财，大吉大利\u0000\u001f.msg.appmsg.wcpayinfo.scenetext\u0000\u0004微信红包\u0000\u0011.msg.appmsg.title\u0000\u0004微信红包\u0000\u001e.msg.appmsg.wcpayinfo.paymsgid\u0000\u001f1000039401201709156010055624213\u0000\u000b.msg.appmsg\u0000\u0002\n\n\u0000#.msg.appmsg.wcpayinfo.locallogoicon\u0000\u0013c2c_hongbao_icon_cn\u0000\u0014.msg.appmsg.thumburl\u0000'http:\/\/wx.gtimg.com\/hongbao\/1701\/hb.png\u0000\u0004.msg\u0000\u0001\n\u0000!.msg.appmsg.wcpayinfo.receiverdes\u0000\u0004领取红包\u0000\u0012.msg.appmsg.$appid\u0000\u0000\u0000\u0010.msg.appmsg.type\u0000\u00042001\u0000\u0015.msg.appmsg.wcpayinfo\u0000\u0002\n\n\u0000\u001f.msg.appmsg.wcpayinfo.innertype\u0000\u00010\u0000!.msg.appmsg.wcpayinfo.invalidtime\u0000\n1505534024\u0000 .msg.appmsg.wcpayinfo.scenetext1\u0000\u0004微信红包",

    public static  String getSendidFromContent(String content){
        String result=null;
        final String firstStr="[CDATA[wxpay:";
        final String findStr="&sendid=";
        final String endStr="&";
        if(content.contains(findStr)){
            int first_index=content.indexOf(firstStr);
            int index=content.indexOf(findStr, first_index);
            if(index>0){
                int begin=index+findStr.length();
                int end=content.indexOf(endStr, begin);
                if(end>0){
                    result=content.substring(begin, end);
                    return result;
                }
            }
        }
        return result;
    }

    public static  String getSendUserNameFromContent(String content){
        String result=null;
        final String firstStr="[CDATA[wxpay:";
        final String findStr="&sendusername=";
        final String endStr="&";
        if(content.contains(findStr)){
            int first_index=content.indexOf(firstStr);
            int index=content.indexOf(findStr, first_index);
            if(index>0){
                int begin=index+findStr.length();
                int end=content.indexOf(endStr, begin);
                if(end>0){
                    result=content.substring(begin, end);
                    return result;
                }
            }
        }
        return result;
    }


    public static boolean hongbao_is_finish=false;

    public static  List<WalletLuckyMoneyDetail> getCurrentHongBaoDetails(Activity currentActivity){
        List<WalletLuckyMoneyDetail> list=null;
        hongbao_is_finish=false;
        try{
            String acName=currentActivity.getClass().getName();
            String needActivityName=(String)field_method_map.get(CURRENT_NEED_ACTIVITY_NAME);
            if(needActivityName.equals(acName)){
                MyLog.e(LIST_GETVIEW_BASEADAPTER+"\t%s",(String)field_method_map.get(LIST_GETVIEW_BASEADAPTER));
                Object npP=ReflectionUtils.getValue(currentActivity, (String)field_method_map.get(LIST_GETVIEW_BASEADAPTER));
                if(npP!=null){
                    MyLog.e("getCount  ---- ");
                    Integer size= (Integer) ReflectionUtils.callMethod(npP,"getCount");
                    if(size!=null && size>0){
                        String key_sendId=currentActivity.getIntent().getStringExtra((String)field_method_map.get(KEY_SENDID));
                        if(TextUtils.isEmpty(key_sendId)){
                            return null;
                        }
                        MyLog.e("size --- "+size);
                        list=new ArrayList();
                        for(int i=0; i<size; i++){
                            WalletLuckyMoneyDetail walletLuckyMoneyDetail=new WalletLuckyMoneyDetail();
                            Object m=ReflectionUtils.callMethod(npP, "getItem", new Class[]{int.class}, new Object[]{i});
                            if(m!=null){
                                MyLog.e("*************************");
                              //  MyLog.e(m.toString());
                              //  ReflectionUtils.getAllFields(m);
                                walletLuckyMoneyDetail.setKey_sendid(key_sendId);
                                walletLuckyMoneyDetail.setReceive_userName((String)ReflectionUtils.getValue(m, (String)field_method_map.get(RECEIVE_USERNAME)));
                                walletLuckyMoneyDetail.setNick_name((String)ReflectionUtils.getValue(m, (String)field_method_map.get(NICK_NAME)));
                                walletLuckyMoneyDetail.setIs_finished_desc((String)ReflectionUtils.getValue(m, (String)field_method_map.get(IS_FINISHED_DESC)));
                                String createTime=(String) ReflectionUtils.getValue(m, (String)field_method_map.get(RECEIVE_TIME));
                                walletLuckyMoneyDetail.setReceive_time(createTime);
                                walletLuckyMoneyDetail.setReceive_money_amount((Long)ReflectionUtils.getValue(m, (String)field_method_map.get(RECEIVE_MONEY_AMOUNT)));
                                if(walletLuckyMoneyDetail.testGenJsonPrintStr().contains("手气最佳")){
                                    hongbao_is_finish=true;
                                }
                                MyLog.e("getCurrentHongBaoDetails "+walletLuckyMoneyDetail.testGenJsonPrintStr());
                                list.add(walletLuckyMoneyDetail);
                            }else {
                                // MyLog.e("m is null ... ");
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            MyLog.e(e.getMessage());
        }
        return list;
    }




    public String getKey_sendid() {
        return key_sendid;
    }

    public void setKey_sendid(String key_sendid) {
        this.key_sendid = key_sendid;
    }

    public String getReceive_userName() {
        return receive_userName;
    }

    public void setReceive_userName(String receive_userName) {
        this.receive_userName = receive_userName;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getIs_finished_desc() {
        return is_finished_desc;
    }

    public void setIs_finished_desc(String is_finished_desc) {
        this.is_finished_desc = is_finished_desc;
    }

    public long getReceive_money_amount() {
        return receive_money_amount;
    }

    public void setReceive_money_amount(long receive_money_amount) {
        this.receive_money_amount = receive_money_amount;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }




    public String testGenJsonPrintStr(){
        JSONObject js=new JSONObject();
        try{
            js.put(KEY_SENDID, key_sendid);
            js.put(RECEIVE_USERNAME, receive_userName);
            js.put(NICK_NAME, nick_name);
            js.put(RECEIVE_MONEY_AMOUNT, receive_money_amount);
            js.put(RECEIVE_TIME, receive_time);
            js.put(IS_FINISHED_DESC, is_finished_desc);
        }catch (Exception e){
           return "";
        }
        return js.toString();
    }

}
