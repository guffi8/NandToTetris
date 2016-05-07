
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
	
	/**
	 * Check if the current line is empty or if it is a label line.
	 * @return True if the line is empty or a label line, False otherwise.
	 */
	public boolean isNotCommand();
	
	/**
	 * Check if the current line is an A_Command line.
	 * @return True if the line is an A_Command line, False otherwise.
	 */
	public boolean isAcommand();
	
	/**
	 * Check if the current line is an C_Command line.
	 * @return True if the line is an C_Command line, False otherwise.
	 */
	public boolean isCcommand();
	
	/**
	 * Check if the current line is an L_Command line.
	 * @return True if the line is an L_Command line, False otherwise.
	 */
	public boolean isLcommand();

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
