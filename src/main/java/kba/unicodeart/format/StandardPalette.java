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

import com.googlecode.lanterna.terminal.swing.TerminalPalette;

public enum StandardPalette {
	TERM_88_COLORS,
	TERM_256_COLORS,
	GNOME_TERMINAL_88,
	GNOME_TERMINAL_256,
	STANDARD_VGA_88,
	STANDARD_VGA_256,
	WINDOWS_XP_COMMAND_PROMPT_88,
	WINDOWS_XP_COMMAND_PROMPT_256,
	MAC_OS_X_TERMINAL_APP_88,
	MAC_OS_X_TERMINAL_APP_256,
	PUTTY_88,
	PUTTY_256,
	XTERM_88,
	XTERM_256,
	DEFAULT,
	;
	 
	private TMColorPalette palette;
	public TMColorPalette palette() {
		return palette;
	}

	static {
		Map<String,List<Color>> mapToArr = new HashMap<>();
		TERM_88_COLORS.palette = new TMColorPalette(new ArrayList<Color>());
		TERM_256_COLORS.palette = new TMColorPalette(new ArrayList<Color>());
		mapToArr.put("/terminal-colors/88colors-hex.txt", TERM_88_COLORS.palette.getColorIndex());
		mapToArr.put("/terminal-colors/256colors-hex.txt", TERM_256_COLORS.palette.getColorIndex());
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
		GNOME_TERMINAL_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.GNOME_TERMINAL.asList(), StandardPalette.TERM_88_COLORS.palette().getColorIndex());

	 GNOME_TERMINAL_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.GNOME_TERMINAL.asList(), TERM_256_COLORS.palette().getColorIndex());

	 STANDARD_VGA_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.STANDARD_VGA.asList(), TERM_88_COLORS.palette().getColorIndex());

	 STANDARD_VGA_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.STANDARD_VGA.asList(), TERM_256_COLORS.palette().getColorIndex());

	 WINDOWS_XP_COMMAND_PROMPT_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_88_COLORS.palette().getColorIndex());

	 WINDOWS_XP_COMMAND_PROMPT_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.WINDOWS_XP_COMMAND_PROMPT.asList(), TERM_256_COLORS.palette().getColorIndex());

	 MAC_OS_X_TERMINAL_APP_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_88_COLORS.palette().getColorIndex());

	 MAC_OS_X_TERMINAL_APP_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.MAC_OS_X_TERMINAL_APP.asList(), TERM_256_COLORS.palette().getColorIndex());

	 PUTTY_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.PUTTY.asList(), TERM_88_COLORS.palette().getColorIndex());

	 PUTTY_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.PUTTY.asList(), TERM_256_COLORS.palette().getColorIndex());

	 XTERM_88.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.XTERM.asList(), TERM_88_COLORS.palette().getColorIndex());

	 XTERM_256.palette = new TMColorPalette(
			StandardCharacterIndex.DEFAULT.index(), TerminalPalette.XTERM.asList(), TERM_256_COLORS.palette().getColorIndex());
	 
	 DEFAULT.palette = MAC_OS_X_TERMINAL_APP_256.palette;
	}
	
	static StandardPalette findStandardPalette(TMColorPalette needle) {
		StandardPalette ret = null;
		for (StandardPalette palEnum : values()) {
			if (palEnum == StandardPalette.DEFAULT) {
				continue;
			}
			if (palEnum.palette.equals(needle)) {
				ret = palEnum;
			}
		}
		return ret;
	}
}
