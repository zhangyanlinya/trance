package com.trance.android.util;

import android.os.Build;

import java.util.UUID;

public class GetDeviceId {

    //获得独一无二的Psuedo ID
    public String getUniquePsuedoID() {
        String serial;
        StringBuilder sb = new StringBuilder();
        sb.append("35");
        sb.append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10);

        sb.append(Build.CPU_ABI.length() % 10).append(Build.DEVICE.length() % 10);

        sb.append(Build.DISPLAY.length() % 10).append(Build.HOST.length() % 10);

        sb.append(Build.ID.length() % 10).append(Build.MANUFACTURER.length() % 10);

        sb.append(Build.MODEL.length() % 10).append(Build.PRODUCT.length() % 10);

        sb.append(Build.TAGS.length() % 10).append(Build.TYPE.length() % 10);

        sb.append(Build.USER.length() % 10); //13 位

        String uuid;
        try {
            //API>=9 使用serial号
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        uuid = new UUID(sb.toString().hashCode(), serial.hashCode()).toString();
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }
}
