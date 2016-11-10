package com.trance.view.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashSet;
import java.util.Set;

public class FontUtil {
	
	private static BitmapFont font;
//	private static FreeTypeFontGener generator;
	private final static Set<String> set = new HashSet<String>();
	
	public static BitmapFont getSingleFont(){
		if(font == null){
			font = getFont();
		}
		return font;
	}
	
	/**
	 * get BitmapFont from config
	 * 
	 * @param size    font size
	 * @param append  追加String 
	 * @param color   font color
	 * @return
	 */
	public static BitmapFont getFont(int size, String append, Color color){
		set.clear();
		for(int i = 0; i < append.length(); i++){
			char c = append.charAt(i);
			if(CharUtil.isChinese(c)){
				set.add(String.valueOf(c));
			}
		}
		BitmapFont font = new BitmapFont();
		font.setColor(color);
		return font;
	}
	
	public static BitmapFont getFont(){
		BitmapFont font = getFont(25);
		font.setColor(Color.WHITE);
		return font;
	}
	
	public BitmapFont getFont(int size, Color color){
		BitmapFont font = getFont(size);
		font.setColor(color);
		return font;
	}
	
	public static BitmapFont getFont(int size){
		BitmapFont font = new BitmapFont();
		return font;
	}
	
	public static void dispose(){
		if(font != null){
			font.dispose();
		}
	}
}
