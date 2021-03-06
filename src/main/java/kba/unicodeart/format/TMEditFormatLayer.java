package kba.unicodeart.format;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import kba.unicodeart.format.colored_char.TMColoredCharacter;
import kba.unicodeart.format.colored_char.TMColoredCharacterFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Layer of a Map
 * 
 */
/**
 * @author kba
 *
 */
public class TMEditFormatLayer {

	private static final Logger log = LoggerFactory
			.getLogger(TMEditFormatLayer.class);
	private static final String GLOBAL_DEFAULT_LEGEND_VALUE = "";

	/**
	 * Internally the charMap is backed by an array
	 */
	private TMColoredCharacter[][] charMap;

	/**
	 * The legend is a map of char keys to Strin values
	 */
	private final Map<Character, String> legend = new HashMap<>();

	/**
	 * this is used to speed up color mapping
	 */
	private final Map<Color, Integer> colorMappingCache = new HashMap<>();

	/**
	 * The character-color-index palette
	 */
	private TMColorPalette palette;
	
	/**
	 * Factory to create TMColoredCharacters
	 */
	private TMColoredCharacterFactory colorCharFactory;

	/**
	 * Name of the Layer
	 */
	private String name;
	
	// TODO parameterize
	private int width = 80; 

	// TODO parameterize
	private int height = 24;

	// TODO parameterize
	private String defaultLegendValue = GLOBAL_DEFAULT_LEGEND_VALUE; 

	//
	// Constructors
	//
	
	public TMEditFormatLayer(String name, int width, int height, String defaultLegendValue) {
		this(name, width, height, defaultLegendValue, StandardPalette.DEFAULT.palette());
	}

	/**
	 * @param name the name of the layer
	 * @param width the width of the layer in charactwrs
	 * @param height the height of the layer in charactwrs
	 * @param defaultLegendValue the default legend value (TODO)
	 * @param palette the {@link TMColorPalette} to use for color
	 */
	public TMEditFormatLayer(String name, int width, int height, String defaultLegendValue, TMColorPalette palette) {
		this.name = (null != name) ? name : "newLayer";
		this.width = width;
		this.height = height;
		this.defaultLegendValue = (null != defaultLegendValue) ? defaultLegendValue : GLOBAL_DEFAULT_LEGEND_VALUE;
		this.palette = (null != palette) ? palette : StandardPalette.DEFAULT.palette();
		this.charMap = new TMColoredCharacter[height][width];
		this.colorCharFactory = new TMColoredCharacterFactory(this.palette);
		setupEmpty();
	}

	/**
	 * @param width the width of the layer in characters
	 * @param height the height of the layer in characters
	 */
	public TMEditFormatLayer(int width, int height) {
		this(null, width, height, null, null);
	}

	//
	// Mutators
	//

