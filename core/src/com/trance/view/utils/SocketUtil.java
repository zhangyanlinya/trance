package com.trance.view.utils;


import com.alibaba.fastjson.JSON;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.common.util.ProtostuffUtil;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.empire.modules.player.model.Player;
import com.trance.empire.modules.player.model.ReqReconnect;
import com.trance.view.net.ClientService;
import com.trance.view.net.ClientServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SocketUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(SocketUtil.class);

    private static ClientService clientService;

    public static boolean heartbeat;

    public static void init() {
        clientService = new ClientServiceImpl();
        clientService.init();
    }

    /**
     * 同步发送请求
     *
     * @param request
     * @return
     */
    public static Response send(Request request) {
        return send(request, false);
    }

    public static Response send(Request request, boolean showDialog) {
        return send(request, showDialog, true);
    }

    /**
     * 同步发送请求
     *
     * @param request
     * @param showDialog 是否显示悬浮进度
     * @return
     */

    public static Response send(Request request, boolean showDialog, boolean showMsg) {
        Response response = clientService.send(request, showDialog);
        if (response == null) {
            if (showMsg) {
                MsgUtil.getInstance().showMsg("Connection to server failed");
            }
            return null;
        }

        if (response.getStatus() == 3) {
            if (!heartbeat) {//心跳死了。
                MsgUtil.getInstance().showMsg("please login again");
                return null;
            }
        }

        return response;
    }

    /**
     * 异步发送请求
     *
     * @param request Request
     */
    public static void sendAsync(Request request) {
        clientService.sendAsync(request);
    }

    public static void destroy() {
        clientService.destroy();
    }

    public static boolean offlineReconnect() {
//		String src = MainActivity.userName + MainActivity.loginKey;
//		String LoginMD5 = null;
//		try {
//			LoginMD5 = CryptUtil.md5(src);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

        if (!heartbeat) {//心跳死了。
            return true;
        }

        //断线重连
        ReqReconnect req = new ReqReconnect();
        req.setUserName(Player.userName);
        req.setServer(1);
//		params.put("loginKey", LoginMD5); //TODO 暂时不校验
        Response response = send(Request.valueOf(Module.PLAYER, PlayerCmd.OFFLINE_RECONNECT, req), false, false);
        if (response == null || response.getStatus() != 0) {
            return false;
        }
        byte[] bytes = response.getValueBytes();
        Result<?> result = ProtostuffUtil.parseObject(bytes, Result.class);
        if (result != null) {
            if (result.getCode() != Result.SUCCESS) {
                if (result.getCode() == -10005) {//-10005 重连被禁止
                    heartbeat = false;
                    return true;
                }
                MsgUtil.getInstance().showMsg(Module.PLAYER, result.getCode());
                logger.error("断线重连失败 code =" + result.getCode());
                return false;
            }
        }
        logger.error("断线重连成功");
        MsgUtil.getInstance().showMsg("Reconnect the server successfully");
        return true;

    }
}
