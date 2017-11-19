
public class ConcreteSyntax implements ConcreteSyntaxInterface {
	
	public TokenStream input; 
	public TokensManager tm;

	public ConcreteSyntax(TokenStream ts) { 
		input = ts; 
		tm = input.tokenManager;
		//System.out.println(token.toString());
	}
	public ConcreteSyntax() {
		
	}

	public Program program() {
		// Program --> void main ( ) '{' Declarations Statements '}'
		String[] header = { "void", "main", "(", ")" };
		Program p = new Program();
		for (int i = 0; i < header.length; i++)
		       tm.matchAndMove(header[i]);
	    tm.matchAndMove("{");
		p.decpart = declarations();
		p.body = statements();
		tm.matchAndMove("}");
		return p;
	}
	
	public Statement statement() {
		// Statement --> ; | Block | Assignment | IfStatement | WhileStatement
		Statement s = new Skip();
		if (tm.currentTokenValueIs(";")) { // Skip
			tm.moveOneToken();
		} else if (tm.currentTokenValueIs("{")) { // Block
			tm.matchAndMove("{");
			s = statements();
			tm.matchAndMove("}");
		} else if (tm.currentTokenValueIs("if")) // IfStatement
			s = ifStatement();
		else if (tm.currentTokenValueIs("while")) {
			s = whileStatement();
		} else if (tm.currentTokenTypeIs("Identifier")) { // Assignment
			s = assignment();
		} else
			throw new RuntimeException(SyntaxError("Statement",tm.getCurrentToken()));
		return s;
	}
	
	public Block statements() {
		// Block --> '{' Statements '}'
		Block b = new Block();
		Token token = tm.getCurrentToken();
		while (!tm.currentTokenValueIs("}") && !tm.isLastToken()) {
			Statement statement = statement();
			b.blockmembers.addElement(statement);
		}
		return b;
	}
	
	public Conditional ifStatement() {
		// IfStatement --> if ( Expression ) Statement { else Statement }opt
		Conditional c = new Conditional();
		tm.matchAndMove("if");
		tm.matchAndMove("("); 
		c.test = expression();
		tm.matchAndMove(")");
		c.thenbranch = statement();
		if (tm.currentTokenValueIs("else")) 
				c.elsebranch = statement();
		return c;
	}

	public Loop whileStatement() {
		// WhileStatement --> while ( Expression ) Statement
		Loop l = new Loop();
		tm.matchAndMove("while");
		tm.matchAndMove("(");
		l.test = expression();
		tm.matchAndMove(")");
		l.body = statement();
		return l;
	}
	
	
	
	public Assignment assignment() {
		// Assignment --> Identifier = Expression ;
		Assignment a = new Assignment();
		if (tm.currentTokenTypeIs("Identifier")) {
			a.target =  new Variable(tm.currentToken().getValue());
			tm.moveOneToken();
			tm.matchAndMove("=");
			Expression e = expression();
			a.source = e;
			tm.matchAndMove(";");
		} else
			throw new RuntimeException(SyntaxError("Identifier",tm.getCurrentToken()));
		return a;
	}
	public Expression expression() {
		// Expression --> Conjunction { || Conjunction }*
		Binary b;
		Expression e;
		e = conjunction();
		while (tm.currentTokenValueIs("||")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(tm.currentToken().getValue());
			tm.moveOneToken();
			b.term2 = conjunction();
			e = b;
		}
		return e;
	}
	public Expression conjunction() {
		// Conjunction --> Relation { && Relation }*
		Binary b;
		Expression e;
		e = relation();
		while (tm.currentTokenValueIs("&&")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(tm.currentToken().getValue());
			tm.moveOneToken();
			b.term2 = relation();
			e = b;
		}
		return e;
	}
	public Expression relation() {
		// Relation --> Addition [ < | <= | > | >= | == | <> ] Addition }*
		//System.out.println("Enter Relation: "+tm.currentToken().getValue());
		
		Binary b;
		Expression e;
		e = addition();
		while (tm.currentTokenValueIs("<") || tm.currentTokenValueIs("<=")
				|| tm.currentTokenValueIs(">")
				|| tm.currentTokenValueIs(">=")
				|| tm.currentTokenValueIs("==")
				|| tm.currentTokenValueIs("<>")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(tm.currentToken().getValue());
			tm.moveOneToken();
			b.term2 = addition();
			e = b;
		}
		//System.out.println("Exit Relation: "+tm.currentToken().getValue());
		return e;
	}
	public Expression addition() {
		// Addition --> Term { [ + | - ] Term }*
		//System.out.println("Enter Addition: "+tm.currentToken().getValue());
		
		Binary b;
		Expression e;
		e = term();
		while (tm.currentTokenValueIs("+") || tm.currentTokenValueIs("-")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(tm.currentToken().getValue());
			tm.moveOneToken();
			b.term2 = term();
			e = b;
		}
		//System.out.println("Exit Addition: "+tm.currentToken().getValue());
		return e;
	}
	public Expression term() {
		// Term --> Negation { [ '*' | / ] Negation }*
		//System.out.println("Enter Term: "+tm.currentToken().getValue());
		
		Binary b;
		Expression e;
		e = negation();
		while (tm.currentTokenValueIs("*") || tm.currentTokenValueIs("/")) {
			b = new Binary();
			b.term1 = e;
			b.op = new Operator(tm.currentToken().getValue());
			tm.moveOneToken();
			b.term2 = negation();
			e = b;
		}
		//System.out.println("Exit Term: "+tm.currentToken().getValue());
		return e;
	}
	public Expression negation() {
		//System.out.println("Enter negation: "+tm.currentToken().getValue());
		
		// Negation --> { ! }opt Factor
		Token token = tm.getCurrentToken();
		Unary u;
		if (token.getValue().equals("!")) {
			u = new Unary();
			u.op = new Operator("!");
			tm.moveOneToken();
			u.term = factor();
			return u;
		} else {
			Expression e = factor();
			//System.out.println("Exit Negation: "+tm.currentToken().getValue());
			return e;
		}
	}
	
