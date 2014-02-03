package kba.unicodeart.format;

import java.awt.Color;

public class TMColoredCharacter {
	
	private char character;
	public char getCharacter() { return character; }
	public void setCharacter(char character) { this.character = character; }

	private Color fg;
	public Color getFg() { return fg; }
	public void setFg(Color fg) { this.fg = fg; }

	private Color bg;
	public Color getBg() { return bg; }
	public void setBg(Color bg) { this.bg = bg; }

	public TMColoredCharacter(char character, Color fg, Color bg) {
		this.character = character;
		this.fg = fg;
		this.bg = bg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bg == null) ? 0 : bg.hashCode());
		result = prime * result + character;
		result = prime * result + ((fg == null) ? 0 : fg.hashCode());
		return result;
	}
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
	public TMColoredCharacter(char character) {
		this(character, Color.WHITE, Color.BLACK);
	}
	
	@Override
	public String toString() {
		return character + "[" + fg + " / " + bg + "]";
	}

}
