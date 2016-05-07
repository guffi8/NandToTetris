import java.util.Hashtable;

public abstract class Code {
	
	private static final Hashtable<String, String> destTable = new Hashtable<String, String>() {
		private static final long serialVersionUID = 1L;

	{
		put("null", "000");
		put("M", "001");
		put("D", "010");
		put("MD", "011");
		put("A", "100");
		put("AM", "101");
		put("AD", "110");
		put("AMD", "111");
	}};
	
	private static final Hashtable<String, String> compTable = new Hashtable<String, String>() {
		private static final long serialVersionUID = 1L;
	{
		// when a = 0
		put("0", "110101010");
		put("1", "110111111");
		put("-1", "110111010");
		put("D", "110001100");
		put("A", "110110000");
		put("!D", "110001101");
		put("!A", "110110001");
		put("-D", "110001111");
		put("-A", "110110011");
		put("D+1", "110011111"); 
		put("1+D", "110011111");
		put("A+1", "110110111"); 
		put("1+A", "110110111");
		put("D-1", "110001110");
		put("A-1", "110110010");
		put("D+A", "110000010"); 
		put("A+D", "110000010");
		put("D-A", "110010011");
		put("A-D", "110000111");
		put("D&A", "110000000"); 
		put("D&A", "110000000");
		put("D|A", "110010101"); 
		put("A|D", "110010101");
		
		// when a = 1
		put("M", "111110000");
		put("!M", "111110001");
		put("-M", "111110011");
		put("M+1", "111110111"); 
		put("1+M", "111110111");
		put("M-1", "111110010");
		put("D+M", "111000010"); 
		put("M+D", "111000010");
		put("D-M", "111010011");
		put("M-D", "111000111");
		put("D&M", "111000000"); 
		put("M&D", "111000000");
		put("D|M", "111010101"); 
		put("M|D", "111010101");
		
		// dealing with shifting
		put("D<<", "010110000");
		put("A<<", "010100000");
		put("M<<", "011100000");
		put("D>>", "010010000");
		put("A>>", "010000000");
		put("M>>", "011000000");
		
	}};
	
	private static final Hashtable<String, String> jumpTable = new Hashtable<String, String>() {
		private static final long serialVersionUID = 1L;
	{
		put("null", "000");
		put("JGT", "001");
		put("JEQ", "010");
		put("JGE", "011");
		put("JLT", "100");
		put("JNE", "101");
		put("JLE", "110");
		put("JMP", "111");
	}};

	/**
	 * Returns the binary code of the dest mnemonic.
	 * @param dest The dest.
	 * @return The binary code of the dest.	
	 */
	public static String dest(String dest) {
		return destTable.get(dest);
	}

	/**
	 * Returns the binary code of the comp mnemonic.
	 * @param dest The comp.
	 * @return The binary code of the comp.	
	 */
	public static String comp(String comp) {
		return compTable.get(comp);
	}

	/**
	 * Returns the binary code of the jump mnemonic.
	 * @param dest The jump.
	 * @return The binary code of the jump.	
	 */
	public static String jump(String jump) {
		return jumpTable.get(jump);
	}

}
