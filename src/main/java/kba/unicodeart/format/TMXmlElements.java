package kba.unicodeart.format;

import kba.unicodeart.format.colored_char.TMColoredCharacter;

/**
 * The XML elements used for the serialization format
 * 
 */
public enum TMXmlElements {
	/**
	 * Character data: A map of characters of the {@link TMColoredCharacter}s in this layer.
	 */
	Character,
	Color,
	/**
	 * Color data: A map of foreground colors of the {@link TMColoredCharacter}s in this layer.
	 */
	Foreground,
	/**
	 * Color data: A map of background colors of the {@link TMColoredCharacter}s in this layer.
	 */
	Background,
	Legend,
	/**
	 * Represent a layer
	 */
	Layer,
	/**
	 * Transparency data: A map of transparency booleans of the {@link TMColoredCharacter}s in this layer.
	 */
	Transparency
	;
	/**
	 * @return the XML Element Local Name of this XML Element
	 */
	public String getXmlName() {
		return this.name();
	}
}