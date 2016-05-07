import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Set;

/**
 * The tokenizer removes all comments and white space from the input stream and breaks it 
 * into Jacklanguage tokens, as specified in the Jack grammar.
 */
public class JackTokenizer {
	
	public static final String TT_KEYWORD = "KEYWORD";
	public static final String TT_SYMBOL = "SYMBOL";
	public static final String TT_IDENTIFIER = "IDENTIFIER";
	public static final String TT_INT_COSNT = "INT_CONST";
	public static final String TT_STRING_CONST = "STRING_CONST";	
	
	/* Set of all valid symbols in Jack langauge */
	private static Set<Character> symbols = new HashSet<>();
	static {
		symbols.add('{');
		symbols.add('}');
		symbols.add('(');
		symbols.add(')');
		symbols.add('[');
		symbols.add(']');
		symbols.add('.');
		symbols.add(',');
		symbols.add(';');
		symbols.add('+');
		symbols.add('-');
		symbols.add('*');
		symbols.add('/');
		symbols.add('&');
		symbols.add('|');
		symbols.add('<');
		symbols.add('>');
		symbols.add('=');
		symbols.add('~');
	};
	
	/* set of all valid keywords in Jack langauge */
	private static Set<String> keywords = new HashSet<>();
	static {
		keywords.add("class");
		keywords.add("constructor");
		keywords.add("function");
		keywords.add("method");
		keywords.add("field");
		keywords.add("static");
		keywords.add("var");
		keywords.add("int");
		keywords.add("char");
		keywords.add("boolean");
		keywords.add("void");
		keywords.add("true");
		keywords.add("false");
		keywords.add("null");
		keywords.add("this");
		keywords.add("let");
		keywords.add("do");
		keywords.add("if");
		keywords.add("else");
		keywords.add("while");
		keywords.add("return");
	};
	/* set of all valid op signs in Jack langauge */
	public static Set<String> op = new HashSet<>();
	static {
		op.add("+");
		op.add("-");
		op.add("*");
		op.add("/");
		op.add("&amp;");
		op.add("|");
		op.add("&lt;");
		op.add("&gt;");
		op.add("=");
	};
	/* set of all valid unary op signs in Jack langauge */
	public static Set<String> unaryOp = new HashSet<>();
	static {
		op.add("~");
		op.add("-");
	};
	
	/* the stream tokenizer */
	private StreamTokenizer tokenizer;
	/* the file reader */
	private Reader reader;
	/* holds current token data */
	private int currentToken;
	
	/**
	 * Opens the input file and gets ready to tokenize it 
	 */
	public JackTokenizer(File fileName) {
		try {
			this.reader = new JackReader(new FileReader(fileName));
			this.tokenizer = new StreamTokenizer(this.reader);
			
			// remove comments
			this.tokenizer.slashSlashComments(true);
			this.tokenizer.slashStarComments(true);
			
			// ignore end of line
			this.tokenizer.eolIsSignificant(false);
			
			this.tokenizer.quoteChar('"');
			this.tokenizer.wordChars('_', '_');
			
			for(Character symbol : JackTokenizer.symbols) {
				this.tokenizer.ordinaryChar(symbol);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * close the reader
	 */
	public void close() throws IOException {
		this.reader.close();
	}
	
	
public String peekTwiceVal(){
		
		int preToken = this.currentToken;
		String res = "";
		if(hasMoreTokens()){
			advance();
			advance();
			switch (tokenType()) {
			case JackTokenizer.TT_IDENTIFIER:
				res = identifier();
				break;
			case JackTokenizer.TT_INT_COSNT:
				res = String.valueOf(intVal());
				break;
			case JackTokenizer.TT_KEYWORD:
				res = keyWord();
				break;
			case JackTokenizer.TT_STRING_CONST:
				res = stringVal();
				break;
			case JackTokenizer.TT_SYMBOL:
				res = symbol();
				break;
			}
			
			this.tokenizer.pushBack();
			this.tokenizer.pushBack();
			this.currentToken = preToken;
		}
		return res;
	}
	
	
	/**
	 * peekVal - return the value of the next tokken without moving to it.
	 */ 
	public String peekVal(){
		
		int preToken = this.currentToken;
		String res = "";
		if(hasMoreTokens()){
			advance();
			switch (tokenType()) {
			case JackTokenizer.TT_IDENTIFIER:
				res = identifier();
				break;
			case JackTokenizer.TT_INT_COSNT:
				res = String.valueOf(intVal());
				break;
			case JackTokenizer.TT_KEYWORD:
				res = keyWord();
				break;
			case JackTokenizer.TT_STRING_CONST:
				res = stringVal();
				break;
			case JackTokenizer.TT_SYMBOL:
				res = symbol();
				break;
			}
			
			this.tokenizer.pushBack();
			this.currentToken = preToken;
		}
		return res;
	}
	
	/**
	 * peekType - return the type of the next tokken without moving to it.
	 */
	public String peekType() {
		int preToken = this.currentToken;
		String res = "";
		if(hasMoreTokens()){
			advance();
			res = tokenType();
			
			this.tokenizer.pushBack();
			this.currentToken = preToken;
		}
		return res;
	}
	
	/**
	 * do we have more tokens in the input? 
	 */
	public boolean hasMoreTokens() {
		boolean returnVal = false;
		try {
			if(this.tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
				returnVal = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.tokenizer.pushBack();
		return returnVal;	
	}
	
	/**
	 * gets the next token from the input and makes it the current token. 
	 * This method should only be called if hasMoreTokens() is true. 
	 * Initially there is no current token.
	 */ 
	public void advance() {
		try {
			this.currentToken = this.tokenizer.nextToken();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * returns the type of the current token 
	 */
	public String tokenType() {
		switch (this.currentToken) {
		case StreamTokenizer.TT_NUMBER:
			return JackTokenizer.TT_INT_COSNT;
		case StreamTokenizer.TT_WORD:
			if (keywords.contains(tokenizer.sval)) {
				return JackTokenizer.TT_KEYWORD;
			} 
			return JackTokenizer.TT_IDENTIFIER;
		default:
			if ((char) tokenizer.ttype == '"') {
				return JackTokenizer.TT_STRING_CONST;
			}
			return JackTokenizer.TT_SYMBOL;
		}
	}
	
	/**
	 * returns the keyword which is the current token. 
	 * Should be called only when tokenType() is KEYWORD.
	 */
	public String keyWord() {
		return tokenizer.sval.toLowerCase();
	}
	
	/**
	 * returns the keyword which is the current token. 
	 * Should be called only when tokenType() is symbol.
	 */
	public String symbol() {
		if ((char) this.currentToken == '<') {
			return "&lt;";
		}
		if ((char) this.currentToken == '>') {
			return "&gt;";
		}
		if ((char) this.currentToken == '&') {
			return "&amp;";
		}
		return "" + (char) this.currentToken;	
	}
	
	/**
	 * returns the keyword which is the current token. 
	 * Should be called only when tokenType() is identifier.
	 */
	public String identifier() {
		return tokenizer.sval;
	}
	
	/**
	 * returns the keyword which is the current token. 
	 * Should be called only when tokenType() is int_const.
	 */
	public int intVal() {
		return (int) tokenizer.nval;
	}
	
	/**
	 * returns the keyword which is the current token. 
	 * Should be called only when tokenType() is string_const.
	 */
	public String stringVal() {
		return tokenizer.sval.replaceAll("\n", "\\\\n").replaceAll("\t", "\\\\t").replace((char) 14, (char) 9);
	}
}

