import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * deligate the buffer reader - for handeling with the tab problem.
 */ 
public class JackReader extends BufferedReader  {


	public JackReader(Reader in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		int read = super.read();
		if((char) read == '\t') {
			return 14;
		}
		return read;
	}

}
