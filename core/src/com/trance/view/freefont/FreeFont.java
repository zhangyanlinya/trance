package com.trance.view.freefont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashMap;



/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class FreeFont {

    static FreeListener listener;
    static HashMap<String, FreeBitmapFont> fonts = new HashMap<String, FreeBitmapFont>();

    public static void Start(FreeListener freeListener) {
        listener = freeListener;
    }

    public static Pixmap getPixmap(String txt) {
        return listener.getFontPixmap(txt, new FreePaint());
    }

    public static Pixmap getPixmap(String txt, FreePaint paint) {
        return listener.getFontPixmap(txt, paint);
    }

    public static FreeBitmapFont getBitmapFont() {
        return getBitmapFont("var3dfont");
    }

    public static FreeBitmapFont getBitmapFont(String fontName) {
        FreeBitmapFont font = fonts.get(fontName);
        if (font == null) {
            font = new FreeBitmapFont(listener);
            fonts.put(fontName, font);
            return font;
        } else
            return font;
    }

    public static FreeLabel getLabel(String text) {
        return new FreeLabel(text, new Label.LabelStyle(getBitmapFont(), Color.WHITE));
    }

    public static FreeLabel getLabel(Color color, String text) {
        return new FreeLabel(text, new Label.LabelStyle(getBitmapFont(), color));
    }

    public static FreeLabel getLabel(String fontName, String text) {
        return new FreeLabel(text, new Label.LabelStyle(getBitmapFont(fontName),
                Color.WHITE));
    }

    public static FreeLabel getLabel(String fontName, Color color, String text) {
        return new FreeLabel(text, new Label.LabelStyle(getBitmapFont(fontName),
                color));
    }
}
