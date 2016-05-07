import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles the parsing of a single .vm file, and encapsulates access to the input code. It reads 
 * VM commands, parses them, and provides convenient access to their components. In addition, 
 * it removes all white space and comments.
 * 
 * @author yuval & Eric
 *
 */
public class Parser {
	
	/** The regex that catch the first word in the command */
	private final String firstWord = "(^[^\\s]*)";
	private Matcher firstWordMatch;
	
	/** The regex that catch the first word in the command */
	private final String commandRegex = "(^[^\\s]*)\\s*([^\\s]*)\\s*(-?[0-9]+)";
	private Matcher commandRegexMatch;
	
	private Scanner file;
	String command;
	
	/**
	 * Opens the input file/stream and gets ready to parse it.
	 * @param file The file to parse.
	 */
	public Parser(File file) {
		try {
			this.file = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file \n");
		}
	}
	
	/**
	 * Are there more commands in the input?
	 * 
	 * @return True if there is, false otherwise.
	 */
	public boolean hasMoreCommands() {
		
		return this.file.hasNext();
	}

	/**
	 * Reads the next command from the input and makes it the current command. Should be called 
	 * only if hasMoreCommands is true. Initially there is no current command.
	 */
	public void advance() {
		this.command = this.file.nextLine();
		clearCommand();
	
		Pattern pattern = Pattern.compile(this.firstWord);
		this.firstWordMatch = pattern.matcher(this.command);
		this.firstWordMatch.find();
		
		pattern = Pattern.compile(this.commandRegex);
		this.commandRegexMatch = pattern.matcher(this.command);
		this.commandRegexMatch.find();
	}
	
	/**
	 * @return The type of the current VM command. C_ARITHMETIC is returned for all the 
	 * arithmetic commands.
	 */
	public String commandType() {
		try{
			switch (this.firstWordMatch.group(1)) {
			
			case "pop":
				return "C_POP";
			case "push":
				return "C_PUSH";
			case "add":
			case "sub":
			case "and":
			case "or":
			case "not":
			case "neg":
			case "eq":
			case "lt":
			case "gt":
				return "C_ARITHMETIC";
			}
		} catch (Exception e){

			return "null";	
		}
		return "null2";
	}
	
	/**
	 * @return The first arg. of the current command. In the case of C_ARITHMETIC, the command 
	 * itself (add, sub, etc.) is returned. 
	 */
	public String arg1() {
		if(commandType().equals("C_ARITHMETIC"))
			return this.firstWordMatch.group(1);
		return this.commandRegexMatch.group(2);
	}
	
	/**
	 * 
	 * @return The second argument of the current command. Should be called only if the current 
	 * command is C_PUSH, C_POP.
	 */
	public String arg2() {
		return this.commandRegexMatch.group(3);
	}
	
	/**
	 * Clear all comments from the current command.
	 */
	private void clearCommand() {
		if(this.command.contains("//"))
		{
			this.command = this.command.substring(0, this.command.indexOf("//"));
		}
}
	
}
