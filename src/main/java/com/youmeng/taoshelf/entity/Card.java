package com.youmeng.taoshelf.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Card implements Serializable {
    //卡密
    @Id
    private String id;

    private Integer day;

    //添加时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    @CreatedDate
    private Date createTime;

    //使用时间
    private Date usedTime;

    //状态
    @Transient
    private String status;

    //用户
    @ManyToOne
    private User user;

    public Card() {
    }

    public Card(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", day=" + day +
                ", createTime=" + createTime +
                ", usedTime=" + usedTime +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }
}
