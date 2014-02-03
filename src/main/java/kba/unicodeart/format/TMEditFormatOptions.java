package kba.unicodeart.format;

public class TMEditFormatOptions {

	public enum ColorType { ANSI, ANSI256, RGB }
	public enum Strictness { VERY_PERMISSIVE, TOLERANT, STRICT }

	public static final TMEditFormatOptions DEFAULT_OPTIONS = new TMEditFormatOptions(
			true, Strictness.VERY_PERMISSIVE, ColorType.ANSI, false);
	
	private boolean enableColor;
	private Strictness strictness;
	private ColorType colorType;
	private boolean useCompression;

	public TMEditFormatOptions(boolean enableColor, Strictness strictness,
			ColorType colorType, boolean useCompression) {
		super();
		this.enableColor = enableColor;
		this.strictness = strictness;
		this.colorType = colorType;
		this.useCompression = useCompression;
	}
	public boolean isEnableColor() { return enableColor; }
	public Strictness getStrictness() { return strictness; }
	public ColorType getColorType() { return colorType; }
	public boolean isUseCompression() { return useCompression; }
	
	public TMEditFormatOptions withEnableColor(boolean enableColor) {
		return new TMEditFormatOptions(enableColor, strictness, colorType, useCompression);
	}
	public TMEditFormatOptions withStrictness(Strictness strictness) {
		return new TMEditFormatOptions(enableColor, strictness, colorType, useCompression);
	}
	public TMEditFormatOptions withColorType(ColorType colorType) {
		return new TMEditFormatOptions(enableColor, strictness, colorType, useCompression);
	}
	public TMEditFormatOptions withUseCompression(boolean useCompression) {
		return new TMEditFormatOptions(enableColor, strictness, colorType, useCompression);
	}

}
