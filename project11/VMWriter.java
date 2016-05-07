import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Emits VM commands into a file, encapsulating the VM command syntax.
 */
public class VMWriter {

	private BufferedWriter writer;
	
	/**
	 * Creates a new file and prepares it for writing. 
	 */
	public VMWriter(BufferedWriter writer) {
		this.writer = writer;
	}
	
	/**
	 * Writes a VM push command.
	 */
	public void writePush(Segment seg, int index) throws IOException {
		//System.out.println("push " + Segment.getValue(seg) + " " + Integer.toString(index));
		this.writer.write("push " + Segment.getValue(seg) + " " + Integer.toString(index));
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM pop command.
	 */
	public void writePop(Segment seg, int index) throws IOException {
		//System.out.println("pop " + Segment.getValue(seg) + " " + Integer.toString(index)); 
		this.writer.write("pop " + Segment.getValue(seg) + " " + Integer.toString(index)); 
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM arithmetic command.
	 */
	public void writeArithmetic(String cmd) throws IOException {
		//System.out.println(cmd);
		this.writer.write(cmd);
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM label command.
	 */
	public void writeLabel(String label) throws IOException {
		//System.out.println("label " + label);
		this.writer.write("label " + label);
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM if-goto command.
	 */
	public void writeGoto(String label) throws IOException {
		//System.out.println("goto " + label);
		this.writer.write("goto " + label);
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM if command.
	 */
	public void writeIf(String label) throws IOException {
		//System.out.println("if-goto " + label);
		this.writer.write("if-goto " + label);
		this.writer.newLine();
	}

	/**
	 * Writes a VM call command.
	 */
	public void writeCall(String name, int nArgs) throws IOException {
		//System.out.println("call " + name + " " + Integer.toString(nArgs));
		this.writer.write("call " + name + " " + Integer.toString(nArgs));
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM function command.
	 */
	public void writeFunction(String name, int nLocals) throws IOException {
		//System.out.println("function " + name + " " + Integer.toString(nLocals));
		this.writer.write("function " + name + " " + Integer.toString(nLocals));
		this.writer.newLine();
	}
	
	/**
	 * Writes a VM return command.
	 */
	public void writeReturn() throws IOException {
		//System.out.println("return");
		this.writer.write("return");
		this.writer.newLine();
	}
	
	/**
	 * Close the output file.
	 */
	public void close() throws IOException {
		this.writer.close();
	}
	
}