	/**
	 * Initializes the backing array of {@link TMColoredCharacter} with
	 * transparent characters.
	 */
	public void setupEmpty() {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				set(x, y, TMColoredCharacter.TRANSPARENT);
			}
		}
	}

	/**
	 * Return the {@link TMColoredCharacter} at position x, y
	 * @return the {@link TMColoredCharacter} at position x, y
	 * @param x the x position of the colored char
	 * @param y the y position of the colored char
	 */
	public TMColoredCharacter get(int x, int y) {
		if (TMColoredCharacter.TRANSPARENT == charMap[y][x])
			charMap[y][x] = colorCharFactory
					.create(TMColoredCharacter.TRANSPARENT);
		return charMap[y][x];
	}

	/**
	 * Sets the character of the {@code TMColoredCharacter} at the specified position to the supplied character.
	 * 
	 * <p>
	 * If the character currently at [x,y] is the transparent character, create a new character with default
	 * foreground and background. Otherwise just replace the character.
	 * </p>
	 * 
	 * @param x the x location
	 * @param y the y location
	 * @param ch the {@code char} to set to
	 */
	public void set(int x, int y, char ch) {
		TMColoredCharacter charAtXY = get(x, y);
		if (charAtXY.equals(getTransparentChar()))
			set(x, y, colorCharFactory.create(ch));
		else {
			get(x, y).setCharacter(ch);
		}

	}
	/**
	 * Replaces the colored character at position [x,y] with the supplied character
	 * @param x the x position
	 * @param y the y position
	 * @param colorChar the character to replace with
	 */
	public void set(int x, int y, TMColoredCharacter colorChar) {
		charMap[y][x] = colorChar;
	}

	public void setLegend(char key, String value) {
		legend.put(key, value);
	}

	public String getLegend(int curX, int curY) {
		String thisLegend = legend.get(get(curX, curY));
		if (null == thisLegend)
			return defaultLegendValue;
		return thisLegend;
	}

	public String getDefaultLegendValue() {
		return defaultLegendValue;
	}

	//
	// Deserialization
	//
	
	/**
	 * Initializes a layer by parsing XML.
	 * @param width the width of the layer
	 * @param height the height of the layer
	 * @param xml the XML stream to read from
	 * @return the initialized layer
	 * @throws XMLStreamException if parsing fails
	 */
	public static TMEditFormatLayer fromXML(int width, int height, XMLStreamReader xml) throws XMLStreamException {

		TMEditFormatLayer newLayer = new TMEditFormatLayer(width, height);

		if (! xml.isStartElement()) {
			log.error("Must be start element");
			return null;
		}

		TMXmlElements currentElement = TMXmlElements.fromXmlName(xml.getLocalName());

		if (TMXmlElements.Layer != currentElement) {
			log.error ("Start tag must be 'Layer'");
		}
		for (int i = 0; i < xml.getAttributeCount(); i++) {
			if ("name".equals(xml.getAttributeLocalName(i))) {
				newLayer.setName(xml.getAttributeValue(i));
			}
		}

		PARSE_LOOP: while (xml.hasNext()) {
			int next = xml.next();
			log.trace("Current event: " + next);

			if (xml.isStartElement()) {
				currentElement = TMXmlElements.fromXmlName(xml.getLocalName());
				log.trace("START: " + currentElement.getXmlName());
			} else if (xml.isCharacters()) {
				// TODO trim leading/trailing whitespace
				String text = xml.getText().replaceAll("\n", "");
				if (xml.isWhiteSpace()) {
					continue;
				}
				log.debug(currentElement.getXmlName());
				switch (currentElement) {
				case Character:
					newLayer.parseCharString(text);
					break;
				case Background: case Foreground:
					newLayer.parseColorString(text, currentElement == TMXmlElements.Background);
					break;
				case Transparency:
					newLayer.parseTransparencyString(text);
					break;
				default:
					log.error("Unhandled CDATA for element " + currentElement);
					break;
				}
			} else if (xml.isEndElement()) {
				log.debug("END: " + xml.getLocalName());
				if (xml.getLocalName().equals(TMXmlElements.Layer.getXmlName())) {
					log.debug("WE'RE DONE WITH THIS LAYER.");
					break PARSE_LOOP;
				}
			} else {
				log.error("Unhandled event: " + xml.getEventType());
			}

		}

		return newLayer;
	}

	/**
	 * TODO make it the other way round, read charData (stream) and calculate
	 * the right positions
	 * 
	 * @param charData
	 */
	private void parseCharString(String charData) {
		char[] asChars = charData.toCharArray();
		log.debug("Parsing <Characters>: '" + charData.substring(0, 10) +"...'.");
		// log.debug(new String(asChars));
		// log.debug("LENGTH : " + asChars.length);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int asCharsOffset = width * y + x;
				// log.debug("cur off: " + asCharsOffset);
				this.get(x, y).setCharacter(asChars[asCharsOffset]);
			}
		}
	}

	/**
	 * @param colorData
	 * @param parseAsBackground
	 */
	private void parseColorString(String colorData, boolean parseAsBackground) {
		char[] asChars = colorData.toCharArray();
		if (parseAsBackground) {
			log.debug("Parsing <Background>: '" + colorData.substring(0, 10) +"...'.");
		} else {
			log.debug("Parsing <Foreground>: '" + colorData.substring(0, 10) +"...'.");
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				TMColoredCharacter cChar = this.get(x, y);
				char ch = asChars[width * y + x];
				Color color;
				if (ch == TMColoredCharacter.TRANSPARENT.getCharacter()) {
					color = (parseAsBackground) 
							? TMColoredCharacter.TRANSPARENT.getBg()
							: TMColoredCharacter.TRANSPARENT.getFg();
				} else {
					color = this.palette.getColorByChar(ch);
				}
				if (null == color) {
					log.error("No color for char '{}'", ch);
				}
				if (parseAsBackground)
					cChar.setBg(color);
				else
					cChar.setFg(color);
			}
		}
	}

	private void parseTransparencyString(String data) {
		char[] asChars = data.toCharArray();
		log.debug("Parsing <Background>: '" + data.substring(0, 10) +"...'.");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				TMColoredCharacter cChar = this.get(x, y);
				if (asChars[width * y + x] == '1') {
					cChar.setTransparent();
				}
			}
		}
	}
	//
	// Serialization
	// TODO adapt to new palette system
	//

	/**
	 * @param xml the {@link XMLStreamWriter} to write to
	 * @throws XMLStreamException
	 */
	public void writeXML(XMLStreamWriter xml) throws XMLStreamException {
		xml.writeCharacters("\n");
		xml.writeStartElement(TMXmlElements.Layer.getXmlName());
		xml.writeAttribute("name", this.getName());
		xml.writeCharacters("\n");
		xml.writeStartElement(TMXmlElements.Character.getXmlName());
		exportMapToXML(xml);
		xml.writeCharacters("\n");
		xml.writeEndElement();
		 xml.writeCharacters("\n");
		 xml.writeStartElement(TMXmlElements.Foreground.getXmlName());
		 xml.writeCData(exportColorToString(false));
		 xml.writeEndElement();
		 xml.writeCharacters("\n");
		 xml.writeStartElement(TMXmlElements.Background.getXmlName());
		 xml.writeCData(exportColorToString(true));
		 xml.writeEndElement();
		 xml.writeCharacters("\n");
		 xml.writeStartElement(TMXmlElements.Transparency.getXmlName());
		 xml.writeCData(exportTransparencyToString());
		 xml.writeEndElement();
		 xml.writeCharacters("\n");
		 xml.writeStartElement(TMXmlElements.Legend.getXmlName());
		 xml.writeCData(exportLegendToString());
		 xml.writeEndElement();
		 xml.writeCharacters("\n");
		xml.writeEndElement();
	}

	/**
	 * Exports the colored characters layer to XML
	 * @param xml the {@link XMLStreamWriter} to write to
	 * @throws XMLStreamException
	 */
	protected void exportMapToXML(XMLStreamWriter xml) throws XMLStreamException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(get(x,y).getCharacter());
			}
			sb.append("\n");
		}
		xml.writeCData(sb.toString());

	}

	/**
	 * @return the transparency as a string representation
	 */
	private String exportTransparencyToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				sb.append(get(x, y).isTransparent() ? "1" : "0");
			}
			if (y < height - 1)
				sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * @return the legend as a string
	 */
	private String exportLegendToString() {
		Gson gson = new Gson();
		return gson.toJson(legend);
	}

	/**
	 * Export the colors of the layer as a string.
	 * @param exportBg whether to export the background (<code>true</code>) or foreground (<code>false</code>).
	 * @return the colors of the layer as a string
	 */
	private String exportColorToString(boolean exportBg) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = exportBg ? get(x, y).getBg() : get(x, y).getFg();
				log.trace(String.format("%s At %s/%s: %s", (exportBg ? "Background" : "Foreground"), x, y, color));
				sb.append(palette.getCharByColor(color));
			}
			if (y < height - 1)
				sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}
	
	//
	// Getters / Setters
	//

	// Getters / Setters
	//

	/**
	 * @return the width of the layer
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @return the height of the layer
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * Return the name of the layer.
	 * @return the name of the layer
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name of the layer
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the character representing a transparent background in this layer
	 */
	public TMColoredCharacter getTransparentChar() {
		return TMColoredCharacter.TRANSPARENT;
	}


	/**
	 * @return the palette of this layer
	 */
	public TMColorPalette getPalette() {
		return palette;
	}
	
	//
	// @Overrides 
	//

	/**
	 * Layer Name + x/y size, i.e.
	 * <code>newLayer [10x20]</code>
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Layer " + getName() + " [" + width + "x" + height + "]";
	}

}