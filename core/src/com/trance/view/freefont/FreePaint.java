package com.trance.view.freefont;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.badlogic.gdx.graphics.Color;

public class FreePaint {
    private int textSize = 30;
    private Color color;
    private boolean isFakeBoldText;
    private boolean isUnderlineText;
    private boolean isStrikeThruText;
    private Color strokeColor;
    private int strokeWidth;

    public String getName() {
        StringBuffer name = new StringBuffer();
        name.append(this.textSize);
        name.append("_");
        name.append(this.color.toIntBits());
        name.append("_");
        name.append(this.booleanToInt(this.isFakeBoldText));
        name.append("_");
        name.append(this.booleanToInt(this.isUnderlineText));
        if(this.strokeColor != null) {
            name.append("_");
            name.append(this.strokeColor.toIntBits());
            name.append("_");
            name.append(this.strokeWidth);
        }

        return name.toString();
    }

    private int booleanToInt(boolean b) {
        return b?0:1;
    }

    public FreePaint() {
        this.color = Color.WHITE;
        this.isFakeBoldText = false;
        this.isUnderlineText = false;
        this.isStrikeThruText = false;
        this.strokeColor = null;
        this.strokeWidth = 3;
    }

    public FreePaint(int textSize, Color color, Color stroke, int strokeWidth, boolean bold, boolean line, boolean thru) {
        this.color = Color.WHITE;
        this.isFakeBoldText = false;
        this.isUnderlineText = false;
        this.isStrikeThruText = false;
        this.strokeColor = null;
        this.strokeWidth = 3;
        this.textSize = textSize;
        this.color = color;
        this.strokeColor = stroke;
        this.strokeWidth = strokeWidth;
        this.isFakeBoldText = bold;
        this.isUnderlineText = line;
        this.isStrikeThruText = thru;
    }

    public FreePaint(int size) {
        this.color = Color.WHITE;
        this.isFakeBoldText = false;
        this.isUnderlineText = false;
        this.isStrikeThruText = false;
        this.strokeColor = null;
        this.strokeWidth = 3;
        this.textSize = size;
    }

    public FreePaint(Color color) {
        this.color = Color.WHITE;
        this.isFakeBoldText = false;
        this.isUnderlineText = false;
        this.isStrikeThruText = false;
        this.strokeColor = null;
        this.strokeWidth = 3;
        this.color = color;
    }

    public FreePaint(int size, Color color) {
        this.color = Color.WHITE;
        this.isFakeBoldText = false;
        this.isUnderlineText = false;
        this.isStrikeThruText = false;
        this.strokeColor = null;
        this.strokeWidth = 3;
        this.textSize = size;
        this.color = color;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean getFakeBoldText() {
        return this.isFakeBoldText;
    }

    public void setFakeBoldText(boolean isFakeBoldText) {
        this.isFakeBoldText = isFakeBoldText;
    }

    public boolean getUnderlineText() {
        return this.isUnderlineText;
    }

    public void setUnderlineText(boolean isUnderlineText) {
        this.isUnderlineText = isUnderlineText;
    }

    public boolean getStrikeThruText() {
        return this.isStrikeThruText;
    }

    public void setStrikeThruText(boolean isStrikeThruText) {
        this.isStrikeThruText = isStrikeThruText;
    }

    public Color getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
