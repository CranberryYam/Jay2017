import static org.junit.jupiter.api.Assertions.*;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

class ConcreteSyntax2Test {	
	
	/*@Test   
	void testDeclarations() {
		
		TokenStream2 tStream = new TokenStream2("/Users/yihl/Desktop/Java/Jay2017/src/prog_declarations.jay");
		ConcreteSyntax2 cSyntax = new ConcreteSyntax2(tStream);
		TokensManager tm = cSyntax.input.tokenManager;
		
		tm.printAllTokens();
		
		Declarations ds = cSyntax.scanDeclarations(tm);
		System.out.println(ds.display(0));
	}
	@Test   
	void testTokenStream() {
		
		TokenStream2 tStream = new TokenStream2("/Users/yihl/Desktop/Java/Jay2017/src/prog1.jay");
		tStream.tokenManager.printAllTokens();
	}*/
	
	@Test
	void testFactor() {
		TokenStream2 tStream = new TokenStream2("/Users/yihl/Desktop/Java/Jay2017/src/prog_factor.jay");
		ConcreteSyntax2 cSyntax = new ConcreteSyntax2(tStream);
		TokensManager tm = cSyntax.input.tokenManager;
		
		tm.printAllTokens();
		
		Expression expression = cSyntax.factor();
		System.out.println(expression.display(0));
	}
	

}
