package com.trance.view.screens.astar;

import java.util.ArrayList;
import java.util.List;

/*原作者是http://www.codefans.net的JAROD
        之所以说这个A-star算法高效，是因为它的open-list和close-list使用的完全是静态数组，这样就极大地降低了入栈出栈的负担。这个代码非常值得推荐。

        用法很简单：
        Route_pt[] result = null;
        result_pt是一个简单类，它只有两个成员变量：int x和int y。
        参数说明：map是一个二维数组，具体见程序注释
        AStarRoute2 asr = new AStarRoute(map, start_x, start_y, end_x, end_y);
        try {
        result = asr.getResult();
        }catch (Exception ex) {
        }
        如果result != null那么寻路就成功了。
        注意：最后获得的路径，是从终点指向起点的。您可以把两组参数倒过来传，以获得正常方向的返回值。

        本人对JAROD的代码略作修改，主要是用循环取代了递归，避免栈溢出。
        这是本人修改后的八方向寻路算法。如下：*/

public class AStarRoute {
    private int[][] map;  // 地图矩阵，0表示能通过，1表示不能通过
    private int map_w;    // 地图宽度
    private int map_h;    // 地图高度
    private int start_x;  // 起点坐标X
    private int start_y;  // 起点坐标Y
    private int goal_x;   // 终点坐标X
    private int goal_y;   // 终点坐标Y

    private boolean closeList[][];            // 关闭列表
    public  int openList[][][];               // 打开列表
    private int openListLength;

    private static final int EXIST = 1;
    private static final int NOT_EXIST = 0;

    private static final int ISEXIST = 0;
    private static final int EXPENSE = 1;     // 自身的代价
    private static final int DISTANCE = 2;    // 距离的代价
    private static final int COST = 3;        // 消耗的总代价
    private static final int FATHER_DIR = 4;  // 父节点的方向

    public static final int DIR_NULL = 0;
    public static final int DIR_DOWN = 1;     // 方向：下
    public static final int DIR_UP = 2;       // 方向：上
    public static final int DIR_LEFT = 3;     // 方向：左
    public static final int DIR_RIGHT = 4;    // 方向：右
    public static final int DIR_UP_LEFT = 5;
    public static final int DIR_UP_RIGHT = 6;
    public static final int DIR_DOWN_LEFT = 7;
    public static final int DIR_DOWN_RIGHT = 8;

    private int astar_counter;                // 算法嵌套深度
    private boolean isFound;                  // 是否找到路径

    public AStarRoute(int[][] mx, int sx, int sy, int gx, int gy){
        start_x = sx;
        start_y = sy;
        goal_x  = gx;
        goal_y  = gy;
        map     = mx;
        map_w   = mx.length;
        map_h   = mx[0].length;
        astar_counter = 5000;
        initCloseList();
        initOpenList(goal_x, goal_y);
    }

    // 得到地图上这一点的消耗值
    private int getMapExpense(int x, int y, int dir)
    {
        if(dir < 5){
            return 10;
        }else{
            return 14;
        }
    }

    // 得到距离的消耗值
    private int getDistance(int x, int y, int ex, int ey)
    {
        return 10 * (Math.abs(x - ex) + Math.abs(y - ey));
    }

    // 得到给定坐标格子此时的总消耗值
    private int getCost(int x, int y)
    {
        return openList[x][y][COST];
    }

    // 开始寻路
    public void searchPath()
    {
        addOpenList(start_x, start_y);
        aStar(start_x, start_y);
    }

    // 寻路
    private void aStar(int x, int y)
    {
        // 控制算法深度
        for(int t = 0; t < astar_counter; t++){
            if(((x == goal_x) && (y == goal_y))){
                isFound = true;
                return;
            }
            else if((openListLength == 0)){
                isFound = false;
                return;
            }

            removeOpenList(x, y);
            addCloseList(x, y);

            // 该点周围能够行走的点
            addNewOpenList(x, y, x, y + 1, DIR_UP);
            addNewOpenList(x, y, x, y - 1, DIR_DOWN);
            addNewOpenList(x, y, x - 1, y, DIR_RIGHT);
            addNewOpenList(x, y, x + 1, y, DIR_LEFT);
            addNewOpenList(x, y, x + 1, y + 1, DIR_UP_LEFT);
            addNewOpenList(x, y, x - 1, y + 1, DIR_UP_RIGHT);
            addNewOpenList(x, y, x + 1, y - 1, DIR_DOWN_LEFT);
            addNewOpenList(x, y, x - 1, y - 1, DIR_DOWN_RIGHT);

            // 找到估值最小的点，进行下一轮算法
            int cost = 0x7fffffff;
            for(int i = 0; i < map_w; i++){
                for(int j = 0; j < map_h; j++){
                    if(openList[i][j][ISEXIST] == EXIST){
                        if(cost > getCost(i, j)){
                            cost = getCost(i, j);
                            x = i;
                            y = j;
                        }
                    }
                }
            }
        }
        // 算法超深
        isFound = false;
        return;
    }

