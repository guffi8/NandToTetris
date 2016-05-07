import java.io.BufferedWriter;
import java.io.IOException;

/**
 * 
 * Effects the actual compilation output. Gets its input from a JackTokenizer and emits its parsed 
 * structure into an output file/stream. The output is generated by a series of compilexxx() 
 * routines, one for every syntactic element xxx of the Jack grammar. The contract between these 
 * routines is that each compilexxx() routine should read the syntactic construct xxx from the 
 * input, advance() the tokenizer exactly beyond xxx , and output the parsing of xxx . Thus, 
 * compilexxx() may only be called if indeed xxx is the next syntactic element of the input.
 * 
 * @author yuval & eric
 *
 */
public class CompilationEngine {

	/* The tokenizer */
	private JackTokenizer tokenizer;
	/* The output file */
	private BufferedWriter output;
	/* The indent value */
	private int indent;

	
	/**
	 * The constructor. Gets the tokenizer and the output buffer.   
	 * 
	 * @param tokenizer
	 * @param outputFile
	 */
	public CompilationEngine(JackTokenizer tokenizer, BufferedWriter outputFile ) {
		
		this.tokenizer = tokenizer;
		this.output = outputFile;
		this.indent = 0;

	}
	
	/**
	 * Generate the XML file.
	 * @throws IOException
	 */
	public void generateXML() throws IOException {
		compileClass();
		this.output.close();
		this.tokenizer.close();
	}
	
	/**	 
	 * @return  The current indent
	 */
	private String getIndent() {
		String theIndent = "";
		for(int i = 0; i < this.indent; i++) {
			theIndent += "  ";
		}
		return theIndent;
	}
	
	/**
	 * Print the current keyword to the XML output 
	 * @throws IOException
	 */
	private void writeKeyword() throws IOException {
		this.output.write(getIndent() + "<keyword> " + this.tokenizer.keyWord() + " </keyword>\n");
		//System.out.println(this.tokenizer.keyWord());
	}
	
	/**
	 * Print the current identefier to the XML output 
	 * @throws IOException
	 */
	private void writeIdentefier() throws IOException {
		this.output.write(getIndent() + "<identifier> " + this.tokenizer.identifier() + " </identifier>\n");
		//System.out.println(this.tokenizer.identifier());
	}

	/**
	 * Print the current symbol to the XML output 
	 * @throws IOException
	 */
	private void writeSymbol() throws IOException {
		this.output.write(getIndent() + "<symbol> " + this.tokenizer.symbol() + " </symbol>\n");
		//System.out.println(this.tokenizer.symbol());
	}
	
	/**
	 * Print the current integet constant to the XML output 
	 * @throws IOException
	 */
	private void writeInteger() throws IOException {
		this.output.write(getIndent() + "<integerConstant> " + this.tokenizer.intVal() + " </integerConstant>\n");
		//System.out.println(this.tokenizer.intVal());
	}
	
	/**
	 * Print the current string constant to the XML output 
	 * @throws IOException
	 */
	private void writeString() throws IOException {
		this.output.write(getIndent() + "<stringConstant> " + this.tokenizer.stringVal() + " </stringConstant>\n");
		//System.out.println(this.tokenizer.stringVal());
	}
	
