/**
 * The enum of variable segment
 */
public enum Segment {
	
	CONST("constant"), ARG("argument"), LOCAL("local"), 
	STATIC("static"), THIS("this"), THAT("that"),
	POINTER("pointer"), TEMP("temp");
	
	private String segment;
	
	private Segment(String segment) {
		this.segment = segment;
	}
	
	public static Segment getSegment(String seg) {
		for (Segment element : Segment.values()) {
			if(element.segment.equals(seg)) {
				return element;
			}
		}
		return null;
	}
	
	public static String getValue(Segment seg) {
		return seg.segment;
	}
	
}