    // 添加一个新的节点
    private void addNewOpenList(int x, int y, int newX, int newY, int dir)
    {
        if(isCanPass(newX, newY)){
            if(openList[newX][newY][ISEXIST] == EXIST){
                if(openList[x][y][EXPENSE] + getMapExpense(newX, newY, dir) <
                        openList[newX][newY][EXPENSE]){
                    setFatherDir(newX, newY, dir);
                    setCost(newX, newY, x, y, dir);
                }
            }else{
                addOpenList(newX, newY);
                setFatherDir(newX, newY, dir);
                setCost(newX, newY, x, y, dir);
            }
        }
    }

    // 设置消耗值
    private void setCost(int x, int y, int ex, int ey, int dir)
    {
        openList[x][y][EXPENSE] = openList[ex][ey][EXPENSE] + getMapExpense(x, y, dir);
        openList[x][y][DISTANCE] = getDistance(x, y, ex, ey);
        openList[x][y][COST] = openList[x][y][EXPENSE] + openList[x][y][DISTANCE];
    }

    // 设置父节点方向
    private void setFatherDir(int x, int y, int dir)
    {
        openList[x][y][FATHER_DIR] = dir;
    }

    // 判断一个点是否可以通过
    private boolean isCanPass(int x, int y)
    {
        // 超出边界
        if(x < 0 || x >= map_w || y < 0 || y >= map_h){
            return false;
        }
        // 地图不通
        if(map[x][y] != 0){
            return false;
        }
        // 在关闭列表中
        if(isInCloseList(x, y)){
            return false;
        }
        return true;
    }

    // 移除打开列表的一个元素
    private void removeOpenList(int x, int y)
    {
        if(openList[x][y][ISEXIST] == EXIST){
            openList[x][y][ISEXIST] = NOT_EXIST;
            openListLength--;
        }
    }

    // 判断一点是否在关闭列表中
    private boolean isInCloseList(int x, int y)
    {
        return closeList[x][y];
    }

    // 添加关闭列表
    private void addCloseList(int x, int y)
    {
        closeList[x][y] = true;
    }

    // 添加打开列表
    private void addOpenList(int x, int y)
    {
        if(openList[x][y][ISEXIST] == NOT_EXIST){
            openList[x][y][ISEXIST] = EXIST;
            openListLength++;
        }
    }

    // 初始化关闭列表
    private void initCloseList()
    {
        closeList = new boolean[map_w][map_h];
        for(int i = 0; i < map_w; i++){
            for(int j = 0; j < map_h; j++){
                closeList[i][j] = false;
            }
        }
    }

    // 初始化打开列表
    private void initOpenList(int ex, int ey)
    {
        openList  = new int[map_w][map_h][5];
        for(int i = 0; i < map_w; i++){
            for(int j = 0; j < map_h; j++){
                openList[i][j][ISEXIST] = NOT_EXIST;
                openList[i][j][EXPENSE] = getMapExpense(i, j, DIR_NULL);
                openList[i][j][DISTANCE] = getDistance(i, j, ex, ey);
                openList[i][j][COST] = openList[i][j][EXPENSE] + openList[i][j][DISTANCE];
                openList[i][j][FATHER_DIR] = DIR_NULL;
            }
        }
        openListLength = 0;
    }

    // 获得寻路结果
    public Route_pt[] getResult(){
        Route_pt[] result;
        List<Route_pt> route;
        searchPath();
        if(! isFound){
            return null;
        }
        route = new ArrayList<Route_pt>();
        // openList是从目标点向起始点倒推的。
        int iX = goal_x;
        int iY = goal_y;
        while((iX != start_x || iY != start_y)){
            route.add(new Route_pt(iX, iY));
            switch(openList[iX][iY][FATHER_DIR]){
                case DIR_DOWN:          iY++;            break;
                case DIR_UP:            iY--;            break;
                case DIR_LEFT:          iX--;            break;
                case DIR_RIGHT:         iX++;            break;
                case DIR_UP_LEFT:       iX--;   iY--;    break;
                case DIR_UP_RIGHT:      iX++;   iY--;    break;
                case DIR_DOWN_LEFT:     iX--;   iY++;    break;
                case DIR_DOWN_RIGHT:    iX++;   iY++;    break;
            }
        }
        int size = route.size();
        result = new Route_pt[size];
        for(int i = 0; i < size; i++){
            result[i] = new Route_pt((Route_pt)route.get(i));
        }
        return result;
    }
}

