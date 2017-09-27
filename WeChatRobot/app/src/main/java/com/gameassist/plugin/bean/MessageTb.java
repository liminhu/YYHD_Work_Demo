package com.gameassist.plugin.bean;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hulimin on 2017/9/5.
 */

public class MessageTb extends InstructionsBean{
   // private static  final  String INSTRUCT_TYPE="instruct_type";


    public static   String  _msgId="msgId";
    public static   String  _msgSvrId="msgSvrId";
    public static   String  _type="type";

    public static   String  _status="status";
    public static   String  _isSend="isSend";
    public static   String  _isShowTimer="isShowTimer";

    public static   String  _createTime="createTime";
    public static   String  _talker="talker";
    public static   String  _content="content";

    public static   String  _imgPath="imgPath";
    public static   String  _reserved="reserved";
    public static   String  _lvbuffer="lvbuffer";


    public static   String  _transContent="transContent";
    public static   String  _transBrandWording="transBrandWording";
    public static   String  _talkerId="talkerId";

    public static   String  _bizClientMsgId="bizClientMsgId";
    public static   String  _bizChatId="bizChatId";
    public static   String  _bizChatUserId="bizChatUserId";

    public static   String  _msgSeq="msgSeq";
    public static   String  _flag="flag";
/*
    private int instruct_type;

    public int getInstruct_type() {
        return instruct_type;
    }

    public void setInstruct_type(int instruct_type) {
        this.instruct_type = instruct_type;
    }*/

    public String genJSONData(){
        JSONObject js=new JSONObject();
        try{
            js.put(_msgId, msgId);
            js.put(_msgSvrId, msgSvrId);
            js.put(_type, type);
            js.put(_status, status);
            js.put(_isSend, isSend);
            js.put(_isShowTimer, isShowTimer);


            js.put(_createTime, createTime);
            js.put("格式化时间 ---- ", getFormatrerTime(createTime));

            js.put(_talker, talker);
            js.put(_content, content);


            js.put(_imgPath, imgPath);
            js.put(_reserved, reserved);
            js.put(_lvbuffer, lvbuffer);

            js.put(_transContent, transContent);
            js.put(_transBrandWording, transBrandWording);
            js.put(_talkerId, talkerId);


            js.put(_bizClientMsgId, bizClientMsgId);
            js.put(_bizChatId, bizChatId);
            js.put(_bizChatUserId, bizChatUserId);

            js.put(_msgSeq, msgSeq);
            js.put(_flag, flag);

          //  js.put(INSTRUCT_TYPE, getInstruct_type());

        }catch (Exception e){
            return  "";
        }
        return js.toString();
    }



    //所有属性
     private int   msgId;   //主键
    private  int msgSvrId;
    private  int type;
    private  int status;
    private  int isSend;
    private  int isShowTimer;
    private  long createTime;

    private  String talker;
    private  String content;
    private  String imgPath;
    private  String  reserved;

    //暂时改成byte[]
    private byte[] lvbuffer;

    private  String transContent;
    private  String  transBrandWording;
    private  int talkerId;
    private  String bizClientMsgId;
    private  int bizChatId;
    private  String bizChatUserId;

    private  int msgSeq;
    private  int flag;


    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getMsgSvrId() {
        return msgSvrId;
    }

    public void setMsgSvrId(int msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public int getIsShowTimer() {
        return isShowTimer;
    }

    public void setIsShowTimer(int isShowTimer) {
        this.isShowTimer = isShowTimer;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public byte[] getLvbuffer() {
        return lvbuffer;
    }

    public void setLvbuffer(byte[] lvbuffer) {
        this.lvbuffer = lvbuffer;
    }

    public String getTransContent() {
        return transContent;
    }

    public void setTransContent(String transContent) {
        this.transContent = transContent;
    }

    public String getTransBrandWording() {
        return transBrandWording;
    }

    public void setTransBrandWording(String transBrandWording) {
        this.transBrandWording = transBrandWording;
    }

    public int getTalkerId() {
        return talkerId;
    }

    public void setTalkerId(int talkerId) {
        this.talkerId = talkerId;
    }

    public String getBizClientMsgId() {
        return bizClientMsgId;
    }

    public void setBizClientMsgId(String bizClientMsgId) {
        this.bizClientMsgId = bizClientMsgId;
    }

    public int getBizChatId() {
        return bizChatId;
    }

    public void setBizChatId(int bizChatId) {
        this.bizChatId = bizChatId;
    }

    public String getBizChatUserId() {
        return bizChatUserId;
    }

    public void setBizChatUserId(String bizChatUserId) {
        this.bizChatUserId = bizChatUserId;
    }

    public int getMsgSeq() {
        return msgSeq;
    }

    public void setMsgSeq(int msgSeq) {
        this.msgSeq = msgSeq;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public MessageTb() {
    }

    public MessageTb(int msgSvrId, int type, int status, int isSend, int isShowTimer, int createTime, String talker, String content, String imgPath, String reserved, byte[] lvbuffer, String transContent, String transBrandWording, int talkerId, String bizClientMsgId, int bizChatId, String bizChatUserId, int msgSeq, int flag) {
        super();
        this.msgSvrId = msgSvrId;
        this.type = type;
        this.status = status;
        this.isSend = isSend;
        this.isShowTimer = isShowTimer;
        this.createTime = createTime;
        this.talker = talker;
        this.content = content;
        this.imgPath = imgPath;
        this.reserved = reserved;
        this.lvbuffer = lvbuffer;
        this.transContent = transContent;
        this.transBrandWording = transBrandWording;
        this.talkerId = talkerId;
        this.bizClientMsgId = bizClientMsgId;
        this.bizChatId = bizChatId;
        this.bizChatUserId = bizChatUserId;
        this.msgSeq = msgSeq;
        this.flag = flag;
    }



    public static String getFormatrerTime(long currenTime){
        Date date=new Date(currenTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月-dd日 HH:mm:ss:SSS");
        String dateString = formatter.format(date);
        return dateString;
    }

}
