package kba.unicodeart;

public enum CompassDir {
	NORTH(		 0, -1),
	EAST(		+1,  0),
	SOUTH(		 0, +1),
	WEST(		-1,  0),
	NORTHEAST(	-1, -1),
	SOUTHEAST(	+1, +1),
	SOUTHWEST(	-1, +1),
	NORTHWEST(	+1, -1)
	;
	
	private int deltaX = 0;
	public int getDeltaX() { return this.deltaX; }
	private int deltaY = 0;
	public int getDeltaY() { return this.deltaY; }
	
	CompassDir(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
}