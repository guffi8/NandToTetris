import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

/**
 * The main class. gets the path of the .jack file or directory with .jack files and generate .xml 
 * file for each .jac file.
 * 
 * @author yuval & eric
 *
 */
public class JackCompiler {

	public static final String JACK_FILE_SUFFIX = ".jack";
	public static final String VM_FILE_SUFFIX = ".vm";
	
	public static void main(String[] args) {
		try {
			String xmlFilePath;
			File root = new File(args[0]);
			CompilationEngine compiler;
			JackTokenizer tokenizer;
			LinkedList<File> files = new LinkedList<>();
			VMWriter writer;
			
			// check if the given path is a directory
			if (root.isDirectory()) {
				for (File file : root.listFiles()) {
					if (file.getName().endsWith(JACK_FILE_SUFFIX)) {
						files.add(file);
					}
				}
			} else {
				if (root.getName().endsWith(JACK_FILE_SUFFIX)) {
					files.add(root);
				}
			}
			
			// generate a .xml file for every .jack file.
			for(File file : files) {
				tokenizer = new JackTokenizer(file);
				xmlFilePath = file.getAbsolutePath().replace(JACK_FILE_SUFFIX, VM_FILE_SUFFIX);
				writer = new VMWriter(new BufferedWriter(new OutputStreamWriter
												(new FileOutputStream(xmlFilePath))));
				compiler = new CompilationEngine(tokenizer, writer);
				compiler.generateVM();
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
