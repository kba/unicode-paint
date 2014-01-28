package kba.gui.editor;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unicode.UCC_Dingbats;


public class CharPaletteTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	@Test
	public void test() {
		log.debug(CharPalette.CARD_SUITS.toString());
		log.debug(CharPalette.CHESS.toString());
		log.debug(CharPalette.FILL.toString());
		log.debug(CharPalette.SHAPES_BIG.toString());
		logc(UCC_Dingbats.AIRPLANE);
		logc(UCC_Dingbats.BALLOT_X);
		logc(UCC_Dingbats.BLACK_FOUR_POINTED_STAR);
	}
	
	private void logc(char ch) {
		log.debug("" + ch);
	}

}
