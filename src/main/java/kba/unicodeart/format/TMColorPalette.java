package kba.unicodeart.format;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import kba.unicodeart.format.colored_char.TMColoredCharacter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A trimap of character, integer index and {@link Color}
 *
 */
public class TMColorPalette {
	
	private static final Logger log = LoggerFactory.getLogger(TMColorPalette.class);

	private final Map<Character,Integer> charToIndex = new HashMap<>();
	private final Map<Color,Integer> colorToIndex = new HashMap<>();
	private final Map<Character,Color> charToColor = new HashMap<>();
	private final Map<Color,Character> colorToChar = new HashMap<>();
	private final List<Character> charIndex = new ArrayList<>();
	private final List<Color> colorIndex = new ArrayList<>();

	public int size() {
		return charIndex.size();
	}

	@SafeVarargs
	public TMColorPalette(List<Color>... colorIndexes) {
		this(StandardCharacterIndex.DEFAULT.index(), colorIndexes);
	}

	@SafeVarargs
	public TMColorPalette(List<Character> characterIndex, List<Color>... colorLists) {
		ArrayList<Color> colorList = new ArrayList<>();
		for (List<Color> thisColorList : colorLists) {
			colorList.addAll(thisColorList);
		}
		for (int i = 0; i < Math.min(characterIndex.size(), colorList.size()); i++) {
			Character ch = characterIndex.get(i);
			Color color = colorList.get(i);
			if (this.colorIndex.contains(color))
				continue;
			this.charIndex.add(ch);
			this.colorIndex.add(color);
			this.charToColor.put(ch, color);
			this.charToIndex.put(ch, i);
			this.colorToChar.put(color, ch);
			this.colorToIndex.put(color, i);
		}
	}

	public List<Color> getColorIndex() { return colorIndex; }

	public char getCharByIndex(int index) { return charIndex.get(index); }

	public int getIndexByChar(char ch) { return charToIndex.get(ch); }
	
	public Color getColorByChar(char ch) { return charToColor.get(ch); }
	
	public Color getColorByIndex(int index) { return colorIndex.get(index); }
	
	public String serialize() {
		StringBuilder sb = new StringBuilder();
		sb.append("PALETTE\n");
		for (int i = 0; i < colorIndex.size() ; i++) {
			log.debug("cur idx: " + i);
			sb.append(getCharByIndex(i));
			sb.append("\t");
			String hexColor = String.format("#%06X", (0xFFFFFF & getColorByIndex(i).getRGB()));
			sb.append(hexColor);
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Return the <code>char</code> that represents this {@link java.awt.Color}
	 * @param color the Color to find a char for
	 * @return the character that represents this Color
	 */
	public char getCharByColor(Color color) {
//		log.debug("Asking for Color " + color +". ");
//		log.debug("Indexed as " + getIndexByColor(color) + ". ");
		char ch = '␀'; // TODO Magic Number \u2400
		if (null == color) {
			log.error("COLOR IS NULL");
		}
		if (color.equals(TMColoredCharacter.TRANSPARENT.getFg()) || color.equals(TMColoredCharacter.TRANSPARENT.getBg())) {
			return ch;
		} else {
			try {
				ch = colorToChar.get(color);
			} catch (NullPointerException e) {
				log.error("This color isn't in the palette: " + color);
				e.printStackTrace();
				log.debug("Exception!", e);
			}
		}
		return ch;
	}
	
	public int getIndexByColor(Color color) {
//		log.debug("colorToIndex: " + colorToIndex);
		int colorIndex = -1; // TODO Magic Number
		try {
			colorIndex = colorToIndex.get(color);
		} catch (NullPointerException e) {
			log.error("This color isn't in the palette: " + color);
			e.printStackTrace();
			log.debug("Exception!", e);
		}
		return colorIndex;
	}
	
	public void writeXML(XMLStreamWriter xml) throws XMLStreamException {
		writeXML(xml, false);
	}

	public void writeXML(XMLStreamWriter xml, boolean forceWriteout) throws XMLStreamException {
		xml.writeStartElement(TMXmlElements.Palette.getXmlName());
		StandardPalette matchingStandardPalette = null;
		if (! forceWriteout) {
			matchingStandardPalette = StandardPalette.findStandardPalette(this);
		}
		if (! forceWriteout && null != matchingStandardPalette) {
			xml.writeAttribute("ref", matchingStandardPalette.name());
		} else {
			for (int i = 0 ; i < getColorIndex().size() ; i++) {
				xml.writeStartElement(TMXmlElements.PaletteChar.getXmlName());
				xml.writeAttribute("color", String.format("#%06X", (0xFFFFFF & getColorByIndex(i).getRGB())));
				xml.writeAttribute("index", String.valueOf(i));
				xml.writeCData(String.valueOf(getCharByIndex(i)));
				xml.writeEndElement();
				xml.writeCharacters("\n");
			}
		}
		xml.writeEndElement();

	}

	public static TMColorPalette readXML(XMLStreamReader xml) throws XMLStreamException {
		if (! xml.isStartElement()) {
			log.error("Must be start element");
		}
		if (! TMXmlElements.Palette.getXmlName().equals(xml.getLocalName())) {
			log.error("Must be start with <Palette>");
		}
		for (int i = 0; i < xml.getAttributeCount(); i++) {
			String attrName = xml.getAttributeLocalName(i);
			String attrValue = xml.getAttributeValue(i);
			if ("ref".equals(attrName)) {
				try {
					StandardPalette sp = StandardPalette.valueOf(attrValue);
					return sp.palette();
				} catch (IllegalArgumentException e) {
					log.error("Invalid palette name " + attrValue);
				}
			}
		}
		while (xml.hasNext()) {
			// TODO Parse the palette
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charIndex == null) ? 0 : charIndex.hashCode());
		result = prime * result + ((colorIndex == null) ? 0 : colorIndex.hashCode());
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
		TMColorPalette other = (TMColorPalette) obj;
		if (charIndex == null) {
			if (other.charIndex != null)
				return false;
		} else if (!charIndex.equals(other.charIndex))
			return false;
		if (colorIndex == null) {
			if (other.colorIndex != null)
				return false;
		} else if (!colorIndex.equals(other.colorIndex))
			return false;
		return true;
	}

}
