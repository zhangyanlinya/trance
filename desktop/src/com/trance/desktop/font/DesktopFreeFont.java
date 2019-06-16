package com.trance.desktop.font;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.trance.view.freefont.FreeFont;
import com.trance.view.freefont.FreeListener;
import com.trance.view.freefont.FreePaint;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.AttributedString;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class DesktopFreeFont implements FreeListener {
    public static void Strat() {
        FreeFont.Start(new DesktopFreeFont());
    }

    public Pixmap getFontPixmap(String txt, FreePaint vpaint) {
//        Pixmap.setFilter(Pixmap.Filter.BiLinear);
        Font font = getFont(vpaint.getTextSize(), vpaint.getFakeBoldText()
                || vpaint.getStrokeColor() != null);
        FontMetrics fm = metrics.get(vpaint.getTextSize());
        int strWidth = fm.stringWidth(txt);
        int strHeight = fm.getAscent() + fm.getDescent();
        if (strWidth == 0) {
            strWidth = strHeight = vpaint.getTextSize();

        }
        BufferedImage bi = new BufferedImage(strWidth, strHeight,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = bi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        if (vpaint.getStrokeColor() != null) {

            GlyphVector v = font.createGlyphVector(fm.getFontRenderContext(),
                    txt);
            Shape shape = v.getOutline();
            g.setColor(getColor(vpaint.getColor()));
            g.translate(0, fm.getAscent());
            g.fill(shape);
            g.setStroke(new BasicStroke(vpaint.getStrokeWidth()));
            g.setColor(getColor(vpaint.getStrokeColor()));
            g.draw(shape);
        } else if (vpaint.getUnderlineText()) {

            AttributedString as = new AttributedString(txt);
            as.addAttribute(TextAttribute.FONT, font);
            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            g.setColor(getColor(vpaint.getColor()));
            g.drawString(as.getIterator(), 0, fm.getAscent());
        } else if (vpaint.getStrikeThruText()) {

            AttributedString as = new AttributedString(txt);
            as.addAttribute(TextAttribute.FONT, font);
            as.addAttribute(TextAttribute.STRIKETHROUGH,
                    TextAttribute.STRIKETHROUGH_ON);
            g.setColor(getColor(vpaint.getColor()));
            g.drawString(as.getIterator(), 0, fm.getAscent());
        } else {

            g.setColor(getColor(vpaint.getColor()));
            g.drawString(txt, 0, fm.getAscent());
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "png", buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pixmap pixmap = new Pixmap(buffer.toByteArray(), 0,
                buffer.toByteArray().length);
        return pixmap;
    }

    private HashMap<Integer, Font> fonts = new HashMap<Integer, Font>();
    private HashMap<Integer, FontMetrics> metrics = new HashMap<Integer, FontMetrics>();

    private Font getFont(int defaultFontSize, boolean isBolo) {
        Font font = fonts.get(defaultFontSize);
        if (font == null) {
            font = new Font("", isBolo ? Font.BOLD : Font.PLAIN,
                    defaultFontSize);
            fonts.put(defaultFontSize, font);
            BufferedImage bi = new BufferedImage(1, 1,
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = bi.createGraphics();
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            metrics.put(defaultFontSize, fm);
        }
        return font;
    }

    private java.awt.Color getColor(Color libColor) {
        return new java.awt.Color(libColor.r, libColor.g, libColor.b,
                libColor.a);
    }
}
