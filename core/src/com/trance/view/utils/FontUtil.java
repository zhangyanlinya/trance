package com.trance.view.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import var3d.net.freefont.FreeFont;

public class FontUtil {
	
	private static BitmapFont font;

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

		BitmapFont font = FreeFont.getBitmapFont();
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
		BitmapFont font = FreeFont.getBitmapFont();
		return font;
	}
	
	public static void dispose(){
		if(font != null){
			font.dispose();
		}
	}
}
