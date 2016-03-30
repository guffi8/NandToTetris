
public enum IdentifierKind {
	STATIC("static"), FIELD("field"), ARG("argument"), VAR("local");
	
	private String kindName;
	
	private IdentifierKind(String kind) {
		this.kindName = kind;
	}
	
	public static IdentifierKind getKind(String kind) {
		for (IdentifierKind element : IdentifierKind.values()) {
			if(element.kindName.equals(kind)) {
				return element;
			}
		}
		return null;
	}
	
	public static String getValue(IdentifierKind kind) {
		return kind.kindName;
	}

}
