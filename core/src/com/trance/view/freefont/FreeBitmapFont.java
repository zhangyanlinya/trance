package com.trance.view.freefont;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Set;



public class FreeBitmapFont extends BitmapFont {
    private FreeListener listener;
    private int pageWidth;
    private FreePaint paint;
    private Set<String> charSet;
    private PixmapPacker packer;
    private Texture.TextureFilter minFilter;
    private Texture.TextureFilter magFilter;
    private BitmapFontData data;
    private int size;

    public FreeBitmapFont(FreeListener listener) {
        this(listener, new FreePaint());
    }

    public FreeBitmapFont(FreeListener listener, FreePaint paint) {
        super(new BitmapFontData(), new TextureRegion(), false);
        this.pageWidth = 512;
        this.paint = new FreePaint();
        this.charSet = new HashSet();
        this.packer = null;
        this.minFilter = Texture.TextureFilter.Linear;
        this.magFilter = Texture.TextureFilter.Linear;
        this.updataSize(paint.getTextSize());
        this.listener = listener;
        this.paint = paint;
    }

    private void updataSize(int prefSize) {
        this.data = this.getData();
        this.size = Math.max(prefSize, this.paint.getTextSize());
        this.data.down = (float)(-this.size);
        this.data.ascent = (float)(-this.size);
        this.data.capHeight = (float)this.size;
    }

    public FreeBitmapFont setTextColor(Color color) {
        this.paint.setColor(color);
        return this;
    }

    public FreeBitmapFont setStrokeColor(Color color) {
        this.paint.setStrokeColor(color);
        return this;
    }

    public FreeBitmapFont setStrokeWidth(int width) {
        this.paint.setStrokeWidth(width);
        return this;
    }

    public FreeBitmapFont setSize(int size) {
        this.paint.setTextSize(size);
        return this;
    }

    public FreeBitmapFont setBold(boolean istrue) {
        this.paint.setFakeBoldText(istrue);
        return this;
    }

    public FreeBitmapFont setUnderline(boolean istrue) {
        this.paint.setUnderlineText(istrue);
        return this;
    }

    public FreeBitmapFont setStrikeThru(boolean istrue) {
        this.paint.setStrikeThruText(istrue);
        return this;
    }

    public FreeBitmapFont setPaint(FreePaint paint) {
        this.paint = paint;
        return this;
    }

