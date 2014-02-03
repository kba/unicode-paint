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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.terminal.swing.TerminalPalette;

public class TMEditFormatColorPalette {
	
	static Logger log = LoggerFactory.getLogger(TMEditFormatColorPalette.class);

	public static final List<Character> DEFAULT_CHARACTER_INDEX;
	static {
		DEFAULT_CHARACTER_INDEX = new ArrayList<>();
		String filename = "/character-index/default_mapping";
		try {
			InputStream is = TMEditFormatColorPalette.class.getResource(filename).openStream();
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
				InputStream is = TMEditFormatColorPalette.class.getResource(filename).openStream();
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
	public static final TMEditFormatColorPalette GNOME_TERMINAL_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.GNOME_TERMINAL.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette GNOME_TERMINAL_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.GNOME_TERMINAL.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette STANDARD_VGA_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.STANDARD_VGA.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette STANDARD_VGA_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.STANDARD_VGA.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette WINDOWS_XP_COMMAND_PROMPT_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette WINDOWS_XP_COMMAND_PROMPT_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette MAC_OS_X_TERMINAL_APP_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette MAC_OS_X_TERMINAL_APP_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette PUTTY_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.PUTTY.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette PUTTY_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.PUTTY.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette XTERM_88 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.XTERM.asList(), TERM_88_COLORS);
	public static final TMEditFormatColorPalette XTERM_256 = new TMEditFormatColorPalette(
			DEFAULT_CHARACTER_INDEX, TerminalPalette.XTERM.asList(), TERM_256_COLORS);
	public static final TMEditFormatColorPalette DEFAULT_PALETTE = TMEditFormatColorPalette.MAC_OS_X_TERMINAL_APP_256;

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
	public TMEditFormatColorPalette(List<Color>... colorIndexes) {
		this(DEFAULT_CHARACTER_INDEX, colorIndexes);
	}
	
	@SafeVarargs
	public TMEditFormatColorPalette(List<Character> characterIndex, List<Color>... colorLists) {
		ArrayList<Color> colorList = new ArrayList<>();
		for (List<Color> thisColorList : colorLists) {
			colorList.addAll(thisColorList);
		}
		log.debug("Colors: " + colorList.size());
		for (int i = 0; i < Math.min(characterIndex.size(), colorList.size()); i++) {
			Character ch = characterIndex.get(i);
			Color color = colorList.get(i);
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

}
