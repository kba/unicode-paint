package kba.unicodeart.format;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import kba.unicodeart.format.colored_char.TMColoredCharacter;
import kba.unicodeart.format.colored_char.TMColoredCharacterFactory;

import com.googlecode.lanterna.screen.ScreenCharacter;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TextColor;
import com.googlecode.lanterna.terminal.TextColor.RGB;

/**
 * Helper functions to map from {@link TMEditFormat} data types to Lanterna {@link Terminal} data types.
 *
 */
public class TMLanternaAdapter {
	
	public static Map<Integer,Color> ansiToRgbCache = new HashMap<>();
	
	/**
	 * Converts an {@link java.awt.Color} to a {@link TextColor}.
	 * @param awtColor the AWT color
	 * @return the {@link TextColor.RGB} version of this color
	 */
	public static TextColor toTextColor(Color awtColor) {
		return new TextColor.RGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
	}

	/**
	 * Converts a {@link TextColor} to an AWT color.
	 * @param textColor
	 * @param palette 
	 * @return the {@link java.awt.Color} for this {@link TextColor}
	 * @todo This should use the {@link TMColorPalette}
	 */
	public static Color toAwtColor(TextColor textColor, TMColorPalette palette) {
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
			return palette.getColorByIndex(idx);
//			if (ansiToRgbCache.containsKey(idx)) {
//				retCol = ansiToRgbCache.get(idx);
//			} else {
//				retCol = XTerm8bitIndexedColorUtils.getAWTColor(idx);
//				ansiToRgbCache.put(idx, retCol);
//			}
		}
		return retCol;
	}

	/**
	 * Converts a Lanterna {@link ScreenCharacter} to a {@link TMColoredCharacter}.
	 * @param scr the input {@link ScreenCharacter}
	 * @param palette the {@link TMColorPalette} to use
	 * @return the {@link TMColoredCharacter} for this {@link ScreenCharacter}
	 */
	public static TMColoredCharacter toTMColoredCharacter(ScreenCharacter scr, TMColorPalette palette) {
		TMColoredCharacterFactory factory = new TMColoredCharacterFactory(palette);
		TMColoredCharacter tmColoredCharacter = factory.create(scr.getCharacter());
		tmColoredCharacter.setBg(toAwtColor(scr.getBackgroundColor(), palette));
		tmColoredCharacter.setFg(toAwtColor(scr.getForegroundColor(), palette));
		return tmColoredCharacter;
	}
}
