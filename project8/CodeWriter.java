import java.io.BufferedWriter;
import java.io.IOException;

/**
 * 
 * Translates VM commands into Hack assembly code.
 * 
 * @author yuval & Eric
 *
 */
public class CodeWriter {
	
	/** Arithmetics commands */
	public static final String NEGATIVE = "neg";
	public static final String NOT = "not";
	public static final String ADDER = "add";
	public static final String SUBTRACTOR = "sub";
	public static final String AND = "and";
	public static final String OR = "or";
	public static final String EQUALE = "eq";
	public static final String LOWER_THAN = "lt";
	public static final String GREATER_THAN = "gt";
	
	/** Memory Segment commands */
	public static final String ARG = "argument";
	public static final String LOCAL = "local";
	public static final String THIS = "this";
	public static final String THAT = "that";
	public static final String STATIC = "static";
	public static final String CONSTANT = "constant";
	public static final String TEMP = "temp";
	public static final String POINTER = "pointer";
	
	private int labelCounter;
	private int callCounter;
	
	private String code;
	private BufferedWriter writer;
	private String fileName;
	private String currentFuncName;
	
	/**
	 * Opens the output file/stream and gets ready to write into it.
	 * 
	 * @param write The output file/stream.
	 */
	public CodeWriter(BufferedWriter write) {
		this.currentFuncName = "Main.main";
		this.labelCounter = 0;
		this.callCounter = 0;
		this.code = "";
		this.writer = write;
	}
	
	/**
	 * Informs the code writer that the translation of a new VM file is started and saving the 
	 * file name.
	 * 
	 * @param fileName The new file's name.
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Write a difference arithmetic (JEQ, JGT, JLT). First check the sign of each element. If both
	 * has the same sign, subtract between them and compare to zero. Otherwise, return push true or
	 * false to the stack according to the signs.  
	 * 
	 * @param command The arithmetic command.
	 */
	private void writeDifferenceArithmetic(String command) {
		this.code += "@SP\n" + 
					 "A=M\n" + 
					 "D=M\n" + 
					 "@Y_POSITIVE_" + Integer.toString(this.labelCounter) + "\n" + 
					 "D;JGE\n" + 
					 "@R14\n" + // True if Y is positive
					 "M=0\n" + 
					 "@Y_NEGATIVE_" + Integer.toString(this.labelCounter) + "\n" +
					 "0;JMP\n" + 
					 "(Y_POSITIVE_" + Integer.toString(this.labelCounter) + ")\n" + 
					 "@R14\n" + 
					 "M=-1\n" + 
					 "(Y_NEGATIVE_" + Integer.toString(this.labelCounter) + ")\n" + 
					 "@SP\n" + 
					 "A=M-1\n" + 
					 "D=M\n" + 
					 "@X_POSITIVE_" + Integer.toString(this.labelCounter) + "\n" + 
					 "D;JGE\n" + 
					 "@R13\n" + // True if X is positive
					 "M=0\n" + 
					 "@X_NEGATIVE_" + Integer.toString(this.labelCounter) + "\n" +
					 "0;JMP\n" + 
					 "(X_POSITIVE_" + Integer.toString(this.labelCounter) + ")\n" + 
					 "@R13\n" + 
					 "M=-1\n" + 
					 "(X_NEGATIVE_" + Integer.toString(this.labelCounter) + ")\n" + 
					 "@R13\n" + 
					 "D=M\n" + 
					 "@R14\n" + 
					 "D=D-M\n"; // sign(X) - sign(y)
		switch(command) {
		case CodeWriter.EQUALE:
			this.code += "@FALSE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
						 "D;JNE\n";
			break;
		case CodeWriter.LOWER_THAN:
			this.code += "@TRUE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
					 	 "D;JGT\n" + 
					 	 "@FALSE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
						 "D;JLT\n";
			break;
		case CodeWriter.GREATER_THAN:
			this.code += "@TRUE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
					 	 "D;JLT\n" + 
					 	 "@FALSE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
						 "D;JGT\n";
			break;
		}
		
		this.code += "@SP\n" + 
					 "A=M\n" + 
					 "D=M\n" +
					 "@SP\n" + 
					 "A=M-1\n" + 
					 "D=M-D\n" + 
					 "@TRUE_LABEL_" + Integer.toString(this.labelCounter) + "\n";
		
		switch(command) {
		case CodeWriter.EQUALE:
			this.code += "D;JEQ\n";
			break;
		case CodeWriter.LOWER_THAN:
			this.code += "D;JLT\n";
			break;
		case CodeWriter.GREATER_THAN:
			this.code += "D;JGT\n";
			break;
		} 
				 	 
		this.code += "@FALSE_LABEL_" + Integer.toString(this.labelCounter) + "\n" + 
					 "D;JMP\n" + 
				 	 "(TRUE_LABEL_" + Integer.toString(this.labelCounter) + ")\n" +
					 "@SP\n" + 
				 	 "A=M-1\n" + 
					 "M=-1\n" + 
				 	 "@END_" + Integer.toString(this.labelCounter) + "\n" + 
					 "0;JMP\n" + 
					 "(FALSE_LABEL_" + Integer.toString(this.labelCounter) + ")\n" +
					 "@SP\n" + 
				 	 "A=M-1\n" + 
					 "M=0\n" + 
					 "(END_" + Integer.toString(this.labelCounter) + ")\n"; 
		
		this.labelCounter++;
					 
	}
	
