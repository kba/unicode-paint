package kba.unicodeart.format;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.googlecode.lanterna.terminal.XTerm8bitIndexedColorUtils;

public class TMEditFormat {
	static Logger log = LoggerFactory.getLogger(TMEditFormat.class);
	
	public static final String CURRENT_VERSION = "0.01";
	public static final int DEFAULT_HEIGHT = 24;
	public static final int DEFAULT_WIDTH = 80;
	public static final char DEFAULT_TRANSPARENT_CHAR = '.';
	

	public enum LayerType {
		ASCII_CHAR("IGNORE"),
		PASSABLE("true"),
//		ROOM(InRoomComponent.class),
		;

		private String defaultValue;
		public String getDefaultValue() { return defaultValue; }
		public LayerType getNext() {
			return values()[(ordinal()+1) % values().length];
		}

		private LayerType(String defaultValue) {
			this.defaultValue = defaultValue;
		}
	}
	
	static transient Gson gson = new Gson();

	private String name;
	private int width = DEFAULT_WIDTH;
	public int getWidth() { return width; }
	private int height = DEFAULT_HEIGHT;
	public int getHeight() { return height; }
	private Map<String,TMEditFormatLayer> layers = new LinkedHashMap<>();
	private TMEditFormatColorPalette palette = TMEditFormatColorPalette.DEFAULT_PALETTE;
	public TMEditFormatColorPalette getPalette() { return palette; }
	private TMEditFormatOptions options = TMEditFormatOptions.DEFAULT_OPTIONS;

