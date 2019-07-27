package com.trance.common.socket.handler;

import com.badlogic.gdx.Gdx;
import com.trance.common.socket.converter.JsonConverter;
import com.trance.common.socket.converter.ProtostuffConverter;
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

//        if (!(message instanceof Response)) {
//            logger.error("未能识别的响应消息类型！");
//            Gdx.app.error("socket"," 未能识别的响应消息类型！");
//            MsgUtil.getInstance().showLog("a123！");
//            return;
//        }
        Response response = (Response) message;

        if (response.getCompressed() == 1) {
            // 解压
            response.setValueBytes(GZIPUtil.uncompress(response.getValueBytes()));
        }

        short sn = response.getSn();
        if (response.getSn() > -1) { // sync
            Request ctx = this.requestContext.remove(sn);
            if (ctx != null) {
                // 同步返回
                ctx.setResponse(response);
                ctx.release();
            }
        } else { // async
            ResponseProcessor processor = this.responseProcessors.getProcessor(response.getModule(), response.getCmd());
            if (processor != null && processor.getType() != null) {
                // 对象转换
                Object obj = ProtostuffConverter.decode(response.getValueBytes(), processor.getType());
                response.setValue(obj);
                processor.callback(session, response);
            } else {
                logger.error("没有对应的响应消息处理器[module:{}, cmd:{}]！",
                        new Object[] { response.getModule(), response.getCmd() });
            }
        }
    }

    /**
     * 响应消息处理器集合
     */
    private ResponseProcessors responseProcessors;


    /**
     * 请求上下文 {sn: ClientContext}
     */
    private Map<Short, Request> requestContext;

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


    public Map<Short, Request> getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(Map<Short, Request> requestContext) {
        this.requestContext = requestContext;
    }

}
