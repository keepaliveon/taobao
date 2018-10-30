package com.youmeng.taoshelf.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class User implements Serializable {

    @Id
    private String nick;

    private String sessionKey1;

    private String sessionKey2;

    //删除标志
    private Boolean deleted;

    //加入时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    @CreatedDate
    private Date joinTime;

    //服务使用到期时间
    private Date endTime;

    //App1使用到期时间
    private Date endDate1;

    //App2使用到期时间
    private Date endDate2;

    @OneToMany
    private List<Task> taskList = new ArrayList<>();

    @Transient
    private List<Good> goodList = new ArrayList<>();

    public User() {
    }

    public User(String nick) {
        this.deleted = false;
        this.nick = nick;
        this.endTime = new Date();
    }

    public String getRole() {
        if (endTime.after(new Date())) {
            return "ROLE_MEMBER";
        } else {
            return "ROLE_VISITOR";
        }
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public List<Good> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<Good> goodList) {
        this.goodList = goodList;
    }

    public String getSessionKey1() {
        return sessionKey1;
    }

    public void setSessionKey1(String sessionKey1) {
        this.sessionKey1 = sessionKey1;
    }

    public String getSessionKey2() {
        return sessionKey2;
    }

    public void setSessionKey2(String sessionKey2) {
        this.sessionKey2 = sessionKey2;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Date getEndDate1() {
        return endDate1;
    }

    public void setEndDate1(Date endDate1) {
        this.endDate1 = endDate1;
    }

    public Date getEndDate2() {
        return endDate2;
    }

    public void setEndDate2(Date endDate2) {
        this.endDate2 = endDate2;
    }
}
