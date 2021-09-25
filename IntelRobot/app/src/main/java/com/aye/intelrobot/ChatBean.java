package com.aye.intelrobot;

public class ChatBean {
    public static final int SEND=1;   //发送消息
    public static final int RECEIVE=2;   //接收消息
    private int state;   //消息的状态（发送还是接收）
    private String message;   //消息的内容

    public ChatBean(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "state=" + state +
                ", message='" + message + '\'' +
                '}';
    }
}
