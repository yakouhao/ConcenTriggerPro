package com.ct.ct.concentrigger.Model;

import java.util.Date;

/**
 * Created by holic on 2018/5/28.
 */

public class Event {

    User owner; //创建事件的人
    String id; //事件id
    int concentrateId; //监听事件
    int triggerId; //触发事件
    String key; //关键词
    Date time; //触发时间
    Location location; //规定位置
    int pullNum; //拉取数
    int likeNum; //点赞数

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getConcentrateId() {
        return concentrateId;
    }

    public void setConcentrateId(int concentrateId) {
        this.concentrateId = concentrateId;
    }

    public int getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(int triggerId) {
        this.triggerId = triggerId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getPullNum() {
        return pullNum;
    }

    public void setPullNum(int pullNum) {
        this.pullNum = pullNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
