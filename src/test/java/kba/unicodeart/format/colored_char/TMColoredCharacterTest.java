package kba.unicodeart.format.colored_char;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.awt.Color;

import kba.unicodeart.format.colored_char.TMColoredCharacter;

import org.junit.Test;


public class TMColoredCharacterTest {
	
	@Test
	public void testEquals() {
		
		TMColoredCharacter c1 = new TMColoredCharacter('X', Color.WHITE, Color.BLACK);
		TMColoredCharacter c2 = new TMColoredCharacter('.', Color.WHITE, Color.BLACK);
		assertThat(c1, sameInstance(c1));
		assertThat(c1, equalTo(c1));
		assertThat(c1, not(sameInstance(c2)));
		assertThat(c1, not(equalTo(c2)));
	}

}
