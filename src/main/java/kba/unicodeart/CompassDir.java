package kba.unicodeart;

/**
 * Constants for the X/Y delta of the eight major compass directions
 * @author kba
 *
 */
public enum CompassDir {
	/**
	 * One up
	 */
	NORTH(0, -1),
	/**
	 * One right
	 */
	EAST(+1, 0),
	/**
	 * One down
	 */
	SOUTH(0, +1),
	/**
	 * One left
	 */
	WEST(-1, 0),
	/**
	 * One up, one right
	 */
	NORTHEAST(-1, -1),
	/**
	 * One down, One right
	 */
	SOUTHEAST(+1, +1),
	/**
	 * One down, one left
	 */
	SOUTHWEST(-1, +1),
	/**
	 * One up, one left
	 */
	NORTHWEST(+1, -1);

	private int deltaX = 0;
	private int deltaY = 0;

	/**
	 * @return the x delta
	 */
	public int getDeltaX() {
		return this.deltaX;
	}

	/**
	 * @return the y delta
	 */
	public int getDeltaY() {
		return this.deltaY;
	}

	/**
	 * @param deltaX
	 *            the delta for the X axis
	 * @param deltaY
	 *            the delta for the Y axis
	 */
	CompassDir(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

}