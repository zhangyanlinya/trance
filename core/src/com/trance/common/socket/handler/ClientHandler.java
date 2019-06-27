package com.trance.common.socket.handler;

import com.badlogic.gdx.Gdx;
import com.trance.common.socket.ClientContext;
import com.trance.common.socket.converter.ObjectConverters;
import com.trance.common.socket.model.Request;
import com.trance.common.socket.model.Response;
import com.trance.common.util.GZIPUtil;
import com.trance.empire.config.Module;
import com.trance.empire.modules.player.handler.PlayerCmd;
import com.trance.view.utils.MsgUtil;
import com.trance.view.utils.SocketUtil;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * 客户端 {@link IoHandler}
 *
 * @author bingshan
 */
public class ClientHandler extends IoHandlerAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    /**
     * session建立时调用
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.error("-IoSession实例:" + session.toString());
        // 设置IoSession闲置时间，参数单位是秒
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 180);
    }

    /**
     * session闲置的时候调用
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        SocketUtil.sendAsync(Request.valueOf(Module.PLAYER, PlayerCmd.HEART_BEAT, null));
    }


    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        if (message == null) {
            return;
        }

        if (!(message instanceof Response)) {
            logger.error("未能识别的响应消息类型！");
            Gdx.app.error("socket"," 未能识别的响应消息类型！");
            MsgUtil.getInstance().showLog("a123！");
            return;
        }

        Response response = (Response) message;

        if (response.isCompressed()) {
            // 解压
            //System.out.println("解压前 " + response.getValueBytes().length);
            response.setValueBytes(GZIPUtil.uncompress(response.getValueBytes()));
            //System.out.println("解压后 " + response.getValueBytes().length);
        }

//		System.out.println("收到消息：module[" +response.getModule() + " ]  cmd [" +response.getCmd()+"]");
        ResponseProcessor processor = this.responseProcessors.getProcessor(response.getModule(), response.getCmd());
        if (processor != null && processor.getType() != null) {
            //对象转换
            Object obj = this.objectConverters.decode(response.getFormat(), response.getValueBytes(), processor.getType());
            response.setValue(obj);
        }

        Request ctx = this.requestContext.remove(response.getSn());
        if (ctx != null) {
            //同步返回
            ctx.setResponse(response);
            ctx.release();
        } else {//没有sn
            if (processor == null) {
                logger.error("没有对应的响应消息处理器[module:" + response.getModule() + ", cmd:" + response.getCmd() + "]！");
            } else {
                //响应回调
                processor.callback(session, response);
            }
        }
    }

    /**
     * 响应消息处理器集合
     */
    private ResponseProcessors responseProcessors;

    /**
     * 对象转换器集合
     */
    private ObjectConverters objectConverters;

    /**
     * 请求上下文 {sn: ClientContext}
     */
    private Map<Integer, Request> requestContext;

    /**
     * 取得响应消息处理器集合
     *
     * @return ResponseProcessors
     */
    public ResponseProcessors getResponseProcessors() {
        return responseProcessors;
    }

    /**
     * 设置响应消息处理器集合
     *
     * @param responseProcessors ResponseProcessors
     */
    public void setResponseProcessors(ResponseProcessors responseProcessors) {
        this.responseProcessors = responseProcessors;
    }

    /**
     * 取得对象转换器集合
     *
     * @return ObjectConverters
     */
    public ObjectConverters getObjectConverters() {
        return objectConverters;
    }

    /**
     * 设置对象转换器集合
     *
     * @param objectConverters ObjectConverters
     */
    public void setObjectConverters(ObjectConverters objectConverters) {
        this.objectConverters = objectConverters;
    }

    public Map<Integer, Request> getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(Map<Integer, Request> requestContext) {
        this.requestContext = requestContext;
    }

}
