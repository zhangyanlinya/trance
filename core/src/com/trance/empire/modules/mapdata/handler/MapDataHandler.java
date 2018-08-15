package com.trance.empire.modules.mapdata.handler;

import com.alibaba.fastjson.JSON;
import com.trance.common.socket.SimpleSocketClient;
import com.trance.common.socket.handler.HandlerSupport;
import com.trance.common.socket.handler.ResponseProcessorAdapter;
import com.trance.common.socket.model.Response;
import com.trance.common.socket.model.ResponseStatus;
import com.trance.empire.config.Module;
import com.trance.empire.model.Result;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.player.model.Player;
import com.trance.view.utils.MsgUtil;

import org.apache.mina.core.session.IoSession;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * 地图模块
 *
 * @author zyl
 */
public class MapDataHandler extends HandlerSupport {

    public MapDataHandler(SimpleSocketClient socketClient) {
        super(socketClient);
    }

    @Override
    public void init() {
        this.registerProcessor(new ResponseProcessorAdapter() {

            @Override
            public int getModule() {
                return Module.MAP_DATA;
            }

            @Override
            public int getCmd() {
                return MapDataCmd.SAVE_PLAYER_MAP_DATA;
            }

            @Override
            public Object getType() {
                return HashMap.class;
            }

            @Override
            public void callback(IoSession session, Response response,
                                 Object message) {
                ResponseStatus status = response.getStatus();
                if (status == ResponseStatus.SUCCESS) {
                    HashMap<?, ?> result = (HashMap<?, ?>) response.getValue();
                    int code = (Integer) result.get("result");
                    if (code != Result.SUCCESS) {
                        MsgUtil.getInstance().showMsg(Module.MAP_DATA, code);
                    }
                    String changePos = (String) result.get("content");
                    if (changePos != null && !changePos.equals("")) {// 交换
                        String[] ary = changePos.split("#");
                        String from = ary[0];
                        String to = ary[1];

                        String[] fpos = from.split("_");
                        int fx = Integer.parseInt(fpos[0]);
                        int fy = Integer.parseInt(fpos[1]);

                        String[] tpos = to.split("_");
                        int tx = Integer.parseInt(tpos[0]);
                        int ty = Integer.parseInt(tpos[1]);


                        ConcurrentMap<String, BuildingDto> buildings  = Player.player.getBuildings();
                        BuildingDto fdto = buildings.remove(from);
                        BuildingDto tdto = buildings.remove(to);

                        if (fdto != null) {
                            fdto.setX(tx);
                            fdto.setY(ty);
                            String key = fdto.getKey();
                            buildings.put(key, fdto);
                        }

                        if (tdto != null) {
                            tdto.setX(fx);
                            tdto.setY(fy);
                            String key = tdto.getKey();
                            buildings.put(key, tdto);
                        }

                    } else { //新增
                        Object newVoObj = result.get("newVo");
                        if (newVoObj != null) {
                            BuildingDto ndto = JSON.parseObject(JSON.toJSON(newVoObj).toString(),BuildingDto.class);
                            if(ndto != null){
                                Player.player.getBuildings().put(ndto.getKey(), ndto);
                            }
                        }
                    }
                }
            }
        });
        this.registerProcessor(new ResponseProcessorAdapter() {

            @Override
            public int getModule() {
                return Module.MAP_DATA;
            }

            @Override
            public int getCmd() {
                return MapDataCmd.GET_TARGET_PLAYER_MAP_DATA;
            }

            @Override
            public Object getType() {
                return HashMap.class;
            }

            @Override
            public void callback(IoSession session, Response response,
                                 Object message) {

            }
        });

    }
}
