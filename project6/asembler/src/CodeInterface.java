
public interface CodeInterface {
	
	/**
	 * Returns the binary code of the dest mnemonic.
	 * @param dest The dest.
	 * @return The binary code of the dest.	
	 */
	public String dest(String dest);
	
	/**
	 * Returns the binary code of the comp mnemonic.
	 * @param dest The comp.
	 * @return The binary code of the comp.	
	 */
	public String comp(String comp);
	
	/**
	 * Returns the binary code of the jump mnemonic.
	 * @param dest The jump.
	 * @return The binary code of the jump.	
	 */
	public String jump(String jump);

}
