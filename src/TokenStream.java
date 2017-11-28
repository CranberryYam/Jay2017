import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class TokenStream {
	
	private boolean isEof = false;
	private char nextChar = ' '; 
	private BufferedReader input;
    public TokensManager tokenManager;
	
	public TokenStream(String fileName) {
		tokenManager = new TokensManager();
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			isEof = true;
		}
		produceTokens();
		//tokenManager.printAllTokens();
	}
	
//	public void testReadChar() {
//		while(!isEof) {
//			skipWhiteSpace();
//			System.out.print(nextChar);
//		}
//	}
	
	
	private void produceTokens() {
		while(!isEof) {
			Token newToken = newToken();
			
			if (!newToken.getType().equals("void")) {
				
				String newValue = newToken.getValue().replace("\n", "").replace("\r", "").replace("\t", "").replace("\f", "");
				newToken.setValue(newValue);
				tokenManager.add(newToken);
			}
		}
	}
	
	public Token nextToken() {
		Token token = tokenManager.getCurrentToken();
		tokenManager.moveOneToken();
		tokenManager.checkError(token);
		return token;
	}
	
	private Token newToken() { 
		Token t = new Token();
		t.setType("void");
		t.setValue("");
		
        skipWhiteSpace();
        
//        while(!(isLetter(nextChar) || isDigit(nextChar) 
//        		   || isSeparator(nextChar) || isOperator(nextChar))) {
//        	    moveToNextChar();
//        }
        
        while (nextChar == '/' && !isEndOfToken(nextChar)) {   //if the next line is still "//"
        	/**Always be careful with WHILE loop, sometimes it never ends**/
			moveToNextChar();
			if (nextChar == '/') { 
				
				moveToNextChar();
				while(!isEndOfLine(nextChar) && !isEof) {
					moveToNextChar();
				}
                 moveToNextChar();
			}else {
				t.setType("Operator");
				t.setValue("/"+nextChar);
				moveToNextChar();
				return t;
			}
		} 
        
        
		if (isOperator(nextChar)) {
			t.setType("Operator");
			t.setValue("" + nextChar);
			switch (nextChar) {
			case '<':
				moveToNextChar();
				if(nextChar == '=') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				}
				return t;
			case '>':
				moveToNextChar();
				if(nextChar == '=') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				}				
				return t;
			case '=':
				moveToNextChar();
				if(nextChar == '=') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				}				
				return t;
			case '!':
				moveToNextChar();
				if(nextChar == '=') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				}				
				return t;
			case '&':
				moveToNextChar();
				if(nextChar == '&') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				    return t;
				}else {
					t.setType("Other");
				}
			    break;
			case '|':
				moveToNextChar();
				if(nextChar == '|') {
					t.setValue(t.getValue()+nextChar);
				    moveToNextChar();
				    return t;
				}else {
					t.setType("Other");
				}
			    break;
			default: 
				moveToNextChar();
				return t;
			}
		}
        
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue("" + nextChar);
			moveToNextChar();
			return t;
		}
        
		if (isLetter(nextChar)) {
			t.setType("Identifier");
			t.setValue("");
			while ((isLetter(nextChar) || isDigit(nextChar)) && !isEndOfToken(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				moveToNextChar();
			}
	
			if (isKeyword(t.getValue()))
				    t.setType("Keyword");
			if (isBoolean(t.getValue()))
			    t.setType("Boolean-Literal");
			return t;
		} 
		
		if (isDigit(nextChar)) { 
			t.setType("Integer-Literal");
			t.setValue("");
			while ((isDigit(nextChar) || isLetter(nextChar)) && !isEndOfToken(nextChar)) {   //must check isEndOfFile
				t.setValue(t.getValue() + nextChar);
				moveToNextChar();
				if (isLetter(nextChar)) t.setType("Other");
			}
			
			return t;
		}
		
		if(!isEof && !isWhiteSpace(nextChar)) {
		String newValue = t.getValue()+nextChar;
		t.setType("Other");
		t.setValue(newValue);   //some time it looks like ;\n
		moveToNextChar();
		return t;
		}
		
		//System.out.println("newToken: "+token.toString());
	    return t;
	}
	

	
	private void moveToNextChar() {
		if(!isEof) {
			int i;
			try {
				i = input.read();
				if (i == -1) {
					nextChar = (char)i;  //nextChar will be the last chat
					isEof = true;
				}else {
					nextChar = (char)i;
				}
			} catch (IOException e) {
				System.exit(-1);
			}
		}
	}
	private boolean isBoolean(String s) {
		switch (s) {
		case "true":
			return true;
		case "false":
		default:
			return false;
		}
	}
	private boolean isKeyword(String s) {
		switch (s) {
		case "boolean":
			return true;
		case "else":
			return true;
		case "if":
			return true;
		case "int":
			return true;
		case "main":
			return true;
		case "void":
			return true;
		case "while":
			return true;
		default:
			return false;
		}
	}
	
	private void skipWhiteSpace() {
		while (!isEof && isWhiteSpace(nextChar)) {
			moveToNextChar();
            //System.out.println("skip: " + nextChar);
		}
	}
	
	private boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfLine(char c) {
		return (c == '\r' || c == '\n' || c == '\f'); 
		// \r new line, \n carriage return, \f form feed, \t tap
	}

	private boolean isEndOfToken(char c) { // Is the value a separate token?
		return (isWhiteSpace(nextChar) || isOperator(nextChar)
				|| isSeparator(nextChar) || isEof);
	}

	private boolean isSeparator(char c) {
		return (c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == ',');
	}

	private boolean isOperator(char c) {
		return (c == '=' || c == '+' || c == '-' || c == '*' || c == '<' 
			||c == '>' || c == 92 || c == '!' || c=='&' || c=='|');
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		return (c >= '0' && c<='9');
	}
	
	public boolean isEoFile() {
		return isEof;
	}
	
	public boolean isEndofFile() {
		return tokenManager.isLastToken();
	}
	
}

