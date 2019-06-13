package com.trance.android.font;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.trance.view.freefont.FreeFont;
import com.trance.view.freefont.FreeListener;
import com.trance.view.freefont.FreePaint;

import java.io.ByteArrayOutputStream;


/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class AndroidFreeFont implements FreeListener {
    public static void Strat() {
        FreeFont.Start(new AndroidFreeFont());
    }

    private Paint paint = null;

    private int getColor(Color color) {
        return ((int) (255 * color.a) << 24) | ((int) (255 * color.r) << 16)
                | ((int) (255 * color.g) << 8) | ((int) (255 * color.b));
    }

    public Pixmap getFontPixmap(String txt, FreePaint vpaint) {
//        Pixmap.setFilter(Pixmap.Filter.BiLinear);
        if (paint == null) {
            paint = new Paint();
            paint.setAntiAlias(true);
        }
        paint.setTextSize(vpaint.getTextSize());
        Paint.FontMetrics fm = paint.getFontMetrics();
        int w = (int) paint.measureText(txt);
        int h = (int) (fm.descent - fm.ascent);
        if (w == 0) {
            w = h = vpaint.getTextSize();
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 如果是描边类型
        if (vpaint.getStrokeColor() != null) {
            // 绘制外层
            paint.setColor(getColor(vpaint.getStrokeColor()));
            paint.setStrokeWidth(vpaint.getStrokeWidth()); // 描边宽度
            paint.setStyle(Paint.Style.FILL_AND_STROKE); // 描边种类
            paint.setFakeBoldText(true); // 外层text采用粗体
            canvas.drawText(txt, 0, -fm.ascent, paint);
            paint.setFakeBoldText(false);
        } else {
            paint.setUnderlineText(vpaint.getUnderlineText());
            paint.setStrikeThruText(vpaint.getStrikeThruText());
            paint.setFakeBoldText(vpaint.getFakeBoldText());
        }
        // 绘制内层
        paint.setStrokeWidth(0);
        paint.setColor(getColor(vpaint.getColor()));
        canvas.drawText(txt, 0, -fm.ascent, paint);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, buffer);
        byte[] encodedData = buffer.toByteArray();
        Pixmap pixmap = new Pixmap(encodedData, 0, encodedData.length);
        bitmap = null;
        canvas = null;
        return pixmap;
    }
}