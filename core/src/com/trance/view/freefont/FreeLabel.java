package com.trance.view.freefont;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class FreeLabel extends Label {
    public FreeLabel(CharSequence text, Label.LabelStyle style) {
        super(append(text, style), style);
        setSize(getPrefWidth(), getPrefHeight());
    }

    private static CharSequence append(CharSequence text, Label.LabelStyle style) {
        ((FreeBitmapFont) style.font).appendText("" + text);
        return text;
    }

    public void setText(CharSequence newText) {
        super.setText(append(newText, getStyle()));
        setSize(getPrefWidth(), getPrefHeight());
    }

    /**
     * 设置字体缩放
     */
    public void setFontScale(float fontScale) {
        super.setFontScale(fontScale);
        setSize(getPrefWidth(), getPrefHeight());
    }
}
