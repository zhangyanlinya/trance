package com.trance.empire.modules.building.model;


/**
 * 主城建筑DTO
 * 
 * @author zyl
 *
 */
public class BuildingDto{

        private int x;

        private int y;

        /**
         * 建筑id
         */
        private int id;

        /**
         * 建筑等级
         */
        private int level = 1;

        /**
         * cd
         */
        private long cdtime;

        /**
         * 结束
         */
        private long etime;


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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public long getCdtime() {
            return cdtime;
        }

        public void setCdtime(long cdtime) {
            this.cdtime = cdtime;
        }

        public long getEtime() {
            return etime;
        }

        public void setEtime(long etime) {
            this.etime = etime;
        }

        public String getKey(){
            return x + "_" + y;
        }
}
