package com.ct.ct.concentrigger.Model;

import java.util.Date;

/**
 * Created by holic on 2018/6/6.
 */

public class Message {

    String user_id;
    String concen_id;
    String trigger_id;
    String msg_content;
    String url;
    Date time;
    boolean isRead;
    boolean doSth;


    public String getConcen_id() {
        return concen_id;
    }

    public void setConcen_id(String concen_id) {
        this.concen_id = concen_id;
    }

    public String getTrigger_id() {
        return trigger_id;
    }

    public void setTrigger_id(String trigger_id) {
        this.trigger_id = trigger_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