	public Expression factor() {
		// Factor --> Identifier | Literal | ( Expression )
		//System.out.println("Enter Factor: "+tm.currentToken().getValue());
		
		Token token = tm.getCurrentToken();
		Expression e = null;
		if (token.getType().equals("Identifier")) {
			Variable v = new Variable(token.getValue());
			e = v;
		} else if (token.getType().equals("Integer-Literal")) {
			Value v = new Value((new Integer(token.getValue())).intValue());
			e = v;
		}else if (token.getType().equals("Boolean-Literal")) {
			Value v = null;
			if (token.getValue().equals("true")) {
				v = new Value(true);
			}else if(token.getValue().equals("false")) {
				v = new Value(false);
			}else {
				throw new RuntimeException(SyntaxError("Boolean-Literal",token));
			}
			e = v;
		} else if (token.getValue().equals("(")) {
			tm.moveOneToken();
			e = expression();
			tm.staticMatch(")");
		} else
			throw new RuntimeException(SyntaxError("Identifier | Literal | (",token));
		
	    tm.moveOneToken();
		return e;
	}
	
	public Declarations declarations() {
		// Declarations --> { Declaration }*
		Declarations ds = new Declarations();
		while (tm.currentTokenValueIs("int")
				|| tm.currentTokenValueIs("boolean")) {
			declarationsOfOneline(ds);
		}
		return ds;
	}
	
	public void declarationsOfOneline(Declarations ds) {
		// Declaration --> Type Identifiers ;
		Token token = tm.getCurrentToken();
		
		Type t = type();
		tm.moveOneToken();
		Declaration d;
		if (tm.currentTokenTypeIs("Identifier")) {
			d = new Declaration();
			d.t = t;
			d.v = new Variable();
			d.v.id = tm.currentToken().getValue();
			//System.out.println("Declaration: "+d.display());
			ds.addElement(d);
			tm.moveOneToken();
			while (tm.currentTokenValueIs(",")) {
				    d.t = t; // its type
				    tm.moveOneToken();
				    if (tm.currentTokenTypeIs("Identifier")) {
				    	     d = new Declaration();
				    	     d.t = t;
					     d.v = new Variable(); // its value
					     d.v.id = tm.currentToken().getValue();
					     //System.out.println("Declaration: "+d.display());
					     ds.addElement(d);
					     tm.moveOneToken();
				    } else
					     throw new RuntimeException(SyntaxError("Identifier",tm.getCurrentToken()));
			}
		}else
			throw new RuntimeException(SyntaxError("Identifier",tm.getCurrentToken()));
		tm.matchAndMove(";");
	}

	public Type type() {
		// Type --> int | bool
		Token token = tm.getCurrentToken();
		Type t = null;
		if (token.getValue().equals("int"))
			t = new Type(token.getValue());
		else if (token.getValue().equals("boolean"))
			t = new Type(token.getValue());
		else
			throw new RuntimeException(SyntaxError("int | boolean",token));
		return t;
	}
  
	public String SyntaxError(String tok, Token token) {
		String s = "Syntax error - Expecting: " + tok + " But saw "
				+ token.getType() + ": " + token.getValue();
		System.out.println(s);
		return s;
	}
	

}