	/**
	 * Compiles a complete class.
	 * @throws IOException
	 */
	private void compileClass() throws IOException{
		
		this.output.write(getIndent() + "<class>\n");
		this.indent++;
		
		this.tokenizer.advance();
		writeKeyword();
		this.tokenizer.advance();
		writeIdentefier();
		this.tokenizer.advance();
		writeSymbol();
		
		while(this.tokenizer.hasMoreTokens()){
			this.tokenizer.advance();
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals("}")){
					writeSymbol();
					break;
				}	
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				switch(this.tokenizer.keyWord()) {
				case "static":
				case "field":
					compileClassVarDec();
					break;
				case "constructor":
				case "function":
				case "method":
					compileSubroutineDec();
					break;
				}
			}	
		}
		
		this.indent--;
		this.output.write(getIndent() + "</class>\n");
	}
	
	/**
	 * Compiles a static declaration or a field declaration.
	 * @throws IOException
	 */
	private void compileClassVarDec() throws IOException {
		
		this.output.write(getIndent() + "<classVarDec>\n");
		this.indent++;
		
		writeKeyword();
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals(";")){
					writeSymbol();
					break;
				}	
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_IDENTIFIER:
				writeIdentefier();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					complieParameterList();
				}
				break;
			}
			
		}
		
		this.indent--;
		this.output.write(getIndent() + "</classVarDec>\n");
	}
	
	/**
	 * Compiles a complete varible decliration
	 * @throws IOException
	 */
	private void compileSubroutineDec() throws IOException {
		this.output.write(getIndent() + "<subroutineDec>\n");
		this.indent++;
		
		writeKeyword();
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_IDENTIFIER:
				writeIdentefier();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					complieParameterList();
				}
				break;
			}
			
			if(this.tokenizer.peekVal().equals("{")){
				compileSubroutineBody();
				break;
			}	
			
	
		}
		
		this.indent--;
		this.output.write(getIndent() + "</subroutineDec>\n");
	}
	
	/**
	 * Compiles a complete method, function, or constructor.
	 * @throws IOException
	 */
	private void compileSubroutineBody() throws IOException {
		this.output.write(getIndent() + "<subroutineBody>\n");
		this.indent++;
	
		
		while(this.tokenizer.hasMoreTokens()) {
			
			this.tokenizer.advance();
			
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)) {
				writeSymbol();
				if (this.tokenizer.symbol().equals("}")) {
					break;
				}
			}
			
			while(this.tokenizer.peekVal().equals("var")) {
				
				compileVarDec();
				//System.out.println("aaaaaaaaaa  "  +  this.tokenizer.peekType() + "   " + this.tokenizer.peekVal());
				
			}
			if(!this.tokenizer.peekVal().equals("var")) {
				compileStatements();
			}
			
		}
	
		this.indent--;
		this.output.write(getIndent() + "</subroutineBody>\n");
		
	}
	
	/**
	 * Compiles a (possibly empty) parameter list, not including the enclosing “ () ”.
	 * @throws IOException
	 */
	private void complieParameterList() throws IOException {
		this.output.write(getIndent() + "<parameterList>\n");
		this.indent++;
		
		if(!this.tokenizer.peekVal().equals(")")) {
			
			while(this.tokenizer.hasMoreTokens()) {
				this.tokenizer.advance();
				
				switch(this.tokenizer.tokenType()) {
				case JackTokenizer.TT_SYMBOL:
					writeSymbol();
					break;
				case JackTokenizer.TT_IDENTIFIER:
					writeIdentefier();
					break;
				case JackTokenizer.TT_KEYWORD:
					writeKeyword();
					break;
				}
				
				if(this.tokenizer.peekVal().equals(")")) {
					break;
				}
			}
		}
		
		this.indent--;
		this.output.write(getIndent() + "</parameterList>\n");
	}
	
	/**
	 * Compiles a var declaration.
	 * @throws IOException
	 */
	private void compileVarDec() throws IOException {
		this.output.write(getIndent() + "<varDec>\n");
		this.indent++;
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals(";")){
					writeSymbol();
					break;
				}	
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_IDENTIFIER:
				writeIdentefier();
				break;
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					complieParameterList();
				}
				break;
			}	
			
		}
		
		this.indent--;
		this.output.write(getIndent() + "</varDec>\n");
		
	}
	
	/**
	 * Compiles a sequence of statements, not including the enclosing “ {} ”.
	 * @throws IOException
	 */
	private void compileStatements() throws IOException {
		
		this.output.write(getIndent() + "<statements>\n");
		this.indent++;
		while(this.tokenizer.hasMoreTokens()) {
			
			if(this.tokenizer.peekVal().equals("}")) {
				break;
			}
			compileStatemant();
		}
		
		this.indent--;
		this.output.write(getIndent() + "</statements>\n");
	}
	
	/**
	 * Choose which statement to compile according to next token.  
	 * @throws IOException
	 */
	private void compileStatemant() throws IOException {
		
		switch(this.tokenizer.peekVal()) {
		case "let":
			compileLet();
			break;
		case "if":
			compileIf();
			break;
		case "do":
			compileDo();
			break;
		case "while":
			compileWhile();
			break;
		case "return":
			compileReturn();
			break;
		}

	}
	
	/**
	 * Compiles a let statement.
	 * @throws IOException
	 */
	private void compileLet() throws IOException {
		this.output.write(getIndent() + "<letStatement>\n");
		this.indent++;
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals(";")){
					writeSymbol();
					break;
				}	
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_IDENTIFIER:
				writeIdentefier();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("[") || this.tokenizer.symbol().equals("=")) {
					compileExpression();
				}
			}
		}
		
		this.indent--;
		this.output.write(getIndent() + "</letStatement>\n");
		
	}
	
	/**
	 * Compiles a if statement.
	 * @throws IOException
	 */
	private void compileIf() throws IOException {
		this.output.write(getIndent() + "<ifStatement>\n");
		this.indent++;
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals("}")){
					writeSymbol();
					if(!(this.tokenizer.peekVal().equals("else"))) {
						break;
					}
					this.tokenizer.advance();
				}	
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					compileExpression();
				}
				if(this.tokenizer.symbol().equals("{")) {
					compileStatements();
				}
			}
			
		}
		
		this.indent--;
		this.output.write(getIndent() + "</ifStatement>\n");
	}
	
	/**
	 * Compiles a while statement.
	 * @throws IOException
	 */
	private void compileWhile() throws IOException {
		this.output.write(getIndent() + "<whileStatement>\n");
		this.indent++;
		
		while(this.tokenizer.hasMoreTokens()) {
			this.tokenizer.advance();
			
			if(this.tokenizer.tokenType().equals(JackTokenizer.TT_SYMBOL)){
				if(this.tokenizer.symbol().equals("}")) {
					writeSymbol();
					break;
				}
			}
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_KEYWORD:
				writeKeyword();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					compileExpression();
				}
				if(this.tokenizer.symbol().equals("{")) {
					compileStatements();
				}
			}
			
		}
		
		this.indent--;
		this.output.write(getIndent() + "</whileStatement>\n");	
	}
	
	/**
	 * Compiles a do statement.
	 * @throws IOException
	 */
	private void compileDo() throws IOException {
		
		
		this.output.write(getIndent() + "<doStatement>\n");
		this.indent++;
		
		this.tokenizer.advance();
		writeKeyword();
		compileSubroutineCall();
		this.tokenizer.advance();
		writeSymbol();
		
		this.indent--;
		this.output.write(getIndent() + "</doStatement>\n");
	}
	
	/**
	 * Compiles a return statement.
	 * @throws IOException
	 */
	private void compileReturn() throws IOException {
		this.output.write(getIndent() + "<returnStatement>\n");
		this.indent++;
		
		this.tokenizer.advance();
		writeKeyword();
		if(!(this.tokenizer.peekVal().equals(";"))) {
			compileExpression();
		}
		this.tokenizer.advance();
		writeSymbol();
		
		this.indent--;
		this.output.write(getIndent() + "</returnStatement>\n");
	}
	
	/**
	 * Compiles an expression.
	 * @throws IOException
	 */
	private void compileExpression() throws IOException {
		this.output.write(getIndent() + "<expression>\n");
		this.indent++;
		
		while(this.tokenizer.hasMoreTokens()) {
			compileTerm();
			if(JackTokenizer.op.contains(this.tokenizer.peekVal())) {
				this.tokenizer.advance();
				writeSymbol();
			} else {
				break;
			}
		}
		
		this.indent--;
		this.output.write(getIndent() + "</expression>\n");
	}
	
	/**
	 * Compiles a subroutine call.
	 * @throws IOException
	 */
	private void compileSubroutineCall() throws IOException {
	
		boolean flag = false;
		while(this.tokenizer.hasMoreTokens()) {

			this.tokenizer.advance();
			
			switch(this.tokenizer.tokenType()) {
			case JackTokenizer.TT_IDENTIFIER:
				writeIdentefier();
				break;
			case JackTokenizer.TT_SYMBOL:
				writeSymbol();
				if(this.tokenizer.symbol().equals("(")) {
					compileExpressionList();
					this.tokenizer.advance();
					writeSymbol();
					flag = true;
				}
				break;
			}
			if (flag) {
				break;
			}
		}
	}
	
	/**
	 * ompiles a term. This routine is faced with a slight difficulty when trying to decide between
	 * some of the alternative parsing rules. Specifically, if the current token is an identifier, 
	 * the routine must distinguish between a variable, an array entry, and a subroutine call. 
	 * A single look-ahead token, which may be one of “ [ “, “ ( “, or “ . ” suffices to distinguish
	 * between the three possibilities. Any other token is not part of this term and should not 
	 * be advanced over.
	 * @throws IOException
	 */
	private void compileTerm() throws IOException {
		this.output.write(getIndent() + "<term>\n");
		this.indent++;
		
		switch(this.tokenizer.peekType()) {
		case JackTokenizer.TT_IDENTIFIER:
			this.tokenizer.advance();
			writeIdentefier();
			if(this.tokenizer.peekVal().equals("[")) {
				this.tokenizer.advance();
				writeSymbol();
				compileExpression();
				this.tokenizer.advance();
				writeSymbol();
			} else if (this.tokenizer.peekVal().equals("(")) {
				this.tokenizer.advance();
				writeSymbol();
				compileExpressionList();
				this.tokenizer.advance();
				writeSymbol();
			} else if (this.tokenizer.peekVal().equals(".")) {
				this.tokenizer.advance();
				writeSymbol();
				this.tokenizer.advance();
				writeIdentefier();
				this.tokenizer.advance();
				writeSymbol();
				compileExpressionList();
				this.tokenizer.advance();
				writeSymbol();
			}
			break;
		case JackTokenizer.TT_INT_COSNT:
			this.tokenizer.advance();
			writeInteger();
			break;
		case JackTokenizer.TT_KEYWORD:
			this.tokenizer.advance();
			writeKeyword();
			break;
		case JackTokenizer.TT_STRING_CONST:
			this.tokenizer.advance();
			writeString();
			break;
		case JackTokenizer.TT_SYMBOL:
			if(this.tokenizer.peekVal().equals("(")) {
				this.tokenizer.advance();
				writeSymbol();
				compileExpression();
				this.tokenizer.advance();
				writeSymbol();
			} else if(this.tokenizer.peekVal().equals("~") || this.tokenizer.peekVal().equals("-")) {
				this.tokenizer.advance();
				writeSymbol();
				compileTerm();
			}
			
			break;
		}
	
		
		this.indent--;
		this.output.write(getIndent() + "</term>\n");
	}
	
	/**
	 * Compiles a (possibly empty) comma- separated list of expressions.
	 * @throws IOException
	 */
	private void compileExpressionList() throws IOException {
		this.output.write(getIndent() + "<expressionList>\n");
		this.indent++;	
		
		while(!(this.tokenizer.peekVal().equals(")"))) {
		
			compileExpression();
			if(this.tokenizer.peekVal().equals(",")) {
				this.tokenizer.advance();
				writeSymbol();
			} else {
				break;
			}

		} 
		
		
		this.indent--;
		this.output.write(getIndent() + "</expressionList>\n");
	}
}