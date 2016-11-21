/*
package com.trance.view.freefont;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;


import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSMutableAttributedString;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSRange;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.NSAttributedStringAttribute;
import org.robovm.apple.uikit.NSUnderlineStyle;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UIGraphics;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UILabel;


public class IOSFreeFont implements FreeListener {

    public static void Strat() {
        FreeFont.Start(new IOSFreeFont());
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    private UIColor getColor(Color color) {
        return UIColor.fromRGBA(color.r, color.g, color.b, color.a);
    }

    public Pixmap getFontPixmap(String strings, FreePaint vpaint) {
        UIFont font = null;
        if (vpaint.getFakeBoldText() || vpaint.getStrokeColor() != null) {
            font = UIFont.getBoldSystemFont(vpaint.getTextSize());
        } else {
            font = UIFont.getSystemFont(vpaint.getTextSize());
        }
        NSString string = new NSString(strings);
        @SuppressWarnings("deprecation")
        CGSize dim = string.getSize(font);
        UILabel label = new UILabel(new CGRect(0, 0, dim.getWidth(),
                dim.getHeight()));
        UILabel label2 = null;// 描边层
        label.setText(strings);
        label.setBackgroundColor(UIColor.fromRGBA(1, 1, 1, 0));
        label.setTextColor(getColor(vpaint.getColor()));
        label.setFont(font);
        label.setOpaque(false);
        label.setAlpha(1);
        NSRange range = new NSRange(0, strings.length());
        NSMutableAttributedString mutableString = new NSMutableAttributedString(
                strings);
        mutableString.addAttribute(NSAttributedStringAttribute.ForegroundColor,
                getColor(vpaint.getColor()), range);
        if (vpaint.getStrokeColor() != null) {
            label2 = new UILabel(new CGRect(0, 0, dim.getWidth(),
                    dim.getHeight()));
            label2.setText(strings);
            label2.setBackgroundColor(UIColor.fromRGBA(1, 1, 1, 0));
            label2.setTextColor(getColor(vpaint.getColor()));
            label2.setFont(font);
            label2.setOpaque(false);
            label2.setAlpha(1);
            NSMutableAttributedString mutableString2 = new NSMutableAttributedString(
                    strings);
            mutableString2.addAttribute(
                    NSAttributedStringAttribute.StrokeColor,
                    getColor(vpaint.getStrokeColor()), range);
            mutableString2.addAttribute(
                    NSAttributedStringAttribute.StrokeWidth,
                    NSNumber.valueOf(vpaint.getStrokeWidth()), range);
            label2.setAttributedText(mutableString2);
        } else if (vpaint.getUnderlineText() == true) {
            mutableString.addAttribute(
                    NSAttributedStringAttribute.UnderlineStyle,
                    NSNumber.valueOf(NSUnderlineStyle.StyleSingle.value()),
                    range);
        } else if (vpaint.getStrikeThruText() == true) {
            mutableString.addAttribute(
                    NSAttributedStringAttribute.StrikethroughStyle,
                    NSNumber.valueOf(NSUnderlineStyle.StyleSingle.value()
                            | NSUnderlineStyle.PatternSolid.value()), range);
        }
        label.setAttributedText(mutableString);
        UIGraphics.beginImageContext(dim, false, 1);
        label.getLayer().render(UIGraphics.getCurrentContext());
        if (vpaint.getStrokeColor() != null) {
            label2.getLayer().render(UIGraphics.getCurrentContext());
        }
        UIImage image = UIGraphics.getImageFromCurrentImageContext();
        UIGraphics.endImageContext();
        NSData nsData = image.toPNGData();
        Pixmap pixmap = new Pixmap(nsData.getBytes(), 0,
                nsData.getBytes().length);
        return pixmap;
    }

}
*/
