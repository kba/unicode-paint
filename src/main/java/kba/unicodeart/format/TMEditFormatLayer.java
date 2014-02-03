package kba.unicodeart.format;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import kba.unicodeart.format.TMEditFormatOptions.ColorType;

import com.google.gson.Gson;
import com.googlecode.lanterna.terminal.XTerm8bitIndexedColorUtils;

public class TMEditFormatLayer {
	
	private String name;
	private int width = 80;
	private int height = 24;
	private TMColoredCharacter transparentChar = new TMColoredCharacter(' ');
	private String defaultLegendValue;
	private TMColoredCharacter[][] charMap;
	private float opacity = 1;
	private boolean visible = true;
	private final Map<Character,String> legend = new HashMap<>();
	private final char transparentCharacter;
	private final Map<Color,Integer> colorMappingCache = new HashMap<>();
	
	public TMEditFormatLayer(String name, int width, int height, String defaultLegendValue) {
		this(name, width, height, defaultLegendValue, TMEditFormat.DEFAULT_TRANSPARENT_CHAR);
	}
	
	public TMEditFormatLayer(String name, int width, int height, String defaultLegendValue, char transparentChar) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.defaultLegendValue = defaultLegendValue;
		this.transparentCharacter = transparentChar;
		this.charMap = new TMColoredCharacter[height][width];
		setupEmpty();
	}

	public void setupEmpty() {
		for (int y = 0 ; y < this.height ; y++) {
			for (int x = 0 ; x < this.width ; x++) {
				set(x, y, this.transparentCharacter);
			}
		}
	}

	public TMColoredCharacter get(int curX, int curY) { return charMap[curY][curX]; }

//	public void set(TerminalPosition pos, char ch) {
//		set(pos.getColumn(), pos.getRow(), new ColoredCharacter(ch));
//	}
	public void set(int x, int y, char ch) {
		set(x, y, ch, defaultLegendValue);
	}
	public void set(int x, int y, char ch, String legendValue) {
		charMap[y][x] = new TMColoredCharacter(ch);
		if (! legend.containsKey(ch)) setLegend(ch, legendValue);
	}
	public void setLegend(char key, String value) { legend.put(key, value); }

	public String getLegend(int curX, int curY) {
		String thisLegend = legend.get(get(curX,curY));
		if (null == thisLegend) return defaultLegendValue;
		return thisLegend;
	}
	
	public String getDefaultLegendValue() { return defaultLegendValue; }
	
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
	
	// TODO adapt to new palette system
	
	public String exportFgToString(ColorType colorType) { return exportColorToString(false, colorType); }
	public String exportBgToString(ColorType colorType) { return exportColorToString(true, colorType); }

	private String exportColorToString(boolean exportBg, ColorType colorType) {
		StringBuilder sb = new StringBuilder();
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				Color color = exportBg ? get(x,y).getBg() : get(x,y).getFg();
				switch(colorType){
				case ANSI:
					int indexANSI;
					if (! colorMappingCache.containsKey(color)) {
						indexANSI = XTerm8bitIndexedColorUtils.getClosestColorANSI(color.getRed(), color.getGreen(), color.getBlue());
						colorMappingCache.put(color, indexANSI);
					}
					indexANSI = colorMappingCache.get(color);
					sb.append(indexANSI);
					break;
				case ANSI256:
					int index256;
					if (! colorMappingCache.containsKey(color)) {
						index256 = XTerm8bitIndexedColorUtils.getClosestColor(color.getRed(), color.getGreen(), color.getBlue());
						colorMappingCache.put(color, index256);
					}
					index256 = colorMappingCache.get(color);
					sb.append(index256);
					break;
				case RGB:
					sb.append("#");
					sb.append(Integer.toHexString(color.getRGB()).substring(2));
					break;
				}
				sb.append(" ");
			}
			if (y < height -1) sb.append("\n");
		}
		return sb.toString();
	}
	public String exportMapToString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0 ; y < height ; y++) {
			for (int x = 0 ; x < width ; x++) {
				sb.append(get(x, y).getCharacter());
			}
			if (y < height -1) sb.append("\n");
		}
		return sb.toString();
	}

	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	@Override
	public String toString() {
		return name;
	}

	public TMColoredCharacter getTransparentChar() {
		return transparentChar;
	}

	public void setTransparentChar(TMColoredCharacter transparentChar) {
		this.transparentChar = transparentChar;
	}

}