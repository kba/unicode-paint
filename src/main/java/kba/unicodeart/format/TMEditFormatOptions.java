package kba.unicodeart.format;

/**
 * Options for serialization / deserialization of a layer / map
 *
 */
public class TMEditFormatOptions {

	/**
	 * How strict to be about parsing
	 *
	 */
	public enum Strictness { 
		/**
		 * Be very permissive
		 */
		VERY_PERMISSIVE,
		/**
		 * Be tolerant to most weird things
		 */
		TOLERANT,
		/**
		 * Be strict
		 */
		STRICT
	}

	/**
	 * The default options:
	 * * do use colors
	 * * do use transparency
	 * * do not use compression
	 * * be very permissive
	 */
	public static final TMEditFormatOptions DEFAULT_OPTIONS = new TMEditFormatOptions(
			true, Strictness.VERY_PERMISSIVE, false, true);
	
	private boolean enableColor;
	private Strictness strictness;
	private boolean useCompression;
	private boolean usingTransparency;

	/**
	 * @param enableColor whether or not to use colors
	 * @param strictness how strict to
	 * @param useCompression
	 * @param usingTransparency 
	 */
	public TMEditFormatOptions(boolean enableColor, Strictness strictness, boolean useCompression, boolean usingTransparency) {
		super();
		this.enableColor = enableColor;
		this.strictness = strictness;
		this.useCompression = useCompression;
	}
	/**
	 * @return whether to use colors
	 */
	public boolean isEnableColor() { return enableColor; }
	/**
	 * @return the strictness
	 */
	public Strictness getStrictness() { return strictness; }
	/**
	 * @return whether to use compression
	 */
	public boolean isUseCompression() { return useCompression; }
	/**
	 * @return whether to use transparency
	 */
	public boolean isUsingTransparency() { return usingTransparency; }

//	public TMEditFormatOptions withEnableColor(boolean enableColor) {
//		return new TMEditFormatOptions(enableColor, strictness, useCompression, usingTransparency);
//	}
//	public TMEditFormatOptions withStrictness(Strictness strictness) {
//		return new TMEditFormatOptions(enableColor, strictness, useCompression, usingTransparency);
//	}
//	public TMEditFormatOptions withCompression(boolean usingCompression) {
//		return new TMEditFormatOptions(enableColor, strictness, usingCompression, usingTransparency);
//	}
//	public TMEditFormatOptions withTransparency(boolean usingTransparency) {
//		return new TMEditFormatOptions(enableColor, strictness, useCompression, usingTransparency);
//	}

}
