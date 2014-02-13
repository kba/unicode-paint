package kba.unicodeart.format;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TMEditFormatTest {
	
	private static final String TEST_MAP = "testMap";
	private static final String TEST_LAYER = "testLayer";
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
//	@Test
//	public void test() {
//		TMEditFormat fmt = new TMEditFormat("testMap", 5, 5);
//		fmt.getLayer(LayerType.ASCII_CHAR).set(3, 3, '#');
//		assertEquals('#', fmt.getLayer(LayerType.ASCII_CHAR).get(3, 3));
//		TileMap tileMap = fmt.toTileMap();
//		StringRenderer renderer = new StringRenderer(globalState, tileMap);
//		renderer.renderTileMap();
//		log.debug(renderer.toString());
//	}
	
//	@Test
//	public void testExportLayer() {
//		TMEditFormat fmt = new TMEditFormat(TEST_MAP, 2, 2);
//		fmt.addLayer(TEST_LAYER);
//		TMEditFormatLayer asciiLayer = fmt.getLayerByName(TEST_LAYER);
//		asciiLayer.set(1, 1, '#');
//		assertEquals("..\n.#", asciiLayer.exportMapToString());
//		assertEquals("{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportLegendToString());
//		assertEquals("..\n.#\nLEGEND\n{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportToString());
//	}
	
//	@Test
//	public void testExport() {
//		TMEditFormat fmt = new TMEditFormat(TEST_MAP, 2, 2);
//		log.debug(fmt.exportToString());
//		log.debug(Color.white.toString());
//	}
	
//	@Test
//	public void testFromString() {
//		TMEditFormat fmtOld = new TMEditFormat(TEST_MAP, 2, 2);
//		fmtOld.addLayer(TEST_LAYER);
//		TMEditFormatLayer asciiLayer = fmtOld.getLayerByName(TEST_LAYER);
//		asciiLayer.set(1, 1, '#');
//		asciiLayer.get(1, 1).setFg(Color.BLUE);
//		log.debug(fmtOld.exportToString());
//		TMEditFormat fmtNew = TMEditFormat.fromString(fmtOld.exportToString());
//		log.debug(fmtNew.exportToString());
////		assertEquals(fmtOld.exportToString(), fmtNew.exportToString());
//	}

//	@Test
//	public void testExportColor() {
//		TMEditFormat fmt = new TMEditFormat(TEST_MAP, 20, 20);
//		fmt.addLayer(TEST_LAYER);
//		TMEditFormatLayer asciiLayer = fmt.getLayerByName(TEST_LAYER);
//		asciiLayer.set(1, 1, '#');
////		log.debug(asciiLayer.exportFgToString(ColorType.ANSI256));
////		assertEquals("..\n.#", asciiLayer.exportMapToString());
////		assertEquals("{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportLegendToString());
////		assertEquals("..\n.#\nLEGEND\n{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportToString());
//	}

	@Test
	public void testWriteXML() throws Exception {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
	    XMLStreamWriter writer = factory.createXMLStreamWriter(System.out);
		TMEditFormat fmt = new TMEditFormat(TEST_MAP, 20, 20);
		fmt.addLayer(TEST_LAYER);
		fmt.writeXML(writer);
		writer.close();
	}
}