    public FreeBitmapFont appendEmoji(String txt, String imgname) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
        this.appendEmoji(txt, (Pixmap)pixmap, 0, 0, this.paint.getTextSize(), this.paint.getTextSize());
        return this;
    }

    public FreeBitmapFont appendEmoji(String txt, String imgname, int offsetX, int offsetY) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
        this.appendEmoji(txt, pixmap, offsetX, offsetY, this.paint.getTextSize(), this.paint.getTextSize());
        return this;
    }

    public FreeBitmapFont appendEmoji(String txt, String imgname, int offsetX, int offsetY, int offsetW, int offsetH) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
        this.appendEmoji(txt, pixmap, offsetX, offsetY, offsetW, offsetH);
        return this;
    }

    public FreeBitmapFont appendEmoji(String txt, Pixmap pixmap, int offsetX, int offsetY, int offsetW, int offsetH) {
        if(!this.charSet.add(txt)) {
            return this;
        } else {
            if(this.packer == null) {
                this.packer = new PixmapPacker(this.pageWidth, this.pageWidth, Pixmap.Format.RGBA8888, 2, false);
            }

            Pixmap pixmap2 = new Pixmap(this.paint.getTextSize(), this.paint.getTextSize(), Pixmap.Format.RGBA8888);
            pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), offsetX, offsetY, offsetW, offsetH);
            pixmap.dispose();
            pixmap = null;
            char c = txt.charAt(0);
            this.putGlyph(c, pixmap2);
            this.updataSize(this.paint.getTextSize());
            this.upData();
            return this;
        }
    }

    public FreeBitmapFont createText(String characters) {
        if(characters != null && characters.length() != 0) {
            this.create(characters, true);
            this.end();
            return this;
        } else {
            return this;
        }
    }

    public FreeBitmapFont appendText(String characters) {
        if(characters != null && characters.length() != 0) {
            this.create(characters, false);
            return this;
        } else {
            return this;
        }
    }

    private void create(String characters, boolean haveMinPageSize) {
        characters = characters.replaceAll("[\\t\\n\\x0B\\f\\r]", "");
        Array cs = new Array();
        char[] pixmap;
        int c = (pixmap = characters.toCharArray()).length;

        for(int txt = 0; txt < c; ++txt) {
            char i = pixmap[txt];
            boolean isNewChar = this.charSet.add(String.valueOf(i));
            if(isNewChar) {
                cs.add(String.valueOf(i));
            }
        }

        if(haveMinPageSize) {
            this.pageWidth = (this.paint.getTextSize() + 2) * (int)(Math.sqrt((double)cs.size) + 1.0D);
        }

        if(this.packer == null) {
            this.packer = new PixmapPacker(this.pageWidth, this.pageWidth, Pixmap.Format.RGBA8888, 2, false);
        }

        for(int var9 = 0; var9 < cs.size; ++var9) {
            String var10 = (String)cs.get(var9);
            char var11 = var10.charAt(0);
            Pixmap var12 = this.listener.getFontPixmap(var10, this.paint);
            this.putGlyph(var11, var12);
        }

        this.updataSize(this.size);
        this.upData();
        if(this.getRegions().size == 1) {
            this.setOwnsTexture(true);
        } else {
            this.setOwnsTexture(false);
        }

    }

    private void putGlyph(char c, Pixmap pixmap) {
        Rectangle rect = this.packer.pack(String.valueOf(c), pixmap);
        pixmap.dispose();
        int pIndex = this.packer.getPageIndex(String.valueOf(c));
        Glyph glyph = new Glyph();
        glyph.id = c;
        glyph.page = pIndex;
        glyph.srcX = (int)rect.x;
        glyph.srcY = (int)rect.y;
        glyph.width = (int)rect.width;
        glyph.height = (int)rect.height;
        glyph.xadvance = glyph.width;
        this.data.setGlyph(c, glyph);
    }

    private void upData() {
        Glyph spaceGlyph = this.data.getGlyph(' ');
        if(spaceGlyph == null) {
            spaceGlyph = new Glyph();
            Glyph pages = this.data.getGlyph('l');
            if(pages == null) {
                pages = this.data.getFirstGlyph();
            }

            spaceGlyph.xadvance = pages.xadvance;
            spaceGlyph.id = 32;
            this.data.setGlyph(32, spaceGlyph);
        }

        this.data.spaceXadvance = spaceGlyph.xadvance + spaceGlyph.width;
        Array var13 = this.packer.getPages();
        Array regions = this.getRegions();
        int page = 0;

        int regSize;
        for(regSize = regions.size - 1; page < var13.size; ++page) {
            PixmapPacker.Page p = (PixmapPacker.Page)var13.get(page);
            if(page > regSize) {
                p.updateTexture(this.minFilter, this.magFilter, false);
                regions.add(new TextureRegion(p.getTexture()));
            } else if(p.updateTexture(this.minFilter, this.magFilter, false)) {
                regions.set(page, new TextureRegion(p.getTexture()));
            }
        }

        Glyph[][] var7 = this.data.glyphs;
        int var15 = this.data.glyphs.length;

        for(regSize = 0; regSize < var15; ++regSize) {
            Glyph[] var14 = var7[regSize];
            if(var14 != null) {
                Glyph[] var11 = var14;
                int var10 = var14.length;

                for(int var9 = 0; var9 < var10; ++var9) {
                    Glyph glyph = var11[var9];
                    if(glyph != null) {
                        TextureRegion region = (TextureRegion)this.getRegions().get(glyph.page);
                        if(region == null) {
                            throw new IllegalArgumentException("BitmapFont texture region array cannot contain null elements.");
                        }

                        this.data.setGlyphRegion(glyph, region);
                    }
                }
            }
        }

    }

    public FreeBitmapFont end() {
        this.paint = null;
        if(charSet != null) {
            this.charSet.clear();
            this.charSet = null;
        }
        if(packer != null) {//修复这个BUG
            this.packer.dispose();
            this.packer = null;
        }
        return this;
    }

    public void dispose() {
        this.end();
        super.dispose();
    }
}
