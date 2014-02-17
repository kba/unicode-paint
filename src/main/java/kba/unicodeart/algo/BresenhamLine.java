package kba.unicodeart.algo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BresenhamLine {
	
	/**
	 * @param x0 
	 * @param y0 
	 * @param x1 
	 * @param y1 
	 * @see <a href="http://en.wikipedia.org/wiki/Bresenham's_line_algorithm#Optimization">Wikipedia</a>
	 * @return the list of Points between the positions
	 */
	public static List<Point> drawLine(int x0, int y0, int x1, int y1) {
		boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
		int swap;
		if (steep) {
			swap = y0;
			y0 = x0;
			x0 = swap;
			swap = y1;
			y1 = x1;
			x1 = swap;
		}
		if (x0 > x1) {
			swap = x1;
			x1 = x0;
			x0 = swap;
			swap = y1;
			y1 = y0;
			y0 = swap;
		}
		int deltaX = x1 - x0;
		int deltaY = Math.abs(y1 - y0);
		int error = deltaX / 2;

		ArrayList<Point> ret = new ArrayList<>();
		
		int yStep = (y0 < y1) ? +1 : -1;
		int curY = y0;
		for (int curX = x0 ; curX <= x1 ; curX++) {
			ret.add(steep ? new Point(curY, curX) : new Point(curX, curY));
			error -= deltaY;
			if (error < 0) {
				curY = curY + yStep;
				error += deltaX;
			}
		}
		return ret;
	}

}
