package kba.unicodeart.algo;

import java.awt.Point;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BresenhamLineTest {
	
	private static final Logger log = LoggerFactory.getLogger(BresenhamLineTest.class);

	@Test
	public void testDrawLine() throws Exception {
		List<Point> ret = BresenhamLine.drawLine(4, 1, 0, 3);
		for (Point p : ret) {
			log.debug("{}", p);
		}
	}

}
