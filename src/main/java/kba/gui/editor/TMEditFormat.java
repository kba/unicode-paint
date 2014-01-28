package kba.gui.editor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class TMEditFormat {

	private static final String CURRENT_VERSION = "0.01";
	private static final int DEFAULT_HEIGHT = 24;
	private static final int DEFAULT_WIDTH = 80;
	static final char DEFAULT_CHAR = '.';

	Logger log = LoggerFactory.getLogger(getClass().getName());
	static Gson gson = new Gson();

	private String name;
	private int width = DEFAULT_WIDTH;
	public int getWidth() { return width; }
	private int height = DEFAULT_HEIGHT;
	public int getHeight() { return height; }
	private Map<LayerType,TMEditFormatLayer> layers = new HashMap<>();
	public TMEditFormatLayer getLayer(LayerType layerType) {
		return layers.get(layerType);
	}
	public TMEditFormat(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		for (LayerType key : LayerType.values()) {
			this.layers.put(key, new TMEditFormatLayer(width, height, key.getDefaultValue()));
		}
	}
	
	public TMEditFormat(String name, int width, int height, Map<LayerType, TMEditFormatLayer> layers) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.layers = layers;
	}
	
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

	public String exportToString() {
		HashMap<String, String> metadata = new HashMap<>();
		metadata.put("name", name);
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
		for (LayerType layerType : LayerType.values()) {
			sb.append(layerType.name());
			sb.append("\n");
			sb.append(layers.get(layerType).exportToString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public static class Builder {
		public String name;
		public int width = 80;
		public int height = 24;
		public Map<LayerType,TMEditFormatLayer> layers = new HashMap<>();
		public TMEditFormat build() {
			TMEditFormat newFmt = new TMEditFormat(name, width, height, layers);
			return newFmt;
		}
	}

	private enum ParseState {
		MAGIC_NUMBER,
		METADATA,
		WIDTH,
		HEIGHT,
		LAYER_NAME,
		LAYER_MAP,
		LEGEND_LINE,
		LEGEND,
	}
	public static TMEditFormat fromString(String exportToString) {
		LayerType curLayerType = null;
		String curLayerStr = "";
		ParseState pState = ParseState.MAGIC_NUMBER;
		Builder builder = new TMEditFormat.Builder();
		for (String line : exportToString.split("[\n\r]")) {
			Logger slog = LoggerFactory.getLogger(TMEditFormat.class);
			slog.debug("LINE: " + line);
			slog.debug("STATE: " + pState);
			switch(pState) {
			case MAGIC_NUMBER:
				pState = ParseState.METADATA;
				break;
			case METADATA:
				@SuppressWarnings("unchecked")
				Map<String,String> parsedMetadata = gson.fromJson(line, Map.class);
				builder.name = parsedMetadata.get("name");
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
				curLayerStr = "";
				curLayerType = LayerType.valueOf(line);
				pState = ParseState.LAYER_MAP;
				break;
			case LAYER_MAP:
				if (line.equals("LEGEND")){
					TMEditFormatLayer layer = new TMEditFormatLayer(builder.width, builder.height, curLayerType.defaultValue);
					for (int x = 0 ; x < builder.width ; x ++) {
						for (int y = 0 ; y < builder.height ; y ++) {
							layer.set(x, y, curLayerStr.charAt(builder.width * y + x));
						}
					}
					builder.layers.put(curLayerType, layer);
					pState = ParseState.LEGEND;
				} else {
					curLayerStr += line;
				}
				break;
			case LEGEND:
				// TODO
				pState = ParseState.LAYER_NAME;
				break;
			default:
				break;
			}
		}
		return builder.build();
	}
}
