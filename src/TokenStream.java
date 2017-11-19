// ConcreteSyntax.java

// Implementation of the Scanner for JAY

// This code DOES NOT implement a scanner for JAY. You have to complete
// the code and also make sure it implements a scanner for JAY - not something
// else.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

	private boolean isEof = false;

	private char nextChar = ' '; 

	private BufferedReader input;

	public boolean isEoFile() {
		return isEof;
	}

	public TokenStream(String fileName) {
		try {
			input = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + fileName);
			isEof = true;
		}
	}
    
	public void testToken() {
		while(!isEof) {
			Token nextToken = nextToken();
			System.out.print(nextToken.toString());
		}
	}
	
	public Token nextToken() { 
		Token t = new Token();
		t.setType("Other");
		t.setValue("");

		skipWhiteSpace();

		// Then check for a comment, and bypass it
		// but remember that / is also a division operator.
		while (nextChar == '/') {
			// Changed if to while to avoid the 2nd line being printed when
			// there
			// are two comment lines in a row.
			nextChar = readChar();
			if (nextChar == '/') { // If / is followed by another /
				// skip rest of line - it's a comment.
				// look for <cr>, <lf>, <ff>
				nextChar = readChar();
				while(!isEndOfLine(nextChar)) {
					nextChar = readChar();
				}
                 nextChar = readChar();
			} else {
				// A slash followed by a backslash is an AND operator (/\).
				// 92 is \, the number is used since \ causes an error.
				if (nextChar == 92) {
					t.setValue("/" + nextChar);
					nextChar = readChar();
				} else
					// A slash followed by anything else must be an operator.
					t.setValue("/");
				t.setType("Operator");
				return t;
			}
		}

		// Then check for an operator; recover 2-character operators
		// as well as 1-character ones.
		if (isOperator(nextChar)) {
			t.setType("Operator");
			t.setValue(t.getValue() + nextChar);
			switch (nextChar) {
			case '<':
				nextChar = readChar();
				if(nextChar == '=')
					t.setValue(t.getValue()+nextChar);
				nextChar = readChar();
				return t;
			case '>':
				nextChar = readChar();
				if(nextChar == '=')
					t.setValue(t.getValue()+nextChar);
				nextChar = readChar();
				return t;
			case '=':
				nextChar = readChar();
				if(nextChar == '=')
					t.setValue(t.getValue()+nextChar);
				nextChar = readChar();
				return t;
			case '!':
				nextChar = readChar();
				if(nextChar == '=')
					t.setValue(t.getValue()+nextChar);
				nextChar = readChar();
				return t;
			case 92: // look for the OR operator, \/
				nextChar = readChar();
				if(nextChar == '/')
					t.setValue(t.getValue()+nextChar);
				nextChar = readChar();
				return t;
			default: // all other operators
				nextChar = readChar();
				return t;
			}
		}

		// Then check for a separator.
		if (isSeparator(nextChar)) {
			t.setType("Separator");
			t.setValue(t.getValue() + nextChar);
			nextChar = readChar();
			return t;
		}

		// Then check for an identifier, keyword, or literal.
		if (isLetter(nextChar)) {
			// get an identifier
			t.setType("Identifier");
			while ((isLetter(nextChar) || isDigit(nextChar))) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// now see if this is a keyword
			if (isKeyword(t.getValue()))
				t.setType("Keyword");
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				return t;
		}

		if (isDigit(nextChar)) { // check for integers
			t.setType("Integer-Literal");
			while (isDigit(nextChar)) {
				t.setValue(t.getValue() + nextChar);
				nextChar = readChar();
			}
			// An Integer-Literal is to be only followed by a space,
			// an operator, or a separator.
			if (isEndOfToken(nextChar)) // If token is valid, returns.
				return t;
		}

		if (isEof)
			return t;

//		// Makes sure that the whole unknown token (Type: Other) is printed.
//		while (!isEndOfToken(nextChar) && nextChar != 7) {
//			if (nextChar == '!') {
//				t.setType("Operator");
//				nextChar = readChar();
//				if (nextChar == '=') { // looks for = after !
//					nextChar = 7; // means next token is !=
//					break;
//				} else {
//					t.setValue(t.getValue() + "!");
//				}
//			} else {
//				t.setValue(t.getValue() + nextChar);
//				nextChar = readChar();
//			}
//		}
//
//		if (nextChar == 7) {
//			if (t.getValue().equals("")) { // Looks for a !=
//				t.setType("Operator"); // operator. If token is
//				t.setValue("!="); // empty, sets != as token,
//				nextChar = readChar();
//			}
//
//		} else
//			t.setType("Other"); // otherwise, unknown token.

		return t;
	}

	private char readChar() {
		int i = 0;
		
		if (isEof)
			return (char) 0;
		
		System.out.flush();//flush last data in this output stream
		
		try {
			i = input.read();
		} catch (IOException e) {
			System.exit(-1);
		}
		
		if (i == -1) {
			isEof = true;
			return (char) 0;
		}
		
		return (char) i;
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

	private boolean isWhiteSpace(char c) {
		return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f');
	}

	private boolean isEndOfLine(char c) {
		return (c == '\r' || c == '\n' || c == '\f'); 
		// \r new line, \n carriage return, \f form feed, \t tap
	}

	private boolean isEndOfToken(char c) { // Is the value a seperate token?
		return (isWhiteSpace(nextChar) || isOperator(nextChar)
				|| isSeparator(nextChar) || isEof);
	}

	private void skipWhiteSpace() {
		while (!isEof && isWhiteSpace(nextChar)) {
			nextChar = readChar();
		}
	}

	private boolean isSeparator(char c) {
		return (c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == ',');
	}

	private boolean isOperator(char c) {
		return (c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '<' 
			||c == '>' || c == 92 || c == '!');
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
	}

	private boolean isDigit(char c) {
		return (c >= '0' && c<='9');
	}

	public boolean isEndofFile() {
		return isEof;
	}
}
