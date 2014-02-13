package kba.unicodeart;

/**
     *
       	0	1	2	3	4	5	6	7	8	9	A	B	C	D	E	F
U+250x	─	━	│	┃	┄	┅	┆	┇	┈	┉	┊	┋	┌	┍	┎	┏

U+251x	┐	┑	┒	┓	└	┕	┖	┗	┘	┙	┚	┛	├	┝	┞	┟

U+252x	┠	┡	┢	┣	┤	┥	┦	┧	┨	┩	┪	┫	┬	┭	┮	┯

U+253x	┰	┱	┲	┳	┴	┵	┶	┷	┸	┹	┺	┻	┼	┽	┾	┿

U+254x	╀	╁	╂	╃	╄	╅	╆	╇	╈	╉	╊	╋	╌	╍	╎	╏

U+255x	═	║	╒	╓	╔	╕	╖	╗	╘	╙	╚	╛	╜	╝	╞	╟

U+256x	╠	╡	╢	╣	╤	╥	╦	╧	╨	╩	╪	╫	╬	╭	╮	╯

U+257x	╰	╱	╲	╳	╴	╵	╶	╷	╸	╹	╺	╻	╼	╽	╾	╿
     * @author kb
     * ╱
     * |
     * ╳╳╳╳
     * ╳╳╳╳
     */
public class CharacterBoxBrush {

    	public static CharacterBoxBrush ASCII_SIMPLE = new CharacterBoxBrush('+', '-', '+', '|', '+', '-', '+', '|', '+');
    	public static CharacterBoxBrush BOX_ROUND = new CharacterBoxBrush('╭', '─', '╮', '│', '╯', '─', '╰', '│', '┼');
    	public static CharacterBoxBrush BOX_LIGHT = new CharacterBoxBrush('┌', '─', '┐', '│', '┘', '─', '└', '│', '┼');
    	public static CharacterBoxBrush BOX_DOUBLE = new CharacterBoxBrush('╔', '═', '╗', '║', '╝', '═', '╚', '║', '╬' );
    	public static CharacterBoxBrush BOX_FAT = new CharacterBoxBrush('┏', '━', '┓', '┃', '┛', '━', '┗', '┃', '╋');

    	public final char nw;
    	public final char n;
    	public final char ne;
    	public final char e;
    	public final char se;
    	public final char s;
    	public final char sw;
    	public final char w;
    	public final char c;

    	CharacterBoxBrush( char nw, char n, char ne, char e, char se, char s, char sw, char w, char c) {
    		this.nw = nw; 
    		this.n = n; 
    		this.ne = ne;
    		this.e = e; 
    		this.se = se;
    		this.s = s; 
    		this.sw = sw;
    		this.w = w;
    		this.c = c;
    	}
    	
    	/* (non-Javadoc)
    	 * @see java.lang.Object#toString()
    	 */
    	@Override
    	public String toString() {
    		StringBuilder sb = new StringBuilder();
    		return
			sb.append(nw)	.append(n)	.append(ne).append("\n")
			  .append(w)	.append(c)	.append(e).append("\n")
			  .append(sw)	.append(s)	.append(se).toString();
    	}
    	
    	/**
    	 * @param dir direction to draw towards. Draw this.c if null.
    	 * @return the corresponding char
    	 */
    	public char get(CompassDir dir) {
    		if (null != dir) 
    			switch(dir) {
    			case EAST: return e;
    			case NORTH: return n;
    			case NORTHEAST: return ne;
    			case NORTHWEST: return nw;
    			case SOUTH: return s;
    			case SOUTHEAST: return se;
    			case SOUTHWEST: return sw;
    			case WEST: return w;
    			}
    		return c;
    	}
    }