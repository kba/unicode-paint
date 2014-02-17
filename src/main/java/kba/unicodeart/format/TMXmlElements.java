package kba.unicodeart.format;

import kba.unicodeart.format.colored_char.TMColoredCharacter;

/**
 * The XML elements used for the serialization format
 * 
 */
public enum TMXmlElements {
	/**
	 * Root element
	 */
	Tilemap,

	/**
	 * Layers
	 */
	Layers,

	/**
	 * Represent a layer
	 */
	Layer,

	/**
	 * Character data: A map of characters of the {@link TMColoredCharacter}s in this layer.
	 */
	Character,

	/**
	 * Color data: A map of foreground colors of the {@link TMColoredCharacter}s in this layer.
	 */
	Foreground,

	/**
	 * Color data: A map of background colors of the {@link TMColoredCharacter}s in this layer.
	 */
	Background,

	/**
	 * The map legend in JSON
	 */
	Legend,

	/**
	 * Transparency data: A map of transparency booleans of the {@link TMColoredCharacter}s in this layer.
	 */
	Transparency, 
	
	/**
	 * The palette used
	 */
	Palette,
	PaletteChar,

	;
	/**
	 * @return the XML Element Local Name of this XML Element
	 */
	public String getXmlName() {
		return this.name();
	}

	public static TMXmlElements fromXmlName(String needle) {
		for (TMXmlElements el : values()) {
			if (el.getXmlName().equals(needle)) {
				return el;
			}
		}
		return null;
	}
}