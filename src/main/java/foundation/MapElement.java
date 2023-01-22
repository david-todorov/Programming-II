package foundation;

public enum MapElement {
	EMPTY, LAND, WATER, START, FINISH;    

	public static final String REPRESENTATIONSTRING = " +.SF";

	public char representation() {
		return  REPRESENTATIONSTRING.charAt(this.ordinal());
	}
	
}
