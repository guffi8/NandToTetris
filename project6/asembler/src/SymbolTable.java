import java.util.Hashtable;

public class SymbolTable implements SymbleTableInterface {

	private Hashtable<String, Integer> symbolsList;
	
	public SymbolTable() {
		this.symbolsList = new Hashtable<>();
		this.symbolsList.put("SP", 0);
		this.symbolsList.put("LCL", 1);
		this.symbolsList.put("ARG", 2);
		this.symbolsList.put("THIS", 3);
		this.symbolsList.put("THAT", 4);
		this.symbolsList.put("R0", 0);
		this.symbolsList.put("R1", 1);
		this.symbolsList.put("R2", 2);
		this.symbolsList.put("R3", 3);
		this.symbolsList.put("R4", 4);
		this.symbolsList.put("R5", 5);
		this.symbolsList.put("R6", 6);
		this.symbolsList.put("R7", 7);
		this.symbolsList.put("R8", 8);
		this.symbolsList.put("R9", 9);
		this.symbolsList.put("R10", 10);
		this.symbolsList.put("R11", 11);
		this.symbolsList.put("R12", 12);
		this.symbolsList.put("R13", 13);
		this.symbolsList.put("R14", 14);
		this.symbolsList.put("R15", 15);
		this.symbolsList.put("SCREEN", 16384);
		this.symbolsList.put("KBD", 24576);
	}
	
	@Override
	public void addEntry(String symbol, int address) {
		this.symbolsList.put(symbol, address);
	}

	@Override
	public boolean contains(String symbol) {
		return this.symbolsList.containsKey(symbol);
	}

	@Override
	public int getAddress(String symbol) {
		return this.symbolsList.get(symbol);
	}


}
