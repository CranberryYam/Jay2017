
public class ScannerDemo {

	private static String file1 = "/Users/yihl/Desktop/Java/Jay2017/src/prog1.jay";
	private static int counter = 1;

	public static void main(String args[]) {
        
        TokenStream ts = new TokenStream(file1);
        System.out.println(file1);

		while (!ts.isEndofFile()) {
			Token token = ts.nextToken();
			System.out.println("Token "+counter+" -Type:"+token.getType()
			+" -Value:"+token.getValue());
			counter++;
		}
        
	}
}
