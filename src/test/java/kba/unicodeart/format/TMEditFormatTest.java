package kba.unicodeart.format;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import kba.unicodeart.format.TMEditFormat.LayerType;
import kba.unicodeart.format.TMEditFormatOptions.ColorType;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TMEditFormatTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
//	@Test
//	public void test() {
//		TMEditFormat fmt = new TMEditFormat("test", 5, 5);
//		fmt.getLayer(LayerType.ASCII_CHAR).set(3, 3, '#');
//		assertEquals('#', fmt.getLayer(LayerType.ASCII_CHAR).get(3, 3));
//		TileMap tileMap = fmt.toTileMap();
//		StringRenderer renderer = new StringRenderer(globalState, tileMap);
//		renderer.renderTileMap();
//		log.debug(renderer.toString());
//	}
	
	@Test
	public void testExportLayer() {
		TMEditFormat fmt = new TMEditFormat("test", 2, 2);
		TMEditFormatLayer asciiLayer = fmt.getLayerByName(LayerType.ASCII_CHAR.name());
		asciiLayer.set(1, 1, '#');
		assertEquals("..\n.#", asciiLayer.exportMapToString());
		assertEquals("{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportLegendToString());
		assertEquals("..\n.#\nLEGEND\n{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportToString());
	}
	
	@Test
	public void testExport() {
		TMEditFormat fmt = new TMEditFormat("test", 2, 2);
		log.debug(fmt.exportToString());
		log.debug(Color.white.toString());
	}
	
	@Test
	public void testFromString() {
		TMEditFormat fmtOld = new TMEditFormat("test", 2, 2, TMEditFormatOptions.DEFAULT_OPTIONS.withColorType(ColorType.RGB));
		TMEditFormatLayer asciiLayer = fmtOld.getLayerByName(LayerType.PASSABLE.name());
		asciiLayer.set(1, 1, '#');
		asciiLayer.get(1, 1).setFg(Color.BLUE);
		log.debug(fmtOld.exportToString());
		TMEditFormat fmtNew = TMEditFormat.fromString(fmtOld.exportToString());
		log.debug(fmtNew.exportToString());
//		assertEquals(fmtOld.exportToString(), fmtNew.exportToString());
	}

	@Test
	public void testExportColor() {
		TMEditFormat fmt = new TMEditFormat("test", 20, 20);
		TMEditFormatLayer asciiLayer = fmt.getLayerByName(LayerType.ASCII_CHAR.name());
		asciiLayer.set(1, 1, '#');
//		log.debug(asciiLayer.exportFgToString(ColorType.ANSI256));
//		assertEquals("..\n.#", asciiLayer.exportMapToString());
//		assertEquals("{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportLegendToString());
//		assertEquals("..\n.#\nLEGEND\n{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportToString());
	}
}