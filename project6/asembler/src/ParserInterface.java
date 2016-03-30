
public interface ParserInterface {
	
	enum CommandType {A_COMMAND, C_COMMAND, L_COMMAND};

	/**
	 * Are there more commands in the input?
	 * @return True if there is more commands
	 */
	public boolean hasMoreCommands();
	/**
	 * Reads the next command from the input and makes it the current command. 
	 * Should be called only if hasMoreCommands() is true. 
	 * Initially there is no current command.
	 */
	public void advance();
	
	public boolean isAcommand();
	
	public boolean isCcommand();
	
	public boolean isLcommand();
//	/**
//	 * 
//	 * @return the type of the current command : 
//	 * A_COMMAND for @Xxx where Xxx is either a symbol or a decimal number.
//	 * C_COMMAND for dest=comp;jump.
//	 * L_COMMAND (actually, pseudocommand) for (Xxx) where Xxx is a symbol.
//	 */
//	public CommandType commandType();
	/**
	 * 
	 * @return the symbol or decimal Xxx of the current command @Xxx or (Xxx).
	 *     Should be called only when commandType() is A_COMMAND or L_COMMAND.
	 */
	public String symbol();
	/**
	 * 
	 * @return  the dest mnemonic in the current C-command (8 possibilities).
	 *          Should be called only when commandType() is C_COMMAND.
	 */
	public String dest();
	/**
	 * 
	 * @return the comp mnemonic in the current C-command (28 possibilities). 
	 * 		   Should be called only when commandType() is C_COMMAND.
	 */
	public String comp();
	/**
	 * 
	 * @return the jump mnemonic in the current C-command (8 possibilities). 
	 * 		   Should be called only when commandType() is C_COMMAND.
	 */
	public String jump();
}
