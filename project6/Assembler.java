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
	
	/**
	 * The function get list of .asm files and translate every file to .hack file. 
	 * @param files The list of .asm files.
	 */
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
			int line = 0;
			boolean succeed = true;
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
					String comp = Code.comp(parserObj.comp());
					String dest = Code.dest(parserObj.dest());
					String jump = Code.jump(parserObj.jump());
					if (comp == null || dest == null || jump == null) {
						System.out.println(file.getName() + " - problem in line " + line + ".");
						succeed = false;
						break;
					}
					result += "1" + comp + dest + jump + "\n";
				}
				line++;
			}
			
			// create the new .hack file and write the result into it.
			if (succeed) {
			
				try {
					String directoryName = file.getAbsoluteFile().getParentFile().getAbsolutePath();
					String filePath = directoryName + "/" + file.getName().
							replace(ASM_FILE_SUFFIX, HACK_FILE_SUFFIX);
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
							(new FileOutputStream(filePath)));
					writer.write(result);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
					
		}
		
	}
	
	/**
	 * The main function. Get the file/folder to translate.
	 * @param args In correct file is in args[0].
	 */
	public static void main(String[] args) {
		
		try {
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
		} catch (Exception e) {
			System.out.println("Can't open file");
		}
	}

}
