package com.trance.view.utils;

public class TimeUtil {
	
	private static long deltaTime;
	
	public static void init(long serverTime){
//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String strBeginDate = format.format(new Date(serverTime));
//		System.out.println("服务器当前时间："+strBeginDate);
		deltaTime = System.currentTimeMillis() - serverTime;
		if(deltaTime > 0){
			System.out.println("客户端比服务器时间快了" + deltaTime/1000 +"秒");
		}else{
			System.out.println("客户端比服务器时间慢了" + Math.abs(deltaTime/1000) +"秒");
		}
	}
	
	public static long getServerTime(){
		return System.currentTimeMillis() - deltaTime;
	}


	public static String betweenTime(long oldTime){
		long date = getServerTime() - oldTime;
		StringBuilder sb = new StringBuilder();
		long day = date / 86400000;
		long hour = (date / 3600000 - day * 24);
		long min = ((date / 60000) - day * 1440 - hour * 60);
		long s = (date / 1000 - day * 86400 - hour * 3600 - min * 60);
		sb.append(day).append(":");
		sb.append(hour).append(":");
		sb.append(min).append(":");
		sb.append(s);
		return sb.toString();
	}
}
