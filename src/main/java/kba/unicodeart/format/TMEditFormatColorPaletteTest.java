package kba.unicodeart.format;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TMEditFormatColorPaletteTest {
	
	private static final Logger log = LoggerFactory.getLogger(TMEditFormatColorPaletteTest.class);
	
	@Test
	public void testTerminalColors() {
		List<Color> colors88 = TMEditFormatColorPalette.TERM_88_COLORS;
		assertEquals(72, colors88.size());
		List<Color> colors256 = TMEditFormatColorPalette.TERM_256_COLORS;
		assertEquals(240, colors256.size());
		List<Character> defaultCharIndex = TMEditFormatColorPalette.DEFAULT_CHARACTER_INDEX;
		log.debug("fnork" + defaultCharIndex);
		assertTrue(defaultCharIndex.size() >= 256);
	}

	@Test
	public void testSerialize() throws Exception {
		TMEditFormatColorPalette pal = TMEditFormatColorPalette.DEFAULT_PALETTE;
		assertNotNull(pal);
		log.debug(pal.serialize());
	}
	

}
