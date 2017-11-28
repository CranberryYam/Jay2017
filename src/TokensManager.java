import java.awt.List;
import java.util.ArrayList;

public class TokensManager {
	public int tokenPosition = 0;
	private ArrayList<Token> tokens;
	
	
	public TokensManager() {
		tokens = new ArrayList<Token>();
	}
	public TokensManager(ArrayList<Token> tokens) {
		this.tokens = tokens;
	}
	
	
/*************************************  Move Method   **************************************/
	public void add(Token token) {
		tokens.add(token);
	}
	public int tokesize() {
		return tokens.size();
	}
	public TokensManager newFrom(int from,int to) {
		ArrayList<Token> newTokens = new ArrayList<Token>(tokens.subList(from, to > tokens.size() ? tokens.size() : to));
		TokensManager newOne = new TokensManager(newTokens); 
		return newOne;
	}
	public boolean isLastToken() {
		if (tokenPosition==tokens.size()) {
			return true;
		}
		return false;
	}

/*************************************  Current Token Methods   **************************************/	
	public Token currentToken() {
		Token token;
		if (tokenPosition<tokens.size()) {
			token = tokens.get(tokenPosition);
		}else {
			token = new Token("end", "end");
		}
		return token;
	}
	public Token getCurrentToken() {
		Token token;
		if (tokenPosition<tokens.size()) {
			token = new Token(tokens.get(tokenPosition).getType(),tokens.get(tokenPosition).getValue());
		}else {
			token = new Token("end", "end");
		}
		return token;
	}
	public boolean currentTokenTypeIs(String s) {
		if (currentToken().getType().equals(s)) {
			return true;
		}
		return false;
	}
	public boolean currentTokenValueIs(String s) {
		if (currentToken().getValue().equals(s)) {
			return true;
		}
		return false;
	}
	
/*************************************  Move&Match Token Methods   **************************************/	
	public Token moveOneToken() {
			tokenPosition++;
			return currentToken();
	}
	public void staticMatch(String s) {
		if (!currentTokenValueIs(s)) {
			throw new RuntimeException(SyntaxError(s,getCurrentToken()));
		}
	}
	public void moveUntilMatch(String s) {
		while (!currentTokenValueIs(s)) {
			if (tokenPosition==tokens.size()) {
				System.out.println("There's no "+s+" at this program");
				break;
			}
			moveOneToken();
			//System.out.println("Match: "+currentToken().getValue());
		}
	}
    public void matchAndMove(String s) {
    	//System.out.println(tm.getCurrentToken().toString());
    	if (!currentTokenValueIs(s)) {
			throw new RuntimeException(SyntaxError(s,getCurrentToken()));
		}else {
			moveOneToken();
		}
    }

/*************************************  Extra Methods   **************************************/		
    public void printAllTokens() {
//		for (Token token : tokens) {
//			  System.out.println(token.toString());
//		}
		for(int i=0;i<tokens.size();i++) {
			Token token = tokens.get(i);
			System.out.println("Token "+i+" -Type:"+token.getType()
			+" -Value:"+token.getValue());
		}
	}
	
	public void checkLexicalError() {
		for (Token token : tokens) {
			if (token.getType().equals("Other"))
			  System.out.println("\nLexical error - use of "+token.getValue());
		}
	}
	
	public void checkError(Token token) {
		if (token.getType().equals("Other"))
			  System.out.println("\nLexical error - use of "+token.getValue());
	}
	
	private String SyntaxError(String tok, Token token) {
		String s = "Syntax error - Expecting: " + tok + " But saw "
				+ token.getType() + ": " + token.getValue();
		System.out.println(s);
		return s;
	}
}
