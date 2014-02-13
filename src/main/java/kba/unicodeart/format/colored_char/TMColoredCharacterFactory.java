package kba.unicodeart.format.colored_char;

import java.awt.Color;

import kba.unicodeart.format.TMColorPalette;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create colored characters based on a {@link TMColorPalette}.
 *
 */
public class TMColoredCharacterFactory {
	private static final Logger log = LoggerFactory.getLogger(TMColoredCharacterFactory.class);
	
	private TMColorPalette palette;
	private Color defaultForeground;
	private Color defaultBackground;

	public TMColoredCharacterFactory(TMColorPalette palette) {

//		log.debug("Palette to initailze: " + palette);
		this.palette = palette;
		this.defaultForeground = palette.getColorByIndex(2);
		this.defaultBackground = palette.getColorByIndex(7);
	}
	
	public TMColoredCharacter create(char ch) {
		return create(ch, defaultForeground, defaultBackground);
	}
	public TMColoredCharacter create(char ch, int fgIndex, int bgIndex) {
		return create(ch, palette.getColorByIndex(fgIndex), palette.getColorByIndex(bgIndex));
	}
	protected TMColoredCharacter create(char ch, Color fg, Color bg) {
		return new TMColoredCharacter(ch, fg, bg);
	}

	public TMColoredCharacter create(TMColoredCharacter template) {
		return new TMColoredCharacter(template.getCharacter(), template.getFg(), template.getBg());
	}

	public TMColoredCharacter createTransparent() {
		return TMColoredCharacter.TRANSPARENT;
	}
	
}
