package com.trance.android.util;

import android.os.Build;

import java.util.UUID;

public class GetDeviceId {

    //获得独一无二的Psuedo ID
    public String getUniquePsuedoID() {
        String serial;
        String sb = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        String uuid;
        try {
            //API>=9 使用serial号
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        uuid = new UUID(sb //13 位
                .hashCode(), serial.hashCode()).toString();
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }
}