	/**
	 * Writes the assembly code that is the translation of the given arithmetic command.
	 * 
	 * @param command The arithmetic command.
	 */
	public void writeArithmetic(String command) {
		
		this.code += "@SP\n" + 
					 "A=M-1\n";
		if(command.equals(CodeWriter.NEGATIVE))
			this.code += "M=-M\n";
		else if(command.equals(CodeWriter.NOT))
			this.code += "M=!M\n";
		else {
			this.code += "D=M\n" + 
						 "@SP\n" +
						 "AM=M-1\n" +
						 "A=A-1\n";
			if(command.equals(CodeWriter.ADDER))
				this.code += "M=M+D\n";
			else if(command.equals(CodeWriter.SUBTRACTOR))
				this.code += "M=M-D\n";
			else if(command.equals(CodeWriter.AND))
				this.code += "M=M&D\n";
			else if(command.equals(CodeWriter.OR))
				this.code += "M=M|D\n";
			else {
				writeDifferenceArithmetic(command);
			}
			
		}
	}
	
	/**
	 * Writes the assembly code that is the translation of the given command, where command is 
	 * either C_PUSH or C_POP.
	 * 
	 * @param command push/pop
	 * @param memorySegment 
	 * @param index
	 */
	public void writePushPop(String command, String memorySegment, String index) {
		
		String addressOrValue = "";
		
		if(memorySegment.equals(CodeWriter.ARG) || memorySegment.equals(CodeWriter.LOCAL) || 
				memorySegment.equals(CodeWriter.THIS) || memorySegment.equals(CodeWriter.THAT) || 
				memorySegment.equals(CodeWriter.POINTER) || memorySegment.equals(CodeWriter.TEMP)) {
			this.code += "@" + index + "\n" + 
						 "D=A\n";
			if(memorySegment.equals(CodeWriter.ARG)) {
				this.code += "@ARG\n";
				addressOrValue = "M";
			}
			else if(memorySegment.equals(CodeWriter.LOCAL)) {
				this.code += "@LCL\n";
				addressOrValue = "M";
			}
			else if(memorySegment.equals(CodeWriter.THIS)) {
				this.code += "@THIS\n";
				addressOrValue = "M";
			}
			else if(memorySegment.equals(CodeWriter.THAT)) {
				this.code += "@THAT\n";
				addressOrValue = "M";
			}
			else if(memorySegment.equals(CodeWriter.POINTER)) {
				this.code += "@3\n";
				addressOrValue = "A";
			}
			else if(memorySegment.equals(CodeWriter.TEMP)) {
				this.code += "@5\n";
				addressOrValue = "A";
			}
			if(command.equals("C_PUSH")) 
				this.code += "A=" + addressOrValue + "+D\n" + 
							 "D=M\n" + 
							 "@SP\n" +
							 "M=M+1\n" + 
							 "A=M-1\n" + 
							 "M=D\n";
			else if(command.equals("C_POP"))
				this.code += "D=" + addressOrValue + "+D\n" + 
							 "@R13\n" + 
							 "M=D\n" + 
							 "@SP\n" + 
							 "AM=M-1\n" + 
							 "D=M\n" + 
							 "@R13\n" + 
							 "A=M\n" +
							 "M=D\n";
		} else if(memorySegment.equals(CodeWriter.STATIC)) {
			this.code += "@" + this.fileName + "." + index + "\n";
			if(command.equals("C_PUSH"))
				this.code += "D=M\n" + 
							 "@SP\n" + 
							 "M=M+1\n" + 
							 "A=M-1\n" + 
							 "M=D\n";
			else if(command.equals("C_POP"))
				this.code += "D=A\n" + 
						 	 "@R13\n" + 
						 	 "M=D\n" + 
						 	 "@SP\n" + 
						 	 "AM=M-1\n" + 
						 	 "D=M\n" + 
						 	 "@R13\n" + 
						 	 "A=M\n" +
						 	 "M=D\n";
			
		} else if(memorySegment.equals(CodeWriter.CONSTANT)) {
			if(command.equals("C_PUSH"))
				this.code += "@" + index + "\n" + 
							 "D=A\n" + 
							 "@SP\n" + 
							 "M=M+1\n" + 
							 "A=M-1\n" + 
							 "M=D\n";
		} 
	}
	
	/**
	 * Writes the assembly code that effects the VM initialization (also called bootstrap code). 
	 * This code should be placed in the ROM beginning in address 0x0000.
	 */
	public void writeInit() {
		this.code += "@256\n" +
					 "D=A\n" +
					 "@SP\n" +
					 "M=D\n";
		writeCall("Sys.init", "0");
	}
	
	/**
	 * Writes the assembly code that is the translation of the given label command.
	 * 
	 * @param label The label name.
	 */
	public void writeLabel(String label) {
		this.code += "(" + this.currentFuncName + ":" + label + ")\n";
	}
	
