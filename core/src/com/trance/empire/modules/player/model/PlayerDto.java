package com.trance.empire.modules.player.model;

import com.trance.common.basedb.Basedb;
import com.trance.common.basedb.BasedbService;
import com.trance.empire.modules.army.model.ArmyDto;
import com.trance.empire.modules.army.model.TechDto;
import com.trance.empire.modules.building.model.BuildingDto;
import com.trance.empire.modules.building.model.BuildingType;
import com.trance.empire.modules.building.model.WaitBuildingDto;
import com.trance.empire.modules.building.model.basedb.CityElement;
import com.trance.empire.modules.coolqueue.model.CoolQueueDto;
import com.trance.empire.modules.fitting.model.FittingDto;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * PlayerDto对象
 *
 * @author Along
 */
public class PlayerDto {


    /**
     * 玩家id
     */
    private long id;

    /**
     * 主公名称
     */
    private String playerName;

    /**
     * 等级
     */
    private int level = 1;

    /**
     * 金币
     */
    private long gold;

    /**
     * 银元
     */
    private long silver;

    /**
     * 粮草
     */
    private long foods;

    /**
     * UP
     */
    private long up;

    /**
     * 经验
     */
    private long experience;

    /**
     * 注册时间
     */
    public long registerTime;

    /**
     * 最后一次登录时间
     */
    public long loginTime;

    /**
     * 服标识
     */
    private int server;

    /**
     * myself
     */
    private boolean myself;

    private int[][] map;

    private final ConcurrentMap<Integer, ArmyDto> armys = new ConcurrentHashMap<Integer, ArmyDto>();

    private final ConcurrentMap<String, BuildingDto> buildings = new ConcurrentHashMap<String, BuildingDto>();
    private final ConcurrentMap<Integer, WaitBuildingDto> waitBuildings = new ConcurrentHashMap<Integer, WaitBuildingDto>();

    private final ConcurrentMap<Integer, CoolQueueDto> coolQueues = new ConcurrentHashMap<Integer, CoolQueueDto>();

    private final ConcurrentMap<Integer, TechDto> techs = new ConcurrentHashMap<Integer, TechDto>();

    private final ConcurrentMap<Integer, FittingDto> fittings = new ConcurrentHashMap<Integer, FittingDto>();
    private int x;

    private int y;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public boolean isMyself() {
        return myself;
    }

    public void setMyself(boolean myself) {
        this.myself = myself;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public ConcurrentMap<Integer, ArmyDto> getArmys() {
        return armys;
    }

    public void addAmry(ArmyDto dto) {
        armys.put(dto.getId(), dto);
    }

    public ConcurrentMap<String, BuildingDto> getBuildings() {
        return buildings;
    }

    public void addBuilding(BuildingDto dto) {
        buildings.put(getKey(dto.getX(), dto.getY()), dto);
    }

    public static String getKey(int x, int y) {
        return x + "_" + y;
    }

    public ConcurrentMap<Integer, WaitBuildingDto> getWaitBuildings() {
        return waitBuildings;
    }

    public void addWaitBuilding(WaitBuildingDto dto) {
        waitBuildings.put(dto.getId(), dto);
    }

    public ConcurrentMap<Integer, CoolQueueDto> getCoolQueues() {
        return coolQueues;
    }

    /**
     * 根据类型获得一个冷却队列
     *
     * @param type
     * @return
     */
    public CoolQueueDto getCoolQueueByType(int type) {
        for (CoolQueueDto cool : coolQueues.values()) {
            if (cool.getType() == type) {
                return cool;
            }
        }
        return null;
    }

    public void addCoolQueue(CoolQueueDto dto) {
        coolQueues.put(dto.getId(), dto);
    }

    public long getSilver() {
        return silver;
    }

    public void setSilver(long silver) {
        this.silver = silver;
    }

    public long getFoods() {
        return foods;
    }

    public void setFoods(long foods) {
        this.foods = foods;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ConcurrentMap<Integer, TechDto> getTechs() {
        return techs;
    }

    public void addTech(TechDto dto) {
        techs.put(dto.getId(), dto);
    }


    public ConcurrentMap<Integer, FittingDto> getFittings() {
        return fittings;
    }

    public void addFitting(FittingDto dto) {
        fittings.put(dto.getId(), dto);
    }

    public void refreshWaitBudiing(){
        int officeLvl = getOfficeLevel();
        waitBuildings.clear();
        Map<Integer, Integer> hasMap = getHasBuildingSize();
        Collection<CityElement> list = BasedbService.listAll(CityElement.class);
        for(CityElement element : list){
            if(element.getOpenLevel() <= officeLvl){
                Integer hasBuildNum = hasMap.get(element.getId());
                if(hasBuildNum == null){
                    continue;
                }
                int leftNum = element.getAmount() - hasBuildNum;
                if(leftNum> 0){
                    WaitBuildingDto wdto = new WaitBuildingDto();
                    wdto.setAmount(leftNum);
                    waitBuildings.put(element.getId(), wdto);
                }
            }
        }
    }

    private Map<Integer, Integer> getHasBuildingSize(){
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(BuildingDto dto : buildings.values()){
           Integer count = map.get(dto.getId());
            if(count == null){
                count = 0;
            }
            count += 1;
            map.put(dto.getId(), count);
        }
        return  map;
    }

    private int getOfficeLevel(){
        for(BuildingDto dto : buildings.values()){
            if(dto.getId() == BuildingType.OFFICE){
                return  dto.getLevel();
            }
        }
        return 0;
    }

    public int getHasBuildingSize(int id){
        int sum = 0;
        for(BuildingDto dto : buildings.values()){
            if(dto.getId() == id){
                sum++;
            }
        }
        return sum;
    }
}
