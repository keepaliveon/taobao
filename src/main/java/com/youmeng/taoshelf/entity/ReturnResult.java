package com.youmeng.taoshelf.entity;

public class ReturnResult<T> {

    private boolean result;

    private T t;

    public ReturnResult() {
    }

    public ReturnResult(T t) {
        this.t = t;
        this.result = false;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
