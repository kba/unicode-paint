package kba.unicodeart.format;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;


public class TMEditFormatOptionsTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	@Test
	public void testJson() {
		Gson g = new Gson();
		TMEditFormatOptions opt = TMEditFormatOptions.DEFAULT_OPTIONS;
		log.debug(g.toJson(opt));

	}

}
