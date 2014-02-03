package kba.unicodeart.format;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.lanterna.terminal.TextColor;
import com.googlecode.lanterna.terminal.TextColor.RGB;
import com.googlecode.lanterna.terminal.XTerm8bitIndexedColorUtils;

public class LanternaAdapter {
	
	public static Map<Integer,Color> ansiToRgbCache = new HashMap<>();
	
	public static TextColor toTextColor(Color awtColor) {
		return new TextColor.RGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
	}

	public static Color toAwtColor(TextColor textColor) {
		Color retCol;
		if (textColor instanceof TextColor.RGB) {
			RGB textColorRgb = TextColor.RGB.class.cast(textColor);
			retCol = new Color(textColorRgb.getRed(), textColorRgb.getGreen(), textColorRgb.getBlue());
		} else {
			int idx = 0;
			if (textColor instanceof TextColor.ANSI) {
				idx = TextColor.ANSI.class.cast(textColor).toOldColor().ordinal();
			} else if (textColor instanceof TextColor.Indexed) {
				idx = TextColor.Indexed.class.cast(textColor).getColorIndex();
			}
			if (ansiToRgbCache.containsKey(idx)) {
				retCol = ansiToRgbCache.get(idx);
			} else {
				retCol = XTerm8bitIndexedColorUtils.getAWTColor(idx);
				ansiToRgbCache.put(idx, retCol);
			}
		}
		return retCol;
	}

}