	/**
	 * Writes the assembly code that is the translation of the given goto command. 
	 * 
	 * @param label The label name.
	 */
	public void writeGoto(String label) {
		this.code += "@" + this.currentFuncName + ":" + label + "\n" +
					 "0;JMP\n";
	}
	
	/**
	 * Writes the assembly code that is the translation of the given if-goto command.
	 *  
	 * @param label The label name.
	 */
	public void writeIf(String label) {
		this.code += "@SP\n" +
					 "AM=M-1\n" + 
					 "D=M\n" +
					 "@" + this.currentFuncName + ":" + label + "\n" +
					 "D;JNE\n";
	}
	
	/**
	 * @param segment The segment
	 * @return The code push the value in the segment to the stack
	 */
	private String pushCallerFuncArg(String segment) {
		
		return "@" + segment + "\n" +
			   "D=M\n" +
			   "@SP\n" + 
			   "M=M+1\n" +
			   "A=M-1\n" +
			   "M=D\n";
	}
	
	/**
	 * Writes the assembly code that is the translation of the given Call command
	 * 
	 * @param functionName The name of function.
	 * @param numArgs Number of arguments.
	 */
	public void writeCall(String functionName, String numArgs) {
		this.code += "@RETURN_ADDRESS_" + Integer.toString(this.callCounter) + "\n" +
					 "D=A\n" +
					 "@SP\n" + 
  					 "M=M+1\n" +
				     "A=M-1\n" +
					 "M=D\n";
		this.code += pushCallerFuncArg("LCL");
		this.code += pushCallerFuncArg("ARG");
		this.code += pushCallerFuncArg("THIS");
		this.code += pushCallerFuncArg("THAT");
		//ARG = SP - numArgs - 5
		this.code += "@5\n" +
					 "D=A\n" +
					 "@" + numArgs + "\n" +
					 "D=A+D\n" +
					 "@SP\n" +
					 "D=M-D\n" +
					 "@ARG\n" +
					 "M=D\n" +
					 "@SP\n" + 
					 "D=M\n" + 
					 "@LCL\n" +
					 "M=D\n";
		//goto functionName
		this.code += "@" + functionName + "\n" +
					 "0;JMP\n";
		this.code += "(RETURN_ADDRESS_" + Integer.toString(this.callCounter) + ")\n";
		
		this.callCounter++;
		
	}
	
	/**
	 * @param segment The segment.
	 * @return The code that restore the segment of the calling function.
	 */
	private String restoreCaller(String segment) {
		return "@R13\n" + // R13 = frame
			   "AM=M-1\n" +
			   "D=M\n" +
			   "@" + segment + "\n" +
			   "M=D\n";
	}
	
	/**
	 * Writes the assembly code that is the translation of the given Return command. 
	 */
	public void writeReturn() {
		// frame = LCL
		this.code += "@LCL\n" +
					 "D=M\n" +
					 "@R13\n" + // R13 = frame
					 "M=D\n";
		// RET = *(frame-5)
		this.code += "@5\n" +
				 	 "D=A\n" +
				 	 "@R13\n" + // R13 = frame
				 	 "D=M-D\n" + 
				 	 "A=D\n" + 
				 	 "D=M\n" +
				 	 "@R14\n" + // R14 = RET
				 	 "M=D\n";
		// *ARG = pop()
		this.code += "@SP\n" +
					 "AM=M-1\n" +
					 "D=M\n" +
					 "@ARG\n" + 
					 "A=M\n" +
					 "M=D\n";
		// SP = ARG+1
		this.code += "@ARG\n" +
					 "D=M+1\n" +
					 "@SP\n"+
					 "M=D\n";
		this.code += restoreCaller("THAT");
		this.code += restoreCaller("THIS");
		this.code += restoreCaller("ARG");
		this.code += restoreCaller("LCL");
		//goto RET
		this.code += "@R14\n" + //R14 = RET
					 "A=M\n" +
					 "0;JMP\n";
	}
	
	/**
	 * Writes the assembly code that is the translation of the given Function command. 
	 * 
	 * @param functionName The function's name.
	 * @param numLocals Numbers of local variables.
	 */
	public void writeFunction(String functionName, String numLocals) {
		this.currentFuncName = functionName;
		this.code += "(" + functionName + ")\n" +
					 "@" + numLocals + "\n" +
					 "D=A\n" + 
					 "(" + functionName + ":START_INIT_LOOP)\n" +
					 "@" + functionName + ":END_INIT_LOOP\n" +
					 "D;JLE\n" + 
					 "@SP\n" +
					 "M=M+1\n" + 
					 "A=M-1\n" + 
					 "M=0\n" +
					 "D=D-1\n" + 
					 "@" + functionName + ":START_INIT_LOOP\n" + 
					 "0;JMP\n" +
					 "(" + functionName + ":END_INIT_LOOP)\n"; 
					
	}
	
	/**
	 *  Closes the output file.
	 */
	public void close() {
		try {
			this.writer.write(this.code);
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
