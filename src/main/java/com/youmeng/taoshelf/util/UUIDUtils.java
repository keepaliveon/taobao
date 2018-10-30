package com.youmeng.taoshelf.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UUIDUtils {

    public static String getUUID() {
        //UUID+时间戳
        return UUID.randomUUID().toString().replaceAll("-", "") + "-" + new Date().getTime();
    }

    public static String getSerialNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int a = (int) (Math.random() * 26) + 'A';
        return simpleDateFormat.format(new Date()) + (char) a;
    }
}
