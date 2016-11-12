package com.trance.view.utils;

import var3d.net.freefont.FreeBitmapFont;
import var3d.net.freefont.FreeFont;

public class FontUtil {

	private static FreeBitmapFont font;
	public static final java.lang.String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	public static FreeBitmapFont getFont(){
		if(font == null){
			font = initFont();
		}
		return font;
	}

	private static FreeBitmapFont initFont(){
		FreeBitmapFont font = FreeFont.getBitmapFont();
		font.appendText(DEFAULT_CHARS);
		return font;
	}

	public static void dispose(){
		if(font != null){
			font.dispose();
		}
	}
}
