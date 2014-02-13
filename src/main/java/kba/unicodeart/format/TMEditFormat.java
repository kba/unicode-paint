package kba.unicodeart.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

public class TMEditFormat {

	static Logger log = LoggerFactory.getLogger(TMEditFormat.class);
	public static final String CURRENT_VERSION = "0.01";
	public static final int DEFAULT_HEIGHT = 24;
	public static final int DEFAULT_WIDTH = 80;
	public static final char DEFAULT_TRANSPARENT_CHAR = '.';

	static transient Gson gson = new Gson();

	private Map<String, TMEditFormatLayer> layers = new LinkedHashMap<>();

	private int height = DEFAULT_HEIGHT;
	private int width = DEFAULT_WIDTH;
	private TMColorPalette palette = TMColorPalette.DEFAULT_PALETTE;
	private TMEditFormatOptions options = TMEditFormatOptions.DEFAULT_OPTIONS;

	private String name;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public TMColorPalette getPalette() {
		return palette;
	}

	public TMEditFormat(String name, int width, int height) {
		this(name, width, height, TMEditFormatOptions.DEFAULT_OPTIONS);
	}

	public TMEditFormat(String name, int width, int height, TMEditFormatOptions options) {
		this(name, width, height, options, new HashMap<String, TMEditFormatLayer>());
//		for (LayerType key : LayerType.values()) {
//			TMEditFormatLayer newLayer = new TMEditFormatLayer(key.name(), width, height,
//					key.getDefaultValue());
//			String newLayerName = key.name();
//			addLayer(newLayerName, newLayer);
//		}
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
		public Map<String, TMEditFormatLayer> layers = new HashMap<>();

		public TMEditFormat build() {
			TMEditFormat newFmt = new TMEditFormat(name, width, height, options, layers);
			return newFmt;
		}
	}

	/*
	 * Serialization / Deserialization
	 */
	public void writeXML(XMLStreamWriter xml) throws XMLStreamException {
		xml.writeStartDocument();
		xml.writeCharacters("\n");
		xml.writeStartElement("tilemap");
		xml.writeAttribute("width", String.valueOf(getWidth()));
		xml.writeAttribute("height", String.valueOf(getHeight()));
		xml.writeStartElement("layers");
		xml.writeCharacters("\n");
		for (TMEditFormatLayer layer : getLayers()) {
			layer.writeXML(xml);
		}
		xml.writeEndElement();
		xml.writeEndElement();
	}
	
	// TODO fromXML

	/*
	 * ==> Layers <==
	 */
	public TMEditFormatLayer getLayerByName(String layerName) {
		return layers.get(layerName);
	}

	public void addLayer(String layerName) {
		TMEditFormatLayer newLayer = new TMEditFormatLayer(layerName, width, height, "IGNORE");
		addLayer(layerName, newLayer);
	}

	public void addLayer(String layerName, TMEditFormatLayer layer) {
		Preconditions.checkArgument( ! this.layers.containsKey(layerName), "Layer with this name already exists.");
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
			if (idx == curIdx)
				return curLayer;
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

	public String getName() {
		return name;
	}
}
