import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream.GetField;
import java.util.LinkedList;

import javax.xml.stream.FactoryConfigurationError;

/**
 * The main program. Get file/directory path and translate the .vm file/s to .asm file.
 * 
 * @author yuval & Eric
 *
 */
public class VMtranslator {
	
	public static final int FILE = 0;
	public static final String ASM_FILE_SUFFIX = ".asm";
	public static final String VM_FILE_SUFFIX = ".vm";

	public static void main(String[] args) {
		
		// The new output file path.
		String vmFilePath;
		
		try {
			File root = new File(args[FILE]);
			LinkedList<File> files = new LinkedList<>();
			// If the path is directory, put all the .vm files to a file list.
			if (root.isDirectory()) {
				vmFilePath = root.getAbsolutePath() + "/" + root.getName() + ASM_FILE_SUFFIX;
				for (File file : root.listFiles()) {
					if (file.getName().endsWith(VM_FILE_SUFFIX)) {
						files.add(file);
					}
				}
			} else {
				vmFilePath = root.getAbsoluteFile().getParentFile().getAbsolutePath() + "/" + 
						 root.getName().replace(VM_FILE_SUFFIX, ASM_FILE_SUFFIX);
				if (root.getName().endsWith(VM_FILE_SUFFIX)) {
					files.add(root);
				}
			}
			
			CodeWriter writer = new CodeWriter(new BufferedWriter(new OutputStreamWriter
					(new FileOutputStream(vmFilePath))));
			
			// writing the boot strap code
			writer.writeInit();
			
			Parser parser;
			// Translate each .vm file to the same .asm output file. 
			for (File file : files) {
				parser = new Parser(file);
				writer.setFileName(file.getName().substring(0, file.getName().lastIndexOf('.')));

				while (parser.hasMoreCommands()) {
					parser.advance();
					
					switch (parser.commandType()) {
					case "C_POP":
					case "C_PUSH":
						writer.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
						break;
					case "C_ARITHMETIC":
						writer.writeArithmetic(parser.arg1());
						break;
					case "C_LABEL":
						writer.writeLabel(parser.arg1());
						break;
					case "C_GOTO":
						writer.writeGoto(parser.arg1());
						break;
					case "C_IF_GOTO":
						writer.writeIf(parser.arg1());
						break;
					case "C_FUNCTION":
						writer.writeFunction(parser.arg1(), parser.arg2());
						break;
					case "C_CALL":
						writer.writeCall(parser.arg1(), parser.arg2());
						break;
					case "C_RETURN":
						writer.writeReturn();
						break;
					}
				}
			}
			writer.close();
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
