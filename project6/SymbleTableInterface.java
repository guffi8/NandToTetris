
public interface SymbleTableInterface {
	

	/**
	 * Adds the pair (symbol, address) to the table.
	 * @param symbol The symbol.
	 * @param address The address.
	 */
	public void addEntry(String symbol, int address);
	
	/**
	 * Does the symbol table contain the given symbol?
	 * @param symbol The symbol.
	 * @return True if the symbol exists, else otherwise.
	 */
	public boolean contains(String symbol);
	
	/**
	 * Returns the address associated with the symbol.
	 * @param symbol The symbol.
	 * @return The address of the symbol.
	 */
	public int getAddress(String symbol);

}
