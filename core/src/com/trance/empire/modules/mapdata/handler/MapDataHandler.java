package com.trance.empire.modules.mapdata.handler;

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
            public byte getModule() {
                return Module.MAP_DATA;
            }

            @Override
            public byte getCmd() {
                return MapDataCmd.SAVE_PLAYER_MAP_DATA;
            }

            @Override
            public Class<?>  getType() {
                return Result.class;
            }

            @Override
            public void callback(IoSession session, Response response) {
                ResponseStatus status = response.getStatus();
                if (status == ResponseStatus.SUCCESS) {
                    Result<String> result = (Result<String>) response.getValue();
                    int code = result.getCode();
                    if (code != Result.SUCCESS) {
                        MsgUtil.getInstance().showMsg(Module.MAP_DATA, code);
                    }
                    String changePos = result.getContent();
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

                    } else { //新增 // not TODO
//                        Object newVoObj = result.get("newVo");
//                        if (newVoObj != null) {
//                            BuildingDto ndto = JSON.parseObject(JSON.toJSON(newVoObj).toString(),BuildingDto.class);
//                            if(ndto != null){
//                                Player.player.getBuildings().put(ndto.getKey(), ndto);
//                            }
//                        }
                    }
                }
            }
        });
//        this.registerProcessor(new ResponseProcessorAdapter() {
//
//            @Override
//            public byte getModule() {
//                return Module.MAP_DATA;
//            }
//
//            @Override
//            public byte getCmd() {
//                return MapDataCmd.GET_TARGET_PLAYER_MAP_DATA;
//            }
//
//            @Override
//            public Class<?>  getType() {
//                return Result.class;
//            }
//
//            @Override
//            public void callback(IoSession session, Response response) {
//
//            }
//        });

    }
}
