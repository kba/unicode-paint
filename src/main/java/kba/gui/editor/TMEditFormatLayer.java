package kba.gui.editor;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.googlecode.lanterna.terminal.TerminalPosition;

public class TMEditFormatLayer {
	
	private int width = 80;
	private int height = 24;
	private String defaultValue;
	private char[][] charMap;
	private final Map<Character,String> legend = new HashMap<>();
	
	public TMEditFormatLayer(int width, int height, String defaultValue) {
		this.width = width;
		this.height = height;
		this.defaultValue = defaultValue;
		this.charMap = new char[height][width];
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				set(x, y, TMEditFormat.DEFAULT_CHAR);
			}
		}
	}

	public char get(int curX, int curY) { return charMap[curY][curX]; }

	public void set(TerminalPosition pos, char ch) {
		set(pos.getColumn(), pos.getRow(), ch);
	}
	public void set(int x, int y, char ch) {
		set(x, y, ch, defaultValue);
	}
	public void set(int x, int y, char ch, String legendValue) {
		charMap[y][x] = ch;
		if (! legend.containsKey(ch)) setLegend(ch, legendValue);
	}
	public void setLegend(char key, String value) { legend.put(key, value); }

	public String getLegend(int curX, int curY) {
		String thisLegend = legend.get(get(curX,curY));
		if (null == thisLegend) return defaultValue;
		return thisLegend;
	}
	
	public String exportToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(exportMapToString());
		sb.append("\n");
		sb.append("LEGEND");
		sb.append("\n");
		sb.append(exportLegendToString());
		return sb.toString();
	}

	public String exportLegendToString() {
		Gson gson = new Gson();
		return gson.toJson(legend);
	}

	public String exportMapToString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				sb.append(get(x, y));
			}
			if (y < height -1) sb.append("\n");
		}
		return sb.toString();
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }

}