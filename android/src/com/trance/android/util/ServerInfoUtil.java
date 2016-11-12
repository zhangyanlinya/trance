package com.trance.android.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/12 0012.
 */

public class ServerInfoUtil {
    public final static  List<Server> serverList = new ArrayList<>();
    public static int versionCode;
    public static String versionMsg;

    public static  void init(String serverJson){
        serverList.clear();
        JSONObject jsonObject = JSON.parseObject(serverJson) ;
        if(jsonObject == null ){
            return;
        }

        List<Server> list = JSON.parseArray(jsonObject.getString("serverList"),Server.class);
        serverList.addAll(list);

        versionCode = jsonObject.getInteger("versionCode");
        versionMsg = jsonObject.getString("versionMsg");
    }

}
