package kba.unicodeart.format.colored_char;

import java.awt.Color;

/**
 * Augmentation of Java char with background and foreground AWT colors.
 * 
 */
public class TMColoredCharacter {
	
	/**
	 * Foreground {@link Color} used as a for default transparent character (Bright Cyan with full transparency).
	 */
	protected static Color TRANSPARENT_AWT_FOREGROUND_COLOR = new Color(0, 0x44, 0x44, 0);
	/**
	 * Background {@link Color} used as a for default transparent character (Bright Cyan with full transparency).
	 */
	protected static Color TRANSPARENT_AWT_BACKGROUND_COLOR = new Color(0, 0, 0, 0);
	/**
	 * Template for a transparent character.
	 */
	public static TMColoredCharacter TRANSPARENT = new TMColoredCharacter('\u2400', TRANSPARENT_AWT_FOREGROUND_COLOR, TRANSPARENT_AWT_BACKGROUND_COLOR);
	
	private char character;
	private Color fg;
	private Color bg;

	//
	// Constructors
	//


	/**
	 * @param character the character of this colored character
	 * @param fg the background color of this colored character
	 * @param bg the background color of this colored character
	 */
	protected TMColoredCharacter(char character, Color fg, Color bg) {
		this.character = character;
		this.fg = fg;
		this.bg = bg;
	}

	// 
	// Getters/Setters
	//

	
	/**
	 * @return the char for this colored character
	 */
	public char getCharacter() { return character; }

	/**
	 * @param character the character of this colored character
	 */
	public void setCharacter(char character) {
		this.character = character;
	}

	/**
	 * @return the foreground color of this colored character
	 */
	public Color getFg() { return fg; }

	/**
	 * @param fg the foreground color of this colored character
	 */
	public void setFg(Color fg) { this.fg = fg; }

	/**
	 * @return the background color of this colored character
	 */
	public Color getBg() { return bg; }

	/**
	 * @param bg the background color of this colored character
	 */
	public void setBg(Color bg) { this.bg = bg; }

	/**
	 * @return whether the background color of this character is transparent.
	 */
	public boolean isTransparent() { return bg.getAlpha() > 0; }

	/**
	 * Set this colored character's background to transparent.
	 */
	public void setTransparent() { bg = TMColoredCharacter.TRANSPARENT.getBg(); }

	//
	// @Override
	//

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bg == null) ? 0 : bg.hashCode());
		result = prime * result + character;
		result = prime * result + ((fg == null) ? 0 : fg.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TMColoredCharacter other = (TMColoredCharacter) obj;
		if (getCharacter() != other.getCharacter())
			return false;
		if (getBg() == null) {
			if (other.getBg() != null)
				return false;
		} else if (!getBg().equals(other.getBg()))
			return false;
		if (getFg() == null) {
			if (other.getFg() != null)
				return false;
		} else if (!getFg().equals(other.getFg()))
			return false;
		return true;
	}
	
	/**
	 * String representation: "'$CHAR' [$FOREGROUND / $BACKGROUND]"
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "' " + character + " ' [" + fg + " / " + bg + "]";
	}

}
