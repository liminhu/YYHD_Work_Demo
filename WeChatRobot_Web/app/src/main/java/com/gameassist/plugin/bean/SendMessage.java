package com.gameassist.plugin.bean;

/**
 * Created by hulimin on 2017/9/20.
 */

public class SendMessage {

    public SendMessage(String msg, int type, int sendState, int period) {
        this.msg = msg;
        this.type = type;
        this.sendState = sendState;
        this.period=period;
    }


    private int sendTime;
    private String msg;
    private int type;
    private int sendState;  //发送状态，0，开始发送，1发送中，2发送完成

    private int period;


    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSendState() {
        return sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }


    public int getSendTime() {
        return sendTime;
    }

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }
}
