import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class Assembler {
	
	public static final int FILE = 0;
	public static final String ASM_FILE_SUFFIX = "asm";
	public static final String HACK_FILE_SUFFIX = "hack";
	public static final String BINARY_15_BIT = "%15s";
	
	
	public static void asmToHack(LinkedList<File> files) {
		String result;
		for (File file : files) {
			result = "";
			Parser parserObj = new Parser(file);
			SymbolTable symbolTable = new SymbolTable();
			
			// update labels
			int lineCounter = 0;
			while (parserObj.hasMoreCommands()) {
				parserObj.advance();
				if (parserObj.isLcommand()) {
					symbolTable.addEntry(parserObj.symbol(), lineCounter);
				}
				if (!parserObj.isNotCommand()) {
					lineCounter++;
				}
			}
			// regular running
			int varAddress = 16;
			parserObj = new Parser(file);
			while (parserObj.hasMoreCommands()) {
				parserObj.advance();
				if (parserObj.isAcommand()) {
					String symbol = parserObj.symbol();
					try{
						int num = Integer.parseInt(symbol);
						result += "0" + String.format(BINARY_15_BIT, Integer.toBinaryString(num))
								.replace(' ', '0') + "\n";
					} catch (NumberFormatException e) {
						if (!symbolTable.contains(symbol)) {
							symbolTable.addEntry(symbol, varAddress);
							varAddress++;
						}
						result += "0" + String.format(BINARY_15_BIT, Integer.toBinaryString
								(symbolTable.getAddress(symbol))).replace(' ', '0') + "\n";
					}
				}
				if (parserObj.isCcommand()) {
					result += "1" + Code.comp(parserObj.comp()) + 
							Code.dest(parserObj.dest()) + Code.jump(parserObj.jump()) + "\n";
				}
			}
			
			try {
				String directoryName = file.getAbsoluteFile().getParentFile().
						getAbsolutePath() + "/";
				String filePath = directoryName + 
						file.getName().replace(ASM_FILE_SUFFIX, HACK_FILE_SUFFIX);
				System.out.println(filePath);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
						(new FileOutputStream(filePath)));
				writer.write(result);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
					
		}
		
	}
	
	
	public static void main(String[] args) {
		
		File root = new File(args[FILE]);
		LinkedList<File> files = new LinkedList<>();
		if (root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.getName().endsWith(ASM_FILE_SUFFIX)) {
					files.add(file);
				}
			}
		} else {
			if (root.getName().endsWith(ASM_FILE_SUFFIX)) {
				files.add(root);
			}
		}
		
		asmToHack(files);
		
	}

}
