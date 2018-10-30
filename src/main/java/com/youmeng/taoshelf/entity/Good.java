package com.youmeng.taoshelf.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

public class Good {

    @JSONField(name = "num_iid")
    private Long numIid;

    private String title;

    @Transient
    @JSONField(name = "list_time")
    private Date listTime;

    @Transient
    @JSONField(name = "delist_time")
    private Date delistTime;

    @JSONField(name = "approve_status")
    private String approveStatus;

    @Transient
    private Date modified;

    @Transient
    private Long num;

    public Good() {
    }

    public Good(Long numIid, String title, String approveStatus) {
        this.numIid = numIid;
        this.title = title;
        this.approveStatus = approveStatus;
    }

    public Long getNumIid() {
        return numIid;
    }

    public void setNumIid(Long numIid) {
        this.numIid = numIid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getListTime() {
        return listTime;
    }

    public void setListTime(Date listTime) {
        this.listTime = listTime;
    }

    public Date getDelistTime() {
        return delistTime;
    }

    public void setDelistTime(Date delistTime) {
        this.delistTime = delistTime;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Good{" +
                "numIid=" + numIid +
                ", title='" + title + '\'' +
                ", listTime=" + listTime +
                ", delistTime=" + delistTime +
                ", approveStatus='" + approveStatus + '\'' +
                ", modified=" + modified +
                ", num=" + num +
                '}';
    }
}