	public TMEditFormat(String name, int width, int height) {
		this(name, width, height, TMEditFormatOptions.DEFAULT_OPTIONS);
	}
	public TMEditFormat(String name, int width, int height, TMEditFormatOptions options) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.options = options;
		for (LayerType key : LayerType.values()) {
			TMEditFormatLayer newLayer = new TMEditFormatLayer(key.name(), width, height, key.getDefaultValue());
			String newLayerName = key.name();
			addLayer(newLayerName, newLayer);
		}
	}
	public TMEditFormat(String name, int width, int height, TMEditFormatOptions options, Map<String, TMEditFormatLayer> layers) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.layers = layers;
		this.options = options;
	}
	
	public static class Builder {
		public String name;
		public int width = 80;
		public int height = 24;
		public TMEditFormatOptions options = TMEditFormatOptions.DEFAULT_OPTIONS;
		public Map<String,TMEditFormatLayer> layers = new HashMap<>();
		public TMEditFormat build() {
			TMEditFormat newFmt = new TMEditFormat(name, width, height, options, layers);
			return newFmt;
		}
	}

	/*
	 * Serialization / Deserialization
	 */
	public String exportToString() {
		HashMap<String, Object> metadata = new HashMap<>();
		metadata.put("name", name);
		metadata.put("options", options);
//		Date date = new java.util.Date();
//		metadata.put("last-modified", date.toGMTString());
		StringBuilder sb = new StringBuilder();
		sb.append("Tilemap v" + CURRENT_VERSION);
		sb.append("\n");
		sb.append(gson.toJson(metadata));
		sb.append("\n");
		sb.append(width);
		sb.append("\n");
		sb.append(height);
		sb.append("\n");
		for (String layerName : layers.keySet()) {
			TMEditFormatLayer curLayer = layers.get(layerName);
			sb.append(layerName);
			sb.append(" DEFAULT: ");
			sb.append(curLayer.getDefaultLegendValue());
			sb.append("\n");
			sb.append(layers.get(layerName).exportMapToString());
			sb.append("\n");
			if (options.isEnableColor()) {
				sb.append(ParseState.COLOR_FG);
				sb.append("\n");
				sb.append(layers.get(layerName).exportFgToString(options.getColorType()));
				sb.append("\n");
				sb.append(ParseState.COLOR_BG);
				sb.append("\n");
				sb.append(layers.get(layerName).exportBgToString(options.getColorType()));
				sb.append("\n");
			}
			sb.append(ParseState.LEGEND);
			sb.append("\n");
			sb.append(layers.get(layerName).exportLegendToString());
			sb.append("\n");
		}
		return sb.toString();
	}
	private enum ParseState {
		MAGIC_NUMBER,
		METADATA,
		WIDTH,
		HEIGHT,
		LAYER_NAME,
		COLOR_FG,
		COLOR_BG,
		LAYER_MAP,
		LEGEND_LINE,
		LEGEND,
	}
	public static TMEditFormat fromString(String exportToString) {
		String curLayerName = null;
		StringBuilder curLayerStr = new StringBuilder();
		TMEditFormatLayer curLayer = null;
		ParseState pState = ParseState.MAGIC_NUMBER;
		Builder builder = new TMEditFormat.Builder();
		for (String line : exportToString.split("[\n\r]")) {
			Logger slog = LoggerFactory.getLogger(TMEditFormat.class);
			slog.debug("LINE: " + line);
//			slog.debug("STATE: " + pState);
			switch(pState) {
			case MAGIC_NUMBER:
				pState = ParseState.METADATA;
				break;
			case METADATA:
				@SuppressWarnings("unchecked")
				Map<String,String> parsedMetadata = gson.fromJson(line, Map.class);
				builder.name = parsedMetadata.get("name");
				builder.options = gson.fromJson(gson.toJson(parsedMetadata.get("options")), TMEditFormatOptions.class);
				pState = ParseState.WIDTH;
				break;
			case WIDTH:
				builder.width = Integer.parseInt(line);
				pState = ParseState.HEIGHT;
				break;
			case HEIGHT:
				builder.height = Integer.parseInt(line);
				pState = ParseState.LAYER_NAME;
				break;
			case LAYER_NAME:
				String[] segs = line.split(" DEFAULT: ", 2);
				curLayerName = segs[0];
				curLayer = new TMEditFormatLayer(curLayerName, builder.width, builder.height, segs[1]);
				pState = ParseState.LAYER_MAP;
				break;
			case LAYER_MAP:
				if (line.equals(ParseState.LEGEND.name()) || line.equals(ParseState.COLOR_FG.name())){
					for (int x = 0 ; x < builder.width ; x ++) {
						for (int y = 0 ; y < builder.height ; y ++) {
							curLayer.get(x, y).setCharacter(curLayerStr.charAt(builder.width * y + x));
						}
					}
					builder.layers.put(curLayerName, curLayer);
					curLayerStr.setLength(0);
					if (line.equals(ParseState.LEGEND.name())) pState = ParseState.LEGEND;
					else if (line.equals(ParseState.COLOR_FG.name())) pState = ParseState.COLOR_FG;
				} else {
					curLayerStr.append(line);
				}
				break;
			case LEGEND:
				// TODO
				pState = ParseState.LAYER_NAME;
				break;
			case COLOR_FG:
				if (line.equals(ParseState.COLOR_BG.name())) {
					parseColorStr(pState, curLayerStr.toString(), curLayer, builder.options);
					curLayerStr.setLength(0);
					pState = ParseState.COLOR_BG;
				} else {
					curLayerStr.append(line);
				}
				break;
			case COLOR_BG:
				if (line.equals(ParseState.LEGEND.name())) {
					parseColorStr(pState, curLayerStr.toString(), curLayer, builder.options);
					curLayerStr.setLength(0);
					pState = ParseState.LEGEND;
				} else {
					curLayerStr.append(line);
				}
			default:
				break;
			}
		}
		return builder.build();
	}
	public static void parseColorStr(ParseState pState, String layerStr, TMEditFormatLayer curLayer, TMEditFormatOptions curOptions) {
		int y = 0;
		int x = 0;
		for (String colorValue : layerStr.split("\\s+")) {
			log.debug("" + colorValue);
			Color color = null;
			switch(curOptions.getColorType()) {
			case ANSI:
			case ANSI256:
				color = XTerm8bitIndexedColorUtils.getAWTColor(Integer.parseInt(colorValue));
				break;
			case RGB:
				color = Color.decode("0x" + colorValue.substring(1));
				break;
			}
			log.debug("X/Y: {}/{}", x, y);
			if (pState.equals(ParseState.COLOR_FG)) {
				curLayer.get(x, y).setFg(color);
			} else if (pState.equals(ParseState.COLOR_BG)) {
				curLayer.get(x, y).setBg(color);
			}
			x += 1;
			if (x % curLayer.getWidth() == 0) {
				x = 0;
				y += 1;
			}
		}
	}
	
	/*
	 * Layers
	 */
	public TMEditFormatLayer getLayerByName(String layerName) {
		return layers.get(layerName);
	}
	public void addLayer(String layerName) {
		TMEditFormatLayer newLayer = new TMEditFormatLayer(layerName, width, height, "IGNORE");
		addLayer(layerName, newLayer);
	}
	public void addLayer(String layerName, TMEditFormatLayer layer) {
		this.layers.put(layerName, layer);
	}
	public String getNextLayerName(String layerName) {
		boolean returnNext = false;
		String retLayerName = null;
		for (String curLayerName : layers.keySet()) {
			if (returnNext) {
				retLayerName = curLayerName;
				break;
			} else {
				if (curLayerName.equals(layerName))
					returnNext = true;
			}
		}
		return retLayerName;
	}
	public TMEditFormatLayer getLayerIndex(int idx) {
		int curIdx = 0;
		for (TMEditFormatLayer curLayer : layers.values()) {
			if (idx == curIdx) return curLayer;
		}
		return null;
	}
	public int getIndexByLayer(TMEditFormatLayer layer) {
		int idx = 0;
		for (TMEditFormatLayer curLayer : layers.values()) {
			if (layer == curLayer)
				return idx;
			idx++;
		}
		return -1;
	}
//	private String getNameOfLayer(TMEditFormatLayer layer) {
//		for (Entry<String, TMEditFormatLayer> entry : layers.entrySet()) {
//			if (entry.getValue() == layer) {
//				return entry.getKey();
//			}
//		}
//		return null;
//	}
	public TMEditFormatLayer getNextLayer(TMEditFormatLayer needle) {
		boolean returnNext = false;
		TMEditFormatLayer retLayer = null;
		for (TMEditFormatLayer curLayer : getLayers()) {
			if (returnNext) {
				retLayer = curLayer;
				break;
			} else {
				if (curLayer == needle)
					returnNext = true;
			}
		}
		return retLayer;
	}
	public Collection<TMEditFormatLayer> getLayers() {
		return layers.values();
	}
	public String getName() { return name; }
}
