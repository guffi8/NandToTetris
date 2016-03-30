import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser implements ParserInterface{

	private Scanner file = null;
	private String currCommand;
	
	public Parser(File file) {
		try {
			this.file = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file \n");
		}
	}

	@Override
	public boolean isAcommand() {
		if(this.currCommand.contains("@"))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean isCcommand() {
		if(this.currCommand.contains("=") || this.currCommand.contains(";"))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean isLcommand() {
		if(this.currCommand.contains("(") && this.currCommand.contains(")")) 
		{
			return true;
		}
		return false;
	}
	
	public boolean isNotCommand() {
		if(this.currCommand.equals("") || this.isLcommand()) 
		{
			return true;
		}
		return false;
	}

	@Override
	public String symbol() {
		//first check if it a_command type
		if(this.currCommand.contains("@"))
		{
			return this.currCommand.substring(this.currCommand.indexOf("@") + 1);
		}
		//otherwise the command is l_command type
		else
		{
			return this.currCommand.substring(this.currCommand.indexOf('(') + 1, 
					this.currCommand.indexOf(')'));
		}
	}
	
	@Override
	public String dest() {
		if(this.currCommand.contains("="))
			return this.currCommand.substring(0, this.currCommand.indexOf("="));
		
		return "null";
	}

	@Override
	public String comp() {
		if(this.currCommand.contains("="))
			return this.currCommand.substring(this.currCommand.indexOf("=") + 1);
		return this.currCommand.substring(0, this.currCommand.indexOf(";"));
	}

	@Override
	public String jump() {
		if(this.currCommand.contains(";"))
			return this.currCommand.substring(this.currCommand.indexOf(";") + 1);
		return "null";
	}

	@Override
	public boolean hasMoreCommands() {
		
		return this.file.hasNext();
	}

	@Override
	public void advance() {
		this.currCommand = this.file.nextLine();
		clearCommand();
	}

	private void clearCommand() {
			currCommand = currCommand.replace(" ", ""); //remove all white space
			if(currCommand.contains("//"))
			{
				currCommand = currCommand.substring(0, currCommand.indexOf("//"));
			}
	}

	

}
