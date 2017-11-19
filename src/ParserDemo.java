import org.junit.experimental.theories.Theories;
import org.junit.jupiter.api.TestFactory;

public class ParserDemo {

	public static void main(String[] args) {
	   //testFactory();
	   //testNegation();
	   //testTerm();
	   //testAddition();
	   //testRelation();
	   //testConjunction();
	   //testExpression();
	   //testExpressionByFile();
		testProgram();
	}

	private static void testFactory() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
		
		Token token = new Token("Integer-Literal", "832");
		Token token2 = new Token("Identifier", "temp");
		Token token3 = new Token("Boolean-Literal", "false");	
		Token token4 = new Token("!", "Operator");
		Token token5 = new Token("Identifier", "temp2");
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);
		tokensManager.add(token5);
		
		Expression expression = cSyntax.factor();
		System.out.println(expression.display(0));
		expression = cSyntax.factor();
		System.out.println(expression.display(0));
		expression = cSyntax.factor();
		System.out.println(expression.display(0));
		expression = cSyntax.negation();
		System.out.println(expression.display(0));
	}
	
	private static void testNegation() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Identifier", "temp2");
		tokensManager.add(token);
		tokensManager.add(token2);

		Expression expression = cSyntax.negation();
		System.out.println(expression.display(0));
	}
	
	private static void testTerm() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Identifier", "temp2");
		Token token3 = new Token("Operator", "*");
		Token token4 = new Token("Operator", "!");
		Token token5 = new Token("Identifier", "temep");
		Token token6 = new Token("Operator", "/");
		Token token7 = new Token("Identifier", "879");
		
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);
		tokensManager.add(token5);
		tokensManager.add(token6);
		tokensManager.add(token7);

		Expression expression = cSyntax.term();
		System.out.println(expression.display(0));
	}
	
	private static void testAddition() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Boolean-Literal", "false");
		Token token3 = new Token("Operator", "+");
		Token token4 = new Token("Identifier", "temp2");
		
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);

		Expression expression = cSyntax.addition();
		System.out.println(expression.display(0));
	}
	
	private static void testRelation() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Boolean-Literal", "false");
		Token token3 = new Token("Operator", ">=");
		Token token4 = new Token("Identifier", "temp2");
		
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);

		Expression expression = cSyntax.relation();
		System.out.println(expression.display(0));
	}
	
	private static void testConjunction() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Boolean-Literal", "false");
		Token token3 = new Token("Operator", "&&");
		Token token4 = new Token("Identifier", "temp2");
		
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);
        
		cSyntax.tm.printAllTokens();
		Expression expression = cSyntax.conjunction();
		System.out.println(expression.display(0));
	}
	
	private static void testExpression() {
		TokensManager tokensManager = new TokensManager();
		ConcreteSyntax cSyntax = new ConcreteSyntax();
		cSyntax.tm = tokensManager;
			
		Token token = new Token("Operator", "!");
		Token token2 = new Token("Boolean-Literal", "false");
		Token token3 = new Token("Operator", "||");
		Token token4 = new Token("Identifier", "temp2");
		
		tokensManager.add(token);
		tokensManager.add(token2);
		tokensManager.add(token3);
		tokensManager.add(token4);
        
		cSyntax.tm.printAllTokens();
		Expression expression = cSyntax.expression();
		System.out.println(expression.display(0));
	}
	
	private static void testExpressionByFile() {
		TokenStream tStream = new TokenStream("/Users/yihl/Desktop/Java/Jay2017/src/prog_factor.jay");
		tStream.tokenManager.printAllTokens();
		ConcreteSyntax cSyntax = new ConcreteSyntax(tStream);       
		cSyntax.tm.printAllTokens();
		
		Assignment assignment = cSyntax.assignment();
		System.out.println(assignment.display(0));
	}
	
	private static void testProgram() {
		TokenStream tStream = new TokenStream("/Users/yihl/Desktop/Java/Jay2017/src/prog2.jay");
		ConcreteSyntax cSyntax = new ConcreteSyntax(tStream);       
//		cSyntax.tm.printAllTokens();
		
		cSyntax.tm.checkLexicalError();
		System.out.println(cSyntax.program().display());
	}
}
