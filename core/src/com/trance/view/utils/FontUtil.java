package com.trance.view.utils;


import com.trance.view.freefont.FreeBitmapFont;
import com.trance.view.freefont.FreeFont;

public class FontUtil {

	private static FreeBitmapFont font;
	public static final java.lang.String DEFAULT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"+
			"\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*\u007F\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F ¡¢£¤¥¦§¨©ª«¬\u00AD®¯°±²³´µ¶·¸¹º»¼½¾¿ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";

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

}
