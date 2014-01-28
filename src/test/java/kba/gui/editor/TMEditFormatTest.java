package kba.gui.editor;



public class TMEditFormatTest {
	
//	Logger log = LoggerFactory.getLogger(getClass().getName());
//	
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
//	
//	@Test
//	public void testExportLayer() {
//		TMEditFormat fmt = new TMEditFormat("test", 2, 2);
//		TMEditFormatLayer asciiLayer = fmt.getLayer(LayerType.ASCII_CHAR);
//		asciiLayer.set(1, 1, '#');
//		assertEquals("..\n.#", asciiLayer.exportMapToString());
//		assertEquals("{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportLegendToString());
//		assertEquals("..\n.#\nLEGEND\n{\"#\":\"IGNORE\",\".\":\"IGNORE\"}",  asciiLayer.exportToString());
//	}
//	
//	@Test
//	public void testExport() {
//		TMEditFormat fmt = new TMEditFormat("test", 2, 2);
//		log.debug(fmt.exportToString());
//	}
//	
//	@Test
//	public void testImport() {
//		TMEditFormat fmtOld = new TMEditFormat("test", 2, 2);
//		TMEditFormatLayer asciiLayer = fmtOld.getLayer(LayerType.ASCII_CHAR);
//		asciiLayer.set(1, 1, '#');
//		TMEditFormat fmtNew = TMEditFormat.fromString(fmtOld.exportToString());
//		log.debug(fmtNew.exportToString());
//		assertEquals(fmtOld.exportToString(), fmtNew.exportToString());
//	}
}