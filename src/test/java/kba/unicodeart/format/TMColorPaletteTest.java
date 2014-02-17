package kba.unicodeart.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import kba.unicodeart.BaseTest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TMColorPaletteTest extends BaseTest {
	
	private static final Logger log = LoggerFactory.getLogger(TMColorPaletteTest.class);
	
	@Test
	public void testTerminalColors() {
		List<Color> colors88 = StandardPalette.TERM_88_COLORS.palette().getColorIndex();
		assertEquals(72, colors88.size());
		List<Color> colors256 = StandardPalette.TERM_256_COLORS.palette().getColorIndex();
		assertEquals(240, colors256.size());
		List<Character> defaultCharIndex = StandardCharacterIndex.DEFAULT.index();
		log.debug("fnork" + defaultCharIndex);
		assertTrue(defaultCharIndex.size() >= 256);
	}

	@Test
	public void testSerialize() throws Exception {
		TMColorPalette pal = StandardPalette.DEFAULT.palette();
		assertNotNull(pal);
		StringWriter sw = new StringWriter();
		XMLStreamWriter xml = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);
		pal.writeXML(xml);
		xml.close();
		sw.close();
		log.debug(sw.toString());
//		log.debug(pal.serialize());
	}
	
	@Test
	public void testDuplicates() {
		TMColorPalette pal = StandardPalette.DEFAULT.palette();
		for (int i = 0 ; i < 100 ; i++) {
			log.debug(i + " : " + pal.getColorByIndex(i));
		}
	}

	@Test
	public void testReadXML() throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<Palette ref=\"DEFAULT\"></Palette>");
		StringReader sr = new StringReader(sb.toString());
		XMLStreamReader xml = xmlInputFactory.createXMLStreamReader(sr);
		xml.next();
		TMColorPalette pal = TMColorPalette.readXML(xml);
	}

}
