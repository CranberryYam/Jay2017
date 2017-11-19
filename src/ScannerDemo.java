
public class ScannerDemo {

	private static String file1 = "/Users/yihl/Desktop/Java/Jay2017/src/prog1.jay";

	public static void main(String args[]) {
        
        TokenStream ts = new TokenStream(file1);
        ts.tokenManager.printAllTokens();
        
	}
}
