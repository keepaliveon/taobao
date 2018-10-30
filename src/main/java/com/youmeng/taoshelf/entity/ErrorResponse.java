package com.youmeng.taoshelf.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class ErrorResponse {

    private int code;

    private String msg;

    @JSONField(name = "sub_code")
    private String subCode;

    @JSONField(name = "sub_msg")
    private String subMsg;

    @JSONField(name = "request_id")
    private String requestId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
