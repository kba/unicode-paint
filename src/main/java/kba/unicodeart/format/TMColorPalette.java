package kba.unicodeart.format;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kba.unicodeart.format.colored_char.TMColoredCharacter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.terminal.swing.TerminalPalette;

/**
 * A trimap of character, integer index and {@link Color}
 *
 */
public class TMColorPalette {
	
	private static final Logger log = LoggerFactory.getLogger(TMColorPalette.class);

	public static final List<Character> DEFAULT_CHARACTER_INDEX;
	static {
		DEFAULT_CHARACTER_INDEX = new ArrayList<>();
		String filename = "/character-index/default_mapping";
		try {
			InputStream is = TMColorPalette.class.getResource(filename).openStream();
			try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
				for(String line; (line = br.readLine()) != null; ) {
					if (line.matches("^\\s*#.*")) continue;
					if (! line.contains("\t")) continue;
					String[] segs = line.split("\t");
					if (segs.length < 3) {
						throw new IllegalArgumentException("Invalid mapping line: " + line);
					}
					char ch = segs[1].charAt(0);
					DEFAULT_CHARACTER_INDEX.add(ch);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't locate or parse file " + filename);
		}
	}
	public static final List<Color> TERM_88_COLORS=new ArrayList<>();
	public static final List<Color> TERM_256_COLORS=new ArrayList<>();
	static {
		Map<String,List<Color>> mapToArr = new HashMap<>();
		mapToArr.put("/terminal-colors/88colors-hex.txt", TERM_88_COLORS);
		mapToArr.put("/terminal-colors/256colors-hex.txt", TERM_256_COLORS);
		for (Entry<String, List<Color>> entry : mapToArr.entrySet()) {
			String filename = entry.getKey();
			List<Color> list = entry.getValue();
			try {
				InputStream is = TMColorPalette.class.getResource(filename).openStream();
				try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
					for(String line; (line = br.readLine()) != null; ) {
						list.add(Color.decode(line));
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("Couldn't locate or parse file " + filename);
			}
		}
	}
	public static final TMColorPalette GNOME_TERMINAL_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.GNOME_TERMINAL.asList(), TERM_88_COLORS);
	public static final TMColorPalette GNOME_TERMINAL_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.GNOME_TERMINAL.asList(), TERM_256_COLORS);
	public static final TMColorPalette STANDARD_VGA_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.STANDARD_VGA.asList(), TERM_88_COLORS);
	public static final TMColorPalette STANDARD_VGA_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.STANDARD_VGA.asList(), TERM_256_COLORS);
	public static final TMColorPalette WINDOWS_XP_COMMAND_PROMPT_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_88_COLORS);
	public static final TMColorPalette WINDOWS_XP_COMMAND_PROMPT_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_256_COLORS);
	public static final TMColorPalette MAC_OS_X_TERMINAL_APP_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_88_COLORS);
	public static final TMColorPalette MAC_OS_X_TERMINAL_APP_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_256_COLORS);
	public static final TMColorPalette PUTTY_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.PUTTY.asList(), TERM_88_COLORS);
	public static final TMColorPalette PUTTY_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.PUTTY.asList(), TERM_256_COLORS);
	public static final TMColorPalette XTERM_88 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.XTERM.asList(), TERM_88_COLORS);
	public static final TMColorPalette XTERM_256 = new TMColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.XTERM.asList(), TERM_256_COLORS);
	public static final TMColorPalette DEFAULT_PALETTE = TMColorPalette.MAC_OS_X_TERMINAL_APP_256;

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
		this(DEFAULT_CHARACTER_INDEX, colorIndexes);
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

	private List<Color> getColorIndex() { return colorIndex; }

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

}
