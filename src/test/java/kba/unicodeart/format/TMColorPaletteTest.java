package kba.unicodeart.format;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TMColorPaletteTest {
	
	private static final Logger log = LoggerFactory.getLogger(TMColorPaletteTest.class);
	
	@Test
	public void testTerminalColors() {
		List<Color> colors88 = TMColorPalette.TERM_88_COLORS;
		assertEquals(72, colors88.size());
		List<Color> colors256 = TMColorPalette.TERM_256_COLORS;
		assertEquals(240, colors256.size());
		List<Character> defaultCharIndex = TMColorPalette.DEFAULT_CHARACTER_INDEX;
		log.debug("fnork" + defaultCharIndex);
		assertTrue(defaultCharIndex.size() >= 256);
	}

	@Test
	public void testSerialize() throws Exception {
		TMColorPalette pal = TMColorPalette.DEFAULT_PALETTE;
		assertNotNull(pal);
		log.debug(pal.serialize());
	}
	
	@Test
	public void testDuplicates() {
		TMColorPalette pal = TMColorPalette.DEFAULT_PALETTE;
		for (int i = 0 ; i < 100 ; i++) {
			log.debug(i + " : " + pal.getColorByIndex(i));
		}
	}

}
