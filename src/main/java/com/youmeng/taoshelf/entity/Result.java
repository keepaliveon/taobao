package com.youmeng.taoshelf.entity;

import java.util.List;

public class Result<T> {

    private Long total;

    private List<T> items;

    public Result() {
        total = 0L;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
